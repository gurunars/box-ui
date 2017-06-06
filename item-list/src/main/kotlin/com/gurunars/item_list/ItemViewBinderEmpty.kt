package com.gurunars.item_list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.matchParent

internal class ItemViewBinderEmpty : EmptyViewBinder {

    override fun getView(context: Context): View {
        return TextView(context).apply {
            fullSize()
            setText(R.string.empty)
            gravity = Gravity.CENTER
        }
    }

    companion object {

        val EMPTY_TYPE = -404
    }

}
