package com.gurunars.file_picker.example

import android.app.Activity
import android.os.Bundle
import com.gurunars.box.ui.setAsOne
import com.gurunars.file_picker.filePicker

class ActivityMain : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        filePicker().setAsOne(this)
    }
}