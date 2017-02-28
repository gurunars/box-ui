package com.gurunars.floatmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gurunars.android_utils.property.ColorProperty;
import com.gurunars.android_utils.property.ContextualReloadable;
import com.gurunars.android_utils.property.DrawableRefProperty;
import com.gurunars.android_utils.property.Property;
import com.gurunars.android_utils.ui.AutoBg;
import com.gurunars.android_utils.ui.ColoredShapeDrawable;

import icepick.Icepick;

final class Fab extends FrameLayout implements ContextualReloadable {

    private final static int ICON_PADDING = 100;

    public final DrawableRefProperty openIcon =
            new DrawableRefProperty(this, "openIcon", R.drawable.ic_menu);
    public final ColorProperty openIconBgColor =
            new ColorProperty(this, "openIconBgColor", Color.RED);
    public final ColorProperty openIconFgColor =
            new ColorProperty(this, "openIconFgColor", Color.WHITE);

    public final DrawableRefProperty closeIcon
            = new DrawableRefProperty(this, "closeIcon", R.drawable.ic_menu_close);
    public final ColorProperty closeIconBgColor
            = new ColorProperty(this, "closeIconBgColor", Color.RED);
    public final ColorProperty closeIconFgColor
            = new ColorProperty(this, "closeIconFgColor", Color.WHITE);

    public final Property<Integer> rotationDuration
            = new Property<>(this, "rotationDuration", 400);

    private final DrawableRefProperty currentIcon
            = new DrawableRefProperty(this, "currentIcon", R.drawable.ic_menu);
    private final ColorProperty currentBgColor
            = new ColorProperty(this, "currentBgColor", Color.RED);
    private final ColorProperty currentFgColor
            = new ColorProperty(this, "currentFgColor", Color.WHITE);

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
        actualImageView = new ImageView(getContext());
        actualImageView.setLayoutParams(
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        addView(actualImageView);
        loadFromState();
    }

    private void reloadUi() {
        currentIcon.set(isActivated() ? closeIcon.get() : openIcon.get());
        // Bg
        setBackground(new ColoredShapeDrawable(new OvalShape(), currentBgColor.get()));
        AutoBg.apply(this, 6);
        // Icon
        Drawable fg = ResourcesCompat.getDrawable(getResources(), currentIcon.get(), null);
        assert fg != null;
        fg.setColorFilter(currentFgColor.get(), PorterDuff.Mode.SRC_IN);
        actualImageView.setImageDrawable(new InsetDrawable(fg, ICON_PADDING));
        // Content description
        setContentDescription(
            "|BG:"+ currentBgColor +
            "|IC:"+ currentFgColor +
            "|ACT:" + isActivated()
        );

        requestLayout();
    }

    private void loadFromState() {
        currentIcon.set(isActivated() ? closeIcon.get() : openIcon.get());
        currentBgColor.set(isActivated() ? closeIconBgColor.get() : openIconBgColor.get());
        currentFgColor.set(isActivated() ? closeIconFgColor.get() : openIconFgColor.get());
        actualImageView.setRotation(isActivated() ? 360 : 0);
        reloadUi();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", Icepick.saveInstanceState(this, super.onSaveInstanceState()));
        bundle.putBoolean("isActivated", isActivated());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle localState = (Bundle) state;
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, localState.getParcelable("superState")));
        super.setActivated(localState.getBoolean("isActivated"));
        loadFromState();
    }

    @Override
    public void setActivated(final boolean isActive) {
        if (isActivated() == isActive) {
            return;
        }

        final boolean originalState = isActivated();

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
                currentBgColor.set((int) animation.getAnimatedValue());
            }
        });

        ValueAnimator fgColorAnimation = ValueAnimator.ofObject(
                new ArgbEvaluator(),
                isActive ? openIconFgColor : closeIconFgColor,
                isActive ? closeIconFgColor : openIconFgColor);
        fgColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentFgColor.set((int) animation.getAnimatedValue());
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
        animatorSet.setDuration(rotationDuration.get());
        animatorSet.playTogether(rotation, bgColorAnimation, fgColorAnimation, uiUpdateAnimaton);
        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Fab.super.setActivated(isActive);
                        reloadUi();
                    }
                }, rotationDuration.get() / 2);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Fab.super.setActivated(originalState);
                reloadUi();
            }
        });
        animatorSet.start();

    }

    @Override
    public void reload() {

    }
}
