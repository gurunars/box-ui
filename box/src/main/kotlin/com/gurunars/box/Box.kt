package com.gurunars.box

import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.*

/**
 * An observable box capable to emit changes and listen to change events
 *
 * @param Type type of the value the box is meant to hold
 * @param value initial value of the box
 */
class Box<Type>(initial: Type) : IBox<Type> {
    internal val subject = BehaviorSubject.createDefault<Type>(initial)

    override fun onChange(listener: (value: Type) -> Unit): Disposable {
        return subject.subscribe {
            listener(subject.value)
        }
    }

    override fun set(value: Type, force: Boolean): Boolean {
        // Safeguard against modifications that do not mutate the value
        val current = subject.value
        if (force || !Objects.deepEquals(current, value)) {
            subject.onNext(value)
            return true
        }
        return false
    }

    override fun get(): Type = this.subject.value
}