package com.gurunars.leaflet_view

import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView


inline fun <PageT: Page> ViewManager.leafletView(theme: Int = 0): LeafletView<PageT> = leafletView(theme) {}
inline fun <PageT: Page> ViewManager.leafletView(theme: Int = 0, init: LeafletView<PageT>.() -> Unit) = ankoView({ LeafletView(it) }, theme, init)
