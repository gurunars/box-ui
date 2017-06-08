package com.gurunars.leaflet_view.example

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import com.gurunars.leaflet_view.LeafletView
import com.gurunars.leaflet_view.leafletView
import com.gurunars.shortcuts.fullSize
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.*


class ActivityMain : Activity() {

    private val storage= PersistentStorage(this, "main")
    private val pages = storage.storageField("pages", ArrayList<TitledPage>())

    private lateinit var leafletView: LeafletView<TitledPage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        frameLayout {
            fullSize()
            leafletView=leafletView<TitledPage> {
                id=R.id.leafletView
                fullSize()

                pages.onChange {
                    setPages(it.sortedWith(kotlin.Comparator {
                        lhs, rhs -> lhs.title.compareTo(rhs.title)
                    }))
                }

                currentPage.onChange { title = it.toString() }

                setPageRenderer({page: TitledPage ->
                    AnkoContext.createReusable(context).relativeLayout {
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
                })
            }
        }

        storage.load()

    }

    override fun onDestroy() {
        super.onDestroy()
        storage.unbindAll()
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
            setSingleChoiceItems(TitledPageAdapter(this@ActivityMain, pages.get()), -1
            ) { dialog, which ->
                leafletView.currentPage.set(pages.get()[which])
                dialog.dismiss()
            }
            setCancelable(true)
            show()
        }
    }

    private fun editPage() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.edit)
            val currentPage = leafletView.currentPage.get() as TitledPage
            val input = EditText(this@ActivityMain).apply {
                id = R.id.pageTitle
                inputType = InputType.TYPE_CLASS_TEXT
                setText(currentPage.title)
            }
            setView(input)
            // Set up the buttons
            setPositiveButton(R.string.ok) { _, _ ->
                currentPage.title = input.text.toString()
                // NOTE: surely equals method could have been implemented
                // however the idea is to demo that these methods are not important -
                // only getId method is
                pages.set(pages.get().apply {
                    indices
                        .filter { get(it).id == currentPage.id }
                        .forEach { set(it, currentPage) }
                })
            }
            show()
        }
    }

    private fun deletePage() {
        // NOTE: surely equals method could have been implemented
        // however the idea is to demo that these methods are not important - only getId method is
        pages.set(pages.get().apply {
            indices.filter { get(it).id == leafletView.currentPage.get()!!.id }.forEach { removeAt(it) }
        })
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
                pages.set(pages.get().apply { add(TitledPage(input.text.toString())) })
            }
            show()
        }
    }

    private fun clear() { pages.set(ArrayList()) }

}