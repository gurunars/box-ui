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
 * @param initial temporary payload to be
 */
class DataSource<Type>(
    private val getF: () -> Type,
    private val setF: (value: Type) -> Any,
    initial: Type
) : IBox<Type> {
    private val box = Box(initial)
    private var ready = false

    val buffer = UiThrottleBuffer()

    fun refetch() {
        doAsync {
            try {
                val next = getF()
                uiThread {
                    ready = true
                    box.set(next, true)
                }
            } catch (exe: Exception) {
                uiThread {
                    throw exe
                }
            }
        }
    }

    init {
        refetch()
    }

    override fun get() = box.get()

    override fun set(value: Type, force: Boolean): Boolean {
        ready = true
        if (box.set(value, force)) {
            buffer.call {
                doAsync {
                    try {
                        setF(value)
                        refetch()
                    } catch (exe: Exception) {
                        uiThread {
                            throw exe
                        }
                    }
                }
            }
            return true
        }
        return false
    }

    override fun onChange(hot: Boolean, listener: Listener<Type>) =
        box.onChange(hot, { if (ready) listener(it) })
}