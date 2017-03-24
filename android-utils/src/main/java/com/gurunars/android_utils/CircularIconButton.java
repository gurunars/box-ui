package com.gurunars.android_utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


public class CircularIconButton extends ImageButton {

    private final static int ICON_PADDING = 50;

    private int backgroundColor;
    private int foregroundColor;
    private int innerDrawable;

    public CircularIconButton(Context context) {
        this(context, null);
    }

    public CircularIconButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularIconButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setClickable(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularIconButton);

        backgroundColor = a.getColor(
                R.styleable.CircularIconButton_backgroundColor,
                ContextCompat.getColor(context, R.color.White));
        foregroundColor = a.getColor(
                R.styleable.CircularIconButton_foregroundColor,
                ContextCompat.getColor(context, R.color.Black));
        innerDrawable = a.getResourceId(
                R.styleable.CircularIconButton_innerDrawable,
                R.drawable.ic_plus);

        a.recycle();

        reset();
    }

    private void reset() {
        setBackground(new ColoredShapeDrawable(new OvalShape(), backgroundColor));
        AutoBg.apply(this, 4);

        setScaleType(ImageView.ScaleType.CENTER_CROP);
        setPadding(8, 8, 8, 8);

        Drawable fg = ContextCompat.getDrawable(getContext(), innerDrawable);
        fg.setColorFilter(foregroundColor, PorterDuff.Mode.SRC_IN);
        fg.setAlpha(isEnabled() ? 255 : 50);
        setImageDrawable(new InsetDrawable(fg, ICON_PADDING));
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        reset();
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
        reset();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        reset();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt("backgroundColor", backgroundColor);
        bundle.putInt("foregroundColor", foregroundColor);
        bundle.putInt("innerDrawable", innerDrawable);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle localState = (Bundle) state;
        super.onRestoreInstanceState(localState.getParcelable("superState"));
        backgroundColor = localState.getInt("backgroundColor");
        foregroundColor = localState.getInt("foregroundColor");
        innerDrawable = localState.getInt("innerDrawable");
        reset();
    }
}
