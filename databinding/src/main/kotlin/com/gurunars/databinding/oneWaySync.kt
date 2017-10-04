package com.gurunars.databinding

fun<ItemType> BindableField<ItemType>.sendTo(other: BindableField<ItemType>) =
    onChange { other.set(it) }

fun<ItemType, OtherType> BindableField<ItemType>.sendTo(
    other: BindableField<OtherType>,
    transform: (source: ItemType) -> OtherType
) =
    onChange { other.set(transform(it)) }

fun<ItemType> BindableField<ItemType>.receiveFrom(other: BindableField<ItemType>) =
    other.onChange { set(it) }

fun<ItemType, OtherType> BindableField<ItemType>.receiveFrom(
    other: BindableField<OtherType>,
    transform: (source: OtherType) -> ItemType
) =
    other.onChange { set(transform(it)) }