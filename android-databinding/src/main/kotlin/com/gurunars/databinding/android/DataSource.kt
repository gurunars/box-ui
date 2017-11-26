package com.gurunars.databinding.android

import com.gurunars.databinding.IBox
import com.gurunars.databinding.Listener
import com.gurunars.databinding.box
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.doAsyncResult

class DataSource<ItemType>(
    getF: () -> ItemType,
    setF: (value: ItemType) -> Any,
    initial: ItemType,
    onFetch: () -> Any
) : IBox<ItemType> {

    private val box = initial.box

    init {
        val t = doAsyncResult {
            "FOO"
        }
        t.doAsync {  }
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