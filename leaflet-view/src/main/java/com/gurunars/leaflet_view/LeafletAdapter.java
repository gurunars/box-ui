package com.gurunars.leaflet_view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.esotericsoftware.kryo.Kryo;
import com.gurunars.android_utils.functional.Finder;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class LeafletAdapter extends PagerAdapter {

    private Kryo kryo = new Kryo();

    private ViewPager pager;
    private Context context;

    private List<? extends Page> pages = new ArrayList<>();
    private List<? extends Page> previousPages = new ArrayList<>();

    // TODO: think about having a limited size for state retention
    private HashMap<Long, SparseArray<Parcelable>> childState = new HashMap<>();

    private SparseArray<View> mapping = new SparseArray<>();
    private SparseArray<ConcretePageTransitionObservable> transitionMapping = new SparseArray<>();

    private PageTransitionListener externalListener = new PageTransitionListener.Default();

    private NoPage noPage = new NoPage.Default();

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

    LeafletAdapter(ViewPager pager) {
        this.pager = pager;
        this.context = pager.getContext();
        this.kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
    }

    void setNoPage(NoPage noPage) {
        this.noPage = noPage;
        notifyDataSetChanged();
    }

    void setPages(List<? extends Page> pages) {
        Page oldCurrent = getCurrentPage();

        this.previousPages = this.pages;
        this.pages = this.kryo.copy(pages);

        notifyDataSetChanged();

        goTo(Finder.contains(this.pages, oldCurrent, pageEquator) ? oldCurrent : getCurrentPage());
    }

    Page getCurrentPage() {
        return pages.isEmpty() ? null : pages.get(Math.min(pager.getCurrentItem(), pages.size() - 1));
    }

    private ConcretePageTransitionObservable getTransitionObservable(int position) {
        ConcretePageTransitionObservable observable = transitionMapping.get(position);
        if (observable == null) {
            observable = new ConcretePageTransitionObservable();
            transitionMapping.put(position, observable);
        }
        return observable;
    }

    void goTo(Page page) {
        for (int i = 0; i < transitionMapping.size(); i++){
            transitionMapping.valueAt(i).leave();
        }
        for (Page cursor: pages) {
            externalListener.onLeave(cursor);
        }

        if (pages.isEmpty()) {
            pager.setCurrentItem(0, true);
            return;
        }

        int desiredIndex = Finder.indexOf(pages, page, pageEquator);
        if (desiredIndex == -1) {
            return;
        }
        if (desiredIndex != pager.getCurrentItem()) {
            pager.setCurrentItem(desiredIndex, true);
        }
        getTransitionObservable(desiredIndex).enter();
        externalListener.onEnter(page);
    }

    @Override
    public int getCount() {
        return Math.max(pages.size(), 1);
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

    private View renderPage(ViewGroup collection, final int position, Renderer renderer,
                            Runnable pageEnterListener) {
        View view = mapping.get(position);

        if (view == null) {
            ConcretePageTransitionObservable observable = getTransitionObservable(position);
            observable.addOnEnterListener(pageEnterListener);
            view = renderer.render(context, observable);
            mapping.put(position, view);
            if (position == pager.getCurrentItem()) {
                observable.enter();
            }
            collection.addView(view);
        }

        loadItemState(position);
        return view;
    }

    @Nullable
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {

        if (pages.isEmpty()) {
            return renderPage(collection, position, noPage, new Runnable() {
                @Override
                public void run() {

                }
            });
        } else {
            final Page page = pages.get(position);
            return renderPage(collection, position, page, new Runnable() {
                @Override
                public void run() {
                    externalListener.onEnter(page);
                }
            });
        }

    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, Object view) {
        // Before removing the item from RAM we want to restore its state.
        saveItemState(position);
        collection.removeView((View) view);
        mapping.remove(position);
        transitionMapping.remove(position);
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

    void setExternalListener(PageTransitionListener externalListener) {
        this.externalListener = externalListener;
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
}
