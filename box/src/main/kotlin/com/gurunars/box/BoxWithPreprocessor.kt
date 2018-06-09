package com.gurunars.box


private class BoxWithPreprocessor<Type>(
    private val box: IBox<Type>,
    private val preprocessor: (item: Type) -> Type
) : IBox<Type> by box {
    override fun set(value: Type): Boolean =
        box.set(preprocessor(value))
}

/**
 * A box decorator that run an defined input preprocessor that aims
 * to change the data before it ends up as the box's payload.
 *
 * @param Type type of the value the box is meant to hold
 * @param preprocessor
 */
fun <Type> Box<Type>.withPreprocessor(
    preprocessor: (item: Type) -> Type
): IBox<Type> = BoxWithPreprocessor(this, preprocessor)