package com.gurunars.storybook

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.gurunars.box.IBox
import com.gurunars.box.box
import com.gurunars.box.ui.*
import com.gurunars.box.onChange
import com.gurunars.box.oneWayBranch
import com.gurunars.item_list.Item
import com.gurunars.item_list.itemListView
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.drawerLayout

typealias RenderDemo = Context.() -> View

private data class StringHolder(
    override val id: Long,
    val payload: String
) : Item

private fun Context.bindString(
    field: IBox<StringHolder>,
    activeSection: IBox<String>) = TextView(this).apply {

    listOf(activeSection, field).onChange {
        backgroundColor = if (field.get().payload == activeSection.get()) {
            Color.RED
        } else {
            Color.WHITE
        }
    }

    isClickable = true
    padding = dip(6)
    onClick {
        activeSection.set(field.get().payload)
    }
    txt(field.oneWayBranch { payload })
    textSize = 20f
    asRow()
}

abstract class AbstractActivityStorybook : Activity() {

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var root: FrameLayout

    private val activeSection = "".box
    private val searchPattern = "".box

    abstract val views: Map<String, RenderDemo>

    @SuppressLint("RtlHardcoded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        drawerLayout = drawerLayout {
            fullSize()
            id = R.id.drawer_layout

            root = frameLayout {
                id = R.id.placeholder
                fullSize()

                if (views.isEmpty()) return@frameLayout
                if (activeSection.get().isEmpty()) {
                    activeSection.set(views.keys.first())
                }
            }

            verticalLayout {
                backgroundColor = Color.WHITE
                editText {
                    hint = getString(R.string.storyName)
                    txt(searchPattern)
                }.lparams {
                    asRow()
                    bottomMargin = dip(10)
                }
                itemListView(
                    itemViewBinders = mapOf(
                        Item.Default.ONLY as Enum<*> to { field: IBox<StringHolder> -> bindString(field, activeSection) }
                    ),
                    items = searchPattern.oneWayBranch {
                        views.keys.mapIndexed { index, s -> StringHolder(index.toLong(), s) }
                            .filter { it.payload.contains(this) }
                    }
                ).add(this).lparams {
                    width = matchParent
                    weight = 1f
                }
            }.lparams {
                width = matchParent
                height = matchParent
                gravity = Gravity.START
            }
        }

        activeSection.onChange {
            val renderer = views[it] ?: return@onChange
            renderer(this@AbstractActivityStorybook).setAsOne(root)
            title = it
            drawerLayout.closeDrawer(Gravity.LEFT)
        }

        configureSideMenu()
    }

    private fun configureSideMenu() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        drawerToggle = drawerLayout.getToggle(this)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
        drawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        this.closeKeyboard()
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }
}