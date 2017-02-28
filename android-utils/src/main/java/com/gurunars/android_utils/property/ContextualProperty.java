package com.gurunars.android_utils.property;

import android.content.Context;


public abstract class ContextualProperty {
    private ContextualReloadable reloadable;

    protected ContextualProperty(ContextualReloadable reloadable) {
        this.reloadable = reloadable;
    }

    protected void reload() {
        reloadable.reload();
    }

    protected Context getContext() {
        return reloadable.getContext();
    }

}
