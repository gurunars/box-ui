package com.gurunars.leaflet_view;

import java.io.Serializable;

/**
 * Page to be rendered via a LeafletView.
 */
public interface Page extends Serializable {

    /**
     * @return unique identifier of the page
     */
    long getId();

}
