package com.gurunars.android_utils

import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

inline fun ViewManager.iconView(init: IconView.() -> Unit) = ankoView({ IconView(it) }, 0, init)

inline fun ViewManager.notificationView(init: NotificationView.() -> Unit) = ankoView({ NotificationView(it) }, 0, init)
