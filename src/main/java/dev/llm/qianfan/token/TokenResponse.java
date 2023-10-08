package dev.llm.qianfan.token;

public class TokenResponse {

    /*
     * 访问凭证
     */
    private final String accessToken;

    /*
     * 有效期，Access Token的有效期。
     * 说明：单位是秒，有效期30天
     */
    private final Integer expiresIn;

    /*
     * 暂时未使用，可忽略
     */
    private final String sessionKey;

    /*
     * 暂时未使用，可忽略
     */
    private final String refreshToken;

    /*
     * 暂时未使用，可忽略
     */
    private final String scope;

    /*
     * 暂时未使用，可忽略
     */
    private final String sessionSecret;

    /*
     * 错误码
     * 说明：响应失败时返回该字段，成功时不返回
     */
    private final String error;

    /*
     * 错误描述信息，帮助理解和解决发生的错误
     * 说明：响应失败时返回该字段，成功时不返回
     */
    private final String errorDescription;

    private TokenResponse(Builder builder) {
        this.refreshToken = builder.refreshToken;
        this.expiresIn = builder.expiresIn;
        this.sessionKey = builder.sessionKey;
        this.accessToken = builder.accessToken;
        this.scope = builder.scope;
        this.sessionSecret = builder.sessionSecret;
        this.error = builder.error;
        this.errorDescription = builder.errorDescription;

    }

    public String refreshToken() {
        return this.refreshToken;
    }

    public Integer expiresIn() {
        return this.expiresIn;
    }

    public String sessionKey() {
        return this.sessionKey;
    }

    public String accessToken() {
        return this.accessToken;
    }

    public String scope() {
        return this.scope;
    }

    public String sessionSecret() {
        return this.sessionSecret;
    }

    public String error() {
        return this.error;
    }

    public String errorDescription() {
        return this.errorDescription;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String refreshToken;
        private Integer expiresIn;
        private String sessionKey;
        private String accessToken;
        private String scope;
        private String sessionSecret;

        private String error;
        private String errorDescription;

        private Builder() {
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public Builder expiresIn(Integer expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder scope(String scope) {
            this.scope = scope;
            return this;
        }

        public Builder sessionKey(String sessionKey) {
            this.sessionKey = sessionKey;
            return this;
        }

        public Builder sessionSecret(String sessionSecret) {
            this.sessionSecret = sessionSecret;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder errorDescription(String errorDescription) {
            this.errorDescription = errorDescription;
            return this;
        }

        public TokenResponse build() {
            return new TokenResponse(this);
        }
    }
}