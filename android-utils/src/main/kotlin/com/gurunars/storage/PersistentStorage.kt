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
            val preferences = storage.activity.getPreferences(Context.MODE_PRIVATE)
            val key = storage.storageName+"/"+name

            val loadedValue: Type? = StringSerializer.fromString<Type>(preferences.getString(key, null))
            if (loadedValue != null) field.set(loadedValue)

            field.onChange {
                val editor = preferences.edit()
                editor.putString(key, StringSerializer.toString(it))
                editor.apply()
            }

        }

    }

    private val registry = DisposableRegistryService()
    private val fields = mutableListOf<PersistentField<*>>()

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
    fun load() { fields.forEach { it.load() } }

    /**
     * Drop all the field listeners.
     */
    fun unbindAll() = registry.unbindAll()

}
