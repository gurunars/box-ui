package com.gurunars.functional

import android.content.Context
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout as AndroidLinearLayout

data class LinearLayoutParams(
    override val width: Size = WrapContent,
    override val height: Size = WrapContent,
    val margin: Bounds = Bounds(0.dp),
    val weight: Float = 0f,
    val layoutGravity: Set<Gravity> = setOf()
): LayoutParams


class LinearLayoutParamsBinder : ParamBinder {

    override fun diff(context: Context, old: LayoutParams, new: LayoutParams): List<Mutation> =
        listOf<ChangeSpec<LinearLayoutParams, *, AndroidLinearLayout.LayoutParams>>(
            { it: LinearLayoutParams -> it.width } rendersTo { width = context.toInt(it) },
            { it: LinearLayoutParams -> it.height } rendersTo { height = context.toInt(it) },
            { it: LinearLayoutParams -> it.weight } rendersTo { weight = it },
            { it: LinearLayoutParams -> it.margin.bottom } rendersTo { bottomMargin = context.toInt(it) },
            { it: LinearLayoutParams -> it.margin.top } rendersTo { topMargin = context.toInt(it) },
            { it: LinearLayoutParams -> it.margin.left } rendersTo { leftMargin = context.toInt(it) },
            { it: LinearLayoutParams -> it.margin.right } rendersTo { rightMargin = context.toInt(it) },
            { it: LinearLayoutParams -> it.layoutGravity } rendersTo { gravity = it.toInt() }
        ).diff(old as LinearLayoutParams, new as LinearLayoutParams)

    override val empty = LinearLayoutParams()

    override fun getEmptyTarget(context: Context) =
        AndroidLinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

}

data class LinearLayout(
    val orientation: Orientation = Orientation.HORIZONTAL,
    val verticalGravity: Gravity = Gravity.TOP,
    val horisontalGravity: Gravity = Gravity.LEFT,
    override val children: List<View> = listOf()
): Container {

    enum class Orientation {
        VERTICAL, HORIZONTAL
    }

}

class LinearContainerBinder : ElementBinder {
    override val empty = LinearLayout()
    override fun getEmptyTarget(context: Context) = AndroidLinearLayout(context)

    private val changeSpec = listOf<ChangeSpec<LinearLayout, *, AndroidLinearLayout>>(
        { it: LinearLayout -> it.orientation } rendersTo  {
            orientation = when(it) {
                LinearLayout.Orientation.VERTICAL -> AndroidLinearLayout.VERTICAL
                LinearLayout.Orientation.HORIZONTAL -> AndroidLinearLayout.VERTICAL
            }
        },
        { it: LinearLayout -> it.horisontalGravity } rendersTo { setHorizontalGravity(it.value) },
        { it: LinearLayout -> it.verticalGravity } rendersTo { setVerticalGravity(it.value) }
    )

    override fun diff(context: Context, old: Any, new: Any): List<Mutation> =
        changeSpec.diff(
            old as LinearLayout,
            new as LinearLayout
        ) + getCollectionDiff(context, old.children, new.children)
}
