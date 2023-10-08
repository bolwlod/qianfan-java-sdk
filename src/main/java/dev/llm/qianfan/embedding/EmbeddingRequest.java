package dev.llm.qianfan.embedding;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import dev.llm.qianfan.Model;

public final class EmbeddingRequest {

    transient private final String model;

    /*
     * 输入文本以获取embeddings。说明：
     * （1）文本数量不超过16
     * （2）每个文本长度不超过 384个token
     * （3）输入文本不能为空，如果为空会报错
     */
    private final List<String> input;

    /*
     * 表示最终用户的唯一标识符，可以监视和检测滥用行为，防止接口恶意调用
     */
    private final String userId;

    private EmbeddingRequest(Builder builder) {
        this.model = builder.model;
        this.input = builder.input;
        this.userId = builder.userId;
    }

    public String model() {
        return this.model;
    }

    public List<String> input() {
        return this.input;
    }

    public String userId() {
        return this.userId;
    }

    public boolean equals(Object another) {
        if (this == another) {
            return true;
        } else {
            return another instanceof EmbeddingRequest
                    && this.equalTo((EmbeddingRequest) another);
        }
    }

    private boolean equalTo(EmbeddingRequest another) {
        return Objects.equals(this.model, another.model) && Objects.equals(this.input, another.input)
                && Objects.equals(this.userId, another.userId);
    }

    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.model);
        h += (h << 5) + Objects.hashCode(this.input);
        h += (h << 5) + Objects.hashCode(this.userId);
        return h;
    }

    public String toString() {
        return "EmbeddingRequest{model=" + this.model + ", input=" + this.input + ", user=" + this.userId + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String model;
        private List<String> input;
        private String userId;

        private Builder() {
            this.model = Model.EMBEDDING_V1.stringValue();
        }

        public Builder model(@NotNull String model) {
            this.model = model;
            return this;
        }

        public Builder input(@NotNull String... input) {
            return this.input(Arrays.asList(input));
        }

        public Builder input(@NotNull List<String> input) {
            if (input == null) {
                return this;
            } else {
                this.input = Collections.unmodifiableList(input);
                return this;
            }
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public EmbeddingRequest build() {
            return new EmbeddingRequest(this);
        }
    }
}
