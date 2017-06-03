package com.gurunars.leaflet_view

import android.view.View

/**
 * Renderer of individual pages.
 *
 * @param <ViewT> Type of the view to be rendered.
 * @param <PageT> Page to be rendered via the view.
 */
interface PageRenderer<ViewT : View, PageT : Page> {
    /**
     * Render a page as a view
     *
     * @param page payload to be used to populate the view
     * @return Rendered and populated view
     */
    fun renderPage(page: PageT): ViewT

    /**
     * Called when the page is navigated into
     *
     * @param pageView this view is entered
     */
    fun enter(pageView: ViewT)

    /**
     * Called when the page is navigated from
     *
     * @param pageView this view is abandoned
     */
    fun leave(pageView: ViewT)
}
