package com.gurunars.shortcuts

import android.util.Log

fun log(vararg params: Any?) = Log.e("MSG", params.fold("", {
    acc, obj -> if (acc.isNotEmpty()) acc + ", " + obj else acc + obj
}))
