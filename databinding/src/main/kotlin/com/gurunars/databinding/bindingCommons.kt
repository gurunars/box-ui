package com.gurunars.databinding

fun <From, To> BindableField<From>.childField(
    reduce: From.() -> To
) = BindableField(get().reduce()).apply {
    this@childField.onChange {
        set(this@childField.get().reduce())
    }
}

fun<ItemType> BindableField<ItemType>.patch(patcher: ItemType.() -> ItemType) =
    set(get().patcher())

fun <From, To> BindableField<From>.childField(
    reduce: From.() -> To,
    patchParent: From.(part: To) -> From
) = BindableField(get().reduce()).apply {
    this@childField.bind(
        this,
        { it.reduce() },
        { this@childField.get().patchParent(it) }
    )
}
