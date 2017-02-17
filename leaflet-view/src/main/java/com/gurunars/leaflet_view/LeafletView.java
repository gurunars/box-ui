package com.gurunars.leaflet_view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * View pager without fragments on steroids.
 *
 * @param <ViewT> View subclass to be used to render individual pages
 * @param <PageT> Page subclass to be used to populate the pages
 */
public class LeafletView<ViewT extends View, PageT extends Page> extends LinearLayout {

    private ViewPager viewPager;
    private ViewGroup emptyHolder;
    private ConcreteLeafletAdapter<ViewT, PageT> leafletAdapter;

    public LeafletView(Context context) {
        this(context, null);
    }

    public LeafletView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeafletView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.leaflet_view, this);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        emptyHolder = (ViewGroup) findViewById(R.id.empty_holder);
        configureAdapter();
    }

    private void configureAdapter() {
        leafletAdapter = new ConcreteLeafletAdapter<>(viewPager, emptyHolder);
        // Note casting the default view group here is fine since we know for sure that the enter
        // and leave methods won't use them
        leafletAdapter.setDefaultPageRenderer((PageRenderer<ViewT, PageT>) new PageRenderer<View, PageT>() {
            @Override
            public View renderPage(PageT page) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.default_page_view, LeafletView.this, false);
                TextView textView = (TextView) viewGroup.findViewById(R.id.pageTitle);
                textView.setText(page.toString());
                return viewGroup;
            }

            @Override
            public void enter(View pageView) {

            }

            @Override
            public void leave(View pageView) {

            }
        });
        leafletAdapter.setNoPageRenderer(new NoPageRenderer() {
            @Override
            public View renderNoPage() {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                return inflater.inflate(R.layout.default_no_page_view, LeafletView.this, false);
            }

            @Override
            public void enter() {

            }
        });
        viewPager.setAdapter(leafletAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                leafletAdapter.goTo(leafletAdapter.getCurrentPage());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * @param pageRenderer a substance that produces a view to be shown for a given page
     */
    public void setPageRenderer(PageRenderer<ViewT, PageT> pageRenderer) {
        leafletAdapter.setPageRenderer(pageRenderer);
    }

    /**
     * @param noPageRenderer a substance that produces a view to be show when there are no pages
     */
    public void setNoPageRenderer(NoPageRenderer noPageRenderer) {
        leafletAdapter.setNoPageRenderer(noPageRenderer);
    }

    /**
     * @param pages a collection of payloads to traverse
     */
    public void setPages(List<PageT> pages) {
        leafletAdapter.setPages(pages);
    }

    /**
     * @return currently selected page
     */
    public PageT getCurrentPage() {
        return leafletAdapter.getCurrentPage();
    }

    /**
     * Scroll to a specific page. The page is expected to exist in a collection set via setPages.
     *
     * @param page page's payload to navigate to
     */
    public void goTo(PageT page) {
        leafletAdapter.goTo(page);
    }
}
