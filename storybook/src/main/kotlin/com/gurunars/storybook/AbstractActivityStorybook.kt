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
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.gurunars.box.*
import com.gurunars.box.ui.*
import com.gurunars.item_list.Item
import com.gurunars.item_list.itemListView

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
) = with<LinearLayout> {
    asRow()
    onClick {
        activeSection.set(field.get().fullName)
    }
    merge(activeSection, field).onChange {
        setBackgroundColor(
            if (it.second.fullName == it.first)
                Color.RED
            else
                Color.WHITE
        )
    }
    isClickable = true
    padding = Bounds(dip(6))
    with<TextView> {
        text(field.oneWayBranch { fullName.split(".").last() })
        textSize = 20f
    }.layout(this) {
        asRow()
    }
    with<TextView> {
        text(field.oneWayBranch { fullName })
        textSize = 10f
    }.layout(this) {
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

        drawerLayout = with<DrawerLayout> {
            id = R.id.drawer_layout

            root = with<FrameLayout> {
                id = R.id.placeholder

                if (views.isEmpty()) return
                if (activeSection.get().isEmpty()) {
                    activeSection.set(views.keys.first())
                }
            }.layout(this) {
                fullSize()
            }

            with<LinearLayout> {
                setBackgroundColor(Color.WHITE)
                with<EditText> {
                    hint = getString(R.string.storyName)
                    text(searchPattern)
                }.layout(this) {
                    asRow()
                    margin = Bounds(bottom=dip(10))
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
                ).layout(this) {
                    width = MATCH_PARENT
                    weight = 1f
                }
            }.layout(this) {
                fullSize()
                gravity = Gravity.START
            }
        }.layoutAsOne(this)

        activeSection.onChange {
            val renderer = views[it] ?: return@onChange
            renderer(this@AbstractActivityStorybook).layoutAsOne(root)
            title = it.split(".").last()
            drawerLayout.closeDrawer(Gravity.LEFT)
        }

        configureSideMenu()
    }

    private fun configureSideMenu() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        drawerToggle = drawerLayout.getToggle(this)
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        drawerToggle.syncState()
    }

    /** @suppress */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        this.closeKeyboard()
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }
}