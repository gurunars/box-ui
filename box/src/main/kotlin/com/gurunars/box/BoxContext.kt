package com.gurunars.box

import android.arch.lifecycle.LifecycleOwner

class BoxContext<T>(
    private val lifecycleOwner: LifecycleOwner,
    val context: T
) : LifecycleOwner by lifecycleOwner {

    /**
     * Returns a nested lifecycle annotated context
     */
    fun <T> with(t: T): BoxContext<T> =
        BoxContext(lifecycleOwner, t)

    /**
     * Subscribes to changes of the value. Aware of the previous state.
     *
     * @param listener a function called with a new value after the change takes place
     * @param hot if true, immediately executes the listener with the current value
     *            otherwise just adds it to the collection of subscribers
     */
    fun <T> Box<T>.onChange(hot: Boolean=true, listener: (value: T) -> Unit) {
        onChange(this@BoxContext, hot, listener)
    }

    /**
     * Creates a two-way binding between this and a target box. Whenever one changes another
     * gets updated automatically.
     *
     * @param target box to bind to
     */
    fun <T> Box<T>.bind(target: Box<T>) {
        onChange(this@BoxContext) { item -> target.set(item) }
        target.onChange(this@BoxContext) { item -> this.set(item) }
    }

    /**
     * Shortcut function that alters the value of the box.
     *
     * @param patcher a mutator meant to transform the original value into the patched vale.
     */
    fun <T> Box<T>.patch(patcher: T.() -> T) =
        set(get().patcher())

    /**
     * A short way to wrap a value into a Box
     */
    val <T> T.box
       get(): Box<T> = Box(this)

    /**
     * Returns a box that has a one-way binding to this one.
     * I.e. if this one changes another box gets changes as well. However if another box
     * changes this one remains unchanged.
     *
     * @param reduce transforms the value of this box into the value of the other box
     */
    fun <From, To> Box<From>.branch(
        reduce: From.() -> To
    ) = Box(get().reduce()).apply {
        this@branch.onChange(this@BoxContext) { set(it.reduce()) }
    }

    /**
     * Returns a box that has a two-way binding to this one.
     * I.e. if this one changes another box gets changed and vice versa.
     *
     * @param reduce transforms the value of this box into the value of the other box
     * @param patchSource transforms the value of this box based on the value of the box
     */
    fun <From, To> Box<From>.branch(
        reduce: From.() -> To,
        patchSource: From.(part: To) -> From
    ): Box<To> {
        val branched = Box(get().reduce())
        branched.onChange(this@BoxContext) { item -> this@branch.patch { patchSource(item) } }
        onChange(this@BoxContext) { parent -> branched.set(parent.reduce()) }
        return branched
    }

    /**
     * Applies a common change listener to a list of fields.
     */
    fun List<Box<*>>.onChange(listener: () -> Unit) {
        forEach { it.onChange { _ -> listener() } }
    }

}