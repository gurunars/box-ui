package com.gurunars.box.ui

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry

open class BoxActivity: Activity(), LifecycleOwner {
    private val observer = LifecycleRegistry(this)
    override fun getLifecycle() = observer
}