package dev.llm.qianfan.chat;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import dev.llm.qianfan.chat.Role.RoleAdapter;

import java.io.IOException;

@JsonAdapter(RoleAdapter.class)
public enum Role {

    USER("user"),
    ASSISTANT("assistant"),
    FUNCTION("function");

    private final String stringValue;

    private Role(String stringValue) {
        this.stringValue = stringValue;
    }

    public String toString() {
        return this.stringValue;
    }

    static Role from(String stringValue) {
        Role[] roles = values();
        int roleCount = roles.length;

        for(int i = 0; i < roleCount; ++i) {
            Role role = roles[i];
            if (role.stringValue.equals(stringValue)) {
                return role;
            }
        }

        throw new IllegalArgumentException("Unknown role: '" + stringValue + "'");
    }

    static class RoleAdapter extends TypeAdapter<Role> {
        RoleAdapter() {
        }

        public void write(JsonWriter out, Role role) throws IOException {
            out.value(role.toString());
        }

        public Role read(JsonReader in) throws IOException {
            return Role.from(in.nextString());
        }
    }
}

