package com.gurunars.leaflet_view;

import android.os.Bundle;
import android.os.Parcelable;
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
import com.gurunars.android_utils.functional.Finder;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

class LeafletAdapter<ViewT extends View, PageT extends Page> extends PagerAdapter {

    private Kryo kryo = new Kryo();
    private ViewPager pager;
    private PageRenderer<ViewT, PageT> pageRenderer;

    private List<PageT> pages = new ArrayList<>();
    private List<PageT> previousPages = new ArrayList<>();

    private SparseArray<ViewT> mapping = new SparseArray<>();
    private NoPageRenderer noPageRenderer;
    private ViewGroup emptyHolder;
    private HashMap<Long, SparseArray<Parcelable>> childState = new HashMap<>();

    private Finder.Equator<Page> pageEquator = new Finder.Equator<Page>() {
        @Override
        public boolean equal(Page one, Page two) {
            if (one == two) {
                return true;
            } else if (one == null || two == null) {
                return false;
            } else {
                return one.getClass().equals(two.getClass()) && one.getId() == two.getId();
            }
        }
    };

    LeafletAdapter(
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
        this.previousPages = this.pages;
        this.pages = this.kryo.copy(pages);
        notifyDataSetChanged();
        if (oldCurrent != null && Finder.contains(pages, oldCurrent, pageEquator)) {
            goTo(oldCurrent);
        } else {
            goTo(getCurrentPage());
        }
    }

    PageT getCurrentPage() {
        return pages.isEmpty() ? null : pages.get(Math.min(pager.getCurrentItem(), pages.size() - 1));
    }

    void goTo(PageT page) {
        if (page == null) {
            pager.setVisibility(View.GONE);
            emptyHolder.setVisibility(View.VISIBLE);
            emptyHolder.removeAllViews();
            emptyHolder.addView(noPageRenderer.renderNoPage());
            noPageRenderer.enter();
        } else {
            int desiredIndex = Finder.indexOf(pages, page, pageEquator);
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
        PageT page = pages.get(position);
        ViewT view = mapping.get(position);
        View actual = view;

        if (actual == null) {
            if (pageRenderer != null){
                view = pageRenderer.renderPage(page);
                mapping.put(position, view);
                if (position == pager.getCurrentItem()) {
                    enter(pageRenderer, view);
                }
            }

            actual = view == null ? renderDefault(collection, page) : view;
            collection.addView(actual);
        }

        loadItemState(position);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, Object view) {
        //It would be nice to have an infinite state storage - but it would lead to state size
        //explosion
        //saveItemState(position);
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
        int pos = -1;
        if (object instanceof Page) {
            pos = Finder.indexOf(pages, (Page) object, pageEquator);
        }
        if (pos == -1) {
            return POSITION_NONE;
        }
        int prevPost = Finder.indexOf(previousPages, (Page) object, pageEquator);
        return pos == prevPost ? POSITION_UNCHANGED : pos;
    }

    @Override
    public Parcelable saveState() {
        // We want to store state for the views currently kept in RAM
        for (int i=0; i<mapping.size(); i++) {
            saveItemState(mapping.keyAt(i));
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("state", childState);
        return bundle;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (state == null) {
            return;
        }
        Bundle bundle = (Bundle) state;
        HashMap<Long, SparseArray<Parcelable>> tmp = (HashMap<Long, SparseArray<Parcelable>>)
                bundle.getSerializable("state");
        if (tmp != null) {
            childState = tmp;
        }
    }

    private void saveItemState(int position) {
        SparseArray<Parcelable> viewState = new SparseArray<>();
        View view = mapping.get(position);
        view.saveHierarchyState(viewState);

        if (pages.isEmpty()) {
            childState.put(0L, viewState);
        } else {
            childState.put(pages.get(position).getId(), viewState);
        }

    }

    private void loadItemState(int position) {
        SparseArray<Parcelable> viewState;

        if (pages.isEmpty()) {
            viewState = childState.get(0L);
        } else {
            viewState = childState.get(pages.get(position).getId());
        }

        if (viewState == null) {
            return;
        }
        View view = mapping.get(position);
        view.restoreHierarchyState(viewState);
    }
}
