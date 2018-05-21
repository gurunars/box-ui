package com.gurunars.box.ui.components

import android.content.Context
import android.view.ViewGroup
import com.gurunars.box.core.IObservableValue
import com.gurunars.box.ui.asRow
import com.gurunars.box.ui.dip
import com.gurunars.box.ui.verticalLayout

fun <PageType> Context.pagedViewWithNavigation(
    currentPage: IObservableValue<PageType>,
    pages: List<Page<PageType>>
) where PageType: MenuItem, PageType: Enum<PageType> = verticalLayout {
    pagedView(
        currentPage = currentPage,
        pages = pages,
        withAnimation = true,
        withSwipe = true
    ).layoutParams {
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = dip(0)
        weight = 1f
    }
    bottomNavigationView(
        currentPage, pages.map { it.type }
    ).layoutParams {
        asRow()
    }
}