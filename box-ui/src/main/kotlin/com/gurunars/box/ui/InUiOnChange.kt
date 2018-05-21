package com.gurunars.box.ui

import com.gurunars.box.core.IObservableValue
import com.gurunars.box.core.IReadOnlyObservableValue
import com.gurunars.box.core.Wrapper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*

private class UiValue<Type>(private val initial: Type) : IObservableValue<Type> {

    private val subject = BehaviorSubject.createDefault<Wrapper<Type>>(Wrapper(initial))

    override fun onChange(listener: (value: Type) -> Unit): Disposable =
        subject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { listener(subject.value?.payload ?: initial) }

    override fun set(value: Type): Boolean {
        // Safeguard against modifications that do not mutate the value
        val current = subject.value?.payload
        if (!Objects.deepEquals(current, value)) {
            subject.onNext(Wrapper(value))
            return true
        }
        return false
    }

    override fun get(): Type = this.subject.value?.payload ?: initial
}

val <T>IReadOnlyObservableValue<T>.inUi: IReadOnlyObservableValue<T>
    get() = UiValue(get()).let {
        onChange { value -> it.set(value) }
        return it
    }
