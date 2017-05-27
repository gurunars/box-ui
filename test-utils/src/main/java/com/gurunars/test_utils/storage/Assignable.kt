package com.gurunars.test_utils.storage

import java.io.Serializable

abstract class Assignable<ItemType>: Cloneable, Serializable {

    fun assign(block: ItemType.() -> Unit): ItemType {
        val copy = clone() as ItemType
        copy.apply(block)
        return copy
    }

}