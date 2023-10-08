package dev.llm.qianfan.chat;

import static dev.llm.qianfan.chat.JsonSchemaProperty.STRING;
import static dev.llm.qianfan.chat.JsonSchemaProperty.description;
import static dev.llm.qianfan.chat.JsonSchemaProperty.enums;
import static dev.llm.qianfan.chat.Message.functionMessage;
import static dev.llm.qianfan.chat.Message.userMessage;
import static dev.llm.qianfan.chat.Role.ASSISTANT;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import dev.llm.qianfan.QianfanClient;
import dev.llm.qianfan.RateLimitAwareTest;

class ChatCompletionTest extends RateLimitAwareTest {

        private static final String USER_MESSAGE = "Write exactly the following 2 words: 'hello world'";

        private final QianfanClient client = QianfanClient.builder()
                        .logRequests()
                        .logResponses()
                        .build();

        @Test
        void testSimpleApi() {

                String response = client.chatCompletion(USER_MESSAGE).execute();

                assertThat(response).containsIgnoringCase("hello world");
        }

        @MethodSource
        @ParameterizedTest
        void testCustomizableApi(ChatCompletionRequest request) {

                ChatCompletionResponse response = client.chatCompletion(request).execute();

                assertThat(response.result()).containsIgnoringCase("hello world");
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
        void testFunctions() {

                Message userMessage = userMessage("宁波市今天天气如何?");

                FunctionCall fc1 = FunctionCall.builder()
                                .name("get_current_weather")
                                .arguments("{ \"location\": \"Boston\"}")
                                .build();

                Example e2 = Example.builder()
                                .role(Role.ASSISTANT)
                                .content(null)
                                .functionCall(fc1)
                                .build();

                ChatCompletionRequest request = ChatCompletionRequest.builder()
                                .messages(userMessage)
                                .addFunction(Function.builder()
                                                .name("get_current_weather")
                                                .description("获得指定地点的天气")
                                                .addParameter("location", STRING, description(
                                                                "省，市名，例如：浙江省，宁波"))
                                                .addOptionalParameter("unit", STRING, enums(Unit.class))
                                                .addExample(
                                                                Example.builder()
                                                                                .role(Role.USER)
                                                                                .content("今天上海天气如何？")
                                                                                .build())
                                                .addExample(e2)
                                                .addExample(Example.builder()
                                                                .role(Role.FUNCTION)
                                                                .name("get_current_weather")
                                                                .content("{\"temperature\": \"25\", \"unit\": \"centigrade\", \"description\": \"cloud\"}")
                                                                .build())
                                                .build())
                                .build();

                ChatCompletionResponse response = client.chatCompletion(request)
                                .execute();

                Message assistantMessage = Message.assistantMessage(response.result());
                assertThat(assistantMessage.role()).isEqualTo(ASSISTANT);
                assertThat(assistantMessage.content()).isNull();

                FunctionCall functionCall = assistantMessage.functionCall();
                assertThat(functionCall.name()).isEqualTo("get_current_weather");
                assertThat(functionCall.arguments()).isNotBlank();

                Map<String, Object> arguments = functionCall.argumentsAsMap();
                assertThat(arguments).hasSize(1);
                assertThat(arguments.get("location").toString()).contains("宁波");

                String location = functionCall.argument("location");
                String unit = functionCall.argument("unit");

                String weatherApiResponse = getCurrentWeather(location, unit == null ? null : Unit.valueOf(unit));

                Message functionMessage = functionMessage("get_current_weather", weatherApiResponse);

                ChatCompletionRequest secondRequest = ChatCompletionRequest.builder()
                                .messages(userMessage, assistantMessage, functionMessage)
                                .build();

                ChatCompletionResponse secondResponse = client.chatCompletion(secondRequest).execute();

                assertThat(secondResponse.result().contains("22"));
        }

        public static String getCurrentWeather(String location, Unit unit) {
                System.out.println(location);
                System.out.println(unit);
                return "{ \"temperature\": 22, \"unit\": \"celsius\", \"description\": \"Sunny\" }";
        }

        enum Unit {
                摄氏度, 华氏度
        }
}
