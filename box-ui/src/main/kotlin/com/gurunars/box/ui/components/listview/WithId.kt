package com.gurunars.box.ui.components.listview

interface WithId {
    val id: Any
}

interface WithType {
    val type: Any
}

data class Wrapped<T : Any>(override val id: Any, val payload: T): WithId
