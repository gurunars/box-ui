package com.gurunars.shortcuts

import android.util.Log

/**
 * Logs all passed vararg parameters joined into a string with a comma (,) used as a delimiter.
 *
 * @param tag tag to be used in for the log message
 */
fun log(vararg params: Any?, tag: String="MSG") = Log.e(tag, params.fold("", {
    acc, obj -> if (acc.isNotEmpty()) acc + ", " + obj else acc + obj
}))
