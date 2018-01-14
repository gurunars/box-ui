package com.gurunars.box.ui

import com.gurunars.box.Box
import com.gurunars.box.IBox
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Special IBox to glue data storage with an Observerable value
 * in asynchronous manner.
 *
 * @param getF reads data from the storage
 * @param setF writes data to the storage
 * @param initial temporary payload to be
 * @property ready true if the box contains the latest version of the payload, false if the payload
 *                 is being fetched
 */
class DataSource<Type>(
    private val getF: () -> Type,
    private val setF: (value: Type) -> Any,
    initial: Type
) : IBox<Type> {
    private val box = Box(initial)
    private val buffer = ThrottleBuffer()

    val ready = Box(false)

    /** Reloads the payload from data storage. */
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

    /** @see Box.get */
    override fun get() = box.get()

    /** @see Box.set */
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

    /** @see Box.onChange */
    override fun onChange(hot: Boolean, listener: (item: Type) -> Unit) =
        box.onChange(hot, { if (ready.get()) listener(it) })
}