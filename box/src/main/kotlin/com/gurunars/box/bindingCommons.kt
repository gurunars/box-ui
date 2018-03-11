package com.gurunars.box

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

/**
 * Returns a box that has a one-way binding to this one.
 * I.e. if this one changes another box gets changes as well. However if another box
 * changes this one remains unchanged.
 *
 * @param reduce a function to transform the payload of this box into the payload of the other
 * box
 */
inline fun <From, To> IRoBox<From>.oneWayBranch(
    crossinline reduce: From.() -> To
): IRoBox<To> = Box(get().reduce()).apply {
    this@oneWayBranch.onChange { item -> set(item.reduce()) }
}

/**
 * Shortcut function that alters the value of the box.
 *
 * @param patcher a mutator meant to transform the original value into the patched value.
 */
inline fun <ItemType> IBox<ItemType>.patch(patcher: ItemType.() -> ItemType) =
    set(get().patcher())

/**
 * Creates a two-way binding between this and a target box. Whenever one changes another
 * gets updated automatically.
 *
 * @param target box to bind to
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <Type> IBox<Type>.bind(target: IBox<Type>): Disposable {
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
inline fun <Type> IRoBox<Type>.bind(target: IBox<Type>): Disposable {
    return onChange { item -> target.set(item) }
}

/**
 * Returns a box that has a two-way binding to this one.
 * I.e. if this one changes another box gets changed and vice versa.
 *
 * @param reduce a function to transform the payload of this box into the payload of the other
 * box
 * @param patchSource a function to transform the value of this box based on the value of the
 * other box
 */
inline fun <From, To> IBox<From>.branch(
    crossinline reduce: From.() -> To,
    crossinline patchSource: From.(part: To) -> From
): IBox<To> {
    val branched = Box(get().reduce())
    branched.onChange { item -> this@branch.patch { patchSource(item) } }
    onChange { parent -> branched.set(parent.reduce()) }
    return branched
}

/**
 * A special case of a two-way oneWayBranch function for the situation when
 * a both this and another box are of the same type.
 *
 * @param transform a function to be called whenever value of this or another box changes.
 */
inline fun <Type> IBox<Type>.fork(
    crossinline transform: Type.() -> Type
) = branch(transform, { it.transform() })

/** A short way to wrap a value into a Box without parentheses */
@Suppress("NOTHING_TO_INLINE")
inline val <F> F.box
    get(): IBox<F> = Box(this)

/** A short way to wrap a IBox into a BoxWithLifecycle without parentheses */
inline val <F> IBox<F>.disposable
    get() = BoxWithLifecycle(this)

/** A short way to wrap a IRoBox into a RoBoxWithLifecycle without parentheses */
inline val <F> IRoBox<F>.disposable
    get() = RoBoxWithLifecycle(this)

/** Obtain an observable from a box */
fun <T> IRoBox<T>.toObservable(): Observable<T> {
    // There is no need to spawn extra instances if we know that there is one already.
    return if (this is Box) {
        this.subject
    } else {
        BehaviorSubject.createDefault(get()).apply {
            onChange { onNext(it) }
        }
    }
}

/**
 * Combines changes from several boxes together
 * and invokes a consumer with the combined set
 * of box payloads.
 */
fun onChange(vararg params: IRoBox<*>, listener: () -> Unit) {
    params.forEach { it.onChange { _ -> listener() } }
}
