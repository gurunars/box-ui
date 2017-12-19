package com.gurunars.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import java.util.*

/**
 * A convenient wrapper around
 */
class Box<Type>(
    private val initialValue: Type,
    override val owner: LifecycleOwner
) : IBox<Type> {
    private val data = MutableLiveData<Type>()
    private var prevValue: Type = initialValue

    init {
        data.value = initialValue
    }

    override fun LifecycleOwner.onChange(hot: Boolean, listener: Listener<Type>) {
        if (hot) listener(this@Box.prevValue, this@Box.get())
        data.observe(this, object : Observer<Type> {
            override fun onChanged(t: Type?) {
                listener(this@Box.prevValue, t ?: initialValue)
            }
        })
    }

    override fun set(value: Type, force: Boolean): Boolean {
        if (force || !Objects.deepEquals(get(), value)) {
            this.data.value = value
            this.prevValue = value
            return true
        }
        return false
    }

    override fun get(): Type = this.data.value!!

    /**
     * Creates a two-way binding between this and a target box. Whenever one changes another
     * gets updated automatically.
     */
    inline fun <To>LifecycleOwner.bind(
        target: IBox<To>,
        crossinline sourceToTarget: (source: Type) -> To,
        crossinline targetToSource: (target: To) -> Type
    ) {
        onChange { item -> target.set(sourceToTarget(item))}
        target.apply {
            onChange { item -> this@Box.set(targetToSource(item)) }
        }
    }

    /**
     * Returns a box that has a two-way binding to this one.
     * I.e. if this one changes another box gets changed and vice versa.
     */
    inline fun <To>LifecycleOwner.branch(
        crossinline reduce: Type.() -> To,
        crossinline patchSource: Type.(part: To) -> Type
    ) {
        val branched = Box(get().reduce(), owner)
        bind(branched, )
    }

}

fun LifecycleOwner.bla() {
    val box = Box(true, this)
    box.branch {  }
}