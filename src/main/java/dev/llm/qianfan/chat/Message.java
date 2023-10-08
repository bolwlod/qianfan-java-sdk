package dev.llm.qianfan.chat;

import java.util.Objects;

import lombok.NonNull;

public final class Message {

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

    private Message(Builder builder) {
        this.role = builder.role;
        this.name = builder.name;
        this.content = builder.content;
        this.functionCall = builder.functionCall;
    }

    public Role role() {
        return role;
    }

    public String name() {
        return name;
    }

    public String content() {
        return content;
    }

    public FunctionCall functionCall() {
        return functionCall;
    }

    @Override
    public boolean equals(Object another) {
        if (this == another)
            return true;
        return another instanceof Message
                && equalTo((Message) another);
    }

    private boolean equalTo(Message another) {
        return Objects.equals(role, another.role)
                && Objects.equals(content, another.content)
                && Objects.equals(name, another.name)
                && Objects.equals(functionCall, another.functionCall);
    }

    @Override
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(role);
        h += (h << 5) + Objects.hashCode(content);
        h += (h << 5) + Objects.hashCode(name);
        h += (h << 5) + Objects.hashCode(functionCall);
        return h;
    }

    @Override
    public String toString() {
        return "Message{"
                + "role=" + role
                + ", content=" + content
                + ", name=" + name
                + ", functionCall=" + functionCall
                + "}";
    }

    public static Message userMessage(String content) {
        return Message.builder()
                .role(Role.USER)
                .content(content)
                .build();
    }

    public static Message assistantMessage(String content) {
        return Message.builder()
                .role(Role.ASSISTANT)
                .content(content)
                .build();
    }

    public static Message functionMessage(String name, String content) {
        return Message.builder()
                .role(Role.FUNCTION)
                .name(name)
                .content(content)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Role role;
        private String content;
        private String name;
        private FunctionCall functionCall;

        private Builder() {
        }

        public Builder role(@NonNull Role role) {
            this.role = role;
            return this;
        }

        public Builder role(@NonNull String role) {
            return role(Role.from(role));
        }

        public Builder content(@NonNull String content) {
            this.content = content;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder functionCall(FunctionCall functionCall) {
            this.functionCall = functionCall;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
