package dev.llm.qianfan.chat;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.NonNull;

public class FunctionCall {

    private static final Gson GSON = new Gson();
    private static final Type MAP_TYPE = new TypeToken<Map<String, Object>>() {
    }.getType();

    /*
     * 触发的function名
     */
    private final String name;

    /*
     * 模型思考过程
     */
    private final String thoughts;

    /*
     * 请求参数
     */
    private final String arguments;

    private FunctionCall(Builder builder) {
        this.name = builder.name;
        this.arguments = builder.arguments;
        this.thoughts = builder.thoughts;
    }

    public String name() {
        return this.name;
    }

    public String thoughts() {
        return this.thoughts;
    }

    public String arguments() {
        return this.arguments;
    }

    public Map<String, Object> argumentsAsMap() {
        return GSON.fromJson(this.arguments, MAP_TYPE);
    }

    public <T> T argument(String name) {
        Map<String, Object> arguments = this.argumentsAsMap();
        return (T) arguments.get(name);
    }

    public boolean equals(Object another) {
        if (this == another) {
            return true;
        } else {
            return another instanceof FunctionCall
                    && this.equalTo((FunctionCall) another);
        }
    }

    private boolean equalTo(FunctionCall another) {
        return Objects.equals(this.name, another.name) && Objects.equals(this.arguments, another.arguments)
                && Objects.equals(this.thoughts, another.thoughts);
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", thoughts='" + thoughts + '\'' +
                ", arguments='" + arguments + '\'' +
                '}';
    }

    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.name);
        h += (h << 5) + Objects.hashCode(this.arguments);
        h += (h << 5) + Objects.hashCode(this.thoughts);
        return h;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String name;
        private String arguments;
        private String thoughts;

        private Builder() {
        }

        public Builder name(@NonNull String name) {
            this.name = name;
            return this;
        }

        public Builder arguments(@NonNull String arguments) {
            this.arguments = arguments;
            return this;
        }

        public Builder thoughts(String thoughts) {
            this.thoughts = thoughts;
            return this;
        }

        public FunctionCall build() {
            return new FunctionCall(this);
        }
    }
}
