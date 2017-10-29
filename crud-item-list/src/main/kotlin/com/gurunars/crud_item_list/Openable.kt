package com.gurunars.crud_item_list

import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField

interface Openable {
    val isOpen: BindableField<Boolean>
    val openIcon: BindableField<IconView.Icon>
}