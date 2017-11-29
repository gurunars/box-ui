package com.gurunars.databinding.android

import com.gurunars.databinding.Box
import com.gurunars.databinding.IBox
import com.gurunars.databinding.Listener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DataSource<Type>(
    private val getF: () -> Type,
    private val setF: (value: Type) -> Any,
    private val preset: (value: Type) -> Type = { it },
    initial: Type
) : IBox<Type> {
    private val box = Box(initial)

    fun refetch() {
        doAsync {
            val next = preset(getF())
            uiThread {
                box.set(next, false)
            }
        }
    }

    init {
        refetch()
    }

    override fun get() = box.get()

    override fun set(value: Type, force: Boolean): Boolean {
        val sorted = preset(value)
        if (box.set(sorted, force)) {
            doAsync {
                setF(sorted)
                refetch()
            }
            return true
        }
        return false
    }

    override fun onChange(hot: Boolean, listener: Listener<Type>)
        = box.onChange(hot, listener)

}