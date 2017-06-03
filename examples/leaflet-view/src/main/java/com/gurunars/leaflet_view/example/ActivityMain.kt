package com.gurunars.leaflet_view.example

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gurunars.leaflet_view.LeafletView
import com.gurunars.leaflet_view.NoPageRenderer
import com.gurunars.leaflet_view.PageRenderer

import java.util.ArrayList
import java.util.Collections
import java.util.Comparator

import butterknife.ButterKnife


class ActivityMain : AppCompatActivity() {

    private var pages: MutableList<TitledPage> = ArrayList()
    private var leafletView: LeafletView<View, TitledPage>? = null

    private fun updateAdapter() {
        Collections.sort(pages) { lhs, rhs -> lhs.title.compareTo(rhs.title) }
        leafletView!!.setPages(pages)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        leafletView = ButterKnife.findById<LeafletView<View, TitledPage>>(this, R.id.leafletView)
        leafletView!!.setPageRenderer(object : PageRenderer<View, TitledPage> {
            fun renderPage(page: TitledPage): View {
                val viewGroup = LayoutInflater.from(this@ActivityMain).inflate(
                        R.layout.page_view, leafletView, false)
                val textView = viewGroup.findViewById(
                        R.id.pageTitle) as TextView
                textView.text = page.toString()
                viewGroup.tag = page
                return viewGroup
            }

            override fun enter(pageView: View) {
                title = pageView.tag.toString()
            }

            override fun leave(pageView: View) {

            }
        })

        leafletView!!.setNoPageRenderer(object : NoPageRenderer {
            override fun renderNoPage(): View {
                return LayoutInflater.from(this@ActivityMain).inflate(R.layout.no_page_view, leafletView, false)
            }

            override fun enter() {
                setTitle(R.string.empty)
            }

        })

        load()
        updateAdapter()
    }

    private fun load() {
        pages = Gson().fromJson<List<TitledPage>>(getPreferences(Context.MODE_PRIVATE).getString("data", "[]"), object : TypeToken<ArrayList<TitledPage>>() {

        }.type)
    }

    private fun save() {
        val editor = getPreferences(Context.MODE_PRIVATE).edit()
        editor.putString("data", Gson().toJson(pages))
        editor.apply()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.create -> {
                createPage()
                return true
            }
            R.id.delete -> {
                deletePage()
                return true
            }
            R.id.edit -> {
                editPage()
                return true
            }
            R.id.go_to -> {
                goToPage()
                return true
            }
            R.id.clear -> {
                clear()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private class TitledPageAdapter internal constructor(context: Context, items: List<TitledPage>) : ArrayAdapter<TitledPage>(context, 0, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
                val text = convertView!!.findViewById(android.R.id.text1) as TextView
                text.text = getItem(position)!!.title
            }
            return convertView
        }

    }

    private fun goToPage() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.go_to)
        builder.setSingleChoiceItems(
                TitledPageAdapter(this, pages), -1
        ) { dialog, which ->
            leafletView!!.goTo(pages[which])
            dialog.dismiss()
        }
        builder.setCancelable(true)
        builder.show()
    }

    private fun editPage() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.edit)
        val input = EditText(this)
        input.id = R.id.pageTitle
        input.inputType = InputType.TYPE_CLASS_TEXT
        val currentPage = leafletView!!.getCurrentPage() ?: return
        input.setText(currentPage.title)
        builder.setView(input)
        // Set up the buttons
        builder.setPositiveButton(R.string.ok) { dialog, which ->
            val page = leafletView!!.getCurrentPage()
            page!!.title = input.text.toString()
            // NOTE: surely equals method could have been implemented
            // however the idea is to demo that these methods are not important -
            // only getId method is
            for (i in pages.indices) {
                if (pages[i].id == page!!.id) {
                    pages[i] = page
                }
            }
            updateAdapter()
            save()
        }
        builder.show()
    }

    private fun deletePage() {
        // NOTE: surely equals method could have been implemented
        // however the idea is to demo that these methods are not important - only getId method is
        val page = leafletView!!.getCurrentPage()
        for (i in pages.indices) {
            if (pages[i].id == page!!.id) {
                pages.removeAt(i)
            }
        }
        updateAdapter()
        save()
    }

    private fun createPage() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.edit)
        val input = EditText(this)
        input.id = R.id.pageTitle
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        // Set up the buttons
        builder.setPositiveButton(R.string.ok) { dialog, which ->
            val page = TitledPage(input.text.toString())
            pages.add(page)
            updateAdapter()
            save()
        }
        builder.show()
    }

    private fun clear() {
        pages.clear()
        updateAdapter()
        save()
    }

}