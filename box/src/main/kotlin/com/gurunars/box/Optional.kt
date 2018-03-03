package com.gurunars.box

import java.util.Optional

fun <T> T?.toOptional() = Optional.ofNullable(this)

fun <T> Optional<T>.toNullable(): T? = if (isPresent) get() else null