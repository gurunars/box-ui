package com.gurunars.databinding.android

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import android.view.ViewGroup
import android.widget.FrameLayout
import com.gurunars.databinding.BindableField
import java.io.Serializable

class UnsupportedStateType: Exception()

class WithState internal constructor(context: Context, vararg fields: BindableField<*>): FrameLayout(context) {

    private val fields: List<BindableField<*>> = fields.asList()

    /**
     * @suppress
     */
    override fun onSaveInstanceState() = Bundle().apply {
        putParcelable("superState", super.onSaveInstanceState())
        fields.forEachIndexed { index, bindableField -> with(bindableField.get()) {
            val key = index.toString()
            when(this) {
                is Int -> putInt(key, this)
                is Float -> putFloat(key, this)
                is Boolean -> putBoolean(key, this)
                is String -> putString(key, this)
                is Long -> putLong(key, this)
                is IBinder -> putBinder(key, this)
                is Bundle -> putBundle(key, this)
                is Byte -> putByte(key, this)
                is Char -> putChar(key, this)
                is CharSequence -> putCharSequence(key, this)
                is Parcelable -> putParcelable(key, this)
                is Short -> putShort(key, this)
                is Size -> putSize(key, this)
                is SizeF -> putSizeF(key, this)
                is Serializable -> putSerializable(key, this)
                else -> throw UnsupportedStateType()
            }
        } }
    }

    /**
     * @suppress
     */
    override fun onRestoreInstanceState(state: Parcelable) {
        (state as Bundle).apply {
            super.onRestoreInstanceState(getParcelable<Parcelable>("superState"))
            fields.forEachIndexed { index, bindableField -> with(bindableField.get()) {
                val key = index.toString()
                when(this) {
                    is Int -> {
                        bindableField as BindableField<Int>
                        bindableField.set(getInt(key))
                    }
                    is Float -> {
                        bindableField as BindableField<Float>
                        bindableField.set(getFloat(key))
                    }
                    is Boolean -> {
                        bindableField as BindableField<Boolean>
                        bindableField.set(getBoolean(key))
                    }
                    is String -> {
                        bindableField as BindableField<String>
                        bindableField.set(getString(key))
                    }
                    is Long -> {
                        bindableField as BindableField<Long>
                        bindableField.set(getLong(key))
                    }
                    is IBinder -> {
                        bindableField as BindableField<IBinder>
                        bindableField.set(getBinder(key))
                    }
                    is Bundle -> {
                        bindableField as BindableField<Bundle>
                        bindableField.set(getBundle(key))
                    }
                    is Byte -> {
                        bindableField as BindableField<Byte>
                        bindableField.set(getByte(key))
                    }
                    is Char -> {
                        bindableField as BindableField<Char>
                        bindableField.set(getChar(key))
                    }
                    is CharSequence -> {
                        bindableField as BindableField<CharSequence>
                        bindableField.set(getCharSequence(key))
                    }
                    is Parcelable -> {
                        bindableField as BindableField<Parcelable>
                        bindableField.set(getParcelable(key))
                    }
                    is Short -> {
                        bindableField as BindableField<Short>
                        bindableField.set(getShort(key))
                    }
                    is Size -> {
                        bindableField as BindableField<Size>
                        bindableField.set(getSize(key))
                    }
                    is SizeF -> {
                        bindableField as BindableField<SizeF>
                        bindableField.set(getSizeF(key))
                    }
                    is Serializable -> {
                        bindableField as BindableField<Serializable>
                        bindableField.set(getSerializable(key))
                    }
                    else -> throw UnsupportedStateType()
                }
            } }
        }
    }

}

fun ViewGroup.withState(id: Int, vararg fields: BindableField<*>, init: WithState.() -> Unit ) =
    WithState(context, *fields).apply {
        init()
        this.id = id
        this@withState.addView(this)
    }

fun Activity.withState(id: Int, vararg fields: BindableField<*>, init: WithState.() -> Unit ) =
    WithState(this, *fields).apply {
        init()
        this.id = id
        setContentView(this)
    }
