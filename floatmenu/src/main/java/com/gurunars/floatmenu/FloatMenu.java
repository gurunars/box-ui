package com.gurunars.floatmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.support.v4.content.res.ResourcesCompat;
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

    private AnimationListener onCloseListener;
    private AnimationListener onOpenListener;

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

        contentPane = ButterKnife.findById(this, R.id.contentPane);

        openFab = ButterKnife.findById(this, R.id.openFab);

        openFab.setRotationDuration(DURATION_IN_MILLIS);

        openFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setFloatingMenuVisibility(!openFab.isActivated());
            }
        });

        menuPane = ButterKnife.findById(this, R.id.menuPane);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatMenu);

        menuPane.setHasOverlay(a.getBoolean(
                R.styleable.FloatMenu_fabHasOverlay,
                true));

        int openIconBgColor = a.getColor(
                R.styleable.FloatMenu_fabOpenIconBgColor,
                ResourcesCompat.getColor(getResources(), R.color.Red, null));
        int openIconFgColor = a.getColor(
                R.styleable.FloatMenu_fabOpenIconFgColor,
                ResourcesCompat.getColor(getResources(), R.color.White, null));
        int closeIconBgColor = a.getColor(
                R.styleable.FloatMenu_fabCloseIconBgColor,
                openIconBgColor);
        int closeIconFgColor = a.getColor(
                R.styleable.FloatMenu_fabCloseIconFgColor,
                openIconFgColor);

        openFab.setOpenIcon(a.getResourceId(
                R.styleable.FloatMenu_fabOpenIcon,
                R.drawable.ic_menu));
        openFab.setCloseIcon(a.getResourceId(
                R.styleable.FloatMenu_fabCloseIcon,
                R.drawable.ic_menu_close));

        openFab.setOpenIconBgColor(openIconBgColor);
        openFab.setOpenIconFgColor(openIconFgColor);
        openFab.setCloseIconBgColor(closeIconBgColor);
        openFab.setCloseIconFgColor(closeIconFgColor);

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

    private void hide() {
        menuPane.setVisibility(GONE);
        if (onCloseListener != null) {
            onCloseListener.onFinish();
        }
        openFab.setClickable(true);
    }

    private void show() {
        menuPane.setVisibility(VISIBLE);
        if (onOpenListener != null) {
            onOpenListener.onFinish();
        }
        openFab.setClickable(true);
    }

    private void setFloatingMenuVisibility(boolean visible) {
        openFab.setClickable(false);
        openFab.setActivated(visible);
        menuPane.setActivated(visible);

        if (visible) {
            if (onOpenListener != null) {
                onOpenListener.onStart(DURATION_IN_MILLIS);
            }
            menuPane.setVisibility(VISIBLE);
            menuPane.setAlpha(0.0f);
            menuPane.animate()
                    .alpha(1.0f)
                    .setDuration(DURATION_IN_MILLIS)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            show();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            show();
                        }
                    });
        } else {
            if (onCloseListener != null) {
                onCloseListener.onStart(DURATION_IN_MILLIS);
            }
            menuPane.animate()
                    .alpha(0.0f)
                    .setDuration(DURATION_IN_MILLIS)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            hide();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            hide();
                        }
                    });
        }
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
        if (isOpen()) {
            setFloatingMenuVisibility(false);
        }
    }

    /**
     * Expand the menu.
     */
    public void open() {
        if (!isOpen()) {
            setFloatingMenuVisibility(true);
        }
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
        menuPane.setHasOverlay(hasOverlay);
    }

    /**
     * @return true if the pane has a shaded background, false otherwise
     */
    public boolean hasOverlay() {
        return menuPane.hasOverlay();
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

