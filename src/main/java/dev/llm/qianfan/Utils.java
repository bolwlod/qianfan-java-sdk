package dev.llm.qianfan;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.llm.qianfan.chat.MessageTypeAdapter;
import okhttp3.Headers;
import retrofit2.Response;

public class Utils {

    public static final Gson GSON;

    static {
        GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapterFactory(MessageTypeAdapter.MESSAGE_TYPE_ADAPTER_FACTORY)
            .create();
    }
    
    Utils() {
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return GSON.fromJson(json, type);
    }

    public static String printHeadersInOneLine(Headers headers) {
        return (String) StreamSupport.stream(headers.spliterator(), false).map((header) -> {
            String headerKey = (String) header.component1();
            String headerValue = (String) header.component2();

            return String.format("[%s: %s]", headerKey, headerValue);
        }).collect(Collectors.joining(", "));
    }

    public static RuntimeException toException(Response<?> response) throws IOException {
        return new QianfanHttpException(response.code(), response.errorBody().string());
    }

    public static RuntimeException toException(okhttp3.Response response) throws IOException {
        return new QianfanHttpException(response.code(), response.body().string());
    }
}