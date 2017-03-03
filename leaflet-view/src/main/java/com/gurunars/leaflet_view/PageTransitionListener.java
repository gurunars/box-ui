package com.gurunars.leaflet_view;

public interface PageTransitionListener<T extends Page> {

    void onEnter(T page);
    void onLeave(T page);

    class Default implements PageTransitionListener {

        @Override
        public void onEnter(Page page) {

        }

        @Override
        public void onLeave(Page page) {

        }
    }

}
