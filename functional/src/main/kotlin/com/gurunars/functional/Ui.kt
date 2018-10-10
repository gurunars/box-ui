package com.gurunars.functional

import android.app.Activity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.gurunars.box.IRoBox

fun <StateType, ComponentType> Activity.ui(
    state: IRoBox<StateType>,
    init: StateType.() -> ComponentType
) {
    var currentState = state.get().init()
    Registry.getElementBinder(currentState).let { binder ->
        val view = binder.getEmptyTarget(this@ui).apply {
            binder.diff(this@ui, binder.empty, currentState as Any).forEach {
                it(this)
            }
        }
        state.onChange { new ->
            val newState = new.init()
            binder.diff(this@ui, currentState as Any, newState as Any).forEach {
                it(view)
            }
            currentState = newState
        }
        setContentView(
            view, ViewGroup.LayoutParams(
            MATCH_PARENT, MATCH_PARENT
        ))
    }
}


