package com.gurunars.box.ui.components.floatmenu

import android.animation.FloatEvaluator
import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import com.gurunars.box.core.*
import com.gurunars.box.ui.*
import com.gurunars.box.ui.components.Icon
import com.gurunars.box.ui.components.iconView


class BooleanAnimator(
    private val duration: Long,
    flag: IReadOnlyObservableValue<Boolean>,
    private val attachedToWindow: IReadOnlyObservableValue<Boolean>
) {

    private val _animatedValue = ObservableValue(0f)

    fun int(start: Int, end: Int): IReadOnlyObservableValue<Int> =
        _animatedValue.bind { IntEvaluator().evaluate(this, start, end) }

    fun float(): IReadOnlyObservableValue<Float> =
        _animatedValue

    fun float(start: Float, end: Float): IReadOnlyObservableValue<Float> =
        _animatedValue.bind { FloatEvaluator().evaluate(this, start, end) }

    private var animator: ValueAnimator? = null

    private fun transitionTo(float: Float) {
        if (attachedToWindow.get()) {
            animator?.cancel()
            animator = ValueAnimator.ofFloat(_animatedValue.get(), float).apply {
                startDelay = 0
                duration = this@BooleanAnimator.duration
                addUpdateListener {
                    addUpdateListener { _animatedValue.set(it.animatedValue as Float) }
                }
                start()
            }
        } else {
            _animatedValue.set(float)
        }
    }

    init {
        flag.inUi.onChange {
            if (it) {
                transitionTo(1f)
            } else {
                transitionTo(0f)
            }
        }
    }

}


fun Context.contextualFloatMenu(
    contentView: View,
    menuView: View,
    isOpen: IObservableValue<Boolean> = ObservableValue(false)
) = relativeLayout {

    val animator = BooleanAnimator(
        duration = 400L,
        flag = isOpen,
        attachedToWindow = attachedToWindow
    )

    contentView.layoutParams {
        matchParent()
    }

    menuView.apply {
        alpha_ = animator.float()
        isVisible_ = merge(isOpen, animator.float()).bind { first || second != 0f }
    }.layoutParams {
        matchParent()
    }

    iconView(icon = ObservableValue(Icon(
        icon = R.drawable.ic_menu_close
    ))).apply {
        id = R.id.openFab
        rotation_ = animator.float(0f, 360f)
        onClick { isOpen.set(false) }
    }.layoutParams {
        alignInParent(HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)

        animator.int(0, 60).onChange { size ->
            width = dip(size)
            height = dip(size)
            margin = Bounds(dip((60 / 2) + 15 - size / 2))
            it.requestLayout()
        }

    }

}