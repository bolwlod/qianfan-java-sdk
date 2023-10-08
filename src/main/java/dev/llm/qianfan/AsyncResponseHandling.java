package dev.llm.qianfan;

import java.util.function.Consumer;

public interface AsyncResponseHandling {

    public ErrorHandling onError(Consumer<Throwable> errorHandler);

    public ErrorHandling ignoreErrors();
}