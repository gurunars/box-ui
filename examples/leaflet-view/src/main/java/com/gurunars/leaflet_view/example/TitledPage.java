package com.gurunars.leaflet_view.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gurunars.leaflet_view.Page;
import com.gurunars.leaflet_view.PageTransitionObservable;

import butterknife.ButterKnife;

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
    public View render(Context context, PageTransitionObservable transitionObservable) {
        ViewGroup layout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.page_view, null);
        TextView textView = ButterKnife.findById(layout, R.id.pageTitle);
        textView.setText(title);
        return layout;
    }

    @Override
    public String toString() {
        return title;
    }

}
