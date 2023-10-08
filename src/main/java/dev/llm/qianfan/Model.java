package dev.llm.qianfan;

public enum Model {
    
    // chat models
    ERNIE_BOT("completions"),
    ERNIE_BOT_TURBO("eb-instant"),
    BLOOMZ_7B("bloomz_7b1"),
    QIANFAN_BLOOMZ_7B_COMPRESSED("qianfan_bloomz_7b_compressed"),
    LLAMA_2_7B_CHAT("llama_2_7b"),
    LLAMA_2_13B_CHAT("llama_2_13b"),
    LLAMA_2_70B_CHAT("llama_2_70b"),
    QIANFAN_CHINESE_LLAMA_2_7B("qianfan_chinese_llama_2_7b"),
    CHATGLM2_6B_32K("chatglm2_6b_32k"),
    AQUILACHAT_7B("aquilachat_7b"),

    // completion models

    // embedding models
    EMBEDDING_V1("embedding-v1"),
    BGE_LARGE_ZH("bge_large_zh"),
    BGE_LARGE_EN("bge_large_en");

    // image models

    private final String stringValue;

    private Model(String stringValue) {
        this.stringValue = stringValue;
    }

    public String stringValue() {
        return this.stringValue;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }

    
}
