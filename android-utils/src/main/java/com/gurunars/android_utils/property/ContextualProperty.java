package com.gurunars.android_utils.property;

import android.content.Context;
import android.support.annotation.NonNull;


public abstract class ContextualProperty {
    private String name;
    private ContextualReloadable reloadable;

    @NonNull
    public String getName() {
        return name;
    }

    protected ContextualProperty(@NonNull ContextualReloadable reloadable, @NonNull String name) {
        this.name = name;
        this.reloadable = reloadable;
    }

    protected void reload() {
        reloadable.reload();
    }

    protected Context getContext() {
        return reloadable.getContext();
    }

}
