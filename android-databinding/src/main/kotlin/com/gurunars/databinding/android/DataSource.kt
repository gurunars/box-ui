package com.gurunars.databinding.android

import com.gurunars.databinding.Box
import com.gurunars.databinding.IBox
import com.gurunars.databinding.Listener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DataSource<Type>(
    private val getF: () -> Type,
    private val setF: (value: Type) -> Any,
    initial: Type
) : IBox<Type> {
    private val box = Box(initial)

    fun refetch() {
        doAsync {
            val next = getF()
            uiThread {
                box.set(next, false)
            }
        }
    }

    override fun get() = box.get()

    override fun set(value: Type, force: Boolean): Boolean {
        if (box.set(value, force)) {
            doAsync {
                setF(value)
                refetch()
            }
            return true
        }
        return false
    }

    override fun onChange(hot: Boolean, listener: Listener<Type>)
        = box.onChange(hot, listener)

}