package com.gurunars.box

import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.*

/**
 * An observable box capable to emit changes and listen to change events
 *
 * @param Type type of the value the box is meant to hold
 * @param initial default value of the box
 */
class Box<Type>(private val initial: Type) : IBox<Type> {

    override fun broadcast() {
        subject.onNext(subject.value ?: initial)
    }

    internal val subject = BehaviorSubject.createDefault<Type>(initial)

    override fun onChange(listener: (value: Type) -> Unit): Disposable {
        return subject.subscribe {
            listener(subject.value ?: initial)
        }
    }

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