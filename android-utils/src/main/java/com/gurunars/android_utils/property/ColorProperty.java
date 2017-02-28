package com.gurunars.android_utils.property;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

public class ColorProperty extends ContextualProperty implements SerializableProperty<Integer> {

    private @ColorInt int value=-1;
    private Reference valueRef;

    public static class Reference {
        @ColorRes int value;

        public Reference(@ColorRes int value) {
            this.value = value;
        }

        public @ColorRes int getValue() {
            return value;
        }
    }

    public ColorProperty(ContextualReloadable reloadable, Reference defaultValue) {
        super(reloadable);
        valueRef = defaultValue;
    }

    public ColorProperty(ContextualReloadable reloadable, @ColorInt int defaultValue) {
        super(reloadable);
        value = defaultValue;
    }

    public ColorProperty(ContextualReloadable reloadable, String defaultValue) {
        super(reloadable);
        value = Color.parseColor(defaultValue);
    }

    @Override
    public @NonNull @ColorInt Integer get() {
        if (value == -1) {
            value = ContextCompat.getColor(getContext(), valueRef.getValue());
        }
        return value;
    }

    public void set(Reference value) {
        this.valueRef = value;
        reload();
    }

    @Override
    public void set(@NonNull @ColorInt Integer value) {
        this.value = value;
        reload();
    }

    public void set(String value) {
        this.value = Color.parseColor(value);
        reload();
    }

}
