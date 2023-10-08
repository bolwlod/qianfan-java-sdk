package dev.llm.qianfan;

import static dev.llm.qianfan.Utils.toException;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

import retrofit2.Call;

public class AsyncRequestExecutor<Response, ResponseContent> {

    private final Call<Response> call;
    private final Function<Response, ResponseContent> responseContentExtractor;

    public AsyncRequestExecutor(Call<Response> call, Function<Response, ResponseContent> responseContentExtractor) {
        this.call = call;
        this.responseContentExtractor = responseContentExtractor;
    }

    public AsyncResponseHandling onResponse(Consumer<ResponseContent> responseHandler) {
        return new AsyncResponseHandling() {

            @Override
            public ErrorHandling onError(final Consumer<Throwable> errorHandler) {
                return new ErrorHandling() {

                    @Override
                    public ResponseHandle execute() {
                        try {
                            retrofit2.Response<Response> retrofitResponse = AsyncRequestExecutor.this.call.execute();
                            if (retrofitResponse.isSuccessful()) {
                                Response response = retrofitResponse.body();
                                ResponseContent responseContent = AsyncRequestExecutor.this.responseContentExtractor
                                        .apply(response);
                                responseHandler.accept(responseContent);
                            } else {
                                errorHandler.accept(toException(retrofitResponse));
                            }
                        } catch (IOException ioe) {
                            errorHandler.accept(ioe);
                        }

                        return new ResponseHandle();
                    }
                };
            }

            @Override
            public ErrorHandling ignoreErrors() {
                return new ErrorHandling() {

                    @Override
                    public ResponseHandle execute() {
                        try {
                            retrofit2.Response<Response> retrofitResponse = AsyncRequestExecutor.this.call.execute();
                            if (retrofitResponse.isSuccessful()) {
                                Response response = retrofitResponse.body();
                                ResponseContent responseContent = AsyncRequestExecutor.this.responseContentExtractor
                                        .apply(response);
                                responseHandler.accept(responseContent);
                            }
                        } catch (IOException ioe) {
                        }
                        return new ResponseHandle();
                    }
                };
            }
        };
    }
}
