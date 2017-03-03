package com.gurunars.leaflet_view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.List;

import butterknife.ButterKnife;

/**
 * View pager without fragments on steroids.
 *
 */
public class LeafletView extends FrameLayout {

    private ViewPager viewPager;
    private LeafletAdapter leafletAdapter;

    public LeafletView(Context context) {
        this(context, null);
    }

    public LeafletView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeafletView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.leaflet_view, this);
        viewPager = ButterKnife.findById(this, R.id.view_pager);
        configureAdapter();
    }

    private void configureAdapter() {
        leafletAdapter = new LeafletAdapter(viewPager);
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
     * @param noPage a substance that produces a view to be show when there are no pages
     */
    public void setNoPage(NoPage noPage) {
        leafletAdapter.setNoPage(noPage);
    }

    /**
     * @param pages a collection of payloads to traverse
     */
    public void setPages(List<? extends Page> pages) {
        leafletAdapter.setPages(pages);
    }

    /**
     * Scroll to a specific page. The page is expected to exist in a collection set via setPages.
     *
     * @param page page's payload to navigate to
     */
    public void goTo(Page page) {
        leafletAdapter.goTo(page);
    }

    /**
     * Subscribe to pager enter/leave events.
     *
     * @param listener a listener that is located externally with respect to the pager views.
     */
    public void setPageTransitionListener(PageTransitionListener listener) {
        leafletAdapter.setExternalListener(listener);
    }
}
