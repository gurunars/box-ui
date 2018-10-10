package com.gurunars.functional

class Box<T>(
    val get: () -> T,
    val set: (value: T) -> Unit
)