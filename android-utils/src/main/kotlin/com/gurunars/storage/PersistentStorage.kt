package com.gurunars.storage

import android.app.Activity
import android.content.Context
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.Disposable
import com.gurunars.databinding.DisposableRegistryService

/**
 * Observable storage based on SharedPreferences
 *
 * @property activity Android activity to be used for instantiation of SharedPreferences
 * @property storageName name of the data store in SharedPreferences
 */
class PersistentStorage(
        private val activity: Activity,
        private val storageName: String
) {

    internal class PersistentField<Type>(
            val storage: PersistentStorage,
            val name: String,
            val field: BindableField<Type>): Disposable {
        override fun disposeAll() {
            field.disposeAll()
        }

        fun load() {
            val loadedValue: Type? = StringSerializer.fromString<Type>(
                storage.activity.getPreferences(Context.MODE_PRIVATE)
                .getString(storage.storageName+"/"+name, null))
            if (loadedValue != null) field.set(loadedValue)
        }
        fun save() {
            if (!storage.wasLoaded) return
            val editor = storage.activity.getPreferences(Context.MODE_PRIVATE).edit()
            editor.putString(storage.storageName+"/"+name, StringSerializer.toString(field.get()))
            editor.apply()
        }


    }

    private val registry = DisposableRegistryService()
    private val fields = mutableListOf<PersistentField<*>>()

    private var wasLoaded = false

    /**
     * @param name used internally to persist the field in SharedPreferences
     * @param defaultValue value to be used if none was set yet
     * @param Type data type of the value to be stored
     */
    fun<Type> storageField(name: String, defaultValue: Type): BindableField<Type> {
        val field = BindableField(defaultValue)
        val wrapper = PersistentField(this, name, field)
        registry.add(wrapper)
        fields.add(wrapper)
        return field
    }

    /**
     * Load all the fields from SharedPreferences.
     */
    fun load() {
        fields.forEach {
            val field = it
            field.load()
            field.field.onChange { field.save() }
        }
        wasLoaded = true
    }

    /**
     * Drop all the field listeners.
     */
    fun unbindAll() = registry.unbindAll()

}
