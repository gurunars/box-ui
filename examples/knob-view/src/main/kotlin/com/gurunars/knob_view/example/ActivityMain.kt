package com.gurunars.knob_view.example

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import com.gurunars.databinding.BindableField
import com.gurunars.knob_view.KnobView
import com.gurunars.shortcuts.setAsOne
import org.jetbrains.anko.*

enum class COUNT {
    ONE, TWO, THREE, FOUR, FIVE
}


internal class InnerView(ctx: Context) : FrameLayout(ctx) {

    private val txt = BindableField("")

    init {
        verticalLayout {
            textView {
                padding = dip(10)
                txt.bind(this)
            }
            editText {
                padding = dip(10)
                txt.bind(this)
            }
            gravity = Gravity.CENTER
        }
    }

}


class ActivityMain : Activity() {

    private lateinit var knobView: KnobView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        knobView = KnobView(this, mapOf<Enum<*>, View>(
            COUNT.ONE to InnerView(this).apply { id = R.id.one },
            COUNT.TWO to InnerView(this).apply { id = R.id.two },
            COUNT.THREE to InnerView(this).apply { id = R.id.three },
            COUNT.FOUR to InnerView(this).apply { id = R.id.four },
            COUNT.FIVE to InnerView(this).apply { id = R.id.five }
        )).setAsOne(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.one -> {
                knobView.selectedView.set(COUNT.ONE)
                return true
            }
            R.id.two -> {
                knobView.selectedView.set(COUNT.TWO)
                return true
            }
            R.id.three -> {
                knobView.selectedView.set(COUNT.THREE)
                return true
            }
            R.id.four -> {
                knobView.selectedView.set(COUNT.FOUR)
                return true
            }
            R.id.five -> {
                knobView.selectedView.set(COUNT.FIVE)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}