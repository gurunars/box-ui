package com.gurunars.leaflet_view

import java.io.Serializable

/**
 * Page to be rendered via a LeafletView.
 */
interface Page : Serializable {

    /**
     * @return unique identifier of the page
     */
    val id: Long

}
