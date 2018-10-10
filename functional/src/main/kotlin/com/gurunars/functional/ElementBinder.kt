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
typealias ValueGetter<PropsType, PropType> = PropsType.() -> PropType

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

class ChangeSpecs<PropsType, ViewType> {
    protected val specs: MutableList<ChangeSpec<PropsType, Any, ViewType>> = mutableListOf()

    @Suppress("UNCHECKED_CAST")
    private fun <PropType> add(spec: ChangeSpec<PropsType, PropType, ViewType>) =
        specs.add(spec as ChangeSpec<PropsType, Any, ViewType>)

    fun <PropType> rendersTo(getter: ValueGetter<PropsType, PropType>, mutator: ViewType.(new: PropType) -> Unit) =
        add(ChangeSpec(getter, { _: PropType, new: PropType -> mutator(this, new) }))

    fun <PropType> transitsTo(getter: ValueGetter<PropsType, PropType>, mutator: Mutator<PropType, ViewType>) =
        add(ChangeSpec(getter, mutator))

    fun diff(old: PropsType, new: PropsType) =
        specs.mapNotNull { it.produce(old, new) }

    operator fun plus(other: ChangeSpecs<PropsType, ViewType>) =
        ChangeSpecs<PropsType, ViewType>().apply {
            specs.addAll(this@ChangeSpecs.specs)
            specs.addAll(other.specs)
        }

}

fun<PropsType, ViewType> changeSpecs(init: ChangeSpecs<PropsType, ViewType>.() -> Unit) =
    ChangeSpecs<PropsType, ViewType>().apply {
        init()
    }


interface Binder<Source, Target> {
    val empty: Source
    fun getEmptyTarget(context: Context): Target
    fun diff(context: Context, old: Source, new: Source): List<Mutation>
}

interface LayoutParams {
    val width: Size
    val height: Size
}

interface ElementBinder : Binder<Any, View>
interface ParamBinder : Binder<LayoutParams, ViewGroup.LayoutParams>


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


