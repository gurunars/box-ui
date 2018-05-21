package com.gurunars.box.ui

import android.widget.CompoundButton

var CompoundButton.isChecked_ by SyncFieldField<CompoundButton, Boolean>(
    initializeEmitter = { setOnCheckedChangeListener { _, isChecked -> it.set(isChecked) } },
    onChange = { isChecked = it }
)