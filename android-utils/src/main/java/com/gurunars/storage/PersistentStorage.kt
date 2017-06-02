package com.gurunars.storage

import android.app.Activity
import android.content.Context
import com.gurunars.databinding.BindableField
import java.io.Serializable


class PersistentStorage(
        private val activity: Activity,
        private val storageName: String
) {

    internal class PersistentField<Type: Serializable>(defaultValue: Type): BindableField<Type>(defaultValue) {
        fun setVal(value: Serializable) {
            set(value as Type)
        }
    }

    private val fields = mutableMapOf<String, PersistentField<*>>()
    private var wasLoaded = false

    private fun<ItemType: Serializable> load(name: String): ItemType? {
        val preferences = activity.getPreferences(Context.MODE_PRIVATE)
        return StringSerializer.fromString<ItemType>(
                preferences.getString(storageName+"/"+name, null))
    }

    private fun<ItemType: Serializable> save(name: String, value: ItemType) {
        if (!wasLoaded) return
        val editor = activity.getPreferences(Context.MODE_PRIVATE).edit()
        editor.putString(storageName+"/"+name, StringSerializer.toString(value))
        editor.apply()
    }

    fun<Type: Serializable> storageField(name: String, defaultValue: Type): BindableField<Type> {
        val field = PersistentField(defaultValue)
        fields.put(name, field)
        return field
    }

    fun load() {
        fields.keys.forEach {
            val name = it
            val field = fields.get(name)
            val loadedValue: Serializable? = load(name)
            if (loadedValue != null) {
                field?.setVal(loadedValue)
            }
            field?.bind { save(name, it) }
        }
        wasLoaded = true
    }

}
