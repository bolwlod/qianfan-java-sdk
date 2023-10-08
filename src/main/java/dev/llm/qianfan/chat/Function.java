package dev.llm.qianfan.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.NonNull;

public class Function {

    /*
     * 函数名
     */
    private final String name;

    /*
     * 函数描述
     */
    private final String description;

    /*
     * 函数请求参数，说明：
     * （1）JSON Schema 格式，参考JSON Schema描述
     * （2）如果函数没有请求参数，parameters值格式如下：
     *     {"type": "object","properties": {}}
     */
    private final Parameters parameters;

    /*
     * 函数响应参数，JSON Schema 格式，参考JSON Schema描述
     */
    private final Responses responses;

    /*
     * function调用的一些历史示例
     */
    private final List<Example> examples;

    private Function(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.parameters = builder.parameters;
        this.examples = builder.examples;
        this.responses = builder.responses;
    }

    public String name() {
        return this.name;
    }

    public String description() {
        return this.description;
    }

    public Parameters parameters() {
        return this.parameters;
    }

    public Responses responses() {
        return responses;
    }

    public List<Example> examples() {
        return examples;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Function function = (Function) o;

        return new EqualsBuilder().append(name, function.name)
                .append(description, function.description).append(parameters, function.parameters)
                .append(responses, function.responses).append(examples, function.examples).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).append(description).append(parameters).append(responses)
                .append(examples).toHashCode();
    }

    @Override
    public String toString() {
        return "Function{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", parameters=" + parameters +
                ", responses=" + responses +
                ", examples=" + examples +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String name;
        private String description;
        private Parameters parameters;
        private Responses responses;
        private List<Example> examples;

        private Builder() {
        }

        public Builder name(@NonNull String name) {
            this.name = name;
            return this;
        }

        public Builder description(@NonNull String description) {
            this.description = description;
            return this;
        }

        public Builder parameters(@NonNull Parameters parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder addParameter(String name, JsonSchemaProperty... jsonSchemaProperties) {
            this.addOptionalParameter(name, jsonSchemaProperties);
            this.parameters.required().add(name);
            return this;
        }

        public Builder addOptionalParameter(String name, JsonSchemaProperty... jsonSchemaProperties) {
            if (this.parameters == null) {
                this.parameters = Parameters.builder().build();
            }

            Map<String, Object> jsonSchemaPropertiesMap = new HashMap<>();
            JsonSchemaProperty[] properties = jsonSchemaProperties;
            int len = jsonSchemaProperties.length;

            for (int i = 0; i < len; ++i) {
                JsonSchemaProperty jsonSchemaProperty = properties[i];
                jsonSchemaPropertiesMap.put(jsonSchemaProperty.key(), jsonSchemaProperty.value());
            }

            this.parameters.properties().put(name, jsonSchemaPropertiesMap);
            return this;
        }

        public Builder responses(Responses responses) {
            this.responses = responses;
            return this;
        }

        public Builder examples(List<Example> examples) {
            this.examples = examples;
            return this;
        }

        public Builder addExample(Example example) {
            if (this.examples == null) {
                this.examples = new ArrayList<Example>();
            }
            this.examples.add(example);
            return this;
        }

        public Function build() {
            return new Function(this);
        }
    }
}
