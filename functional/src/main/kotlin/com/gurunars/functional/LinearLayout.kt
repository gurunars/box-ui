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
        changeSpecs<LinearLayoutParams, AndroidLinearLayout.LayoutParams> {
            rendersTo({ width }) { width = context.toInt(it) }
            rendersTo({ height }) { height = context.toInt(it) }
            rendersTo({ weight }) { weight = it }
            rendersTo({ margin.bottom }) {
                bottomMargin = context.toInt(it)
            }
            rendersTo({ margin.top }) { topMargin = context.toInt(it) }
            rendersTo({ margin.left }) {
                leftMargin = context.toInt(it)
            }
            rendersTo({ margin.right }) {
                rightMargin = context.toInt(it)
            }
            rendersTo({ layoutGravity }) { gravity = it.toInt() }
        }.diff(old as LinearLayoutParams, new as LinearLayoutParams)

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

class LinearLayoutBinder : ElementBinder {
    override val empty = LinearLayout()
    override fun getEmptyTarget(context: Context) = AndroidLinearLayout(context)

    private val changeSpec = changeSpecs<LinearLayout, AndroidLinearLayout> {
        rendersTo({ orientation }) {
            orientation = when (it) {
                LinearLayout.Orientation.VERTICAL -> AndroidLinearLayout.VERTICAL
                LinearLayout.Orientation.HORIZONTAL -> AndroidLinearLayout.VERTICAL
            }
        }
        rendersTo({ horisontalGravity }) { setHorizontalGravity(it.value) }
        rendersTo({ verticalGravity }) { setVerticalGravity(it.value) }
    }

    override fun diff(context: Context, old: Any, new: Any): List<Mutation> =
        changeSpec.diff(
            old as LinearLayout,
            new as LinearLayout
        ) + getCollectionDiff(context, old.children, new.children)
}
