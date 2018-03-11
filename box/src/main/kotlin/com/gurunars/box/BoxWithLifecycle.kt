package com.gurunars.box

import io.reactivex.disposables.Disposable

/**
 * Interface to manipulate lifecycle related behavior of the Box
 */
interface WithLifecycle {
    /** Temporarily pauses value syncing */
    fun stop()
    /** Resumes value syncing */
    fun start()
    /** Drops all the disposables of the IRoBox */
    fun dispose()
}

private class LifecycleHandler: WithLifecycle {

    private val disposables = mutableListOf<Disposable>()
    private var isActive = false

    override fun stop() { isActive = false }
    override fun start() { isActive = true }

    override fun dispose() {
        disposables.forEach { if (!it.isDisposed) it.dispose() }
    }

    fun<Type> onChange(box: IRoBox<Type>, listener: (value: Type) -> Unit) =
        box.onChange({ if (isActive) listener(it) }).apply { disposables.add(this) }

}

/** */
class BoxWithLifecycle<Type> private constructor(
    private val box: IBox<Type>, private val lifecycleHandler: LifecycleHandler
): IBox<Type> by box, WithLifecycle by lifecycleHandler {
    /** */
    constructor(box: IBox<Type>): this(box, LifecycleHandler())
    override fun onChange(listener: (value: Type) -> Unit) =
        lifecycleHandler.onChange(box, listener)

    override fun start() {
        lifecycleHandler.start()
        broadcast()
    }
}

/** */
class RoBoxWithLifecycle<Type> private constructor(
    private val box: IRoBox<Type>, private val lifecycleHandler: LifecycleHandler
): IRoBox<Type> by box, WithLifecycle by lifecycleHandler {
    /** */
    constructor(box: IRoBox<Type>): this(box, LifecycleHandler())
    override fun onChange(listener: (value: Type) -> Unit) =
        lifecycleHandler.onChange(box, listener)

    override fun start() {
        lifecycleHandler.start()
        broadcast()
    }
}
