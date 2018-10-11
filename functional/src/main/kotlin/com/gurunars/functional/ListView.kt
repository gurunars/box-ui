package com.gurunars.functional

import android.content.Context
import android.view.View

data class ListView(
    val items: List<*>
)

class ListViewBinder : ElementBinder {
    override val empty = listOf<Any>()

    override fun getEmptyTarget(context: Context): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun diff(context: Context, old: Any, new: Any): List<Mutation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}