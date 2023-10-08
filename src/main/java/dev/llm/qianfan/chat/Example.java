package dev.llm.qianfan.chat;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.NonNull;

public class Example {

    /*
     * 当前支持以下：
     * user: 表示用户
     * assistant: 表示对话助手
     * function: 表示函数
     */
    private final Role role;

    /*
     * message作者；当role=function时，必填，且是响应内容中function_call中的name
     */
    private final String name;

    /*
     * 对话内容，当前message存在function_call时可以为空，其他场景不能为空
     */
    private final String content;

    /*
     * 函数调用，function call场景下第一轮对话的返回，第二轮对话作为历史信息在message中传入
     */
    private final FunctionCall functionCall;

    private Example(Builder builder) {
        this.name = builder.name;
        this.role = builder.role;
        this.content = builder.content;
        this.functionCall = builder.functionCall;
    }

    public Role role() {
        return this.role;
    }

    public String name() {
        return this.name;
    }

    public String content() {
        return this.content;
    }

    public FunctionCall functionCall() {
        return this.functionCall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Example example = (Example) o;

        return new EqualsBuilder().append(role, example.role).append(name, example.name)
                .append(content, example.content).append(functionCall, example.functionCall).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(role).append(name).append(content).append(functionCall).toHashCode();
    }

    @Override
    public String toString() {
        return "Examples{" +
                "role=" + role +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", function_call=" + functionCall +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Role role;
        private String name;
        private String content;
        private FunctionCall functionCall;

        private Builder() {
        }

        public Builder role(@NonNull Role role) {
            this.role = role;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder content(@NonNull String content) {
            this.content = content;
            return this;
        }

        public Builder functionCall(FunctionCall functionCall) {
            this.functionCall = functionCall;
            return this;
        }

        public Example build() {
            return new Example(this);
        }
    }
}
