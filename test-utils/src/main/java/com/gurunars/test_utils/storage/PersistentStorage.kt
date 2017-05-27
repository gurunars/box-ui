package com.gurunars.test_utils.storage

import android.app.Activity
import android.content.Context


class PersistentStorage<ItemType : Assignable<ItemType>>(
        private val activity: Activity,
        private val typeAlias: String,
        private val defaultPayload: ItemType,
        private val changeListener: (payload: ItemType) -> Unit) {

    private var payload: ItemType

    init {
        this.payload = defaultPayload
        load()
    }

    fun patch(block: ItemType.() -> Unit) {
        set(this.payload.assign(block))
    }

    private fun load() {
        val preferences = activity.getPreferences(Context.MODE_PRIVATE)
        val loaded = StringSerializer.fromString<ItemType>(preferences.getString(typeAlias, null))

        payload = loaded ?: payload

        changeListener(payload)
    }

    /**
     * @param payload payload to persist
     */
    fun set(payload: ItemType) {
        this.payload = payload
        save()
    }

    private fun save() {
        val editor = activity.getPreferences(Context.MODE_PRIVATE).edit()
        editor.putString(typeAlias, StringSerializer.toString(payload))
        editor.apply()

        this.changeListener(payload)
    }

    /**
     * Set persisted payload to the default one.
     */
    fun clear() {
        payload = defaultPayload
        save()
    }
}
