package com.gurunars.functional

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.gurunars.box.IRoBox
import java.util.*


typealias Mutation = (view: Any) -> Unit
typealias Mutator<PropType, ViewType> = ViewType.(old: PropType, new: PropType) -> Unit
typealias ValueGetter<PropsType, PropType> = (item: PropsType) -> PropType


class ChangeSpec<in PropsType, PropType, ViewType>(
    private val valueGetter: ValueGetter<PropsType, PropType>,
    private val mutator: Mutator<PropType, ViewType>
) {
    @Suppress("UNCHECKED_CAST")
    fun produce(old: PropsType, new: PropsType): Mutation? {
        val newP = valueGetter(new)
        val oldP = valueGetter(old)
        return if (Objects.deepEquals(oldP, newP)) {
            null
        } else {
            { view: Any -> mutator(view as ViewType, oldP, newP) }
        }
    }
}

infix fun <PropsType, PropType, ViewType> ValueGetter<PropsType, PropType>.transitsTo(mutator: Mutator<PropType, ViewType>) =
    ChangeSpec(this, mutator)


infix fun <PropsType, PropType, ViewType> ValueGetter<PropsType, PropType>.rendersTo(mutator: ViewType.(new: PropType) -> Unit) =
    ChangeSpec<PropsType, PropType, ViewType>(
        this,
        { _: PropType, new: PropType -> mutator(this, new) })

interface Binder<Source, Target> {
    val empty: Source
    fun getEmptyTarget(context: Context): Target
    fun diff(old: Source, new: Source): List<Mutation>
}

interface LayoutParams {
    val width: Size
    val height: Size
}

interface ElementBinder : Binder<Any, View>
interface ParamBinder : Binder<LayoutParams, ViewGroup.LayoutParams>

@Suppress("UNCHECKED_CAST")
fun <PropsType> List<ChangeSpec<PropsType, *, *>>.diff(old: PropsType, new: PropsType) =
    mapNotNull { it.produce(old, new) }


fun <StateType, ComponentType> Activity.ui(
    state: IRoBox<StateType>,
    init: StateType.() -> ComponentType
) {
    var currentState = state.get().init()
    Registry.getElement(currentState).let {
        val view = it.getEmptyTarget(this@ui).apply {
            it.diff(it.empty, currentState as Any).forEach {
                it(this)
            }
        }
        state.onChange { new ->
            val newState = new.init()
            it.diff(currentState as Any, newState as Any).forEach {
                it(view)
            }
            currentState = newState
        }
        view.let {
            setContentView(it, ViewGroup.LayoutParams(
                MATCH_PARENT, MATCH_PARENT
            ))
        }
    }
}


