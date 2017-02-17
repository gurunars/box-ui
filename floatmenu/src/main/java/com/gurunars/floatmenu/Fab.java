package com.gurunars.floatmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gurunars.android_utils.ui.AutoBg;
import com.gurunars.android_utils.ui.ColoredShapeDrawable;

import icepick.Icepick;
import icepick.State;

class Fab extends FrameLayout {

    private final static int ICON_PADDING = 100;

    @State int openIconBgColor, openIconFgColor, closeIconBgColor, closeIconFgColor;
    @State int currentBgColor, currentIcon, currentFgColor;
    @State int closeIcon = R.drawable.ic_menu_close;
    @State int openIcon = R.drawable.ic_menu;
    @State int rotationDuration = 400;
    @State boolean isActive;

    private boolean noAnimation = false;

    private ImageView actualImageView;

    public Fab(Context context) {
        this(context, null);
    }

    public Fab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Fab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        openIconBgColor = closeIconBgColor = ResourcesCompat.getColor(
                getResources(), R.color.Red, null);
        openIconFgColor = closeIconFgColor = ResourcesCompat.getColor(
                getResources(), R.color.White, null);
        actualImageView = new ImageView(getContext());
        actualImageView.setLayoutParams(
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        addView(actualImageView);
        fullInit();
    }

    private void reloadActivationBasedState() {
        setActivated(isActive);
        currentIcon = isActivated() ? closeIcon : openIcon;
        currentBgColor = isActivated() ? closeIconBgColor : openIconBgColor;
        currentFgColor = isActivated() ? closeIconFgColor : openIconFgColor;
    }

    private void reloadUi() {
        // Bg
        setBackground(new ColoredShapeDrawable(new OvalShape(), currentBgColor));
        AutoBg.apply(this, 6);
        // Icon
        Drawable fg = ResourcesCompat.getDrawable(getResources(), currentIcon, null);
        assert fg != null;
        fg.setColorFilter(currentFgColor, PorterDuff.Mode.SRC_IN);
        actualImageView.setImageDrawable(new InsetDrawable(fg, ICON_PADDING));
        // Content description
        setContentDescription(
            "|BG:"+ openIconBgColor +
            "|IC:"+ openIconFgColor +
            "|ACT:" + isActivated()
        );
    }

    private void fullInit() {
        reloadActivationBasedState();
        reloadUi();
        requestLayout();
    }

    public void setRotationDuration(int durationInMillis) {
        this.rotationDuration = durationInMillis;
    }

    public void setOpenIconBgColor(int openIconBgColor) {
        this.openIconBgColor = openIconBgColor;
        fullInit();
    }

    public void setOpenIconFgColor(int openIconFgColor) {
        this.openIconFgColor = openIconFgColor;
        fullInit();
    }

    public void setCloseIconBgColor(int closeIconBgColor) {
        this.closeIconBgColor = closeIconBgColor;
        fullInit();
    }

    public void setCloseIconFgColor(int closeIconFgColor) {
        this.closeIconFgColor = closeIconFgColor;
        fullInit();
    }

    public void setOpenIcon(int icon) {
        this.openIcon = icon;
        fullInit();
    }

    public void setCloseIcon(int icon) {
        this.closeIcon = icon;
        fullInit();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
        noAnimation = true;
        fullInit();
        noAnimation = false;
    }

    @Override
    public void setActivated(final boolean isActive) {
        if (isActivated() == isActive) {
            return;
        }

        final int actualRotationDuration = noAnimation ? 0 : rotationDuration;

        final boolean originalState = this.isActive;
        this.isActive = isActive;

        ObjectAnimator rotation = ObjectAnimator.ofFloat(actualImageView,
                "rotation",
                isActive ? 0 : 360,
                isActive ? 360 : 0);

        ValueAnimator bgColorAnimation = ValueAnimator.ofObject(
                new ArgbEvaluator(),
                isActive ? openIconBgColor : closeIconBgColor,
                isActive ? closeIconBgColor : openIconBgColor);
        bgColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentBgColor = (int) animation.getAnimatedValue();
            }
        });

        ValueAnimator fgColorAnimation = ValueAnimator.ofObject(
                new ArgbEvaluator(),
                isActive ? openIconFgColor : closeIconFgColor,
                isActive ? closeIconFgColor : openIconFgColor);
        fgColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentFgColor = (int) animation.getAnimatedValue();
            }
        });

        ValueAnimator uiUpdateAnimaton = ValueAnimator.ofFloat(0, 1);
        uiUpdateAnimaton.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                reloadUi();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setStartDelay(0);
        animatorSet.setDuration(actualRotationDuration);
        animatorSet.playTogether(rotation, bgColorAnimation, fgColorAnimation, uiUpdateAnimaton);
        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Fab.super.setActivated(isActive);
                        fullInit();
                    }
                }, actualRotationDuration / 2);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Fab.super.setActivated(originalState);
                fullInit();
            }
        });
        animatorSet.start();

    }
}
