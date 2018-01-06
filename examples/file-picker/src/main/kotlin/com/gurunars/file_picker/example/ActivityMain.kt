package com.gurunars.file_picker.example

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.gurunars.box.ui.setAsOne
import com.gurunars.file_picker.ExternalStorageBrowser
import com.gurunars.file_picker.filePicker

class ActivityMain : Activity() {

    private val filePicker = ExternalStorageBrowser()

    companion object {
        val permissionGranted: Int = 200
    }

    private fun Iterable<Int>.granted() = all { it == PackageManager.PERMISSION_GRANTED }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!filePicker.permissions.map { ContextCompat.checkSelfPermission(this, it) }.granted()) {
            ActivityCompat.requestPermissions(
                this,
                filePicker.permissions.toTypedArray(),
                permissionGranted
            )
        }

        filePicker(filePicker).setAsOne(this)
    }

    override fun onRequestPermissionsResult(
        requestCode:
        Int, permissions: Array<out String>?,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        filePicker.hasPermissions.set(grantResults.asIterable().granted())
    }

}