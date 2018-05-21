package com.gurunars.box.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.Menu
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.gurunars.box.core.IObservableValue
import com.gurunars.box.ui.R
import com.gurunars.box.ui.children
import com.gurunars.box.ui.themeColor

interface MenuItem {
    val id: Int
    val title: Int
    val icon: Int
}

@SuppressLint("RestrictedApi")
fun <T: MenuItem> Context.bottomNavigationView(
    selectedItem: IObservableValue<T>,
    items: List<T>
) = BottomNavigationView(this).apply {
    labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED

    setBackgroundColor(themeColor(R.attr.controlBackground)!!)

    val mapping = items.associateBy { it.id }

    items.forEach { item ->
        menu.add(Menu.NONE, item.id, Menu.NONE, item.title).apply {
            setIcon(item.icon)
        }
    }

    selectedItem.onChange { selection ->
        selectedItemId = selection.id
        menu.findItem(selection.id).isChecked = true
    }

    setOnNavigationItemSelectedListener {
        selectedItem.set(mapping.getValue(it.itemId))
    }

    val colors = ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_selected),
            intArrayOf(-android.R.attr.state_selected)
        ),
        intArrayOf(themeColor(R.attr.colorControlActivated)!!, themeColor(R.attr.colorControlNormal)!!)
    )

    val menuView = getChildAt(0) as BottomNavigationMenuView
    menuView.children.map {
        it as BottomNavigationItemView
        it.setIconTintList(colors)
        it.setTextColor(colors)
    }

}
