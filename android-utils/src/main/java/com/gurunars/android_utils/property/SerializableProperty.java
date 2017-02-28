package com.gurunars.android_utils.property;

import android.support.annotation.NonNull;

import java.io.Serializable;

public interface SerializableProperty<ItemType extends Serializable> {

    @NonNull ItemType get();
    void set(@NonNull ItemType value);

}
