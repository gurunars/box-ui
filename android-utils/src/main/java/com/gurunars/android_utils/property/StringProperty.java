package com.gurunars.android_utils.property;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public class StringProperty extends ContextualProperty implements SerializableProperty<String> {

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

    public StringProperty(ContextualReloadable reloadable, Reference defaultValue) {
        super(reloadable);
        valueRef = defaultValue;
    }

    public StringProperty(ContextualReloadable reloadable, String defaultValue) {
        super(reloadable);
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
