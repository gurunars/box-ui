package com.gurunars.leaflet_view;

import android.view.View;

/**
 * Renderer to be used when there are no pages in the list.
 */
public interface NoPageRenderer {
    /**
     * Render a view to be shown when all pages are removed
     *
     * @return Rendered and populated view
     */
    View renderNoPage();

    /**
     * Called when the no page view is navigated into
     */
    void enter();
}
