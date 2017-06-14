package com.gurunars.item_list.example

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.gurunars.item_list.EmptyViewBinder
import com.gurunars.item_list.ItemList
import com.gurunars.item_list.ItemViewBinder
import com.gurunars.item_list.itemList
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.dip
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent


class ActivityMain : Activity() {

    internal class AnimalBinder : ItemViewBinder<TextView, AnimalItem> {

        override fun getView(context: Context): TextView {
            return TextView(context).apply {
                layoutParams=ViewGroup.LayoutParams(matchParent, wrapContent)
                val padding = context.dip(5)
                setPadding(padding, padding, padding, padding)
            }
        }

        override fun bind(itemView: TextView, item: AnimalItem, previousItem: AnimalItem?) {
            itemView.text = item.toString()
            if (previousItem != null) {
                animateUpdate(itemView)
            }
        }

        private fun animateUpdate(view: View) {
            view.clearAnimation()
            ValueAnimator().apply {
                setFloatValues(1.0.toFloat(), 0.0.toFloat(), 1.0.toFloat())
                addUpdateListener { animation -> view.alpha = animation.animatedValue as Float }
                duration = 1300
                start()
            }
        }

    }

    internal class EmptyBinder : EmptyViewBinder {

        override fun getView(context: Context): View {
            val view = TextView(context)
            view.gravity = Gravity.CENTER
            view.setText(R.string.empty)
            return view
        }

    }

    private lateinit var itemList: ItemList<AnimalItem>
    private val items = ArrayList<AnimalItem>()
    private var count = 0

    private fun add(type: AnimalItem.Type) {
        items.add(AnimalItem(count.toLong(), 0, type))
        count++
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout {
            layoutParams=ViewGroup.LayoutParams(matchParent, matchParent)
            itemList=itemList<AnimalItem> {
                fullSize()
                id=R.id.itemList

                itemViewBinders.set(mutableMapOf<Enum<*>, ItemViewBinder<*, AnimalItem>>().apply {
                    AnimalItem.Type.values().forEach { put(it, AnimalBinder()) }
                })
                emptyViewBinder.set(EmptyBinder())
            }
        }

        reset()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear -> return showToast(clear())
            R.id.create -> return showToast(create())
            R.id.delete -> return showToast(delete())
            R.id.update -> return showToast(update())
            R.id.moveDown -> return showToast(moveTopToBottom())
            R.id.moveUp -> return showToast(moveBottomToTop())
            R.id.reset -> return showToast(reset())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToast(@StringRes text: Int): Boolean {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        return true
    }

    @StringRes private fun clear(): Int {
        items.clear()
        itemList.items.set(items)
        return R.string.did_clear
    }

    @StringRes private fun create(): Int {
        add(AnimalItem.Type.TIGER)
        add(AnimalItem.Type.WOLF)
        add(AnimalItem.Type.MONKEY)
        add(AnimalItem.Type.LION)
        itemList.items.set(items)
        return R.string.did_create
    }

    @StringRes private fun delete(): Int {
        for (i in items.indices.reversed()) {
            if (i % 2 != 0) {
                items.removeAt(i)
            }
        }
        itemList.items.set(items)
        return R.string.did_delete
    }

    @StringRes private fun update(): Int {
        for (i in items.indices) {
            if (i % 2 != 0) {
                items[i].update()
            }
        }
        itemList.items.set(items)
        return R.string.did_update
    }

    @StringRes private fun moveBottomToTop(): Int {
        if (items.size <= 0) {
            return R.string.no_action
        }
        itemList.items.set(items.apply {
            val item = get(size - 1)
            removeAt(size - 1)
            add(0, item)
        })
        return R.string.did_move_to_top
    }

    @StringRes private fun moveTopToBottom(): Int {
        if (items.size <= 0) {
            return R.string.no_action
        }
        itemList.items.set(items.apply {
            val item = get(0)
            removeAt(0)
            add(item)
        })
        return R.string.did_move_to_bottom
    }

    @StringRes private fun reset(): Int {
        count = 0
        items.clear()
        create()
        return R.string.did_reset
    }

}