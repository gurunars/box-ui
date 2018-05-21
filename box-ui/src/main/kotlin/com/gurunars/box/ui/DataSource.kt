package com.gurunars.box.ui

import com.gurunars.box.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.TimeUnit

/**
 * Special IBox to glue data storage with an Observerable value
 * in asynchronous manner.
 *
 * @property ready true if the box contains the latest version of the payload, false if the payload
 *           is being fetched
 */
class DataSource<Type> private constructor(
    private val getF: () -> Type,
    private val setF: (value: Type) -> Any,
    private val preprocess: (value: Type) -> Type = { it },
    timeout: Long = 500,
    private val box: BoxWithLifecycle<Type>
) : IBox<Type> by box, WithLifecycle by box {

    /**
     * @param getF reads data from the storage
     * @param setF writes data to the storage
     * @param initial temporary payload to be
     * @param preprocess function to process the data just after it is set before refetching - it is
     *                   a reasonable way e.g. to enforce sorting without UI flickering
     */
    constructor(
        getF: () -> Type,
        setF: (value: Type) -> Any,
        preprocess: (value: Type) -> Type = { it },
        initial: Type,
        timeout: Long = 500
    ) : this(getF, setF, preprocess, timeout, Box<Type>(preprocess(initial)).withLifecycle)

    private val _ready = Box(false)
    val ready: IRoBox<Boolean> = _ready
    private val started = false.box

    private fun setV(value: Type) {
        box.set(value)
        _ready.set(true)
        started.set(true)
    }

    init {
        box.toObservable()
            .filter { _ready.get() }
            .skip(1)
            .debounce(timeout, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { _ready.set(false) }
            .observeOn(Schedulers.io())
            .map {
                val original = getF()
                if (original == it) {
                    return@map original
                } else {
                    setF(it)
                    preprocess(getF())
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::setV, { throw it })

        started.onChange {
            if (it) {
                box.start()
            } else {
                box.stop()
            }
        }
    }

    /** Triggers data source refetch */
    fun reload() {
        _ready.set(false)
        doAsync {
            val init = preprocess(getF())
            uiThread { _ -> this@DataSource.setV(init) }
        }
    }

    /** @see Box.set */
    override fun set(value: Type): Boolean {
        // We do not want to set anything before at least the initial load took place
        return if (!_ready.get()) {
            false
        } else {
            box.set(preprocess(value))
        }
    }

    override fun start() {
        reload()
    }

    override fun stop() {
        started.set(false)
    }

}