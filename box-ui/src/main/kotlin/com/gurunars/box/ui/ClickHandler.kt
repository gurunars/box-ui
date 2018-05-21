package com.gurunars.box.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit

enum class ClickEvent {
    SINGLE, LONG, DOUBLE
}

data class Tap(val duration: Long)

sealed class FingerMoveDirection {
    val timestamp = System.currentTimeMillis()

    class Up: FingerMoveDirection()
    class Down: FingerMoveDirection()
}

private const val DOUBLE_TAP_LIMIT = 500L

private const val LONG_PRESS_LIMIT = 500L

@SuppressLint("ClickableViewAccessibility")
private fun View.fingerMoves(): Observable<FingerMoveDirection> =
    BehaviorSubject.create<FingerMoveDirection>().also {
        setOnTouchListener { _, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> it.onNext(FingerMoveDirection.Down())
                MotionEvent.ACTION_UP -> it.onNext(FingerMoveDirection.Up())
                MotionEvent.ACTION_CANCEL -> it.onNext(FingerMoveDirection.Up())
                else -> {}
            }
            true
        }
    }

private fun Observable<FingerMoveDirection>.asTaps(): Observable<Tap> =
    scan(Pair<FingerMoveDirection, FingerMoveDirection>(
        FingerMoveDirection.Up(),
        FingerMoveDirection.Up()
    )) { acc: Pair<FingerMoveDirection, FingerMoveDirection>, item -> when {
        acc.second::class == item::class -> acc.copy(second=item)
        else -> Pair(acc.second, item)
    } }.filter {
        (it.first is FingerMoveDirection.Down) and (it.second is FingerMoveDirection.Up)
    }.map {
        Tap(it.second.timestamp - it.first.timestamp)
    }


private fun Observable<Tap>.onClick(): Observable<ClickEvent> =
    buffer(DOUBLE_TAP_LIMIT, TimeUnit.MILLISECONDS, 2).filter { it.isNotEmpty() }.map<Optional<ClickEvent>> {
        when (it.size) {
            2 -> ClickEvent.DOUBLE
            1 -> when {
                it.first().duration > LONG_PRESS_LIMIT -> ClickEvent.LONG
                else -> ClickEvent.SINGLE
            }
            else -> null
        }.let { value -> Optional.ofNullable(value) }
    }.filter { it.isPresent }.map { it.get() }

fun View.onClick(clickEvent: ClickEvent, handler: () -> Unit) {
    fingerMoves()
        .asTaps()
        .onClick()
        .filter { it == clickEvent }
        .observeOn(AndroidSchedulers.mainThread()).subscribe({
            handler()
        }, {
            Log.e("ClickHandler", "did not work", it)
        })
}