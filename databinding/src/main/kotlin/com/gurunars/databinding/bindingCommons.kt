package com.gurunars.databinding

inline fun <From, To> BindableField<From>.branch(
    crossinline reduce: From.() -> To
) = BindableField(get().reduce()).apply {
    this@branch.onChange {
        set(this@branch.get().reduce())
    }
}

inline fun<ItemType> BindableField<ItemType>.patch(patcher: ItemType.() -> ItemType) =
    set(get().patcher())

inline fun <From, To> BindableField<From>.branch(
    crossinline reduce: From.() -> To,
    crossinline patchParent: From.(part: To) -> From
) = BindableField(get().reduce()).apply {
    this@branch.bind(
        this,
        { it.reduce() },
        { this@branch.get().patchParent(it) }
    )
}

@Suppress("NOTHING_TO_INLINE")
inline val <F> F.field
    get(): BindableField<F> = BindableField(this)