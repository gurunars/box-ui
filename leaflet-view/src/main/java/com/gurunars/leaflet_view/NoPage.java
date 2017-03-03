package com.gurunars.leaflet_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Renderer to be used when there are no pages in the list.
 */
public interface NoPage extends Renderer {

    class Default implements NoPage {

        @Override
        public View render(Context context, PageTransitionObservable transitionObservable) {
            return LayoutInflater.from(context).inflate(R.layout.default_no_page_view, null);
        }

    }
}
