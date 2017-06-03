package com.gurunars.leaflet_view

import android.content.Context
import android.support.v4.view.ViewPager
import android.widget.LinearLayout
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.support.v4.viewPager

/**
 * View pager without fragments on steroids.
 *
 * @param <PageT> Page subclass to be used to populate the pages
 */
class LeafletView<PageT : Page>  constructor(context: Context) : LinearLayout(context) {

    private lateinit var leafletAdapter: LeafletAdapter<PageT>

    init {

        frameLayout {
            fullSize()
            val viewPager = viewPager {
                id=R.id.viewPager
                fullSize()
            }
            val emptyHolder = frameLayout {
                id=R.id.emptyHolder
                fullSize()
            }
            leafletAdapter = LeafletAdapter<PageT>(viewPager, emptyHolder)
            viewPager.adapter = leafletAdapter
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    leafletAdapter.goTo(leafletAdapter.getCurrentPage())
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
        }
    }

    /**
     * @param pageRenderer a substance that produces a view to be shown for a given page
     */
    fun setPageRenderer(pageRenderer: PageRenderer<PageT>) {
        leafletAdapter.setPageRenderer(pageRenderer)
    }

    /**
     * @param noPageRenderer a substance that produces a view to be show when there are no pages
     */
    fun setNoPageRenderer(noPageRenderer: NoPageRenderer) {
        leafletAdapter.setNoPageRenderer(noPageRenderer)
    }

    /**
     * @param pages a collection of payloads to traverse
     */
    fun setPages(pages: List<PageT>) {
        leafletAdapter.setPages(pages)
    }

    /**
     * @return currently selected page
     */
    fun getCurrentPage() = leafletAdapter.getCurrentPage()

    /**
     * Scroll to a specific page. The page is expected to exist in a collection set via setPages.
     *
     * @param page page's payload to navigate to
     */
    fun goTo(page: PageT) {
        leafletAdapter.goTo(page)
    }
}
