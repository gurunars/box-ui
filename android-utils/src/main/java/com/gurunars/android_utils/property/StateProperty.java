package com.gurunars.android_utils.property;

import java.io.Serializable;

public class StateProperty<ValueType extends Serializable> {
    private ValueType value;
    private Reloadable reloadable;

    public StateProperty(Reloadable reloadable, ValueType defaultValue) {
        this.reloadable = reloadable;
        this.value = defaultValue;
    }

    public ValueType get() {
        return value;
    }

    public void set(ValueType value) {
        this.value = value;
        this.reloadable.reload();
    }
}
