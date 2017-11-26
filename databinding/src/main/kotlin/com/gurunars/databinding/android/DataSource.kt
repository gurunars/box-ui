package com.gurunars.databinding.android

import com.gurunars.databinding.IBox
import com.gurunars.databinding.Listener

class DataSource<ItemType>(
    private val getF: () -> ItemType,
    private val setF: (item: ItemType) -> Unit,
    private val onRefetch: () -> Unit = {}
): IBox<ItemType> {

    fun refetch() {

    }

    override fun set(value: ItemType, force: Boolean) {
        TODO("not implemented")
    }

    override fun get(): ItemType {
        TODO("not implemented")
    }

    override fun onChange(listener: Listener<ItemType>) {
        TODO("not implemented")
    }


}