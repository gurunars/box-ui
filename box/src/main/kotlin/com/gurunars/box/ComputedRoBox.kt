package com.gurunars.box

/**
 * Unidirectional observable mapping between source and target value.
 *
 * The mapping is not a bond. It is rather a decorator of the source box.
 */
class ComputedRoBox<Source, Target>(
    private val source: IRoBox<Source>,
    private val sourceToTarget: Source.() -> Target
): IRoBox<Target> {
    override fun get() = sourceToTarget(source.get())

    override fun onChange(hot: Boolean, listener: (value: Target) -> Unit): Bond {
        return source.onChange(hot) {
            listener(sourceToTarget(it))
        }
    }
}