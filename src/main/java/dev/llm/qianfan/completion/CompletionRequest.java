package dev.llm.qianfan.completion;

import lombok.NonNull;

public final class CompletionRequest {

    transient private final String serviceName;

    /*
     * 请求信息
     */
    private final String prompt;

    /*
     * 是否以流式接口的形式返回数据，默认false
     */
    private final Boolean stream;

    /*
     * 表示最终用户的唯一标识符，可以监视和检测滥用行为，防止接口恶意调用
     */
    private final String userId;

    private CompletionRequest(Builder builder) {
        this.serviceName = builder.serviceName;
        this.prompt = builder.prompt;
        this.stream = builder.stream;
        this.userId = builder.userId;
    }

    public String serviceName() {
        return this.serviceName;
    }

    public String prompt() {
        return this.prompt;
    }

    public Boolean stream() {
        return this.stream;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String serviceName;
        private String prompt;
        private Boolean stream;
        private String userId;

        private Builder() {

        }

        public Builder from(CompletionRequest request) {
            this.serviceName(request.serviceName);
            this.prompt(request.prompt);
            this.stream(request.stream);
            this.user(request.userId);
            return this;
        }

        public Builder serviceName(@NonNull String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder prompt(@NonNull String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder stream(Boolean stream) {
            this.stream = stream;
            return this;
        }

        public Builder user(String userId) {
            this.userId = userId;
            return this;
        }

        public CompletionRequest build() {
            return new CompletionRequest(this);
        }
    }
}
