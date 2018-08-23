package com.gurunars.functional.layouts

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.gurunars.functional.*

data class LinearSlot(
    override val key: Int? = null,
    val width: Size = WrapContent,
    val height: Size = WrapContent,
    val margin: Bounds = Bounds(),
    override val child: Any
): Slot


class LinearSlotComponent(
    private val childComponent: Component<LinearSlot>
): Component {

    override val empty: Any
        get() = LinearSlot(child = childComponent.empty)

    override fun diff(old: Any, new: Any): List<Mutation> {
        old as LinearSlot
        new as LinearSlot
        val childComponent = Registry.getElement(old.child)
        return changeSpec.diff(old, new) + childComponent.diff(old.child, new.child)
    }

    override fun getEmptyView(context: Context): View = childComponent.getEmptyView(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private val View.lp
        get() = layoutParams as LinearLayout.LayoutParams

    private val changeSpec = listOf<ChangeSpec<LinearSlot, *, View>>(
        { it: LinearSlot -> it.margin } rendersTo  {
            lp.apply {
                leftMargin = context.toInt(it.left)
                rightMargin = context.toInt(it.right)
                topMargin = context.toInt(it.top)
                bottomMargin = context.toInt(it.bottom)
            }
        },
        { it: LinearSlot -> it.width } rendersTo  {
            lp.width = context.toInt(it)
        },
        { it: LinearSlot -> it.height } rendersTo  {
            lp.height = context.toInt(it)
        }
    )
}


data class LinearContainer(
    val orientation: Orientation = Orientation.HORIZONTAL,
    override val children: List<LinearSlot> = listOf()
): Container<LinearSlot> {

    enum class Orientation {
        VERTICAL, HORIZONTAL
    }

}


class LinearContainerComponent : Component {
    override val empty = LinearContainer()
    override fun getEmptyView(context: Context) = LinearLayout(context)

    override fun diff(old: Any, new: Any): List<Mutation> =
        changeSpec.diff(
            old as LinearContainer,
            new as LinearContainer
        ) + getCollectionDiff(old.children, new.children)

    private val changeSpec = listOf<ChangeSpec<LinearContainer, *, LinearLayout>>(
        { it: LinearContainer -> it.orientation } rendersTo  {
            orientation = when(it) {
                LinearContainer.Orientation.VERTICAL -> LinearLayout.VERTICAL
                LinearContainer.Orientation.HORIZONTAL -> LinearLayout.VERTICAL
            }
        }
    )
}