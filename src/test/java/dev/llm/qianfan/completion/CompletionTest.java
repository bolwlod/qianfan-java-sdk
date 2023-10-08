package dev.llm.qianfan.completion;

import static org.assertj.core.api.Assertions.assertThat;

import dev.llm.qianfan.QianfanClient;
import dev.llm.qianfan.RateLimitAwareTest;

class CompletionTest extends RateLimitAwareTest {

    private static final String PROMPT = "write exactly the following 2 words: 'hello world'";
    private final String serviceName = "";

    private final QianfanClient client = QianfanClient.builder()
            .logRequests()
            .logResponses()
            .build();

    // @Test
    void testSimpleApi() {

        String response = client.completion(PROMPT, serviceName).execute();

        assertThat(response).containsIgnoringCase("hello world");
    }

    // @Test
    void testCustomizableApi() {

        CompletionRequest request = CompletionRequest.builder()
                .prompt(PROMPT)
                .build();

        CompletionResponse response = client.completion(request).execute();

        assertThat(response.result()).containsIgnoringCase("hello world");
    }
}
