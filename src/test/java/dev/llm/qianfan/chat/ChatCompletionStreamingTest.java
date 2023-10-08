package dev.llm.qianfan.chat;

import static dev.llm.qianfan.chat.JsonSchemaProperty.STRING;
import static dev.llm.qianfan.chat.JsonSchemaProperty.description;
import static dev.llm.qianfan.chat.JsonSchemaProperty.enums;
import static dev.llm.qianfan.chat.Message.userMessage;
import static java.util.Collections.singletonList;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import dev.llm.qianfan.QianfanClient;
import dev.llm.qianfan.RateLimitAwareTest;
import dev.llm.qianfan.ResponseHandle;

class ChatCompletionStreamingTest extends RateLimitAwareTest {

    private static final String USER_MESSAGE = "write exactly the following 2 words: 'hello world'";

    private final QianfanClient client = QianfanClient.builder()
            .logRequests()
            .logResponses()
            .logStreamingResponses()
            .build();

    @Test
    void testSimpleApi() throws Exception {

        StringBuilder responseBuilder = new StringBuilder();
        CompletableFuture<String> future = new CompletableFuture<>();

        client.chatCompletion(USER_MESSAGE)
                .onPartialResponse(responseBuilder::append)
                .onComplete(() -> future.complete(responseBuilder.toString()))
                .onError(future::completeExceptionally)
                .execute();

        String response = future.get(30, SECONDS);
        assertThat(response).containsIgnoringCase("hello world");
    }

    @MethodSource
    @ParameterizedTest
    void testCustomizableApi(ChatCompletionRequest request) throws Exception {

        StringBuilder responseBuilder = new StringBuilder();
        CompletableFuture<String> future = new CompletableFuture<>();

        client.chatCompletion(request)
                .onPartialResponse(partialResponse -> {
                    String content = partialResponse.result();
                    if (content != null) {
                        responseBuilder.append(content);
                    }
                })
                .onComplete(() -> future.complete(responseBuilder.toString()))
                .onError(future::completeExceptionally)
                .execute();

        String response = future.get(30, SECONDS);
        assertThat(response).containsIgnoringCase("hello world");
    }

    static Stream<Arguments> testCustomizableApi() {
        return Stream.of(
                Arguments.of(
                        ChatCompletionRequest.builder()
                                .messages(singletonList(userMessage(USER_MESSAGE)))
                                .build()),
                Arguments.of(
                        ChatCompletionRequest.builder()
                                .messages(userMessage(USER_MESSAGE))
                                .build()),
                Arguments.of(
                        ChatCompletionRequest.builder()
                                .addUserMessage(USER_MESSAGE)
                                .build()));
    }

    // @Test
    void testFunctions() throws Exception {

        Message userMessage = userMessage("What is the weather like in Boston?");

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .messages(userMessage)
                .functions(Function.builder()
                        .name("get_current_weather")
                        .description("获得指定地点的天气")
                        .addParameter("location", STRING, description("省，市名，例如：浙江省，宁波"))
                        .addOptionalParameter("unit", STRING, enums(ChatCompletionTest.Unit.class))
                        .build())
                .build();

        StringBuilder responseBuilder = new StringBuilder();
        CompletableFuture<String> future = new CompletableFuture<>();

        client.chatCompletion(request)
                .onPartialResponse(partialResponse -> {
                    // String content = partialResponse.getResult();

                    // assertThat(content.isNull());

                    // FunctionCall functionCall = partialResponse.getFunctionCall();
                    // if (partialResponse.choices().get(0).finishReason() == null) {
                    // if (functionCall.name() != null) {
                    // responseBuilder.append(functionCall.name());
                    // } else if (functionCall.arguments() != null) {
                    // responseBuilder.append(functionCall.arguments());
                    // }
                    // }
                })
                .onComplete(() -> future.complete(responseBuilder.toString()))
                .onError(future::completeExceptionally)
                .execute();

        String response = future.get(30, SECONDS);

        assertThat(response).contains("get_current_weather");
        assertThat(response).contains("location");
        assertThat(response).contains("Boston");
    }

    // @Test
    void testCancelStreamingAfterStreamingStarted() throws InterruptedException {

        AtomicBoolean streamingStarted = new AtomicBoolean(false);
        AtomicBoolean streamingCancelled = new AtomicBoolean(false);
        AtomicBoolean cancellationSucceeded = new AtomicBoolean(true);

        ResponseHandle responseHandle = client.chatCompletion("Write a poem about AI in 10 words")
                .onPartialResponse(partialResponse -> {
                    streamingStarted.set(true);
                    if (streamingCancelled.get()) {
                        cancellationSucceeded.set(false);
                    }
                })
                .onComplete(() -> cancellationSucceeded.set(false))
                .onError(e -> cancellationSucceeded.set(false))
                .execute();

        while (!streamingStarted.get()) {
            Thread.sleep(200);
        }

        newSingleThreadExecutor().execute(() -> {
            responseHandle.cancel();
            streamingCancelled.set(true);
        });

        while (!streamingCancelled.get()) {
            Thread.sleep(200);
        }
        Thread.sleep(5000);

        assertThat(cancellationSucceeded).isTrue();
    }

    // @Test
    void testCancelStreamingBeforeStreamingStarted() throws InterruptedException {

        AtomicBoolean cancellationSucceeded = new AtomicBoolean(true);

        ResponseHandle responseHandle = client.chatCompletion("Write a poem about AI in 10 words")
                .onPartialResponse(partialResponse -> cancellationSucceeded.set(false))
                .onComplete(() -> cancellationSucceeded.set(false))
                .onError(e -> cancellationSucceeded.set(false))
                .execute();

        AtomicBoolean streamingCancelled = new AtomicBoolean(false);

        newSingleThreadExecutor().execute(() -> {
            responseHandle.cancel();
            streamingCancelled.set(true);
        });

        while (!streamingCancelled.get()) {
            Thread.sleep(200);
        }
        Thread.sleep(5000);

        assertThat(cancellationSucceeded).isTrue();
    }
}
