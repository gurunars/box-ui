package com.gurunars.android_utils.property;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

public final class DrawableRefProperty extends ContextualProperty implements SerializableProperty<Integer> {

    private @DrawableRes int value;

    public DrawableRefProperty(@NonNull ContextualReloadable reloadable,@NonNull String name, @DrawableRes int defaultValue) {
        super(reloadable, name);
        value = defaultValue;
    }

    @Override
    public @NonNull @DrawableRes Integer get() {
        return value;
    }

    public @NonNull Drawable getDrawable() {
        return ContextCompat.getDrawable(getContext(), value);
    }

    @Override
    public void set(@NonNull Integer value) {
        this.value = value;
        reload();
    }

}
