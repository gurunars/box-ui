package com.gurunars.box

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
inline fun <Type> IBox<Type>.bind(target: IBox<Type>): Bond {
    val there = onChange { item -> target.set(item) }
    val back = target.onChange { item -> this.set(item) }
    return object : Bond {
        override fun drop() {
            there.drop()
            back.drop()
        }
    }
}

/**
 * A short way to wrap a value into a Box
 */
@Suppress("NOTHING_TO_INLINE")
inline val <F> F.box
    get(): IBox<F> = Box(this)
