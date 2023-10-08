package dev.llm.qianfan.chat;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import dev.llm.qianfan.Usage;

public final class ChatCompletionResponse {

    /*
     * 本轮对话的id
     */
    private final String id;

    /*
     * 回包类型
     * chat.completion：多轮对话返回
     */
    private final String object;

    /*
     * 时间戳
     */
    private final Integer created;
    
    /*
     * 表示当前子句的序号。只有在流式接口模式下会返回该字段
     */
    private final Integer sentenceId;

    /*
     * 表示当前子句是否是最后一句。只有在流式接口模式下会返回该字段
     */
    private final Boolean isEnd;

    /*
     * 当前生成的结果是否被截断
     */
    private final Boolean isTruncated;

    /*
     * 对话返回结果
     */
    private final String result;

    /*
     * 表示用户输入是否存在安全，是否关闭当前会话，清理历史会话信息
     * true：是，表示用户输入存在安全风险，建议关闭当前会话，清理历史会话信息
     * false：否，表示用户输入无安全风险
     */
    private final Boolean needClearHistory;

    /*
     * 当need_clear_history为true时，此字段会告知第几轮对话有敏感信息，如果是当前问题，ban_round=-1
     */
    private final Integer banRound;

    /*
     * token统计信息，token数 = 汉字数+单词数*1.3 （仅为估算逻辑）
     */
    private final Usage usage;

    /*
     * 由模型生成的函数调用，包含函数名称，和调用参数
     */
    private final FunctionCall functionCall;

    /*
     * 错误码
     */
    private final Integer errorCode;

    /*
     * 错误描述信息，帮助理解和解决发生的错误
     */
    private final String errorMsg;

    private ChatCompletionResponse(Builder builder) {
        this.id = builder.id;
        this.created = builder.created;
        this.object = builder.object;
        this.sentenceId = builder.sentenceId;
        this.isEnd = builder.isEnd;
        this.isTruncated = builder.isTruncated;
        this.result = builder.result;
        this.needClearHistory = builder.needClearHistory;
        this.banRound = builder.banRound;
        this.functionCall = builder.functionCall;
        this.usage = builder.usage;
        this.errorCode = builder.errorCode;
        this.errorMsg = builder.errorMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChatCompletionResponse that = (ChatCompletionResponse) o;

        return new EqualsBuilder().append(id, that.id).append(errorCode, that.errorCode)
                .append(errorMsg, that.errorMsg).append(object, that.object).append(created, that.created)
                .append(sentenceId, that.sentenceId).append(isEnd, that.isEnd)
                .append(isTruncated, that.isTruncated)
                .append(result, that.result).append(needClearHistory, that.needClearHistory)
                .append(banRound, that.banRound).append(usage, that.usage).append(functionCall, that.functionCall)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(errorCode).append(errorMsg).append(object).append(created)
                .append(sentenceId).append(isEnd).append(isTruncated).append(result).append(needClearHistory)
                .append(banRound).append(usage).append(functionCall).toHashCode();
    }

    @Override
    public String toString() {
        return "ChatCompletionResponse{" +
                "id='" + id + '\'' +
                ", errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", object='" + object + '\'' +
                ", created=" + created +
                ", sentence_id=" + sentenceId +
                ", is_end=" + isEnd +
                ", is_truncated=" + isTruncated +
                ", result='" + result + '\'' +
                ", need_clear_history=" + needClearHistory +
                ", ban_round=" + banRound +
                ", usage=" + usage +
                ", function_call=" + functionCall +
                '}';
    }

    public String id() {
        return this.id;
    }

    public Integer errorCode() {
        return this.errorCode;
    }

    public String errorMsg() {
        return this.errorMsg;
    }

    public String object() {
        return this.object;
    }

    public Integer created() {
        return this.created;
    }

    public Integer sentenceId() {
        return this.sentenceId;
    }

    public Boolean isEnd() {
        return this.isEnd;
    }

    public Boolean isTruncated() {
        return this.isTruncated;
    }

    public String result() {
        return this.result;
    }

    public Boolean needClearHistory() {
        return this.needClearHistory;
    }

    public Integer banRound() {
        return this.banRound;
    }

    public Usage usage() {
        return this.usage;
    }

    public FunctionCall functionCall() {
        return this.functionCall;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String id;
        private String object;
        private Integer created;
        private Integer sentenceId;
        private Boolean isEnd;
        private Boolean isTruncated;
        private String result;
        private Boolean needClearHistory;
        private Integer banRound;
        private Usage usage;
        private FunctionCall functionCall;
        private Integer errorCode;
        private String errorMsg;

        private Builder() {
        }

        public Builder errorCode(Integer errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder errorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
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

        public Builder sentenceId(Integer sentenceId) {
            this.sentenceId = sentenceId;
            return this;
        }

        public Builder isEnd(Boolean isEnd) {
            this.isEnd = isEnd;
            return this;
        }

        public Builder result(String result) {
            this.result = result;
            return this;
        }

        public Builder needClearHistory(Boolean needClearHistory) {
            this.needClearHistory = needClearHistory;
            return this;
        }

        public Builder banRound(Integer banRound) {
            this.banRound = banRound;
            return this;
        }

        public Builder usage(Usage usage) {
            this.usage = usage;
            return this;
        }

        public Builder functionCall(FunctionCall functionCall) {
            this.functionCall = functionCall;
            return this;
        }

        public Builder isTruncated(Boolean isTruncated) {
            this.isTruncated = isTruncated;
            return this;
        }

        public ChatCompletionResponse build() {
            return new ChatCompletionResponse(this);
        }
    }
}
