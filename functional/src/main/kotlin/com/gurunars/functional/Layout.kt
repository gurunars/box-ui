package com.gurunars.functional

import android.content.Context
import android.view.View
import com.facebook.yoga.android.YogaLayout


data class Node<T>(
    val padding: Bounds? = null,
    override val child: T,
    override val key: Int? = null
): Slot<T>

class _Node<T>(
    private val childComponent: Component<T>
): Component<Node<T>> {
    override val empty =
        Node(child = childComponent.empty)

    override fun getEmptyView(context: Context): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun diff(old: Node<T>, new: Node<T>): List<Mutation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

data class Container(
    val children: List<Node<*>>
)

class _Container: Component<Container> {
    override val empty =
        Container(listOf())

    override fun getEmptyView(context: Context): View =
        YogaLayout(context)

    override fun diff(old: Container, new: Container): List<Mutation> =
        TODO()
}
