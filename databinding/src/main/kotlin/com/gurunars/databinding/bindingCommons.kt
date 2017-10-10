package com.gurunars.databinding

fun<From, To> BindableField<List<From>>.map(mapper: (from: From) -> To): BindableField<List<To>> =
    reduce { map { mapper(it) } }

fun <From, To> BindableField<From>.reduce(
    reduce: From.() -> To
) = BindableField(get().reduce()).apply {
    this@reduce.onChange {
        set(this@reduce.get().reduce())
    }
}

fun<ItemType> BindableField<ItemType>.patch(patcher: ItemType.() -> ItemType) =
    set(get().patcher())

fun <Parent, Child> BindableField<Parent>.subField(
    reduce: Parent.() -> Child,
    patch: Parent.(part: Child) -> Parent
) = BindableField(get().reduce()).apply {
    this@subField.bind(
        this,
        { it.reduce() },
        { this@subField.get().patch(it) }
    )
}
