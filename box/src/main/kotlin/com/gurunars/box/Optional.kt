package com.gurunars.box

import java.util.Optional

/* Transforms a nullable value into an optional one */
fun <T> T?.toOptional() = Optional.ofNullable(this)

/* Transforms an optional value into a nullable one */
fun <T> Optional<T>.toNullable(): T? = if (isPresent) get() else null