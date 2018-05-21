package com.gurunars.box.core

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject


/**
 * Returns a box that has a one-way binding to this one.
 * I.e. if this one changes another box gets changes as well. However if another box
 * changes this one remains unchanged.
 *
 * @param reduce a function to transform the payload of this box into the payload of the other
 * box
 */
inline fun <From, To> IReadOnlyObservableValue<From>.bind(
    crossinline reduce: From.() -> To
): IReadOnlyObservableValue<To> = ObservableValue(get().reduce()).apply {
    this@bind.onChange { item -> set(item.reduce()) }
}


/**
 * Shortcut function that alters the value of the box.
 *
 * @param patcher a mutator meant to transform the original value into the patched value.
 */
inline fun <ItemType> IObservableValue<ItemType>.patch(patcher: ItemType.() -> ItemType) =
    set(get().patcher())


/**
 * Returns a box that has a two-way binding to this one.
 * I.e. if this one changes another box gets changed and vice versa.
 *
 * @param reduce a function to transform the payload of this box into the payload of the other
 * box
 * @param patchSource a function to transform the value of this box based on the value of the
 * other box
 */
inline fun <From, To> IObservableValue<From>.bind(
    crossinline reduce: From.() -> To,
    crossinline patchSource: From.(part: To) -> From
): IObservableValue<To> {
    val branched = ObservableValue(get().reduce())
    branched.onChange { item -> this@bind.patch { patchSource(item) } }
    onChange { parent -> branched.set(parent.reduce()) }
    return branched
}


/** Obtain an observable from a box */
fun <T> IReadOnlyObservableValue<T>.toObservable(): Observable<Wrapper<T>> =
    // There is no need to spawn extra instances if we know that there is one already.
    if (this is ObservableValue) {
        this.subject
    } else {
        BehaviorSubject.createDefault(Wrapper(get())).apply {
            onChange { onNext(Wrapper(it)) }
        }
    }

fun <T> Observable<T>.toIRoBox(initial: T): IReadOnlyObservableValue<T> = ObservableValue(initial).apply { subscribe { set(it) } }

/** Merges two boxes into a box of a pair */
fun<One, Two> merge(one: IReadOnlyObservableValue<One>, two: IReadOnlyObservableValue<Two>): IReadOnlyObservableValue<Pair<One, Two>> {
    val box = ObservableValue(Pair(one.get(), two.get()))
    one.onChange { box.patch { copy(first=it) } }
    two.onChange { box.patch { copy(second=it) } }
    return box
}

/** Merges three boxes into a box of a triple */
fun<One, Two, Three> merge(one: IReadOnlyObservableValue<One>, two: IReadOnlyObservableValue<Two>, three: IReadOnlyObservableValue<Three>): IReadOnlyObservableValue<Triple<One, Two, Three>> {
    val box = ObservableValue(Triple(one.get(), two.get(), three.get()))
    one.onChange { box.patch { copy(first=it) } }
    two.onChange { box.patch { copy(second=it) } }
    three.onChange { box.patch { copy(third=it) } }
    return box
}

/**
 * Creates a two-way binding between this and a target box. Whenever one changes another
 * gets updated automatically.
 *
 * @param target box to bind to
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <Type> IObservableValue<Type>.bind(target: IObservableValue<Type>): Disposable {
    val there = onChange { item -> target.set(item) }
    val back = target.onChange { item -> this.set(item) }
    return object : Disposable {

        override fun isDisposed() =
            there.isDisposed && back.isDisposed

        override fun dispose() {
            there.dispose()
            back.dispose()
        }
    }
}

/**
 * Creates a one-way binding between this and a target box. Whenever this one changes another
 * one gets updated.
 *
 * @param target box to bind to
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <Type> IReadOnlyObservableValue<Type>.bind(target: IObservableValue<Type>): Disposable =
    onChange { item -> target.set(item) }

private class AtomicCleanup<Type>(
    private val observableValue: IObservableValue<Type>,
    private val cleanup: (input: Type) -> Type
): IObservableValue<Type> by observableValue {
    override fun set(value: Type): Boolean = observableValue.set(cleanup(value))
}

fun <Type> IObservableValue<Type>.withCleanup(cleanup: (input: Type) -> Type): IObservableValue<Type>
    = AtomicCleanup(this, cleanup)