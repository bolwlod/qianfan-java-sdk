package dev.llm.qianfan.txt2img;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class Txt2ImgResponse {

    /*
     * base64 编码的图片
     */
    private final List<String> images;

    /*
     * 错误码
     */
    private final Integer errorCode;

    /*
     * 错误描述信息，帮助理解和解决发生的错误
     */
    private final String errorMsg;

    private Txt2ImgResponse(Builder builder) {
        this.images = builder.images;
        this.errorCode = builder.errorCode;
        this.errorMsg = builder.errorMsg;
    }

    public List<String> images() {
        return this.images;
    }

    public Integer errorCode() {
        return this.errorCode;
    }

    public String errorMsg() {
        return this.errorMsg;
    }

    @Override
    public String toString() {
        return "Txt2ImgResponse{" +
                "images='" + images + '\'' +
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

        Txt2ImgResponse that = (Txt2ImgResponse) o;

        return new EqualsBuilder().append(images, that.images).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(images)
                .toHashCode();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private List<String> images;
        private Integer errorCode;
        private String errorMsg;

        private Builder() {
        }

        public Builder images(List<String> images) {
            if (images == null) {
                return this;
            } else {
                this.images = Collections.unmodifiableList(images);
                return this;
            }
        }

        public Builder errorCode(Integer errorCode) {
            this.errorCode = errorCode;
            return this;
        }   

        public Builder errorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
            return this;
        }  

        public Txt2ImgResponse build() {
            return new Txt2ImgResponse(this);
        }
    }
}
