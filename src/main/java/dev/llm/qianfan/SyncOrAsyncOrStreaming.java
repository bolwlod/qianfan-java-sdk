package dev.llm.qianfan;

import java.util.function.Consumer;

public interface SyncOrAsyncOrStreaming<ResponseContent> extends SyncOrAsync<ResponseContent> {
    
    public StreamingResponseHandling onPartialResponse(Consumer<ResponseContent> responseHandler);
}
