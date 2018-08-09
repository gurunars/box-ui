package com.gurunars.functional

import android.content.Context
import android.view.View
import com.facebook.yoga.android.YogaLayout


data class Node(
    val padding: Bounds? = null,
    override val child: Element,
    override val key: Int? = null
): Slot

class _Node(
    private val childComponent: Component
): Component {
    override val empty =
        Node(child = childComponent.empty)

    override fun getEmptyView(context: Context): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun diff(old: Element, new: Element): List<Mutation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

data class Container(
    val children: List<Node>
): Element

class _Container: Component {
    override val empty =
        Container(listOf())

    override fun getEmptyView(context: Context): View =
        YogaLayout(context)

    override fun diff(old: Element, new: Element): List<Mutation> =
        TODO()
}
