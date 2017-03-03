package com.gurunars.leaflet_view;

public interface PageTransitionObservable {

    void addOnEnterListener(Runnable runnable);
    void addOnLeaveListener(Runnable runnable);

}
