package dev.llm.qianfan.embedding;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

import dev.llm.qianfan.Model;
import dev.llm.qianfan.QianfanClient;
import dev.llm.qianfan.RateLimitAwareTest;

public class EmbeddingsAsyncTest extends RateLimitAwareTest {

        private static final String INPUT = "hello";

        private final QianfanClient client = QianfanClient.builder()
                        .logRequests()
                        .logResponses()
                        .build();

        @Test
        void testSimpleApi() throws Exception {

                CompletableFuture<List<Double>> future = new CompletableFuture<>();

                client.embedding(INPUT, Model.EMBEDDING_V1.toString())
                                .onResponse(future::complete)
                                .onError(future::completeExceptionally)
                                .execute();

                List<Double> response = future.get(30, SECONDS);

                assertThat(response).hasSize(384);
        }

        @Test
        void testCustomizableApi() throws Exception {

                EmbeddingRequest request = EmbeddingRequest.builder()
                                .input(INPUT)
                                .build();

                CompletableFuture<EmbeddingResponse> future = new CompletableFuture<>();

                client.embedding(request)
                                .onResponse(future::complete)
                                .onError(future::completeExceptionally)
                                .execute();

                EmbeddingResponse response = future.get(30, SECONDS);

                assertThat(response.data()).hasSize(1);
                assertThat(response.data().get(0).embedding()).hasSize(384);

                assertThat(response.embedding()).hasSize(384);
        }
}
