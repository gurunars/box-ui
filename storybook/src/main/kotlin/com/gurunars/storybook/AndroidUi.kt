package com.gurunars.storybook

import android.app.Activity
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle

fun DrawerLayout.getToggle(activity: Activity) = ActionBarDrawerToggle(
    activity,
    this,
    R.string.open_main_menu,
    R.string.close_main_menu
).apply {
    addDrawerListener(this)
}
