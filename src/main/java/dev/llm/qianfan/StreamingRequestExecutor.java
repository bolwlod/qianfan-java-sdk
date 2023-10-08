package dev.llm.qianfan;

import static dev.llm.qianfan.Utils.fromJson;
import static dev.llm.qianfan.Utils.toException;
import static dev.llm.qianfan.Utils.toJson;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.llm.qianfan.chat.ChatCompletionResponse;
import dev.llm.qianfan.completion.CompletionResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;

@Slf4j
public class StreamingRequestExecutor<Request, Response, ResponseContent> {

    private final OkHttpClient okHttpClient;
    private final String endpointUrl;
    private final Supplier<Request> requestWithStreamSupplier;
    private final Class<Response> responseClass;
    private final Function<Response, ResponseContent> streamEventContentExtractor;
    private final boolean logStreamingResponses;

    public StreamingRequestExecutor(
            OkHttpClient okHttpClient,
            String endpointUrl,
            Supplier<Request> requestWithStreamSupplier,
            Class<Response> responseClass,
            Function<Response, ResponseContent> streamEventContentExtractor,
            boolean logStreamingResponses) {

        this.okHttpClient = okHttpClient;
        this.endpointUrl = endpointUrl;
        this.requestWithStreamSupplier = requestWithStreamSupplier;
        this.responseClass = responseClass;
        this.streamEventContentExtractor = streamEventContentExtractor;
        this.logStreamingResponses = logStreamingResponses;
    }

    public StreamingResponseHandling onPartialResponse(Consumer<ResponseContent> partialResponseHandler) {

        return new StreamingResponseHandling() {

            @Override
            public StreamingCompletionHandling onComplete(Runnable streamingCompletionCallback) {

                return new StreamingCompletionHandling() {

                    @Override
                    public ErrorHandling onError(Consumer<Throwable> errorHandler) {

                        return new ErrorHandling() {

                            @Override
                            public ResponseHandle execute() {
                                return StreamingRequestExecutor.this.stream(partialResponseHandler,
                                        streamingCompletionCallback, errorHandler);
                            }
                        };
                    }

                    @Override
                    public ErrorHandling ignoreErrors() {

                        return new ErrorHandling() {

                            @Override
                            public ResponseHandle execute() {
                                return StreamingRequestExecutor.this.stream(
                                        partialResponseHandler,
                                        streamingCompletionCallback,
                                        (e) -> {
                                        });
                            }
                        };
                    }
                };
            }

            @Override
            public ErrorHandling onError(Consumer<Throwable> errorHandler) {

                return new ErrorHandling() {

                    @Override
                    public ResponseHandle execute() {
                        return StreamingRequestExecutor.this.stream(
                                partialResponseHandler,
                                () -> {

                                },
                                errorHandler);
                    }
                };
            }

            @Override
            public ErrorHandling ignoreErrors() {

                return new ErrorHandling() {

                    @Override
                    public ResponseHandle execute() {
                        return StreamingRequestExecutor.this.stream(
                                partialResponseHandler,
                                () -> {
                                },
                                (e) -> {
                                });
                    }
                };
            }
        };
    }

    private ResponseHandle stream(
            Consumer<ResponseContent> partialResponseHandler,
            Runnable streamingCompletionCallback,
            Consumer<Throwable> errorHandler) {

        Request request = this.requestWithStreamSupplier.get();
        String requestJson = toJson(request);

        okhttp3.Request okHttpRequest = new okhttp3.Request.Builder()
                .url(this.endpointUrl)
                .post(RequestBody.create(requestJson, MediaType.get("application/json; charset=utf-8")))
                .build();

        ResponseHandle responseHandle = new ResponseHandle();

        EventSourceListener eventSourceListener = new EventSourceListener() {

            @Override
            public void onOpen(EventSource eventSource, okhttp3.Response response) {

                if (responseHandle.cancelled) {
                    eventSource.cancel();
                    return;
                }

                if (StreamingRequestExecutor.this.logStreamingResponses) {
                    ResponseLoggingInterceptor.log(response);
                }

            }

            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {

                if (responseHandle.cancelled) {
                    eventSource.cancel();
                    return;
                }

                if (StreamingRequestExecutor.this.logStreamingResponses) {
                    StreamingRequestExecutor.log.debug("onEvent() {}", data);
                }

                try {
                    Response response = fromJson(data, StreamingRequestExecutor.this.responseClass);
                    ResponseContent responseContent = StreamingRequestExecutor.this.streamEventContentExtractor
                            .apply(response);

                    if (responseContent != null) {
                        partialResponseHandler.accept(responseContent);
                    }

                    if (response instanceof ChatCompletionResponse) {
                        if (((ChatCompletionResponse) response).isEnd()) {
                            streamingCompletionCallback.run();
                            return;
                        }
                    }

                    if (response instanceof CompletionResponse) {
                        if (((CompletionResponse) response).isEnd()) {
                            streamingCompletionCallback.run();
                            return;
                        }
                    }

                } catch (Exception e) {
                    errorHandler.accept(e);
                }

            }

            @Override
            public void onClosed(EventSource eventSource) {

                if (responseHandle.cancelled) {
                    eventSource.cancel();
                    return;
                }

                if (StreamingRequestExecutor.this.logStreamingResponses) {
                    StreamingRequestExecutor.log.debug("onClosed()");
                }

            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, okhttp3.Response response) {

                if (responseHandle.cancelled) {
                    return;
                }

                if (StreamingRequestExecutor.this.logStreamingResponses) {
                    StreamingRequestExecutor.log.debug("onFailure()", t);
                    ResponseLoggingInterceptor.log(response);
                }

                if (t != null) {
                    errorHandler.accept(t);
                } else {
                    try {
                        errorHandler.accept(toException(response));
                    } catch (IOException ioe) {
                        errorHandler.accept(ioe);
                    }
                }

            }
        };

        EventSources.createFactory(this.okHttpClient).newEventSource(okHttpRequest, eventSourceListener);

        return responseHandle;
    }
}
