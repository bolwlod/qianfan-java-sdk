package dev.llm.qianfan.completion;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import dev.llm.qianfan.Usage;

public final class CompletionResponse {

    /*
     * 本轮对话的id
     */
    private final String id;

    /*
     * 回包类型。
     * completion：文本生成返回
     */
    private final String object;

    /*
     * 时间戳
     */
    private final Integer created;
    
    /*
     * 表示当前子句的序号。只有在流式接口模式下会返回该字段
     */
    private final String sentenceId;
    
    /*
     * 表示当前子句是否是最后一句。只有在流式接口模式下会返回该字段
     */
    private final boolean isEnd;

    /*
     * 对话返回结果
     */
    private final String result;
    
    /*
     * 1：表示输入内容无安全风险
     * 0：表示输入内容有安全风险
     */
    private final boolean isSafe;

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

    private CompletionResponse(Builder builder) {
        this.id = builder.id;
        this.created = builder.created;
        this.object = builder.object;
        this.result = builder.result;
        this.sentenceId = builder.sentenceId;
        this.isSafe = builder.isSafe;
        this.isEnd = builder.isEnd;
        this.usage = builder.usage;
        this.errorCode = builder.errorCode;
        this.errorMsg = builder.errorMsg;
    }

    public String id() {
        return this.id;
    }

    public String object() {
        return this.object;
    }

    public String result() {
        return this.result;
    }

    public Integer created() {
        return this.created;
    }

    public Usage usage() {
        return this.usage;
    }

    public boolean isEnd() {
        return this.isEnd;
    }

    public boolean isSafe() {
        return this.isSafe;
    }

    public String sentenceId() {
        return this.sentenceId;
    }

    public Integer errorCode() {
        return this.errorCode;
    }

    public String errorMsg() {
        return this.errorMsg;
    }

    @Override
    public String toString() {
        return "CompletionResponse{" +
                "id='" + id + '\'' +
                ", object='" + object + '\'' +
                ", result='" + result + '\'' +
                ", isEnd=" + isEnd +
                ", isSafe=" + isSafe +
                ", created=" + created +
                ", sentenceId='" + sentenceId + '\'' +
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

        CompletionResponse that = (CompletionResponse) o;

        return new EqualsBuilder().append(isEnd, that.isEnd).append(isSafe, that.isSafe)
                .append(id, that.id).append(object, that.object).append(result, that.result)
                .append(created, that.created)
                .append(sentenceId, that.sentenceId).append(usage, that.usage).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(object).append(result).append(isEnd).append(isSafe)
                .append(created).append(sentenceId).append(usage).toHashCode();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String id;
        private String object;
        private String result;
        private boolean isEnd;
        private boolean isSafe;
        private Integer created;
        private String sentenceId;
        private Usage usage;

        private Integer errorCode;
        private String errorMsg;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder created(Integer created) {
            this.created = created;
            return this;
        }

        public Builder object(String object) {
            this.object = object;
            return this;
        }

        public Builder result(String result) {
            this.result = result;
            return this;
        }

        public Builder isEnd(boolean isEnd) {
            this.isEnd = isEnd;
            return this;
        }

        public Builder isSafe(boolean isSafe) {
            this.isSafe = isSafe;
            return this;
        }

        public Builder usage(Usage usage) {
            this.usage = usage;
            return this;
        }

        public Builder sentenceId(String sentenceId) {
            this.sentenceId = sentenceId;
            return this;
        }

        public Builder errorCode(Integer errorCode) {
            this.errorCode = errorCode;
            return this;
        }   

        public Builder errorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
            return this;
        }  

        public CompletionResponse build() {
            return new CompletionResponse(this);
        }
    }
}
