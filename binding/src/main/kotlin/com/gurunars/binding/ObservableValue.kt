package com.gurunars.binding

import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.Objects

class ObservableValueError(val exception: Throwable): Throwable()

interface Bond {
    fun dismiss()
}

private class ValueBond(private val disposable: Disposable): Bond {

    override fun dismiss() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

}

/**
 * Entity meant to hold the value and notify the observers about its
 * changes.
 */
interface IReadOnlyObservableValue<Type> {
    /** Fetches the current value. */
    fun get(): Type
    /**
     * Subscribes to changes of the value. Aware of the previous state.
     *
     * @param listener a function called with a new value after the change takes place
     */
    fun onChange(listener: (value: Type) -> Unit): Bond
}

/** Mutable IReadOnlyObservableValue. */
interface IObservableValue<Type> : IReadOnlyObservableValue<Type> {
    /**
     * Change fields content to a new value. The change is made only if current and new values
     * actually differ content-wise.
     *
     * @param value payload to set the value to
     * @param true if the value was different from the current one and thus had to be changed
     */
    fun set(value: Type): Boolean
}

/**
 * An observable box capable to emit changes and listen to change events
 *
 * @param Type type of the value the box is meant to hold
 * @param initial default value of the box
 */
class ObservableValue<Type>(private val initial: Type) : IObservableValue<Type> {
    private val subject = BehaviorSubject.createDefault<Type>(initial)

    override fun onChange(listener: (value: Type) -> Unit): Bond =
        subject.subscribe {
            try {
                listener(subject.value ?: initial)
            } catch (ex: Throwable) {
                throw ObservableValueError(ex)
            }
        }.let { ValueBond(it) }

    override fun set(value: Type): Boolean {
        // Safeguard against modifications that do not mutate the value
        val current = subject.value
        if (!Objects.deepEquals(current, value)) {
            subject.onNext(value)
            return true
        }
        return false
    }

    override fun get(): Type = this.subject.value ?: initial
}