package com.gurunars.leaflet_view.example

import com.gurunars.leaflet_view.Page

internal data class TitledPage(override val id: Long, val title: String) : Page {
    override fun toString(): String {
        return title
    }
}
