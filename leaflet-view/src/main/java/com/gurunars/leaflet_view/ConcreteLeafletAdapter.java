package com.gurunars.leaflet_view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esotericsoftware.kryo.Kryo;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

class ConcreteLeafletAdapter<ViewT extends View, PageT extends Page> extends PagerAdapter {

    private Kryo kryo = new Kryo();
    private ViewPager pager;
    private PageRenderer<ViewT, PageT> pageRenderer;
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
                leave(pageRenderer, mapping.get(i));
            }
            enter(pageRenderer, mapping.get(desiredIndex));
        }
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    private void enter(PageRenderer<ViewT, PageT> renderer, ViewT pageView) {
        if (renderer != null && pageView != null) {
            renderer.enter(pageView);
        }
    }

    private void leave(PageRenderer<ViewT, PageT> renderer, ViewT pageView) {
        if (renderer != null && pageView != null) {
            renderer.leave(pageView);
        }
    }

    private View renderDefault(ViewGroup collection, PageT page) {
        LayoutInflater inflater = LayoutInflater.from(collection.getContext());
        View viewGroup = inflater.inflate(R.layout.default_page_view, collection, false);
        TextView textView = ButterKnife.findById(viewGroup, R.id.pageTitle);
        textView.setText(page.toString());
        return viewGroup;
    }

    @Nullable
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        PageT page = pages.get(position).getPage();
        ViewT view = mapping.get(position);
        View actual = view;

        if (actual == null) {
            if (pageRenderer == null) {
                actual = renderDefault(collection, page);
            } else {
                view = pageRenderer.renderPage(page);
                actual = view;
                mapping.put(position, view);
                if (position == pager.getCurrentItem()) {
                    enter(pageRenderer, view);
                }
            }

            collection.addView(actual);
        }

        actual.setId(position);
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
