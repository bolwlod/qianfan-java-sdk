package dev.llm.qianfan;

import org.junit.jupiter.api.BeforeEach;

public abstract class RateLimitAwareTest {

    @BeforeEach
    void waitInOrderToAvoid429ResponseFromOpenAi() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignoring intentionally
        }
    }
}
