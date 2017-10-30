package com.gurunars.floatmenu

import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.Component

interface Openable: Component {
    val isOpen: BindableField<Boolean>
}