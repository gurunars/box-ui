package com.gurunars.leaflet_view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class PageHolder<PageT extends Page> implements Serializable {

    private PageT page;

    PageHolder(PageT page) {
        this.page = page;
    }

    @Override
    public final int hashCode() {
        return Long.valueOf(page.getId()).hashCode();
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof PageHolder)) {
            return false;
        }
        PageHolder other = (PageHolder) o;

        return page.getId() == other.page.getId();
    }

    PageT getPage() {
        return page;
    }

    static <PageT extends Page> ArrayList<PageHolder<PageT>> wrap(List<PageT> pages) {
        ArrayList<PageHolder<PageT>> wrapped = new ArrayList<>();
        for (PageT page : pages) {
            wrapped.add(new PageHolder<>(page));
        }
        return wrapped;
    }

}
