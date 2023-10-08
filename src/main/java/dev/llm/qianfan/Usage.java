package dev.llm.qianfan;

import java.util.List;
import java.util.Objects;

import dev.llm.qianfan.chat.PluginUsage;

public final class Usage {

    /*
     * 问题tokens数
     */
    private final Integer promptTokens;

    /*
     * 回答tokens数
     */
    private final Integer completionTokens;

    /*
     * tokens总数
     */
    private final Integer totalTokens;

    /*
     * plugin消耗的tokens
     */
    private final List<PluginUsage> pluginUsages;

    private Usage(Builder builder) {
        this.promptTokens = builder.promptTokens;
        this.completionTokens = builder.completionTokens;
        this.totalTokens = builder.totalTokens;
        this.pluginUsages = builder.pluginUsages;
    }

    public Integer promptTokens() {
        return promptTokens;
    }

    public Integer completionTokens() {
        return completionTokens;
    }

    public Integer totalTokens() {
        return totalTokens;
    }

    public List<PluginUsage> pluginUsages() {
        return pluginUsages;
    }

    @Override
    public boolean equals(Object another) {
        if (this == another)
            return true;
        return another instanceof Usage
                && equalTo((Usage) another);
    }

    private boolean equalTo(Usage another) {
        return Objects.equals(promptTokens, another.promptTokens)
                && Objects.equals(completionTokens, another.completionTokens)
                && Objects.equals(totalTokens, another.totalTokens);
    }

    @Override
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(promptTokens);
        h += (h << 5) + Objects.hashCode(completionTokens);
        h += (h << 5) + Objects.hashCode(totalTokens);
        return h;
    }

    @Override
    public String toString() {
        return "Usage{"
                + "promptTokens=" + promptTokens
                + ", completionTokens=" + completionTokens
                + ", totalTokens=" + totalTokens
                + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Integer promptTokens;
        private Integer completionTokens;
        private Integer totalTokens;
        private List<PluginUsage> pluginUsages;

        private Builder() {
        }

        public Builder promptTokens(Integer promptTokens) {
            this.promptTokens = promptTokens;
            return this;
        }

        public Builder completionTokens(Integer completionTokens) {
            this.completionTokens = completionTokens;
            return this;
        }

        public Builder totalTokens(Integer totalTokens) {
            this.totalTokens = totalTokens;
            return this;
        }

        public Builder pluginUsages(List<PluginUsage> pluginUsages) {
            this.pluginUsages = pluginUsages;
            return this;
        }

        public Usage build() {
            return new Usage(this);
        }
    }
}