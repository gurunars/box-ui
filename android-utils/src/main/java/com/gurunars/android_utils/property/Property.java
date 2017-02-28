package com.gurunars.android_utils.property;

import android.support.annotation.NonNull;

import java.io.Serializable;

public final class Property<ValueType extends Serializable> implements SerializableProperty<ValueType> {
    private String name;
    private ValueType value;
    private Reloadable reloadable;

    public Property(Reloadable reloadable, String name, ValueType defaultValue) {
        this.reloadable = reloadable;
        this.value = defaultValue;
        this.name = name;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
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
