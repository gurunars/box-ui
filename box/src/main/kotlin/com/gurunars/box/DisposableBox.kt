package com.gurunars.box

import io.reactivex.disposables.Disposable

/** IBox wrapper capable of disposing all the listeners of the IBox */
class DisposableBox<Type>(private val box: IBox<Type>): IBox<Type> by box {

    private val disposables = mutableListOf<Disposable>()

    override fun onChange(listener: (value: Type) -> Unit) =
        box.onChange(listener).apply {
            disposables.add(this)
        }

    /** Drops all the disposables of the IBox */
    fun dispose() {
        disposables.forEach { if (!it.isDisposed) it.dispose() }
    }

}

/** IRoBox wrapper capable of disposing all the listeners of the IRoBox */
class DisposableRoBox<Type>(private val box: IRoBox<Type>): IRoBox<Type> by box {

    private val disposables = mutableListOf<Disposable>()

    override fun onChange(listener: (value: Type) -> Unit) =
        box.onChange(listener).apply {
            disposables.add(this)
        }

    /** Drops all the disposables of the IRoBox */
    fun dispose() {
        disposables.forEach { if (!it.isDisposed) it.dispose() }
    }

}