package com.gurunars.box

/***/
sealed class Optional<out T> {
    /**
     * If there is a value
     *
     * @property element item that is stored within a placeholder
     */
    class Some<out T>(val element: T): Optional<T>()
    /** If there is no value */
    object None: Optional<Nothing>()
}

/** Transforms a nullable value into an optional one */
fun <T> T?.toOptional() = if (this == null) Optional.None else Optional.Some(this)

/** Transforms an optional value into a nullable one */
fun <T> Optional<T>.toNullable(): T? = if (this is Optional.Some<T>) this.element else null