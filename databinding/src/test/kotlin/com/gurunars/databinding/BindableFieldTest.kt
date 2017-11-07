package com.gurunars.databinding

import org.junit.Assert.assertEquals
import org.junit.Test

class BindableFieldTest {

    @Test
    @Throws(Exception::class)
    fun onChange() {
        var value = 0
        val field = BindableField(1)

        field.onChange { it -> value = it }
        assertEquals(1, value)

        field.set(2)
        assertEquals(2, value)
    }

    @Test
    @Throws(Exception::class)
    fun onBind() {
        // master - slave relationship is important
        // for the initial value sync only - master
        // is take as the source of truth
        val masterField = BindableField(1)
        val slaveField = BindableField(2)

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
    @Throws(Exception::class)
    fun onBindWithTransformation() {
        val masterField = BindableField(1)
        val slaveField = BindableField("2")

        masterField.bind(
            slaveField.branch( { Integer.valueOf(this) }, { it.toString() })
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
    @Throws(Exception::class)
    fun multibind_shouldSyncChangesToAll() {
        val masterField = BindableField(1)
        val slaveField1 = BindableField(2)
        val slaveField2 = BindableField(3)

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
    @Throws(Exception::class)
    fun nestedBind_shouldSyncChangesToAll() {
        val masterField = BindableField(1)
        val slaveField1 = BindableField(2)
        val slaveField2 = BindableField(3)

        masterField.bind(slaveField1)
        slaveField1.bind(slaveField2)

        assertEquals(1, masterField.get())
        assertEquals(1, slaveField1.get())
        assertEquals(1, slaveField2.get())

        slaveField2.set(2)

        assertEquals(2, masterField.get())
        assertEquals(2, slaveField1.get())
    }

}