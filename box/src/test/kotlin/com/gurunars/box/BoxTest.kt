package com.gurunars.box

import org.junit.Assert.assertEquals
import org.junit.Test

class BoxTest {

    @Test
    fun onChange() {
        var value = 0
        val field = Box(1)

        field.onChange { it -> value = it }
        assertEquals(1, value)

        field.set(2)
        assertEquals(2, value)
    }

    @Test
    fun onBind() {
        // master - slave relationship is important
        // for the initial value sync only - master
        // is take as the source of truth
        val masterField = Box(1)
        val slaveField = Box(2)

        masterField.bind(slaveField)
        assertEquals(1, masterField.get())
        assertEquals(1, slaveField.get())

        masterField.set(3)
        assertEquals(3, masterField.get())
        assertEquals(3, slaveField.get())

        slaveField.set(4)
        assertEquals(4, masterField.get())
        assertEquals(4, slaveField.get())
    }

    @Test
    fun onBindWithTransformation() {
        val masterField = Box(1)
        val slaveField = Box("2")

        masterField.bind(
            slaveField.branch({ Integer.valueOf(this) }, { it.toString() })
        )
        assertEquals(1, masterField.get())
        assertEquals("1", slaveField.get())

        masterField.set(3)
        assertEquals(3, masterField.get())
        assertEquals("3", slaveField.get())

        slaveField.set("4")
        assertEquals(4, masterField.get())
        assertEquals("4", slaveField.get())
    }

    @Test
    fun multibind_shouldSyncChangesToAll() {
        val masterField = Box(1)
        val slaveField1 = Box(2)
        val slaveField2 = Box(3)

        masterField.bind(slaveField1)
        masterField.bind(slaveField2)

        assertEquals(1, masterField.get())
        assertEquals(1, slaveField1.get())
        assertEquals(1, slaveField2.get())

        slaveField2.set(2)

        assertEquals(2, masterField.get())
        assertEquals(2, slaveField1.get())
    }

    @Test
    fun nestedBind_shouldSyncChangesToAll() {
        val masterField = Box(1)
        val slaveField1 = Box(2)
        val slaveField2 = Box(3)

        masterField.bind(slaveField1)
        slaveField1.bind(slaveField2)

        assertEquals(1, masterField.get())
        assertEquals(1, slaveField1.get())
        assertEquals(1, slaveField2.get())

        slaveField2.set(2)

        assertEquals(2, masterField.get())
        assertEquals(2, slaveField1.get())
    }

    @Test
    fun droppingBond_shouldRemoveTheLinkBetweenBoxes() {
        val masterField = Box(1)
        val slaveField1 = Box(2)
        val slaveField2 = Box(3)

        val masterBond = masterField.bind(slaveField1)
        masterBond.drop()
        slaveField1.bind(slaveField2)

        assertEquals(1, masterField.get())
        assertEquals(1, slaveField1.get())
        assertEquals(1, slaveField2.get())

        slaveField2.set(2)

        assertEquals(1, masterField.get())
        assertEquals(2, slaveField1.get())
    }

    @Test
    fun coldObserver_shouldNotTriggerChangeOnBind() {
        val field = Box(1)
        var test = 123

        field.onChange(hot = false) {
            test = it
        }

        assertEquals(123, test)

        field.set(22)

        assertEquals(22, test)
    }
}