package com.gurunars.floatmenu

import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.Component

interface MenuComponent: Component {
    val isOpen: BindableField<Boolean>
    val openIcon: BindableField<IconView.Icon>
    val closeIcon: BindableField<IconView.Icon>
    val isLeftHanded: BindableField<Boolean>
}