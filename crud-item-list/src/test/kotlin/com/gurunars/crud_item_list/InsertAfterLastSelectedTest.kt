package com.gurunars.crud_item_list

import org.junit.Test

import org.junit.Assert.*

class InsertAfterLastSelectedTest {

    @Test
    fun insertAfterLast_whenNoneAreSelected() {
        assertEquals(
            listOf(0,1,2,3,4,5,6,7),
            insertAfterLastSelected(
                listOf(0,1,2,3,4,5),
                setOf(),
                listOf(6,7)
            )
        )
    }

    @Test
    fun insertAfterLastSelected() {
        assertEquals(
            listOf(0,1,2,3,6,7,4,5),
            insertAfterLastSelected(
                listOf(0,1,2,3,4,5),
                setOf(2,3),
                listOf(6,7)
            )
        )
    }

    @Test
    fun insertAfterAllSelected() {
        assertEquals(
            listOf(0,1,2,3,4,5,6,7),
            insertAfterLastSelected(
                listOf(0,1,2,3,4,5),
                setOf(0,1,2,3,4,5),
                listOf(6,7)
            )
        )
    }

}