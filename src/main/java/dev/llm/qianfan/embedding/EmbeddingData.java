package dev.llm.qianfan.embedding;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class EmbeddingData {

    /*
     * 固定值"embedding"
     */
    private final String object;

    /*
     * embedding 内容
     */
    private final List<Double> embedding;

    /*
     * 序号
     */
    private final Integer index;

    private EmbeddingData(Builder builder) {
        this.embedding = builder.embedding;
        this.index = builder.index;
        this.object = builder.object;
    }

    public List<Double> embedding() {
        return this.embedding;
    }

    public Integer index() {
        return this.index;
    }

    public String object() {
        return this.object;
    }

    @Override
    public String toString() {
        return "EmbeddingData{" +
                "object='" + object + '\'' +
                ", embedding=" + embedding +
                ", index=" + index +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmbeddingData that = (EmbeddingData) o;

        return new EqualsBuilder().append(object, that.object)
                .append(embedding, that.embedding).append(index, that.index).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(object).append(embedding).append(index).toHashCode();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private List<Double> embedding;
        private Integer index;
        private String object;

        private Builder() {
        }

        public Builder embedding(List<Double> embedding) {
            if (embedding == null) {
                return this;
            } else {
                this.embedding = Collections.unmodifiableList(embedding);
                return this;
            }
        }

        public Builder index(Integer index) {
            this.index = index;
            return this;
        }

        public Builder object(String object) {
            this.object = object;
            return this;
        }

        public EmbeddingData build() {
            return new EmbeddingData(this);
        }
    }
}
