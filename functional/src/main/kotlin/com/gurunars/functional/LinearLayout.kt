package com.gurunars.functional

import android.widget.LinearLayout as AndroidLinearLayout

data class LinearLayoutParams(
    override val width: Size = WrapContent,
    override val height: Size = WrapContent,
    val margin: Bounds
): LayoutParams



data class LinearLayout(
    val orientation: Orientation = Orientation.HORIZONTAL,
    override val children: List<View> = listOf()
): Container {

    enum class Orientation {
        VERTICAL, HORIZONTAL
    }

}

/*
class LinearContainerBinder : ElementBinder {
    override val empty = LinearContainer()
    override fun getEmptyTarget(context: Context) = LinearLayout(context)

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
*/