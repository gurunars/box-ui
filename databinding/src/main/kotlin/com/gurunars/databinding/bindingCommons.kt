package com.gurunars.databinding

fun <From, To> BindableField<From>.map(
    reduce: From.() -> To
) = BindableField(get().reduce()).apply {
    this@map.onChange {
        set(this@map.get().reduce())
    }
}

fun<ItemType> BindableField<ItemType>.patch(patcher: ItemType.() -> ItemType) =
    set(get().patcher())

fun <From, To> BindableField<From>.map(
    reduce: From.() -> To,
    patch: From.(part: To) -> From
) = BindableField(get().reduce()).apply {
    this@map.bind(
        this,
        { it.reduce() },
        { this@map.get().patch(it) }
    )
}
