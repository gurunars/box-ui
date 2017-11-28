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
    private var cache = initial

    private fun refetch(after: () -> Unit) {
        doAsync {
            cache = getF()
            uiThread {
                set(cache)
                after()
            }
        }
    }

    fun refetch() = refetch({})

    init {
        refetch {
            box.onChange(false) { it ->
                if (cache != it) {
                    doAsync {
                        setF(it)
                        refetch()
                    }
                }
            }
        }
    }

    override fun set(value: ItemType, force: Boolean) {
        box.set(value, force)
    }

    override fun get(): ItemType =
        box.get()

    override fun onChange(hot: Boolean, listener: Listener<ItemType>) =
        box.onChange(hot, listener)
}