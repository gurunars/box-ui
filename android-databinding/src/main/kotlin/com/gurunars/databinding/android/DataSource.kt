package com.gurunars.databinding.android

import com.gurunars.databinding.Box
import com.gurunars.databinding.IBox
import com.gurunars.databinding.Listener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Special IBox to glue data storage with an Observerable value
 * in asynchronous manner.
 *
 * @param getF reads data from the storage
 * @param setF writes data to the storage
 * @param preprocess alters the payload before storing it
 * @param initial temporary payload to be
 */
class DataSource<Type>(
    private val getF: () -> Type,
    private val setF: (value: Type) -> Any,
    private val preprocess: (value: Type) -> Type = { it },
    initial: Type
) : IBox<Type> {
    private val box = Box(initial)
    private var ready = false

    fun refetch() {
        doAsync {
            val next = preprocess(getF())
            uiThread {
                ready = true
                box.set(next, true)
            }
        }
    }

    init {
        refetch()
    }

    override fun get() = box.get()

    override fun set(value: Type, force: Boolean): Boolean {
        ready = true
        val sorted = preprocess(value)
        if (box.set(sorted, force)) {
            doAsync {
                setF(sorted)
                refetch()
            }
            return true
        }
        return false
    }

    override fun onChange(hot: Boolean, listener: Listener<Type>) =
        box.onChange(hot, { prev, cur -> if (ready) listener(prev, cur) })
}