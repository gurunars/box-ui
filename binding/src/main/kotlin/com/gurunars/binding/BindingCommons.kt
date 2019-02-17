package com.gurunars.binding

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
 * Creates a two-way binding between this and a target box. Whenever one changes another
 * gets updated automatically.
 *
 * @param target box to bind to
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <Type> IObservableValue<Type>.bind(target: IObservableValue<Type>): Bond {
    val there = onChange { item -> target.set(item) }
    val back = target.onChange { item -> this.set(item) }
    return object: Bond {
        override fun dismiss() {
            there.dismiss()
            back.dismiss()
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
inline fun <Type> IReadOnlyObservableValue<Type>.bind(target: IObservableValue<Type>): Bond =
    onChange { item -> target.set(item) }

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

/**
 * Returns a special binding that propagates updates only for non null (Optional.Some) values.
 *
 * @param initial the value that is set before any of the events are propagated
 *
 * @see bind
 */
fun <Type> IObservableValue<Optional<Type>>.bind(initial: Type): IObservableValue<Type> {
    val branched = ObservableValue(initial)
    branched.onChange { set(it.toOptional()) }
    onChange { if (it is Optional.Some<Type>) branched.set(it.element) }
    return branched
}

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
