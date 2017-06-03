package com.gurunars.leaflet_view.example

import com.gurunars.leaflet_view.Page

internal class TitledPage(var title: String) : Page {

    override val id: Long

    init {
        this.id = System.nanoTime()
    }

    override fun toString(): String {
        return title
    }

}
