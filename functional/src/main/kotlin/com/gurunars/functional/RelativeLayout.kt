package com.gurunars.functional

import android.content.Context
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.RelativeLayout as AndroidRelativeLayout

sealed class Rule(private val verb: Int, private val ref: Int? = null) {
    fun remove(params: AndroidRelativeLayout.LayoutParams) {
        params.removeRule(verb)
    }
    fun add(params: AndroidRelativeLayout.LayoutParams) {
        if (ref == null) {
            params.addRule(verb)
        } else {
            params.addRule(verb, ref)
        }
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Rule) return false
        if (verb != other.verb) return false
        if (ref != other.ref) return false
        return true
    }

    override fun hashCode(): Int {
        var result = verb
        result = 31 * result + (ref ?: 0)
        return result
    }

}
class Above(ref: Int): Rule(AndroidRelativeLayout.ABOVE, ref)
class AlignBaseline(ref: Int): Rule(AndroidRelativeLayout.ALIGN_BASELINE, ref)
class AlignBottom(ref: Int): Rule(AndroidRelativeLayout.ALIGN_BOTTOM, ref)
class AlignEnd(ref: Int): Rule(AndroidRelativeLayout.ALIGN_END, ref)
class AlignLeft(ref: Int): Rule(AndroidRelativeLayout.ALIGN_LEFT, ref)
object AlignParentBottom: Rule(AndroidRelativeLayout.ALIGN_PARENT_BOTTOM)
object AlignParentEnd: Rule(AndroidRelativeLayout.ALIGN_PARENT_END)
object AlignParentLeft: Rule(AndroidRelativeLayout.ALIGN_PARENT_LEFT)
object AlignParentRight: Rule(AndroidRelativeLayout.ALIGN_PARENT_RIGHT)
object AlignParentStart: Rule(AndroidRelativeLayout.ALIGN_PARENT_START)
object AlignParentTop: Rule(AndroidRelativeLayout.ALIGN_PARENT_TOP)
class AlignRight(ref: Int): Rule(AndroidRelativeLayout.ALIGN_RIGHT, ref)
class AlignStart(ref: Int): Rule(AndroidRelativeLayout.ALIGN_START, ref)
class AlignTop(ref: Int): Rule(AndroidRelativeLayout.ALIGN_TOP, ref)
class Below(ref: Int): Rule(AndroidRelativeLayout.BELOW, ref)
object CenterHorizontal: Rule(AndroidRelativeLayout.CENTER_HORIZONTAL)
object CenterInParent: Rule(AndroidRelativeLayout.CENTER_IN_PARENT)
object CenterVertical: Rule(AndroidRelativeLayout.CENTER_VERTICAL)
class EndOf(ref: Int): Rule(AndroidRelativeLayout.END_OF, ref)
class LeftOf(ref: Int): Rule(AndroidRelativeLayout.LEFT_OF, ref)
class RightOf(ref: Int): Rule(AndroidRelativeLayout.RIGHT_OF, ref)
class StartOf(ref: Int): Rule(AndroidRelativeLayout.START_OF, ref)

data class RelativeLayoutParams(
    override val width: Size = WrapContent,
    override val height: Size = WrapContent,
    val margin: Bounds = Bounds(0.dp),
    val rules: Set<Rule> = setOf()
): LayoutParams


class RelativeLayoutParamsBinder : ParamBinder {

    override fun diff(context: Context, old: LayoutParams, new: LayoutParams): List<Mutation> =
        listOf<ChangeSpec<RelativeLayoutParams, *, AndroidRelativeLayout.LayoutParams>>(
            { it: RelativeLayoutParams -> it.width } rendersTo { width = context.toInt(it) },
            { it: RelativeLayoutParams -> it.height } rendersTo { height = context.toInt(it) },
            { it: RelativeLayoutParams -> it.margin.bottom } rendersTo { bottomMargin = context.toInt(it) },
            { it: RelativeLayoutParams -> it.margin.top } rendersTo { topMargin = context.toInt(it) },
            { it: RelativeLayoutParams -> it.margin.left } rendersTo { leftMargin = context.toInt(it) },
            { it: RelativeLayoutParams -> it.margin.right } rendersTo { rightMargin = context.toInt(it) },
            { it: RelativeLayoutParams -> it.rules } transitsTo { oldRules, newRules ->
                run {
                    (oldRules - newRules).forEach { it.remove(this) }
                    (newRules - oldRules).forEach { it.add(this) }
                }
            }
        ).diff(old as RelativeLayoutParams, new as RelativeLayoutParams)

    override val empty = RelativeLayoutParams()

    override fun getEmptyTarget(context: Context) =
        AndroidRelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

}

data class RelativeLayout(
    val verticalGravity: Gravity = Gravity.TOP,
    val horisontalGravity: Gravity = Gravity.LEFT,
    override val children: List<View> = listOf()
): Container

class RelativeContainerBinder : ElementBinder {
    override val empty = RelativeLayout()
    override fun getEmptyTarget(context: Context) = AndroidRelativeLayout(context)

    private val changeSpec = listOf<ChangeSpec<RelativeLayout, *, AndroidRelativeLayout>>(
        { it: RelativeLayout -> it.horisontalGravity } rendersTo { setHorizontalGravity(it.value) },
        { it: RelativeLayout -> it.verticalGravity } rendersTo { setVerticalGravity(it.value) }
    )

    override fun diff(context: Context, old: Any, new: Any): List<Mutation> =
        changeSpec.diff(
            old as RelativeLayout,
            new as RelativeLayout
        ) + getCollectionDiff(context, old.children, new.children)
}
