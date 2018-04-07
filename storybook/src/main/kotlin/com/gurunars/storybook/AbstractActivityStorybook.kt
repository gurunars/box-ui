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
import com.gurunars.box.*
import com.gurunars.box.ui.add
import com.gurunars.box.ui.asRow
import com.gurunars.box.ui.closeKeyboard
import com.gurunars.box.ui.fullSize
import com.gurunars.box.ui.onClick
import com.gurunars.box.ui.setAsOne
import com.gurunars.box.ui.text
import com.gurunars.item_list.Item
import com.gurunars.item_list.itemListView
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.dip
import org.jetbrains.anko.editText
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.support.v4.drawerLayout
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

private fun DrawerLayout.getToggle(activity: Activity) = ActionBarDrawerToggle(
    activity,
    this,
    R.string.open_main_menu,
    R.string.close_main_menu
).apply {
    addDrawerListener(this)
}

private data class PackageName(
    override val id: Long,
    val fullName: String
) : Item

private fun Context.bindPackageName(
    field: IRoBox<PackageName>,
    activeSection: IBox<String>
) = verticalLayout {
    asRow()
    onClick {
        activeSection.set(field.get().fullName)
    }
    merge(activeSection, field).onChange {
        backgroundColor = if (it.second.fullName == it.first) {
            Color.RED
        } else {
            Color.WHITE
        }
    }
    isClickable = true
    padding = dip(6)
    textView {
        text(field.oneWayBranch { fullName.split(".").last() })
        textSize = 20f
        asRow()
    }
    textView {
        text(field.oneWayBranch { fullName })
        textSize = 10f
        asRow()
    }
}

/**
 * Base class for Storybook registry.
 *
 * Best to be used with storybook-registry package and @StorybookComponent annotation.
 */
abstract class AbstractActivityStorybook : Activity() {

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var root: FrameLayout

    private val activeSection = "".box
    private val searchPattern = "".box

    /** A collection of view functions registered within the Storybook. */
    abstract val views: Map<String, Context.() -> View>

    /** @suppress */
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
                    text(searchPattern)
                }.lparams {
                    asRow()
                    bottomMargin = dip(10)
                }
                itemListView(
                    itemViewBinders = mapOf(
                        Item.Default.ONLY as Enum<*> to { field: IRoBox<PackageName> ->
                            bindPackageName(field, activeSection)
                        }
                    ),
                    items = searchPattern.oneWayBranch {
                        views.keys.mapIndexed { index, s -> PackageName(index.toLong(), s) }
                            .filter { it.fullName.contains(this) }
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
            title = it.split(".").last()
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

    /** @suppress */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        this.closeKeyboard()
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }
}