package com.gurunars.android_utils.property;

import android.support.annotation.NonNull;

import java.io.Serializable;

public interface SerializableProperty<ValueType extends Serializable> {

    @NonNull ValueType get();
    void set(@NonNull ValueType value);

}
