package com.gurunars.android_utils.property;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class StateHolder implements Serializable {
    private Map<String, Object> state = new HashMap<>();

    public class DuplicateFieldError extends RuntimeException {
        private String name;

        DuplicateFieldError(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Field with name '" + name + "' already exists.";
        }
    }

    public class MissingFieldError extends RuntimeException {
        private String name;

        MissingFieldError(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Field with name '" + name + "' does not exists.";
        }
    }

    public void serialize(SerializableProperty... fields) {
        for (SerializableProperty field: fields) {
            if (state.containsKey(field.getName())) {
                throw new DuplicateFieldError(field.getName());
            }
            state.put(field.getName(), field.get());
        }
    }

    public void deserialize(SerializableProperty... fields) {
        for (SerializableProperty field: fields) {
            if (!state.containsKey(field.getName())) {
                throw new MissingFieldError(field.getName());
            }
            state.put(field.getName(), field.get());
        }
    }
}
