package com.gurunars.item_list

internal interface BiFunction<T, U, R> {
    fun apply(one: T, two: U): R
}