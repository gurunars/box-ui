package com.gurunars.box.ui

import com.gurunars.box.Box
import com.gurunars.box.IBox
import com.gurunars.box.toObservable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.TimeUnit

/**
 * Special IBox to glue data storage with an Observerable value
 * in asynchronous manner.
 *
 * @param getF reads data from the storage
 * @param setF writes data to the storage
 * @param initial temporary payload to be
 * @param preprocess function to process the data just after it is set before refetching - it is
 *                   a reasonable way e.g. to enforce sorting without UI flickering
 * @property ready true if the box contains the latest version of the payload, false if the payload
 *                 is being fetched
 */
class DataSource<Type>(
    private val getF: () -> Type,
    private val setF: (value: Type) -> Any,
    private val preprocess: (value: Type) -> Type = { it },
    initial: Type
) : IBox<Type> {
    private val box = Box(preprocess(initial))

    val ready = Box(false)

    private fun set(value: Type) {
        ready.set(true)
        box.set(value)
    }

    init {
        box.toObservable()
            .skip(1)
            .subscribeOn(Schedulers.newThread())
            .debounce(500, TimeUnit.MILLISECONDS)
            .filter { ready.get() }
            .map {
                ready.set(false)
                setF(it)
                preprocess(getF())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::set, { throw it })
        doAsync {
            val init = preprocess(getF())
            uiThread { _ -> this@DataSource.set(init) }
        }
    }

    /** @see Box.get */
    override fun get() = box.get()

    /** @see Box.set */
    override fun set(value: Type, force: Boolean): Boolean {
        // We do not want to set anything before at least the initial load took place
        return if (!ready.get() && !force) {
            false
        } else {
            box.set(preprocess(value), force)
        }
    }

    /** @see Box.onChange */
    override fun onChange(listener: (item: Type) -> Unit) =
        box.onChange { if (ready.get()) listener(it) }
}