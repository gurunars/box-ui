package com.gurunars.box.core

import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*

data class Wrapper<T>(val payload: T)

/**
 * An observable box capable to emit changes and listen to change events
 *
 * @param Type type of the value the box is meant to hold
 * @param initial default value of the box
 */
class ObservableValue<Type>(private val initial: Type) : IObservableValue<Type> {

    internal val subject = BehaviorSubject.createDefault<Wrapper<Type>>(Wrapper(initial))

    override fun onChange(listener: (value: Type) -> Unit): Disposable {
        return subject.subscribe {
            listener(subject.value?.payload ?: initial)
        }
    }

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