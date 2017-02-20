package com.gurunars.floatmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

/**
 * Floating menu available via a <a href="https://material.google.com/components/buttons-floating-action-button.html">FAB</a>.
 */
public class FloatMenu extends FrameLayout {

    private final Fab openFab;
    private final MenuPane menuPane;
    private final ViewGroup contentPane;

    @State boolean isLeftHanded;

    private AnimationListener onCloseListener, onOpenListener;

    private final static int DURATION_IN_MILLIS = 400;

    public FloatMenu(Context context) {
        this(context, null);
    }

    public FloatMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.float_menu, this);

        onCloseListener = onOpenListener = new AnimationListener() {
            @Override
            public void onStart(int projectedDuration) {}

            @Override
            public void onFinish() {}
        };

        contentPane = ButterKnife.findById(this, R.id.contentPane);
        menuPane = ButterKnife.findById(this, R.id.menuPane);
        openFab = ButterKnife.findById(this, R.id.openFab);

        openFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setFloatingMenuVisibility(!openFab.isActivated());
            }
        });

        setAnimationDuration(DURATION_IN_MILLIS);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatMenu);

        setHasOverlay(a.getBoolean(
                R.styleable.FloatMenu_fabHasOverlay,
                true));

        int openIconBgColor = a.getColor(
                R.styleable.FloatMenu_fabOpenIconBgColor,
                Color.RED);
        int openIconFgColor = a.getColor(
                R.styleable.FloatMenu_fabOpenIconFgColor,
                Color.WHITE);
        int closeIconBgColor = a.getColor(
                R.styleable.FloatMenu_fabCloseIconBgColor,
                openIconBgColor);
        int closeIconFgColor = a.getColor(
                R.styleable.FloatMenu_fabCloseIconFgColor,
                openIconFgColor);

        setOpenIcon(a.getResourceId(
                R.styleable.FloatMenu_fabOpenIcon,
                R.drawable.ic_menu));
        setCloseIcon(a.getResourceId(
                R.styleable.FloatMenu_fabCloseIcon,
                R.drawable.ic_menu_close));

        setOpenIconBgColor(openIconBgColor);
        setOpenIconFgColor(openIconFgColor);
        setCloseIconBgColor(closeIconBgColor);
        setCloseIconFgColor(closeIconFgColor);

        setLeftHanded(a.getBoolean(R.styleable.FloatMenu_fabLeftHanded, false));

        a.recycle();

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
        setLeftHanded(isLeftHanded);
    }

    private void setFloatingMenuVisibility(boolean visible) {

        if (isOpen() == visible) {
            return;
        }

        openFab.setClickable(false);
        openFab.setActivated(visible);
        menuPane.setActivated(visible);

        final AnimationListener listener = visible ? onOpenListener : onCloseListener;
        final int targetVisibility = visible ? VISIBLE : GONE;
        final float sourceAlpha = visible ? 0.0f : 1.0f;
        final float targetAlpha = visible ? 1.0f : 0.0f;

        listener.onStart(openFab.rotationDuration);
        menuPane.setVisibility(VISIBLE);
        menuPane.setAlpha(sourceAlpha);
        menuPane.animate()
                .alpha(targetAlpha)
                .setDuration(openFab.rotationDuration)
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        menuPane.setVisibility(targetVisibility);
                        listener.onFinish();
                        openFab.setClickable(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        onAnimationEnd(animation);
                    }
                });
    }

    /**
     * @return true if the menu is opened.
     */
    public boolean isOpen() {
        return menuPane.getVisibility() == VISIBLE;
    }

    /*
     * Collapse the menu.
     */
    public void close() {
        setFloatingMenuVisibility(false);
    }

    /**
     * Expand the menu.
     */
    public void open() {
        setFloatingMenuVisibility(true);
    }

    /**
     * @param contentView view to be shown in the content area (clickable when the menu is closed)
     */
    public void setContentView(View contentView) {
        contentPane.removeAllViews();
        contentPane.addView(contentView);
    }

    /**
     * @param onCloseListener actions to be triggered before and after the menu is closed
     */
    public void setOnCloseListener(AnimationListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    /**
     * @param onOpenListener actions to be triggered before and after the menu is open
     */
    public void setOnOpenListener(AnimationListener onOpenListener) {
        this.onOpenListener = onOpenListener;
    }

    /**
     * @param menuView view to be shown in the menu area (clickable when the menu is open)
     */
    public void setMenuView(View menuView) {
        menuPane.removeAllViews();
        menuPane.addView(menuView);
    }

    /**
     * @param hasOverlay false to disable a shaded background, true to enable it. If overlay is
     * disabled the clicks go through the view group to the view in the back. If it is enabled the
     * clicks are intercepted by the group.
     */
    public void setHasOverlay(boolean hasOverlay) {
        menuPane.setClickable(hasOverlay);
    }

    /**
     * @return true if the pane has a shaded background, false otherwise
     */
    public boolean hasOverlay() {
        return menuPane.isClickable();
    }

    /**
     * @param leftHanded if true - FAB shall be in the bottom left corner, if false - in the bottom right.
     */
    public void setLeftHanded(boolean leftHanded) {
        setContentDescription("LH:" + leftHanded);
        this.isLeftHanded = leftHanded;
        RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) openFab.getLayoutParams();
        if (layout == null) {
            return;
        }
        layout.removeRule(isLeftHanded ?
                RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT);
        layout.addRule(isLeftHanded ?
                RelativeLayout.ALIGN_PARENT_LEFT : RelativeLayout.ALIGN_PARENT_RIGHT);
        openFab.setLayoutParams(layout);
    }

    /**
     * @return true/false depending if the button is in left handed mode or not.
     */
    public boolean isLeftHanded() {
        return isLeftHanded;
    }

    /**
     * @param durationInMillis FAB rotation and menu appearence duration in milliseconds.
     */
    public void setAnimationDuration(int durationInMillis) {
        openFab.setRotationDuration(durationInMillis);
    }

    /**
     * @param icon - image to be shown in the button clicking which opens the menu.
     */
    public void setOpenIcon(int icon) {
        openFab.setOpenIcon(icon);
    }

    /**
     * @param bgColor - Background color of the button clicking which opens the menu.
     */
    public void setOpenIconBgColor(int bgColor) {
        openFab.setOpenIconBgColor(bgColor);
    }

    /**
     * @param fgColor - Color of the icon shown in the button clicking which opens the menu.
     */
    public void setOpenIconFgColor(int fgColor) {
        openFab.setOpenIconFgColor(fgColor);
    }

    /**
     * @param icon - image to be shown in the button clicking which closes the menu.
     */
    public void setCloseIcon(int icon) {
        openFab.setCloseIcon(icon);
    }

    /**
     * @param bgColor - Background color of the button clicking which closes the menu.
     */
    public void setCloseIconBgColor(int bgColor) {
        openFab.setCloseIconBgColor(bgColor);
    }

    /**
     * @param fgColor - Color of the icon shown in the button clicking which closes the menu.
     */
    public void setCloseIconFgColor(int fgColor) {
        openFab.setCloseIconFgColor(fgColor);
    }

}

