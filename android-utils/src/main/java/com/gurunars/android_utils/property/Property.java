package com.gurunars.android_utils.property;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Property<ValueType extends Serializable> implements SerializableProperty<ValueType> {
    private ValueType value;
    private Reloadable reloadable;

    public Property(Reloadable reloadable, ValueType defaultValue) {
        this.reloadable = reloadable;
        this.value = defaultValue;
    }

    @NonNull
    public ValueType get() {
        return value;
    }

    public void set(@NonNull ValueType value) {
        this.value = value;
        this.reloadable.reload();
    }
}
