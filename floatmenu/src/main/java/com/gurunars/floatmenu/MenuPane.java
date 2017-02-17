package com.gurunars.floatmenu;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


class MenuPane extends FrameLayout {

    public MenuPane(Context context) {
        this(context, null);
    }

    public MenuPane(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuPane(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHasOverlay(boolean hasOverlay) {
        setClickable(hasOverlay);
        setBackgroundColor(hasOverlay ? Color.parseColor("#99000000") : Color.TRANSPARENT);
    }

    private boolean isWithinBounds(View view, MotionEvent ev) {
        int xPoint = Math.round(ev.getRawX());
        int yPoint = Math.round(ev.getRawY());
        int[] l = new int[2];
        view.getLocationOnScreen(l);
        int x = l[0];
        int y = l[1];
        int w = view.getWidth();
        int h = view.getHeight();
        return xPoint >= x && xPoint <= x + w && yPoint >= y && yPoint <= y + h;
    }

    private boolean touchBelongsTo(ViewGroup viewGroup, MotionEvent ev) {
        for (int i=0; i<viewGroup.getChildCount(); i++){
            View child = viewGroup.getChildAt(i);
            if (child == null) {
                continue;
            }
            if (child instanceof ViewGroup) {
                // check for all children first
                if (touchBelongsTo((ViewGroup) child, ev)) {
                    return true;
                // only after that check view group itself
                } else if (child.isClickable() && isWithinBounds(child, ev)) {
                    return true;
                }
            } else if (isWithinBounds(child, ev)) {
                return true;
            }
        }
        return false;
    }

    /* Intercept touch on the view group but not on the icons */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !isClickable() && !touchBelongsTo(this, ev);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt("visibility", getVisibility());
        bundle.putBoolean("hasOverlay", isClickable());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle localState = (Bundle) state;
        super.onRestoreInstanceState(localState.getParcelable("superState"));
        //noinspection WrongConstant
        setVisibility(localState.getInt("visibility"));
        setHasOverlay(localState.getBoolean("hasOverlay"));
    }

    public boolean hasOverlay() {
        return isClickable();
    }
}
