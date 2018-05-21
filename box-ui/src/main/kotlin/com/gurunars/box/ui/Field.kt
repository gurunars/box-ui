package com.gurunars.box.ui

import com.gurunars.box.core.IObservableValue
import com.gurunars.box.core.IReadOnlyObservableValue
import kotlin.reflect.KProperty

class SetOnly(val name: String): Throwable()

class ConsumerField<S, T>(val onChange: S.(it: T) -> Unit) {
    operator fun getValue(thisRef: S, property: KProperty<*>): IReadOnlyObservableValue<T> =
            throw SetOnly(property.name)
    operator fun setValue(thisRef: S, property: KProperty<*>, value: IReadOnlyObservableValue<T>) =
            value.onChange { thisRef.onChange(it) }
}

class PlainConsumerField<S, T>(val onChange: S.(it: T) -> Unit) {
    operator fun getValue(thisRef: S, property: KProperty<*>): T =
            throw SetOnly(property.name)
    operator fun setValue(thisRef: S, property: KProperty<*>, value: T) =
            thisRef.onChange(value)
}

class SyncFieldField<S, T>(
    val initializeEmitter: S.(it: IObservableValue<T>) -> Unit,
    val onChange: S.(it: T) -> Unit
) {
    operator fun getValue(thisRef: S, property: KProperty<*>): IReadOnlyObservableValue<T> =
            throw SetOnly(property.name)
    operator fun setValue(thisRef: S, property: KProperty<*>, value: IReadOnlyObservableValue<T>) {
        value.onChange { thisRef.onChange(it) }
        if (value is IObservableValue<T>) {
            thisRef.initializeEmitter(value)
        }
    }
}
