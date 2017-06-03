package com.gurunars.leaflet_view

import android.view.View

/**
 * Renderer to be used when there are no pages in the list.
 */
interface NoPageRenderer {
    /**
     * Render a view to be shown when all pages are removed
     *
     * @return Rendered and populated view
     */
    fun renderNoPage(): View

    /**
     * Called when the no page view is navigated into
     */
    fun enter()
}
