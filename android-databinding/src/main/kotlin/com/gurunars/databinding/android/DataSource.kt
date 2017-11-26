package com.gurunars.databinding.android

import com.gurunars.databinding.IBox
import com.gurunars.databinding.Listener
import com.gurunars.databinding.box
import com.gurunars.databinding.onChange
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DataSource<ItemType>(
    private val getF: () -> ItemType,
    private val setF: (value: ItemType) -> Any,
    initial: ItemType
) : IBox<ItemType> {

    private val box = initial.box

    private fun refetch(after: () -> Unit) {
        doAsync {
            val data = getF()
            uiThread {
                set(data)
                after()
            }
        }
    }

    fun refetch() = refetch({})

    init {
        refetch {
            box.onChange(false) { it -> setF(it) }
        }
    }

    override fun set(value: ItemType, force: Boolean) =
        box.set(value, force)

    override fun get(): ItemType =
        box.get()

    override fun onChange(hot: Boolean, listener: Listener<ItemType>) =
        box.onChange(hot, listener)
}