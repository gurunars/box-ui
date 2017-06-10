package com.gurunars.storage

import android.app.Activity
import android.content.Context
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.Disposable
import com.gurunars.databinding.DisposableRegistryService
import java.io.Serializable


class PersistentStorage(
        private val activity: Activity,
        private val storageName: String
) {

    internal class PersistentField<Type: Serializable>(
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

    fun<Type: Serializable> storageField(name: String, defaultValue: Type): BindableField<Type> {
        val field = BindableField(defaultValue)
        val wrapper = PersistentField(this, name, field)
        registry.add(wrapper)
        fields.add(wrapper)
        return field
    }

    fun load() {
        fields.forEach {
            val field = it
            field.load()
            field.field.onChange { field.save() }
        }
        wasLoaded = true
    }

    fun unbindAll() = registry.unbindAll()

}
