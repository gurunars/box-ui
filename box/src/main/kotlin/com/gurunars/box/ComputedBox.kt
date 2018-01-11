package com.gurunars.box

/**
 * Bidirectional observable mapping between source and target value.
 *
 * The mapping is not a bond. It is rather a decorator of the source box.
 */
class ComputedBox<Source, Target>(
    private val source: IBox<Source>,
    private val sourceToTarget: Source.() -> Target,
    private val targetToSource: Source.(target: Target) -> Source
): IBox<Target> {

    override fun set(value: Target, force: Boolean): Boolean {
        val transformed = source.get().targetToSource(value)
        if (transformed != value) {
            return source.set(transformed, force)
        }
        return false
    }

    override fun get() = sourceToTarget(source.get())

    override fun onChange(hot: Boolean, listener: (value: Target) -> Unit): Bond {
        return source.onChange(hot) {
            listener(sourceToTarget(it))
        }
    }
}