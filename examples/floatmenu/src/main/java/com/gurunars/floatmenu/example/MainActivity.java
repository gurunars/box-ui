package com.gurunars.floatmenu.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gurunars.floatmenu.AnimationListener;
import com.gurunars.floatmenu.FloatMenu;

import butterknife.ButterKnife;

import static android.view.View.inflate;


public class MainActivity extends AppCompatActivity {

    private FloatMenu floatingMenu;
    private TextView status;
    private boolean flag=true;

    private void toggleButton() {
        if (floatingMenu.isOpen()) {
            floatingMenu.close();
        } else {
            floatingMenu.open();
        }
    }

    private void toggleButtonColor() {
        int color, iconColor;

        if (!flag) {
            color = R.color.DarkRed;
            iconColor = R.color.White;
            flag = true;
        } else {
            color = R.color.RosyBrown;
            iconColor = R.color.Black;
            flag = false;
        }

        floatingMenu.setOpenIconBgColor(ContextCompat.getColor(this, color));
        floatingMenu.setOpenIconFgColor(ContextCompat.getColor(this, iconColor));
    }

    private void toggleBackground() {
        floatingMenu.setHasOverlay(!floatingMenu.hasOverlay());
    }

    private void bind(int viewId, final String value) {
        findViewById(viewId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(value)
                        .setPositiveButton(android.R.string.yes, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void toggleHand() {
        floatingMenu.setLeftHanded(!floatingMenu.isLeftHanded());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (savedInstanceState != null) {
            flag = savedInstanceState.getBoolean("flag", flag);
        }

        floatingMenu = ButterKnife.findById(this, R.id.floatingMenu);

        floatingMenu.setContentView(inflate(this, R.layout.content_view, null));
        floatingMenu.setMenuView(inflate(this, R.layout.menu_view, null));

        floatingMenu.setHasOverlay(true);
        status = ButterKnife.findById(this, R.id.status);

        floatingMenu.setOnCloseListener(new AnimationListener() {
            @Override
            public void onStart(int projectedDuration) {
                status.setText(R.string.menuClosed);
            }

            @Override
            public void onFinish() {

            }

        });

        floatingMenu.setOnOpenListener(new AnimationListener() {
            @Override
            public void onStart(int projectedDuration) {
                status.setText(R.string.menuOpen);
            }

            @Override
            public void onFinish() {

            }

        });

        bind(R.id.textView, "Text Clicked");
        bind(R.id.button, "Button Clicked");
        bind(R.id.buttonFrame, "Button Frame Clicked");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toggleBackground:
                toggleBackground();
                return true;
            case R.id.toggleButtonColor:
                toggleButtonColor();
                return true;
            case R.id.toggleMenu:
                toggleButton();
                return true;
            case R.id.toggleHand:
                toggleHand();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putBoolean("flag", flag);
        super.onSaveInstanceState(bundle);
    }

}