package com.gurunars.functional

import android.app.Activity
import android.content.Context
import android.view.View
import com.gurunars.box.IRoBox
import java.util.*
import kotlin.reflect.KClass


typealias Mutation = (view: View) -> Unit
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
            { view: View -> mutator(view as ViewType, oldP, newP) }
        }
    }
}

infix fun <PropsType, PropType, ViewType> ValueGetter<PropsType, PropType>.transitsTo(mutator: Mutator<PropType, ViewType>) =
    ChangeSpec(this, mutator)


infix fun <PropsType, PropType, ViewType> ValueGetter<PropsType, PropType>.rendersTo(mutator: ViewType.(new: PropType) -> Unit) =
    ChangeSpec<PropsType, PropType, ViewType>(
        this,
        { old: PropType, new: PropType -> mutator(this, new) })


interface Component {
    val empty: Any
    val viewType: KClass<View>
    fun getEmptyView(context: Context): View
    fun diff(old: Any, new: Any): List<Mutation>
}


@Suppress("UNCHECKED_CAST")
fun <PropsType> List<ChangeSpec<PropsType, *, *>>.diff(old: PropsType, new: PropsType) =
    mapNotNull { it.produce(old, new) }


fun <StateType, ComponentType> Activity.ui(
    state: IRoBox<StateType>,
    init: StateType.() -> ComponentType
) {
    var currentState = state.get().init()
    Registry.getElement(currentState).let {
        val view = it.getEmptyView(this@ui).apply {
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
        this.setContentView(view)
    }
}


