package dev.llm.qianfan.txt2img;

import java.util.Objects;

import lombok.NonNull;

public final class Txt2ImgRequest {

    transient private final String serviceName;

    /*
     * 正向提示词
     */
    private final String prompt;

    /*
     * 图片宽度
     */
    private final Integer width;

    /*
     * 图片长度
     */
    private final Integer height;

    private Txt2ImgRequest(Builder builder) {
        this.serviceName = builder.serviceName;
        this.prompt = builder.prompt;
        this.width = builder.width;
        this.height = builder.height;
    }

    public String serviceName() {
        return this.serviceName;
    }

    public String prompt() {
        return this.prompt;
    }

    public Integer width() {
        return this.width;
    }

    public Integer height() {
        return this.height;
    }

    public boolean equals(Object another) {
        if (this == another) {
            return true;
        } else {
            return another instanceof Txt2ImgRequest
                    && this.equalTo((Txt2ImgRequest) another);
        }
    }

    private boolean equalTo(Txt2ImgRequest another) {
        return Objects.equals(this.serviceName, another.serviceName)
                && Objects.equals(this.prompt, another.prompt)
                && Objects.equals(this.width, another.width)
                && Objects.equals(this.height, another.height);
    }

    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.serviceName);
        h += (h << 5) + Objects.hashCode(this.prompt);
        h += (h << 5) + Objects.hashCode(this.width);
        h += (h << 5) + Objects.hashCode(this.height);
        return h;
    }

    public String toString() {
        return "Txt2ImgRequest{model=" + this.serviceName + ", prompt=" + this.prompt + ", width=" + this.width
                + ", height=" + this.height + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String serviceName;
        private String prompt;
        private Integer width;
        private Integer height;

        private Builder() {
        }

        public Builder serviceName(@NonNull String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder prompt(@NonNull String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder width(Integer width) {
            this.width = width;
            return this;
        }

        public Builder height(Integer height) {
            this.height = height;
            return this;
        }

        public Txt2ImgRequest build() {
            return new Txt2ImgRequest(this);
        }
    }
}
