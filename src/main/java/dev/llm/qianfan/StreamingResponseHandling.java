package dev.llm.qianfan;

public interface StreamingResponseHandling extends AsyncResponseHandling {
    
    public StreamingCompletionHandling onComplete(Runnable callbackHandler);
}
