package com.gurunars.android_crud_ui.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gurunars.android_crud_ui.AndroidCrudUi;


public class ActivityMain extends AppCompatActivity {

    private AndroidCrudUi viewCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewCustom = (AndroidCrudUi) findViewById(R.id.customView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // inflater.inflate(R.menu.ActivityMain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*
        int i = item.getItemId();
        if (i == NUMBER) {
            // DO STH
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

}