package com.gurunars.leaflet_view

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.esotericsoftware.kryo.Kryo
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.textView
import org.objenesis.strategy.StdInstantiatorStrategy
import java.util.*

internal class LeafletAdapter<PageT : Page>(
        private val pager: ViewPager,
        private val emptyHolder: ViewGroup) : PagerAdapter() {

    private val kryo = Kryo().apply {
        instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }

    private var pages = listOf<PageT>()
    private var previousPages = listOf<PageT>()

    private val mapping = SparseArray<View>()
    private var childState = HashMap<Long, SparseArray<Parcelable>>()

    private fun getCenteredView(value: String): View {
        return AnkoContext.createReusable(pager.context).frameLayout {
            fullSize()
            textView {
                text=value
            }.lparams {
                gravity=Gravity.CENTER
            }
        }
    }

    private var noPageRenderer: NoPageRenderer = object : NoPageRenderer {
        override fun renderNoPage() = getCenteredView(pager.context.getString(R.string.empty))
        override fun enter() {}
    }
    private var pageRenderer: PageRenderer<PageT> = object: PageRenderer<PageT> {
        override fun renderPage(page: PageT) = getCenteredView(page.toString())
        override fun enter(pageView: View) {}
        override fun leave(pageView: View) {}
    }

    private val pageEquator = { one: Page?, two: Page? ->
        one?.javaClass == two?.javaClass && one?.id == two?.id
    }

    fun setNoPageRenderer(noPageRenderer: NoPageRenderer) { this.noPageRenderer = noPageRenderer }

    fun setPageRenderer(pageRenderer: PageRenderer<PageT>) {
        this.pageRenderer = pageRenderer
        this.mapping.clear()
        notifyDataSetChanged()
    }

    fun setPages(pages: List<PageT>) {
        val oldCurrent = getCurrentPage()
        this.previousPages = this.pages
        this.pages = this.kryo.copy(pages)
        notifyDataSetChanged()
        goTo(pages.find({pageEquator(it, oldCurrent)}) ?: getCurrentPage())
    }

    fun getCurrentPage() = if (pages.isEmpty()) null else pages[Math.min(pager.currentItem, pages.size - 1)]

    fun goTo(page: PageT?) {
        if (page == null) {
            pager.visibility = View.GONE
            emptyHolder.visibility = View.VISIBLE
            emptyHolder.removeAllViews()
            emptyHolder.addView(noPageRenderer.renderNoPage())
            noPageRenderer.enter()
        } else {
            val desiredIndex = pages.indexOfFirst { pageEquator(it, page) }
            if (desiredIndex != pager.currentItem) {
                pager.setCurrentItem(desiredIndex, true)
            }
            pager.visibility = View.VISIBLE
            emptyHolder.visibility = View.GONE

            for (i in 0..mapping.size() - 1) {
                leave(pageRenderer, mapping.get(i))
            }
            enter(pageRenderer, mapping.get(desiredIndex))
        }
    }

    override fun getCount() = pages.size

    private fun enter(renderer: PageRenderer<PageT>?, pageView: View?) {
        if (pageView != null) renderer?.enter(pageView)
    }

    private fun leave(renderer: PageRenderer<PageT>?, pageView: View?) {
        if (pageView != null) renderer?.leave(pageView)
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any? {
        val page = pages[position]
        var view = mapping.get(position)

        if (view == null) {
            view = pageRenderer.renderPage(page)
            mapping.put(position, view)
        }
        collection.addView(view)
        if (position == pager.currentItem) { enter(pageRenderer, view) }
        loadItemState(position)
        return view
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        //It would be nice to have an infinite state storage - but it would lead to state size
        //explosion
        //saveItemState(position);
        collection.removeView(view as View)
        mapping.remove(position)
    }

    override fun isViewFromObject(view: View, obj: Any) = view === obj

    override fun getPageTitle(position: Int) = pages[position].toString()

    override fun getItemPosition(obj: Any?): Int {
        if (obj is Page) {
            val pos = pages.indexOfFirst { pageEquator(it, obj) }
            if (pos == -1) return PagerAdapter.POSITION_NONE
            val prevPost = previousPages.indexOfFirst { pageEquator(it, obj) }
            return if (pos == prevPost) PagerAdapter.POSITION_UNCHANGED else pos
        }
        return PagerAdapter.POSITION_NONE
    }

    override fun saveState(): Parcelable {
        // We want to store state for the views currently kept in RAM
        for (i in 0..mapping.size() - 1) {
            saveItemState(mapping.keyAt(i))
        }
        return Bundle().apply {
            putSerializable("state", childState)
        }
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        if (state == null) return
        val bundle = state as Bundle?
        val tmp = bundle?.getSerializable("state") as HashMap<Long, SparseArray<Parcelable>>?
        if (tmp != null) { childState = tmp }
    }

    private fun saveItemState(position: Int) {
        val viewState = SparseArray<Parcelable>()
        val view = mapping.get(position)
        view.saveHierarchyState(viewState)

        childState.put(
            if (pages.isEmpty()) 0L else pages[position].id,
            viewState)
    }

    private fun loadItemState(position: Int) {
        val viewState = childState[
            if (pages.isEmpty()) 0L else pages[position].id
        ] ?: return
        val view = mapping.get(position)
        view.restoreHierarchyState(viewState)
    }
}
