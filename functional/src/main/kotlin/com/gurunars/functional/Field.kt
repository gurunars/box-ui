package com.gurunars.functional

class Field<T>(
    val get: () -> T,
    val set: (value: T) -> Unit
)