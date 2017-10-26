package com.gurunars.databinding

fun <From, To> BindableField<From>.branch(
    reduce: From.() -> To
) = BindableField(get().reduce()).apply {
    this@branch.onChange {
        set(this@branch.get().reduce())
    }
}

fun<ItemType> BindableField<ItemType>.patch(patcher: ItemType.() -> ItemType) =
    set(get().patcher())

fun <From, To> BindableField<From>.branch(
    reduce: From.() -> To,
    patchParent: From.(part: To) -> From
) = BindableField(get().reduce()).apply {
    this@branch.bind(
        this,
        { it.reduce() },
        { this@branch.get().patchParent(it) }
    )
}
