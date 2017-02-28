package com.gurunars.android_utils.property;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public final class StringProperty extends ContextualProperty implements SerializableProperty<String> {

    private String value;
    private Reference valueRef;

    public static class Reference {
        @StringRes int value;

        public Reference(@StringRes int value) {
            this.value = value;
        }

        public @StringRes int getValue() {
            return value;
        }
    }

    public StringProperty(@NonNull ContextualReloadable reloadable,@NonNull String name,@NonNull Reference defaultValue) {
        super(reloadable, name);
        valueRef = defaultValue;
    }

    public StringProperty(@NonNull ContextualReloadable reloadable,@NonNull String name,@NonNull String defaultValue) {
        super(reloadable, name);
        value = defaultValue;
    }

    @Override
    public @NonNull String get() {
        if (value == null) {
            value = getContext().getString(valueRef.getValue());
        }
        return value;
    }

    public void set(Reference value) {
        this.valueRef = value;
        reload();
    }

    @Override
    public void set(@NonNull String value) {
        this.value = value;
        reload();
    }

}
