package dev.llm.qianfan;

import java.util.function.Consumer;

public interface StreamingCompletionHandling {

    public ErrorHandling onError(Consumer<Throwable> errorHandler);

    public ErrorHandling ignoreErrors();
}
