package com.gurunars.leaflet_view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.gurunars.databinding.BindableField
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.support.v4.viewPager

/**
 * View pager without fragments on steroids.
 *
 * @param <PageT> Page subclass to be used to populate the pages
 */
class LeafletView<PageT : Page>  constructor(context: Context) : FrameLayout(context) {

    private var leafletAdapter: LeafletAdapter<PageT>

    val currentPage = BindableField<PageT?>(null)

    init {
        fullSize()
        val viewPager = viewPager {
            id=R.id.viewPager
            fullSize()
        }
        leafletAdapter = LeafletAdapter<PageT>(viewPager)
        viewPager.adapter = leafletAdapter
        currentPage.bind(leafletAdapter.currentPage)
    }

    /**
     * @param pageRenderer a substance that produces a view to be shown for a given page
     */
    fun setPageRenderer(pageRenderer: (page: PageT) -> View) {
        leafletAdapter.pageRenderer = pageRenderer
    }

    /**
     * @param noPageRenderer a substance that produces a view to be show when there are no pages
     */
    fun setNoPageRenderer(noPageRenderer: () -> View) {
        leafletAdapter.noPageRenderer = noPageRenderer
    }

    /**
     * @param pages a list of pages to be set for the adapter
     */
    fun setPages(pages: List<PageT>) {
        leafletAdapter.setPages(pages)
    }

}