package com.gurunars.crud_item_list

import org.junit.Assert.assertEquals
import org.junit.Test

class StateManagerTest {

    fun getManager() = StateMachine<StringItem>(
        {},
        mapOf(),
        {}
    )

    @Test
    @Throws(Exception::class)
    fun selection_leadsToTrue() {
    }

}