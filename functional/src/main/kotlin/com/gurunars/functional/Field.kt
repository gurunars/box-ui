package com.gurunars.functional

class Field<T>(
    val value: T,
    val set: (value: T) -> Unit
) {

}