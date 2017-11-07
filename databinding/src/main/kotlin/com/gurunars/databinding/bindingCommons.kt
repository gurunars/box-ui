package com.gurunars.databinding

inline fun <From, To> BindableField<From>.branch(
    crossinline reduce: From.() -> To
) = BindableField(get().reduce()).apply {
    this@branch.onChange { item -> set(item.reduce()) }
}

inline fun <ItemType> BindableField<ItemType>.patch(patcher: ItemType.() -> ItemType) =
    set(get().patcher())

/**
 * Create a two-way bond between this and another target. Whenever one changes its value another
 * get updated automatically.
 *
 * @param target target to bind to
 */
fun <Type>BindableField<Type>.bind(target: BindableField<Type>) {
    onChange { item -> target.set(item) }
    target.onChange { item -> this.set(item) }
}

inline fun <From, To> BindableField<From>.branch(
    crossinline reduce: From.() -> To,
    crossinline patchParent: From.(part: To) -> From
): BindableField<To> {
    val branched = BindableField(get().reduce())
    branched.onChange { item -> this@branch.patch { patchParent(item) } }
    onChange { parent -> branched.set(parent.reduce()) }
    return branched
}

inline fun <Type> BindableField<Type>.fork(
    crossinline transform: Type.() -> Type
) = branch(transform, { it.transform() })

@Suppress("NOTHING_TO_INLINE")
inline val <F> F.field
    get(): BindableField<F> = BindableField(this)