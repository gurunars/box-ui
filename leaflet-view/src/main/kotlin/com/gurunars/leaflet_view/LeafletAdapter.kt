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
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.DeepList
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.textView
import org.objenesis.strategy.StdInstantiatorStrategy
import java.util.*

internal class LeafletAdapter<PageT : Page>(
        private val pager: ViewPager,
        private val pages: BindableField<DeepList<PageT>>,
        private val currentPage: BindableField<PageT?>
) : PagerAdapter() {

    private val kryo = Kryo().apply {
        instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }

    private var currentPages = listOf<PageT>()
    private var previousPages = listOf<PageT>()

    init {

        fun calculateCurrentPage() =
                if (currentPages.isEmpty())
                    null
                else
                    currentPages[Math.min(pager.currentItem, currentPages.size - 1)]

        pages.onChange {
            val oldCurrent = calculateCurrentPage()
            this.previousPages = this.currentPages
            this.currentPages = this.kryo.copy(it.array.toList())
            notifyDataSetChanged()
            currentPage.set(this.currentPages.find({pageEquator(it, oldCurrent)}) ?: calculateCurrentPage())
        }
        currentPage.onChange {
            if (it == null) {
                if (currentPages.isNotEmpty()) currentPage.set(currentPages.get(0))
            } else {
                val page = it
                val desiredIndex = currentPages.indexOfFirst { pageEquator(it, page) }
                if (desiredIndex != pager.currentItem) pager.setCurrentItem(desiredIndex, true)
            }
        }
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) { currentPage.set(calculateCurrentPage()) }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

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

    var noPageRenderer: () -> View = {getCenteredView(pager.context.getString(R.string.empty))}
        set(value) {
            field = value
            if (pages.get().array.isEmpty()) {
                notifyDataSetChanged()
            }
        }

    var pageRenderer: (page: PageT) -> View = {page -> getCenteredView(page.toString())}
        set(value) {
            field = value
            mapping.clear()
            notifyDataSetChanged()
        }

    private val pageEquator = { one: Page?, two: Page? ->
        one?.javaClass == two?.javaClass && one?.id == two?.id
    }

    override fun getCount() = Math.max(1, currentPages.size)

    override fun instantiateItem(collection: ViewGroup, position: Int): Any? {
        if (currentPages.isEmpty()) {
            return noPageRenderer()
        }

        val page = currentPages[position]
        var view = mapping.get(position)

        if (view == null) {
            view = pageRenderer(page)
            mapping.put(position, view)
        }
        collection.addView(view)
        if (position == pager.currentItem) { currentPage.set(page) }
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

    override fun getPageTitle(position: Int) = currentPages[position].toString()

    override fun getItemPosition(obj: Any?): Int {
        if (obj is Page) {
            val pos = currentPages.indexOfFirst { pageEquator(it, obj) }
            if (pos == -1) return PagerAdapter.POSITION_NONE
            val prevPost = previousPages.indexOfFirst { pageEquator(it, obj) }
            return if (pos == prevPost) PagerAdapter.POSITION_UNCHANGED else pos
        }
        return PagerAdapter.POSITION_NONE
    }

    override fun saveState(): Parcelable {
        // We want to store state for the views currently kept in RAM
        for (i in 0..mapping.size() - 1) { saveItemState(mapping.keyAt(i)) }
        return Bundle().apply { putSerializable("state", childState) }
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
            if (currentPages.isEmpty()) 0L else currentPages[position].id,
            viewState)
    }

    private fun loadItemState(position: Int) {
        val viewState = childState[
            if (currentPages.isEmpty()) 0L else currentPages[position].id
        ] ?: return
        mapping.get(position).restoreHierarchyState(viewState)
    }
}
