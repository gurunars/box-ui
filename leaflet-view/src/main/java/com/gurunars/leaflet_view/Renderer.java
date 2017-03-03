package com.gurunars.leaflet_view;

import android.content.Context;
import android.view.View;

interface Renderer {
    /**
     * Render a page as a view
     *
     * @param context used to render the page
     * @param transitionObservable used to subscribe to page change events
     * @return Rendered and populated view
     */
    View render(Context context, PageTransitionObservable transitionObservable);
}
