package dev.llm.qianfan;

import java.util.function.Consumer;

public  interface SyncOrAsync<ResponseContent> {
    
    public ResponseContent execute();

    public AsyncResponseHandling onResponse(Consumer<ResponseContent> responseHandler);
}
