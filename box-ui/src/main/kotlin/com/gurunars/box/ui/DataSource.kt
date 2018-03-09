package com.gurunars.box.ui

import com.gurunars.box.Box
import com.gurunars.box.IBox
import com.gurunars.box.IRoBox
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
    initial: Type,
    timeout: Long = 500
) : IBox<Type> {
    private val box = Box(preprocess(initial))

    private val _ready = Box(false)
    val ready: IRoBox<Boolean> = _ready

    private fun set(value: Type) {
        _ready.set(true)
        box.set(value)
    }

    init {
        box.toObservable()
            .skip(1)
            .filter { _ready.get() }
            .debounce(timeout, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                _ready.set(false)
                it
            }
            .observeOn(Schedulers.io())
            .map {
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

    fun reload() {
        _ready.set(false)
        box.set(getF())
        _ready.set(true)
    }

    /** @see Box.get */
    override fun get() = box.get()

    /** @see Box.set */
    override fun set(value: Type, force: Boolean): Boolean {
        // We do not want to set anything before at least the initial load took place
        return if (!_ready.get() && !force) {
            false
        } else {
            box.set(preprocess(value), force)
        }
    }

    /** @see Box.onChange */
    override fun onChange(listener: (item: Type) -> Unit) =
        box.onChange { if (_ready.get()) listener(it) }
}