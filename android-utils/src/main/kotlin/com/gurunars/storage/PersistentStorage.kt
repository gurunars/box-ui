package com.gurunars.storage

import android.content.Context
import android.content.SharedPreferences
import com.gurunars.databinding.Box
import com.gurunars.databinding.IBox
import com.gurunars.databinding.onChange
import java.util.Timer
import kotlin.concurrent.timerTask

/**
 * IBox storage based on SharedPreferences
 *
 * @property context Android context to be used for instantiation of SharedPreferences
 * @property storageName name of the data store in SharedPreferences
 */
class PersistentStorage(
    private val context: Context,
    private val storageName: String
) {

    private val preferences = CachedLazyField {
        context
            .getSharedPreferences(storageName, Context.MODE_PRIVATE)
    }
    private var timer = Timer()

    internal class PersistentField<Type>(
        val preferences: CachedLazyField<SharedPreferences>,
        val name: String,
        val field: IBox<Type>) {

        fun load() {
            val loadedValue: Type? = StringSerializer.fromString<Type>(
                preferences.get().getString(name, null)
            )
            if (loadedValue != null) field.set(loadedValue)
        }

        fun save(editor: SharedPreferences.Editor) {
            editor.putString(name, StringSerializer.toString(field.get()))
        }
    }

    private val fields = mutableListOf<PersistentField<*>>()

    /**
     * @param name used internally to persist the box in SharedPreferences
     * @param defaultValue value to be used if none was set yet
     * @param Type data type of the value to be stored
     */
    fun <Type> storageField(name: String, defaultValue: Type): IBox<Type> {
        val field = Box(defaultValue)
        field.onChange { _ ->
            timer.cancel()
            timer = Timer()
            timer.schedule(timerTask { save() }, 100)
        }
        val wrapper = PersistentField(preferences, name, field)
        fields.add(wrapper)
        return field
    }

    /**
     * Load all the fields from SharedPreferences.
     */
    fun load() {
        fields.forEach { it.load() }
    }

    private fun save() {
        val editor = preferences.get().edit()
        fields.forEach { it.save(editor) }
        editor.apply()
    }

    /**
     * Drop all the box listeners.
     */
    fun unbindAll() {
        timer.cancel()
        save()
    }
}
