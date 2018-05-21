package com.gurunars.box.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.gurunars.box.core.IReadOnlyObservableValue
import com.gurunars.box.core.ObservableValue

data class Page<PageType: Enum<PageType>>(
    val type: PageType,
    val view: View,
    val onEnter: View.() -> Unit = {},
    val onLeave: View.() -> Unit = {}
)

private class PagedAdapter<PageType: Enum<PageType>>(
    private val viewPager: ViewPager,
    private val pages: List<Page<PageType>>,
    private val withAnimation: Boolean = false
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any =
        pages[position].view.also { container.addView(it) }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) =
        container.removeView(view as View)

    override fun isViewFromObject(view: View, obj: Any): Boolean =
        view == obj

    override fun getCount(): Int =
        pages.size

    fun setPage(type: PageType) =
        pages
            .indexOfFirst { it.type == type }
            .takeIf { it >= 0 }
            ?.let {
                pages.getOrNull(viewPager.currentItem)?.let { page ->
                    page.onLeave(page.view)
                }
                viewPager.setCurrentItem(it, withAnimation)
                pages.getOrNull(it)?.let { page ->
                    page.onEnter(page.view)
                }
            }

}

@SuppressLint("ViewConstructor")
private class PagedView(
    context: Context,
    private val withSwipe: Boolean = false
) : ViewPager(context) {

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean =
        if (withSwipe) {
            super.onInterceptTouchEvent(event)
        } else {
            false
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean = if (withSwipe) {
        super.onTouchEvent(event)
    } else {
        false
    }
}

fun <PageType: Enum<PageType>> Context.pagedView(
    currentPage: IReadOnlyObservableValue<PageType>,
    pages: List<Page<PageType>>,
    withAnimation: Boolean = false,
    withSwipe: Boolean = false
): View = (withSwipe && currentPage is ObservableValue<PageType>).let { swipeEnabled ->
    PagedView(this, swipeEnabled).apply {

        if (swipeEnabled) {
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    currentPage as ObservableValue<PageType>
                    currentPage.set(pages[position].type)
                }

            })
        }

        PagedAdapter(this, pages, withAnimation).let { typedAdapter ->
            adapter = typedAdapter
            currentPage.onChange { typedAdapter.setPage(it) }
        }
    }
}
