package dev.llm.qianfan.embedding;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import dev.llm.qianfan.Model;
import dev.llm.qianfan.QianfanClient;
import dev.llm.qianfan.RateLimitAwareTest;

public class EmbeddingsTest extends RateLimitAwareTest {

    private static final String INPUT = "hello";

    private final QianfanClient client = QianfanClient.builder()
            .logRequests()
            .logResponses()
            .build();

    @Test
    void testSimpleApi() {

        List<Double> embedding = client.embedding("hello", Model.EMBEDDING_V1.toString()).execute();

        assertThat(embedding).hasSize(384);
    }

    @MethodSource
    @ParameterizedTest
    void testCustomizableApi(EmbeddingRequest request) {

        EmbeddingResponse response = client.embedding(request).execute();


        assertThat(response.data()).hasSize(1);
        assertThat(response.data().get(0).embedding()).hasSize(384);

        assertThat(response.embedding()).hasSize(384);
    }

    static Stream<Arguments> testCustomizableApi() {
        return Stream.of(
                Arguments.of(
                        EmbeddingRequest.builder()
                                .input(singletonList(INPUT))
                                .build()
                ),
                Arguments.of(
                        EmbeddingRequest.builder()
                                .input(INPUT)
                                .build()
                )
        );
    }
}
