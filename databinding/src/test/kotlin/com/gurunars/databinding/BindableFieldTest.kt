package com.gurunars.databinding

import org.junit.Test
import org.junit.Assert.assertEquals

class BindableFieldTest {

    @Test
    @Throws(Exception::class)
    fun onChange() {
        var value = 0
        val field = BindableField(1)

        field.onChange { value = it }
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
            slaveField,
            object: BindableField.ValueTransformer<Int, String> {
                override fun forward(value: Int) = value.toString()
                override fun backward(value: String) = Integer.valueOf(value)
            }
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
    fun unbindAll_shouldPreventChangePropagation() {
        val masterField = BindableField(1)
        val slaveField1 = BindableField(1)
        val slaveField2 = BindableField(1)

        masterField.bind(slaveField1)
        masterField.bind(slaveField2)

        masterField.unbindAll()

        masterField.set(5)
        slaveField1.set(2)
        slaveField2.set(3)

        assertEquals(5, masterField.get())
        assertEquals(2, slaveField1.get())
        assertEquals(3, slaveField2.get())
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

    @Test
    @Throws(Exception::class)
    fun nestedBindUnbind_shouldNotAffectNeighbourRelations() {
        val masterField = BindableField(1)
        val slaveField1 = BindableField(2)
        val slaveField2 = BindableField(3)

        masterField.bind(slaveField1)
        slaveField1.bind(slaveField2)

        masterField.unbindAll()

        slaveField2.set(2)

        assertEquals(1, masterField.get())
        assertEquals(2, slaveField1.get())
    }

    @Test
    @Throws(Exception::class)
    fun pauseResume_shouldWorkAccordingly() {
        val parent = BindableField(1)
        val child = BindableField(2)
        val grandChild = BindableField(3)

        parent.bind(child)
        child.bind(grandChild)

        fun validate(one: Int, two: Int, three: Int) {
            assertEquals(one, parent.get())
            assertEquals(two, child.get())
            assertEquals(three, grandChild.get())
        }

        validate(1, 1, 1)

        parent.pause()
        child.set(2)

        validate(1, 2, 2)

        parent.resume()

        validate(1, 2, 2)

        child.set(3)

        validate(3, 3, 3)

    }

}