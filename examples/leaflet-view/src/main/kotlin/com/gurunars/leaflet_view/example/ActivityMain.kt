package com.gurunars.leaflet_view.example

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gurunars.leaflet_view.LeafletView
import com.gurunars.leaflet_view.PageRenderer
import com.gurunars.leaflet_view.leafletView
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.*


class ActivityMain : AppCompatActivity() {

    private var pages = mutableListOf<TitledPage>()
    private lateinit var leafletView: LeafletView<TitledPage>

    private fun updateAdapter() {
        leafletView.setPages(pages.sortedWith(kotlin.Comparator { lhs, rhs -> lhs.title.compareTo(rhs.title)}))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        frameLayout {
            fullSize()
            leafletView=leafletView<TitledPage> {
                id=R.id.leafletView
                fullSize()
                setPageRenderer(object : PageRenderer<TitledPage> {
                    override fun renderPage(page: TitledPage): View {
                        return AnkoContext.createReusable(context).relativeLayout {
                            fullSize()
                            gravity=Gravity.CENTER
                            tag = page
                            textView {
                                id=R.id.pageTitle
                                text=page.toString()
                            }.lparams()

                            editText {
                                id=R.id.textEdit
                            }.lparams {
                                width=dip(150)
                                topMargin=dip(15)
                                below(R.id.pageTitle)
                            }
                        }
                    }

                    override fun enter(pageView: View) {
                        title = pageView.tag.toString()
                    }

                    override fun leave(pageView: View) {

                    }
                })
            }
        }

        load()
        updateAdapter()
    }

    private fun load() {
        pages = Gson().fromJson<ArrayList<TitledPage>>(
            getPreferences(Context.MODE_PRIVATE).getString("data", "[]"),
            object : TypeToken<ArrayList<TitledPage>>() {
        }.type)
    }

    private fun save() {
        val editor = getPreferences(Context.MODE_PRIVATE).edit()
        editor.putString("data", Gson().toJson(pages))
        editor.apply()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
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
            return convertView ?: AnkoContext.createReusable(parent.context).textView {
                textSize=15f
                padding=dip(10)
                gravity=Gravity.CENTER_VERTICAL
                text=getItem(position).title
            }
        }

    }

    private fun goToPage() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.go_to)
            setSingleChoiceItems(
                    TitledPageAdapter(this@ActivityMain, pages), -1
            ) { dialog, which ->
                leafletView.goTo(pages[which])
                dialog.dismiss()
            }
            setCancelable(true)
            show()
        }
    }

    private fun editPage() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.edit)
            val input = EditText(this@ActivityMain)
            input.id = R.id.pageTitle
            input.inputType = InputType.TYPE_CLASS_TEXT
            val currentPage = leafletView.getCurrentPage() as TitledPage
            input.setText(currentPage.title)
            setView(input)
            // Set up the buttons
            setPositiveButton(R.string.ok) { _, _ ->
                currentPage.title = input.text.toString()
                // NOTE: surely equals method could have been implemented
                // however the idea is to demo that these methods are not important -
                // only getId method is
                pages.indices
                        .filter { pages[it].id == currentPage.id }
                        .forEach { pages[it] = currentPage }
                updateAdapter()
                save()
            }
            show()
        }
    }

    private fun deletePage() {
        // NOTE: surely equals method could have been implemented
        // however the idea is to demo that these methods are not important - only getId method is
        val page = leafletView.getCurrentPage() as TitledPage
        pages.indices
                .filter { pages[it].id == page.id }
                .forEach { pages.removeAt(it) }
        updateAdapter()
        save()
    }

    private fun createPage() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.edit)
            val input = EditText(this@ActivityMain).apply {
                id = R.id.pageTitle
                inputType = InputType.TYPE_CLASS_TEXT
            }
            setView(input)
            setPositiveButton(R.string.ok) { _, _ ->
                pages.add(TitledPage(input.text.toString()))
                save()
                updateAdapter()
            }
            show()
        }
    }

    private fun clear() {
        pages.clear()
        updateAdapter()
        save()
    }

}