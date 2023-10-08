package dev.llm.qianfan;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.List;

import dev.llm.qianfan.chat.ChatCompletionRequest;
import dev.llm.qianfan.chat.ChatCompletionResponse;
import dev.llm.qianfan.completion.CompletionRequest;
import dev.llm.qianfan.completion.CompletionResponse;
import dev.llm.qianfan.embedding.EmbeddingRequest;
import dev.llm.qianfan.embedding.EmbeddingResponse;
import dev.llm.qianfan.token.TokenResponse;
import dev.llm.qianfan.txt2img.Txt2ImgRequest;
import dev.llm.qianfan.txt2img.Txt2ImgResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Slf4j
public class QianfanClient {

    public static final String GRANT_TYPE = "client_credentials";
    public static final String BASE_URL = "https://aip.baidubce.com/";

    public static final String QIANFAN_AK = "QIANFAN_AK";
    public static final String QIANFAN_SK = "QIANFAN_SK";

    private final String baseUrl;
    private String token;

    private final OkHttpClient okHttpClient;
    private final QianfanApi qianfanApi;
    private String apiKey;
    private String secretKey;
    private final boolean logStreamingResponses;

    private QianfanClient(Builder builder) {

        this.baseUrl = builder.baseUrl;
        this.apiKey = builder.apiKey;
        this.secretKey = builder.secretKey;
        this.token = builder.token;

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .callTimeout(builder.callTimeout)
                .connectTimeout(builder.connectTimeout)
                .readTimeout(builder.readTimeout)
                .writeTimeout(builder.writeTimeout);

        if (builder.token == null && (builder.apiKey == null || builder.secretKey == null)) {
            throw new IllegalArgumentException("Token must be defined or API Key and Secret Key must be defined");
        } else {
            if (builder.proxy != null) {
                okHttpClientBuilder.proxy(builder.proxy);
            }

            if (builder.logRequests) {
                okHttpClientBuilder.addInterceptor(new RequestLoggingInterceptor());
            }

            if (builder.logResponses) {
                okHttpClientBuilder.addInterceptor(new ResponseLoggingInterceptor());
            }

            this.logStreamingResponses = builder.logStreamingResponses;
            this.okHttpClient = okHttpClientBuilder.build();

            // core logic
            Retrofit retrofit = (new Retrofit.Builder())
                    .baseUrl(builder.baseUrl)
                    .client(this.okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(Utils.GSON))
                    .build();

            this.qianfanApi = retrofit.create(QianfanApi.class);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String baseUrl = BASE_URL;
        private String apiKey = System.getenv(QIANFAN_AK);
        private String secretKey = System.getenv(QIANFAN_SK);
        private String token;

        private Duration callTimeout = Duration.ofSeconds(60L);
        private Duration connectTimeout = Duration.ofSeconds(60L);
        private Duration readTimeout = Duration.ofSeconds(60L);
        private Duration writeTimeout = Duration.ofSeconds(60L);
        private Proxy proxy;
        private boolean logRequests = true;
        private boolean logResponses = true;
        private boolean logStreamingResponses = false;

        private Builder() {

        }

        public Builder baseUrl(@NonNull String baseUrl) {
            this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
            return this;

        }

        public Builder apiKey(@NonNull String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder secretKey(@NonNull String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public Builder token(@NonNull String token) {
            this.token = token;
            return this;
        }

        public Builder callTimeout(@NonNull Duration callTimeout) {
            this.callTimeout = callTimeout;
            return this;

        }

        public Builder connectTimeout(@NonNull Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(@NonNull Duration readTimeout) {
            this.readTimeout = readTimeout;
            return this;

        }

        public Builder writeTimeout(@NonNull Duration writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public Builder proxy(Proxy.Type type, String ip, int port) {
            this.proxy = new Proxy(type, new InetSocketAddress(ip, port));
            return this;
        }

        public Builder proxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public Builder logRequests() {
            return this.logRequests(true);
        }

        public Builder logRequests(Boolean logRequests) {
            if (logRequests == null) {
                logRequests = false;
            }

            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses() {
            return this.logResponses(true);
        }

        public Builder logResponses(Boolean logResponses) {
            if (logResponses == null) {
                logResponses = false;
            }

            this.logResponses = logResponses;
            return this;
        }

        public Builder logStreamingResponses() {
            return this.logStreamingResponses(true);
        }

        public Builder logStreamingResponses(Boolean logStreamingResponses) {
            if (logStreamingResponses == null) {
                logStreamingResponses = false;
            }

            this.logStreamingResponses = logStreamingResponses;
            return this;
        }

        public QianfanClient build() {
            return new QianfanClient(this);
        }
    }

    public SyncOrAsyncOrStreaming<CompletionResponse> completion(CompletionRequest request) {

        refreshToken();

        String serviceName = request.serviceName();

        CompletionRequest syncRequest = CompletionRequest.builder()
                .from(request)
                .stream(null)
                .build();

        return new RequestExecutor<>(
                qianfanApi.completions(serviceName, syncRequest, this.token),
                (r) -> r,
                okHttpClient,
                formatUrl("rpc/2.0/ai_custom/v1/wenxinworkshop/completions/" + serviceName + "?access_token="
                        + this.token),
                () -> CompletionRequest.builder().from(request).stream(true).build(),
                CompletionResponse.class,
                (r) -> r,
                logStreamingResponses);
    }

    public SyncOrAsyncOrStreaming<String> completion(String prompt, String serviceName) {

        refreshToken();
        CompletionRequest request = CompletionRequest.builder()
                .prompt(prompt)
                .build();

        CompletionRequest syncRequest = CompletionRequest.builder()
                .from(request)
                .stream(null)
                .build();

        return new RequestExecutor<>(
                this.qianfanApi.completions(serviceName, syncRequest, this.token),
                CompletionResponse::result,
                this.okHttpClient,
                formatUrl("rpc/2.0/ai_custom/v1/wenxinworkshop/completions/" + serviceName + "?access_token="
                        + this.token),
                () -> CompletionRequest.builder()
                        .from(request)
                        .stream(true)
                        .build(),
                CompletionResponse.class,
                CompletionResponse::result,
                logStreamingResponses);
    }

    public SyncOrAsyncOrStreaming<ChatCompletionResponse> chatCompletion(ChatCompletionRequest request) {

        refreshToken();

        String model = request.model();

        ChatCompletionRequest syncRequest = ChatCompletionRequest.builder()
                .from(request)
                .stream(null)
                .build();

        return new RequestExecutor<>(
                this.qianfanApi.chatCompletions(model, syncRequest, this.token),
                (r) -> r,
                this.okHttpClient,
                formatUrl("rpc/2.0/ai_custom/v1/wenxinworkshop/chat/" + model + "?access_token=" + this.token),
                () -> ChatCompletionRequest.builder().from(request).stream(true).build(),
                ChatCompletionResponse.class,
                (r) -> r,
                logStreamingResponses);
    }

    public SyncOrAsyncOrStreaming<String> chatCompletion(String userMessage, String model) {
        refreshToken();
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .addUserMessage(userMessage)
                .build();

        ChatCompletionRequest syncRequest = ChatCompletionRequest.builder()
                .from(request)
                .stream(null)
                .build();

        return new RequestExecutor<>(
                this.qianfanApi.chatCompletions(model, syncRequest, this.token),
                ChatCompletionResponse::result,
                this.okHttpClient,
                formatUrl("rpc/2.0/ai_custom/v1/wenxinworkshop/chat/" + model + "?access_token=" + this.token),
                () -> ChatCompletionRequest.builder().from(request).stream(true).build(),
                ChatCompletionResponse.class,
                ChatCompletionResponse::result,
                logStreamingResponses);
    }

    public SyncOrAsyncOrStreaming<String> chatCompletion(String userMessage) {

        return chatCompletion(userMessage, Model.ERNIE_BOT_TURBO.stringValue());
    }

    public SyncOrAsync<EmbeddingResponse> embedding(EmbeddingRequest request) {
        refreshToken();
        String model = request.model();

        return new RequestExecutor<>(
                this.qianfanApi.embeddings(model, request, this.token),
                (r) -> r);
    }

    public SyncOrAsync<List<Double>> embedding(String input, String model) {
        refreshToken();
        EmbeddingRequest request = EmbeddingRequest.builder()
                .input(new String[] { input })
                .build();
        return new RequestExecutor<>(
                this.qianfanApi.embeddings(model, request, this.token),
                EmbeddingResponse::embedding);
    }

    public SyncOrAsync<List<String>> txt2img(Txt2ImgRequest request) {
        refreshToken();
        String serviceName = request.serviceName();

        return new RequestExecutor<>(
                this.qianfanApi.txt2img(serviceName, request, this.token),
                Txt2ImgResponse::images);
    }

    public SyncOrAsync<List<String>> txt2img(String prompt, Integer width, Integer height, String serviceName) {
        refreshToken();
        Txt2ImgRequest request = Txt2ImgRequest.builder()
                .prompt(prompt)
                .width(width)
                .height(height)
                .build(); 

        return new RequestExecutor<>(
                this.qianfanApi.txt2img(serviceName, request, this.token),
                Txt2ImgResponse::images);
    }

    private String refreshToken() {
        if (this.token == null) {
            RequestExecutor<String, TokenResponse, String> executor = new RequestExecutor<>(
                    this.qianfanApi.getToken(GRANT_TYPE, this.apiKey, this.secretKey),
                    TokenResponse::accessToken);
            String response = executor.execute();
            log.debug("response token is :{}", response);
            this.token = response;
        }
        return this.token;
    }

    public void shutdown() {
        this.okHttpClient.dispatcher().executorService().shutdown();
        this.okHttpClient.connectionPool().evictAll();
        Cache cache = this.okHttpClient.cache();
        if (cache != null) {
            try {
                cache.close();
            } catch (IOException ioe) {
                log.error("Failed to close cache", ioe);
            }
        }

    }

    private String formatUrl(String endpoint) {
        return this.baseUrl + endpoint;
    }
}
