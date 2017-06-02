package com.gurunars.databinding

interface ValueProcessor<Type> {
    fun forward(value: Type): Type
    fun backward(value: Type): Type
}