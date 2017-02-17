package com.gurunars.leaflet_view;

import android.view.View;

/**
 * Renderer of individual pages.
 *
 * @param <ViewT> Type of the view to be rendered.
 * @param <PageT> Page to be rendered via the view.
 */
public interface PageRenderer<ViewT extends View, PageT extends Page> {
    /**
     * Render a page as a view
     *
     * @param page payload to be used to populate the view
     * @return Rendered and populated view
     */
    ViewT renderPage(PageT page);

    /**
     * Called when the page is navigated into
     *
     * @param pageView this view is entered
     */
    void enter(ViewT pageView);

    /**
     * Called when the page is navigated from
     *
     * @param pageView this view is abandoned
     */
    void leave(ViewT pageView);
}
