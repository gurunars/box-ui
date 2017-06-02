package com.gurunars.storage

import android.app.Activity
import android.content.Context
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.BindingRegistryService
import java.io.Serializable


class PersistentStorage(
        private val activity: Activity,
        private val storageName: String
) {

    internal class PersistentField<Type: Serializable>(
            val storage: PersistentStorage,
            val name: String,
            defaultValue: Type): BindableField<Type>(defaultValue) {
        fun load() {
            val loadedValue: Type? = StringSerializer.fromString<Type>(
                storage.activity.getPreferences(Context.MODE_PRIVATE)
                .getString(storage.storageName+"/"+name, null))
            if (loadedValue != null) set(loadedValue)
        }
        fun save() {
            if (!storage.wasLoaded) return
            val editor = storage.activity.getPreferences(Context.MODE_PRIVATE).edit()
            editor.putString(storage.storageName+"/"+name, StringSerializer.toString(get()))
            editor.apply()
        }
    }

    private val registry = BindingRegistryService()

    private var wasLoaded = false

    fun<Type: Serializable> storageField(name: String, defaultValue: Type): BindableField<Type> {
        val field = PersistentField(this, name, defaultValue)
        registry.add(field)
        return field
    }

    fun load() {
        registry.forEach {
            val field = it as PersistentField<*>
            field.load()
            field.bind { field.save() }
        }
        wasLoaded = true
    }

    fun unbindAll() = registry.unbindAll()

}
