package com.gurunars.crud_item_list

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

private fun<T> Any.asyncChain(
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
