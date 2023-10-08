package dev.llm.qianfan.chat;

import java.util.Objects;

public class PluginUsage {

    /*
     * plugin名称，chatFile：chatfile插件消耗的tokens
     */
    private final String name;

    /*
     * 解析文档tokens
     */
    private final Integer parseTokens;

    /*
     * 摘要文档tokens
     */
    private final Integer abstractTokens;

    /*
     * 检索文档tokens
     */
    private final Integer searchTokens;

    /*
     * 总tokens
     */
    private final Integer totalTokens;

    private PluginUsage(Builder builder) {
        this.name = builder.name;
        this.parseTokens = builder.parseTokens;
        this.abstractTokens = builder.abstractTokens;
        this.searchTokens = builder.searchTokens;
        this.totalTokens = builder.totalTokens;
    }

    public String name() {
        return this.name;
    }

    public Integer parseTokens() {
        return this.parseTokens;
    }

    public Integer abstractTokens() {
        return this.abstractTokens;
    }

    public Integer searchTokens() {
        return this.searchTokens;
    }

    public Integer totalTokens() {
        return this.totalTokens;
    }

    @Override
    public boolean equals(Object another) {
        if (this == another)
            return true;
        return another instanceof PluginUsage
                && equalTo((PluginUsage) another);
    }

    private boolean equalTo(PluginUsage another) {
        return Objects.equals(name, another.name)
                && Objects.equals(parseTokens, another.parseTokens)
                && Objects.equals(abstractTokens, another.abstractTokens)
                && Objects.equals(searchTokens, another.searchTokens)
                && Objects.equals(totalTokens, another.totalTokens);
    }

    @Override
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(name);
        h += (h << 5) + Objects.hashCode(parseTokens);
        h += (h << 5) + Objects.hashCode(abstractTokens);
        h += (h << 5) + Objects.hashCode(searchTokens);
        h += (h << 5) + Objects.hashCode(totalTokens);
        return h;
    }

    @Override
    public String toString() {
        return "PluginUsage{"
                + "name=" + name
                + ", parseTokens=" + parseTokens
                + ", abstractTokens=" + abstractTokens
                + ", searchTokens=" + searchTokens
                + ", totalTokens=" + totalTokens
                + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String name;
        private Integer parseTokens;
        private Integer abstractTokens;
        private Integer searchTokens;
        private Integer totalTokens;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder parseTokens(Integer parseTokens) {
            this.parseTokens = parseTokens;
            return this;
        }

        public Builder abstractTokens(Integer abstractTokens) {
            this.abstractTokens = abstractTokens;
            return this;
        }

        public Builder searchTokens(Integer searchTokens) {
            this.searchTokens = searchTokens;
            return this;
        }

        public Builder totalTokens(Integer totalTokens) {
            this.totalTokens = totalTokens;
            return this;
        }

        public PluginUsage build() {
            return new PluginUsage(this);
        }
    }
}
