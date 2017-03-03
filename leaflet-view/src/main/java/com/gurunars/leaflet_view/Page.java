package com.gurunars.leaflet_view;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

/**
 * Page to be rendered via a LeafletView.
 */
public interface Page extends Serializable, Renderer {

    /**
     * @return unique identifier of the page
     */
    long getId();

}
