package dev.llm.qianfan;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseLoggingInterceptor implements Interceptor {

    public ResponseLoggingInterceptor() {
    }

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        log(response);
        return response;
    }

    static void log(Response response) {
        try {
            log.debug("Response:\n- status code: {}\n- headers: {}\n- body: {}", new Object[] { response.code(),
                    Utils.printHeadersInOneLine(response.headers()), getBody(response) });
        } catch (IOException ioe) {
            log.warn("Failed to log response", ioe);
        }

    }

    private static String getBody(Response response) throws IOException {
        return isEventStream(response) ? "[skipping response body due to streaming]"
                : response.peekBody(Long.MAX_VALUE).string();
    }

    private static boolean isEventStream(Response response) {
        String contentType = response.header("content-type");
        return contentType != null && contentType.contains("event-stream");
    }
}
