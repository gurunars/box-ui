package com.gurunars.android_utils.property;

import android.content.Context;

public interface ContextualReloadable extends Reloadable {
    Context getContext();
}
