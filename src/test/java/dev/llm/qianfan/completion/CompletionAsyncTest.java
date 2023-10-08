package dev.llm.qianfan.completion;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CompletableFuture;

import dev.llm.qianfan.QianfanClient;
import dev.llm.qianfan.RateLimitAwareTest;

class CompletionAsyncTest extends RateLimitAwareTest {

        private static final String PROMPT = "write exactly the following 2 words: 'hello world'";
        private static String serviceName = "";

        private final QianfanClient client = QianfanClient.builder()
                        .logRequests()
                        .logResponses()
                        .build();

        // @Test
        void testSimpleApi() throws Exception {

                CompletableFuture<String> future = new CompletableFuture<>();

                client.completion(PROMPT, serviceName)
                                .onResponse(future::complete)
                                .onError(future::completeExceptionally)
                                .execute();

                String response = future.get(30, SECONDS);

                assertThat(response).containsIgnoringCase("hello world");
        }

        // @Test
        void testCustomizableApi() throws Exception {

                CompletionRequest request = CompletionRequest.builder()
                                .prompt(PROMPT)
                                .build();

                CompletableFuture<CompletionResponse> future = new CompletableFuture<>();

                client.completion(request)
                                .onResponse(future::complete)
                                .onError(future::completeExceptionally)
                                .execute();

                CompletionResponse response = future.get(30, SECONDS);

                assertThat(response.result()).containsIgnoringCase("hello world");
        }
}
