package dev.llm.qianfan.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import dev.llm.qianfan.Model;
import lombok.NonNull;

public final class ChatCompletionRequest {

    transient private final String model;

    /*
     * 聊天上下文信息。说明：
     * （1）messages成员不能为空，1个成员表示单轮对话，多个成员表示多轮对话
     * （2）最后一个message为当前请求的信息，前面的message为历史对话信息
     * （3）必须为奇数个成员，成员中message的role必须依次为user(or function)、assistant
     * （4）最后一个message的content长度（即此轮对话的问题）不能超过3000 token；
     *     如果messages中content总长度大于3000 token，系统会依次遗忘最早的历史会话，直到content的总长度不超过3000 token
     */
    private final List<Message> messages;

    /*
     * 一个可触发函数的描述列表
     */
    private final List<Function> functions;

    /*
     * 说明：
     * （1）较高的数值会使输出更加随机，而较低的数值会使其更加集中和确定
     * （2）默认0.95，范围 (0, 1.0]，不能为0
     * （3）建议该参数和top_p只设置1个
     * （4）建议top_p和temperature不要同时更改
     */
    private final Double temperature;

    /*
     * 说明：
     * （1）影响输出文本的多样性，取值越大，生成文本的多样性越强
     * （2）默认0.8，取值范围 [0, 1.0]
     * （3）建议该参数和temperature只设置1个
     * （4）建议top_p和temperature不要同时更改
     */
    private final Double topP;

    /*
     * 通过对已生成的token增加惩罚，减少重复生成的现象。说明：
     * （1）值越大表示惩罚越大
     * （2）默认1.0，取值范围：[1.0, 2.0]
     */
    private final Double penaltyScore;

    /*
     * 是否以流式接口的形式返回数据，默认false
     */
    private final Boolean stream;

    /*
     * 表示最终用户的唯一标识符，可以监视和检测滥用行为，防止接口恶意调用
     */
    private final String userId;

    private ChatCompletionRequest(Builder builder) {
        this.model = builder.model;
        this.messages = builder.messages;
        this.temperature = builder.temperature;
        this.topP = builder.topP;
        this.stream = builder.stream;
        this.penaltyScore = builder.penaltyScore;
        this.userId = builder.userId;
        this.functions = builder.functions;
    }

    public String model(){
        return this.model;
    }

    public List<Message> messages() {
        return this.messages;
    }

    public Double temperature() {
        return this.temperature;
    }

    public Double topP() {
        return this.topP;
    }

    public Boolean stream() {
        return this.stream;
    }

    public Double penaltyScore() {
        return this.penaltyScore;
    }

    public String userId() {
        return this.userId;
    }

    public List<Function> functions() {
        return this.functions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChatCompletionRequest that = (ChatCompletionRequest) o;

        return new EqualsBuilder().append(messages, that.messages)
                .append(temperature, that.temperature).append(topP, that.topP).append(stream, that.stream)
                .append(penaltyScore, that.penaltyScore)
                .append(userId, that.userId).append(functions, that.functions).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(messages).append(temperature).append(topP).append(stream)
                .append(penaltyScore).append(userId).append(functions).toHashCode();
    }

    @Override
    public String toString() {
        return "ChatCompletionRequest{" +
                ", messages=" + messages +
                ", temperature=" + temperature +
                ", top_p=" + topP +
                ", stream=" + stream +
                ", penalty_score=" + penaltyScore +
                ", user_id='" + userId + '\'' +
                ", functions=" + functions +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String model = Model.ERNIE_BOT_TURBO.toString();
        private List<Message> messages;
        private Double temperature;
        private Double topP;
        private Boolean stream;
        private Double penaltyScore;
        private String userId;
        private List<Function> functions;

        private Builder() {
        }

        public Builder from(
                ChatCompletionRequest instance) {
            this.model(instance.model);
            this.messages(instance.messages);
            this.temperature(instance.temperature);
            this.topP(instance.topP);
            this.stream(instance.stream);
            this.penaltyScore(instance.penaltyScore);
            this.userId(instance.userId);
            this.functions(instance.functions);
            return this;
        }

        public Builder model(@NonNull String model) {
            this.model = model;
            return this;
        }

        public Builder messages(@NonNull List<Message> messages) {

            this.messages = Collections.unmodifiableList(messages);
            return this;
            
        }

        public Builder messages(@NonNull Message... messages) {
            return this.messages(Arrays.asList(messages));
        }

        public Builder addUserMessage(String userMessage) {
            if (this.messages == null) {
                this.messages = new ArrayList<Message>();
            }

            this.messages.add(Message.userMessage(userMessage));
            return this;
        }

        public Builder addAssistantMessage(String assistantMessage) {
            if (this.messages == null) {
                this.messages = new ArrayList<Message>();
            }

            this.messages.add(Message.assistantMessage(assistantMessage));
            return this;
        }

        public Builder addFunctionMessage(String name, String content) {
            if (this.messages == null) {
                this.messages = new ArrayList<Message>();
            }

            this.messages.add(Message.functionMessage(name, content));
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public Builder stream(Boolean stream) {
            this.stream = stream;
            return this;
        }

        public Builder penaltyScore(Double penaltyScore) {
            this.penaltyScore = penaltyScore;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder functions(List<Function> functions) {
            if (functions == null) {
                return this;
            } else {
                this.functions = Collections.unmodifiableList(functions);
                return this;
            }
        }

        public Builder functions(Function... functions) {
            return this.functions(Arrays.asList(functions));
        }

        public Builder addFunction(Function function) {
            if (this.functions == null) {
                this.functions = new ArrayList<Function>();
            }

            this.functions.add(function);
            return this;
        }

        public ChatCompletionRequest build() {
            return new ChatCompletionRequest(this);
        }
    }
}
