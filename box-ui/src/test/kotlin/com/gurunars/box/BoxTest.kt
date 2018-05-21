package com.gurunars.box

import com.gurunars.box.core.ObservableValue
import com.gurunars.box.core.bind
import org.junit.Assert.assertEquals
import org.junit.Test

class ObservableValueTest {

    @Test
    fun onChange() {
        var value = 0
        val field = ObservableValue(1)

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
        val masterField = ObservableValue(1)
        val slaveField = ObservableValue(2)

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
        val masterField = ObservableValue(1)
        val slaveField = ObservableValue("2")

        masterField.bind(
            slaveField.bind({ Integer.valueOf(this) }, { it.toString() })
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
        val masterField = ObservableValue(1)
        val slaveField1 = ObservableValue(2)
        val slaveField2 = ObservableValue(3)

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
        val masterField = ObservableValue(1)
        val slaveField1 = ObservableValue(2)
        val slaveField2 = ObservableValue(3)

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
    fun droppingDisposable_shouldRemoveTheLinkBetweenObservableValuees() {
        val masterField = ObservableValue(1)
        val slaveField1 = ObservableValue(2)
        val slaveField2 = ObservableValue(3)

        val masterDisposable = masterField.bind(slaveField1)
        masterDisposable.dispose()
        slaveField1.bind(slaveField2)

        assertEquals(1, masterField.get())
        assertEquals(1, slaveField1.get())
        assertEquals(1, slaveField2.get())

        slaveField2.set(2)

        assertEquals(1, masterField.get())
        assertEquals(2, slaveField1.get())
    }


}