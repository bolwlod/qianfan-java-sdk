package dev.llm.qianfan;

import static dev.llm.qianfan.Utils.toException;

import java.io.IOException;
import java.util.function.Function;

import retrofit2.Call;

public class SyncRequestExecutor<Response, ResponseContent> {

    private final Call<Response> call;
    private final Function<Response, ResponseContent> responseContentExtractor;

    public SyncRequestExecutor(Call<Response> call, Function<Response, ResponseContent> responseContentExtractor) {
        this.call = call;
        this.responseContentExtractor = responseContentExtractor;
    }

    public ResponseContent execute() {
        try {
            retrofit2.Response<Response> retrofitResponse = this.call.execute();

            if (retrofitResponse.isSuccessful()) {
                Response response = retrofitResponse.body();
                return this.responseContentExtractor.apply(response);
            } else {
                throw toException(retrofitResponse);
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
