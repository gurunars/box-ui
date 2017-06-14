package com.gurunars.databinding

fun <Type> Collection<Type>.contains(item: Type, equal: (one: Type, two: Type) -> Boolean) =
    find { equal(it, item) } != null
