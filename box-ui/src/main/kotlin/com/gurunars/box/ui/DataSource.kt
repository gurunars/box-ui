package com.gurunars.box.ui

import com.gurunars.box.Box
import com.gurunars.box.IBox
import com.gurunars.box.Listener
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
    private val buffer = UiThrottleBuffer()

    val ready = Box(false)

    fun refetch() {
        doAsync {
            ready.set(false)
            try {
                val next = getF()
                uiThread {
                    ready.set(true)
                    box.set(next, true)
                }
            } catch (exe: Exception) {
                uiThread {
                    ready.set(true)
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
        // We do not want to set anything before at least the initial load took place
        if (!ready.get() && !force) {
            return false
        }
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
        box.onChange(hot, { if (ready.get()) listener(it) })
}