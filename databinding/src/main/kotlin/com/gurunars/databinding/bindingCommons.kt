package com.gurunars.databinding

/**
 * Returns a box that has a one-way binding to this one.
 * I.e. if this one changes another box gets changes as well. However if another box
 * changes this one remains unchanged.
 *
 * @param reduce a function to transform the payload of this box into the payload of the other
 * box
 */
inline fun <From, To> IBox<From>.branch(
    crossinline reduce: From.() -> To
) = Box(get().reduce()).apply {
    this@branch.onChange { item -> set(item.reduce()) }
}

/**
 * Shortcut function that alters the value of the box.
 *
 * @param patcher a mutator meant to transform the original value into the patched vale.
 */
inline fun <ItemType> IBox<ItemType>.patch(patcher: ItemType.() -> ItemType) =
    set(get().patcher())

/**
 * Creates a two-way binding between this and a target box. Whenever one changes another
 * gets updated automatically.
 *
 * @param target box to bind to
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <Type> IBox<Type>.bind(target: IBox<Type>) {
    onChange { item -> target.set(item) }
    target.onChange { item -> this.set(item) }
}

/**
 * Returns a box that has a two-way binding to this one.
 * I.e. if this one changes another box gets changed and vice versa.
 *
 * @param reduce a function to transform the payload of this box into the payload of the other
 * box
 * @param patchSource a function to transform the value of this box based on the value of the
 * other box
 */
inline fun <From, To> IBox<From>.branch(
    crossinline reduce: From.() -> To,
    crossinline patchSource: From.(part: To) -> From
): IBox<To> {
    val branched = Box(get().reduce())
    branched.onChange { item -> this@branch.patch { patchSource(item) } }
    onChange { parent -> branched.set(parent.reduce()) }
    return branched
}

/**
 * A special case of a two-way branch function for the situation when
 * a both this and another box are of the same type.
 *
 * @param transform a function to be called whenever value of this or another box changes.
 */
inline fun <Type> IBox<Type>.fork(
    crossinline transform: Type.() -> Type
) = branch(transform, { it.transform() })

/**
 * A short way to wrap a value into a Box
 */
@Suppress("NOTHING_TO_INLINE")
inline val <F> F.box
    get(): IBox<F> = Box(this)

/**
 * Listener aware of the current state of the observable.
 */
typealias SimpleListener<Type> = (value: Type) -> Unit

/**
 * Simple onChange listener that does not rely on a previous state of the boxed values.
 */
inline fun <Type> IBox<Type>.onChange(
    hot: Boolean = true,
    crossinline listener: SimpleListener<Type>
) = onChange(hot) { _, value -> listener(value) }