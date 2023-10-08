package dev.llm.qianfan;

import dev.llm.qianfan.chat.ChatCompletionRequest;
import dev.llm.qianfan.chat.ChatCompletionResponse;
import dev.llm.qianfan.completion.CompletionRequest;
import dev.llm.qianfan.completion.CompletionResponse;
import dev.llm.qianfan.embedding.EmbeddingRequest;
import dev.llm.qianfan.embedding.EmbeddingResponse;
import dev.llm.qianfan.token.TokenResponse;
import dev.llm.qianfan.txt2img.Txt2ImgRequest;
import dev.llm.qianfan.txt2img.Txt2ImgResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface QianfanApi {

        @POST("rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions")
        @Headers({ "Content-Type: application/json" })
        Call<ChatCompletionResponse> chatCompletions(
                        @Body ChatCompletionRequest request,
                        @Query("access_token") String accessToken);

        @POST("rpc/2.0/ai_custom/v1/wenxinworkshop/chat/{model}")
        @Headers({ "Content-Type: application/json" })
        Call<ChatCompletionResponse> chatCompletions(
                        @Path(value = "model", encoded = false) String model,
                        @Body ChatCompletionRequest request,
                        @Query("access_token") String accessToken);
        
        @POST("rpc/2.0/ai_custom/v1/wenxinworkshop/completions/{serviceName}")
        @Headers({"Content-Type: application/json"})
        Call<CompletionResponse> completions(
                        @Path(value = "serviceName", encoded = false) String serviceName, 
                        @Body CompletionRequest request, 
                        @Query("access_token") String accessToken);

        @POST("rpc/2.0/ai_custom/v1/wenxinworkshop/embeddings/{model}")
        @Headers({ "Content-Type: application/json" })
        Call<EmbeddingResponse> embeddings(
                        @Path(value = "model", encoded = false) String model,
                        @Body EmbeddingRequest request, @Query("access_token") String accessToken);

        @POST("rpc/2.0/ai_custom/v1/wenxinworkshop/txt2img/{serviceName}")
        @Headers({ "Content-Type: application/json" })
        Call<Txt2ImgResponse> txt2img(
                        @Path(value = "serviceName", encoded = false) String serviceName,
                        @Body Txt2ImgRequest request, @Query("access_token") String accessToken);

        @GET("oauth/2.0/token")
        @Headers({ "Content-Type: application/json" })
        Call<TokenResponse> getToken(
                        @Query("grant_type") String grantType, 
                        @Query("client_id") String clientId,
                        @Query("client_secret") String clientSecret);
                        

}
