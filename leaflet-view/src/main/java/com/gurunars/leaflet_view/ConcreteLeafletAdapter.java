package com.gurunars.leaflet_view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.esotericsoftware.kryo.Kryo;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.util.ArrayList;
import java.util.List;

class ConcreteLeafletAdapter<ViewT extends View, PageT extends Page> extends PagerAdapter {

    private Kryo kryo = new Kryo();
    private ViewPager pager;
    private PageRenderer<ViewT, PageT> pageRenderer;
    private PageRenderer<ViewT, PageT> defaultPageRenderer;
    private List<PageHolder<PageT>> pages = new ArrayList<>();
    private SparseArray<ViewT> mapping = new SparseArray<>();
    private NoPageRenderer noPageRenderer;
    private ViewGroup emptyHolder;

    ConcreteLeafletAdapter(
            ViewPager pager,
            ViewGroup emptyHolder) {
        this.pager = pager;
        this.emptyHolder = emptyHolder;
        this.kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
    }

    void setDefaultPageRenderer(PageRenderer<ViewT, PageT> defaultPageRenderer) {
        this.defaultPageRenderer = defaultPageRenderer;
    }

    void setNoPageRenderer(NoPageRenderer noPageRenderer) {
        this.noPageRenderer = noPageRenderer;
    }

    void setPageRenderer(PageRenderer<ViewT, PageT> pageRenderer) {
        this.pageRenderer = pageRenderer;
        this.mapping.clear();
        notifyDataSetChanged();
    }

    void setPages(List<PageT> pages) {
        PageT oldCurrent = getCurrentPage();
        this.pages = this.kryo.copy(PageHolder.wrap(pages));
        notifyDataSetChanged();
        if (oldCurrent != null && this.pages.contains(new PageHolder<>(oldCurrent))) {
            goTo(oldCurrent);
        } else {
            goTo(getCurrentPage());
        }
    }

    PageT getCurrentPage() {
        return pages.isEmpty() ? null : pages.get(Math.min(pager.getCurrentItem(), pages.size() - 1)).getPage();
    }

    void goTo(PageT page) {
        if (page == null) {
            pager.setVisibility(View.GONE);
            emptyHolder.setVisibility(View.VISIBLE);
            emptyHolder.removeAllViews();
            emptyHolder.addView(noPageRenderer.renderNoPage());
            noPageRenderer.enter();
        } else {
            int desiredIndex = pages.indexOf(new PageHolder<>(page));
            if (desiredIndex != pager.getCurrentItem()) {
                pager.setCurrentItem(desiredIndex, true);
            }
            pager.setVisibility(View.VISIBLE);
            emptyHolder.setVisibility(View.GONE);

            for(int i = 0; i < mapping.size(); i++) {
                pageRenderer.leave(mapping.get(i));
            }
            ViewT currentView = mapping.get(desiredIndex);
            if (currentView != null) {
                pageRenderer.enter(currentView);
            }
        }
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Nullable
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        PageT page = pages.get(position).getPage();
        ViewT view = mapping.get(position);

        if (view == null) {
            PageRenderer<ViewT, PageT> renderer = pageRenderer == null ? defaultPageRenderer : pageRenderer;
            view = renderer.renderPage(page);
            mapping.put(position, view);
            if (position == pager.getCurrentItem()) {
                pageRenderer.enter(view);
            }
            collection.addView(view);
        }

        view.setId(position);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
        mapping.remove(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pages.get(position).toString();
    }

    @Override
    public int getItemPosition(Object object) {
        int pos = pages.indexOf(object);
        return pos == -1 ? POSITION_NONE : pos;
    }
}
