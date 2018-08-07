package com.gurunars.functional

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KClass

val View.isOnScreen: Boolean
    get() {
        val viewBounds = Rect()
        getGlobalVisibleRect(viewBounds)
        val screen = with(Resources.getSystem().displayMetrics) {
            Rect(0, 0, widthPixels, heightPixels)
        }
        return viewBounds.intersect(screen)
    }

class Recycler {

    val registry = Registry

    // map of child to parent
    val viewCache = mutableMapOf<KClass<View>, MutableList<Pair<View, ViewGroup?>>>()

    /**
     * Recycle the view that is gone or has 0 width and height is not shown
     * or is not on a screen
     */
    fun <T> getInstance(parent: ViewGroup, item: T): View =
        registry.getElement(item).let { component ->
            val view =
                viewCache.get(component.viewType)?.first {
                    with(it.first) {
                        visibility == View.GONE ||
                            (width == 0 && height == 0) ||
                            !isShown ||
                            !isOnScreen
                    }
                }?.also {
                    it.second?.removeView(it.first)
                }?.first ?: component.getEmptyView(parent.context).also {
                    if (viewCache.containsKey(component.viewType)) {
                        viewCache.get(component.viewType)?.add(
                            Pair(it, parent as ViewGroup?)
                        )
                    } else {
                        viewCache.set(
                            component.viewType,
                            mutableListOf(Pair(it, parent as ViewGroup?))
                        )
                    }
                }
            view.also { parent.addView(it) }
        }
}