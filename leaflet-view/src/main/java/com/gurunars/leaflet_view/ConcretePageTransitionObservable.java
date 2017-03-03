package com.gurunars.leaflet_view;

import java.util.ArrayList;
import java.util.List;

class ConcretePageTransitionObservable implements PageTransitionObservable {

    private List<Runnable> onEnterObservers = new ArrayList<>();
    private List<Runnable> onLeaveObservers = new ArrayList<>();

    @Override
    public void addOnEnterListener(Runnable runnable) {
        onEnterObservers.add(runnable);
    }

    @Override
    public void addOnLeaveListener(Runnable runnable) {
        onLeaveObservers.add(runnable);
    }

    void enter(){
        for (Runnable runnable: onEnterObservers) {
            runnable.run();
        }
    }

    void leave() {
        for (Runnable runnable: onLeaveObservers) {
            runnable.run();
        }
    }
}
