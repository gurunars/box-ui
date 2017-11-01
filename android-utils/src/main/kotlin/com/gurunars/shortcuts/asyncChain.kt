package com.gurunars.shortcuts

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

fun<T> Any.asyncChain(
    supplier: () -> T,
    consumer: (item: T) -> Unit,
    exceptionHandler: (exe: Exception) -> Unit = { throw it }
) = doAsync {
    // TODO: Add some sort of progress bar to prevent some accidental UI actions
    try {

        val candidate = supplier()
        uiThread {
            consumer(candidate)
        }
    } catch (exe: Exception) {
        uiThread {
            exceptionHandler(exe)
        }
    }
}
