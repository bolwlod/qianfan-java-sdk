package dev.llm.qianfan;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestLoggingInterceptor implements Interceptor {

    public RequestLoggingInterceptor() {
    }

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        log(request);
        return chain.proceed(request);
    }

    private static void log(Request request) {
        try {
            log.debug("Request:\n- method: {}\n- url: {}\n- headers: {}\n- body: {}",
                    new Object[] { request.method(), request.url(), Utils.printHeadersInOneLine(request.headers()), getBody(request) });
        } catch (Exception e) {
            log.warn("Failed to log request", e);
        }

    }

    private static String getBody(Request request) {
        RequestBody body = request.body();

        if (body == null){
            return "";
        }

        try {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            return buffer.readUtf8();
        } catch (Exception e) {
            log.warn("Exception happened while reading request body", e);
            return "[Exception happened while reading request body. Check logs for more details.]";
        }
    }
}
