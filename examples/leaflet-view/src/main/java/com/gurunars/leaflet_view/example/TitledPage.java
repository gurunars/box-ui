package com.gurunars.leaflet_view.example;

import com.gurunars.leaflet_view.Page;

class TitledPage implements Page {

    private long id;
    private String title;

    TitledPage(String title) {
        this.id = System.nanoTime();
        this.title = title;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return title;
    }

}
