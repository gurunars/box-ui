package com.gurunars.item_list;

import android.content.Context;
import android.view.View;

/**
 * Renderer for a situation when the list has not items.
 */
public interface EmptyViewBinder {
    /**
     * Return an inflated view.
     *
     * @param context to be used for rendering
     * @return rendered view
     */
    View getView(Context context);

}
