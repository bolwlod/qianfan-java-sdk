package dev.llm.qianfan.embedding;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import dev.llm.qianfan.Usage;

public final class EmbeddingResponse {
    
    /*
     * 本轮对话的id
     */
    private final String id;
    
    /*
     * 回包类型，固定值“embedding_list”
     */
    private final String object;

    /*
     * 时间戳
     */
    private final Integer created;

    /*
     * embedding信息，data成员数和文本数量保持一致
     */
    private final List<EmbeddingData> data;

    /*
     * token统计信息，token数 = 汉字数+单词数*1.3 （仅为估算逻辑）
     */
    private final Usage usage;

    /*
     * 错误码
     */
    private final Integer errorCode;

    /*
     * 错误描述信息，帮助理解和解决发生的错误
     */
    private final String errorMsg;

    private EmbeddingResponse(Builder builder) {
        this.object = builder.object;
        this.data = builder.data;
        this.usage = builder.usage;
        this.id = builder.id;
        this.created = builder.created;
        this.errorCode = builder.errorCode;
        this.errorMsg = builder.errorMsg;
    }

    public String object() {
        return this.object;
    }

    public List<EmbeddingData> data() {
        return this.data;
    }

    public Usage usage() {
        return this.usage;
    }
    public String id() {
        return this.id;
    }
    public Integer created() {
        return this.created;
    }

    public List<Double> embedding() {
        return ((EmbeddingData)this.data.get(0)).embedding();
    }

    public Integer errorCode() {
        return this.errorCode;
    }

    public String errorMsg() {
        return this.errorMsg;
    }

    @Override
    public String toString() {
        return "EmbeddingResponse{" +
                "object='" + object + '\'' +
                ", id='" + id + '\'' +
                ", created=" + created +
                ", data=" + data +
                ", usage=" + usage +
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

        EmbeddingResponse that = (EmbeddingResponse) o;

        return new EqualsBuilder().append(object, that.object).append(id, that.id)
                .append(created, that.created).append(data, that.data).append(usage, that.usage).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(object).append(id).append(created).append(data).append(usage)
                .toHashCode();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private  String object;
        private  String id;
        private  Integer created;
        private  List<EmbeddingData> data;
        private  Usage usage;

        private Integer errorCode;
        private String errorMsg;

        private Builder() {
        }

        public Builder object(String object) {
            this.object = object;
            return this;
        }

        public Builder data(List<EmbeddingData> data) {
            if (data == null) {
                return this;
            } else {
                this.data = Collections.unmodifiableList(data);
                return this;
            }
        }

        public Builder usage(Usage usage) {
            this.usage = usage;
            return this;
        }
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        public Builder created(Integer created) {
            this.created = created;
            return this;
        }
        public EmbeddingResponse build() {
            return new EmbeddingResponse(this);
        }

        public Builder errorCode(Integer errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder errorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
            return this;
        }
    }
}

