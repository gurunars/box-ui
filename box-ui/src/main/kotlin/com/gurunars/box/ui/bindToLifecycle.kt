package com.gurunars.box.ui

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.gurunars.box.WithLifecycle

/**
 * Bind lifecycle aware box to a specific activity
 *
 * @param activity to bind to
 */
fun WithLifecycle.bindToLifecycle(activity: Activity) {
    activity.application.apply {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(act: Activity?) {}
            override fun onActivityResumed(act: Activity?) {}
            override fun onActivitySaveInstanceState(act: Activity, bundle: Bundle?) {}
            override fun onActivityCreated(act: Activity, bundle: Bundle?) {}
            override fun onActivityStarted(act: Activity) {
                if (act == activity) start()
            }
            override fun onActivityStopped(act: Activity) {
                if (act == activity) stop()
            }
            override fun onActivityDestroyed(act: Activity) {
                if (act == activity) dispose()
            }
        })
    }
}