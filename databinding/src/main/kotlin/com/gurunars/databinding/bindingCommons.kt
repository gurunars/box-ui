package com.gurunars.databinding

/**
 * Returns a field that has a one-way binding to this one.
 * I.e. if this one changes another field gets changes as well. However if another field
 * changes this one remains unchanged.
 *
 * @param reduce a function to transform the payload of this field into the payload of the other
 * field
 */
inline fun <From, To> BindableField<From>.branch(
    crossinline reduce: From.() -> To
) = BindableField(get().reduce()).apply {
    this@branch.onChange { item -> set(item.reduce()) }
}

/**
 * Shortcut function that alters the value of the field.
 *
 * @param patcher a mutator meant to transform the original value into the patched vale.
 */
inline fun <ItemType> BindableField<ItemType>.patch(patcher: ItemType.() -> ItemType) =
    set(get().patcher())

/**
 * Creates a two-way binding between this and a target field. Whenever one changes another
 * gets updated automatically.
 *
 * @param target field to bind to
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <Type>BindableField<Type>.bind(target: BindableField<Type>) {
    onChange { item -> target.set(item) }
    target.onChange { item -> this.set(item) }
}

/**
 * Returns a field that has a two-way binding to this one.
 * I.e. if this one changes another field gets changed and vice versa.
 *
 * @param reduce a function to transform the payload of this field into the payload of the other
 * field
 * @param patchSource a function to transform the value of this field based on the value of the
 * other field
 */
inline fun <From, To> BindableField<From>.branch(
    crossinline reduce: From.() -> To,
    crossinline patchSource: From.(part: To) -> From
): BindableField<To> {
    val branched = BindableField(get().reduce())
    branched.onChange { item -> this@branch.patch { patchSource(item) } }
    onChange { parent -> branched.set(parent.reduce()) }
    return branched
}

/**
 * A special case of a two-way branch function for the situation when
 * a both this and another field are of the same type.
 *
 * @param transform a function to be called whenever value of this or another field changes.
 */
inline fun <Type> BindableField<Type>.fork(
    crossinline transform: Type.() -> Type
) = branch(transform, { it.transform() })

/**
 * A short way to wrap a value into a BindableField
 */
@Suppress("NOTHING_TO_INLINE")
inline val <F> F.field
    get(): BindableField<F> = BindableField(this)