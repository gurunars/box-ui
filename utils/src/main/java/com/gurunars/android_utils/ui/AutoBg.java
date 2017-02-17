package com.gurunars.android_utils.ui;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v4.view.ViewCompat;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class AutoBg {

    private static class AutoBgDrawable extends InsetDrawable {

        // The color filter to apply when the button is pressed
        private final ColorFilter pressedFilter = new LightingColorFilter(Color.LTGRAY, 1);
        private Drawable bg;
        private int shadowWidth;

        @Override
        protected boolean onStateChange(int[] states) {
            List<Integer> stateList = new ArrayList<>();
            for (int state : states) {
                stateList.add(state);
            }

            boolean enabled = stateList.contains(android.R.attr.state_enabled);
            boolean pressed = stateList.contains(android.R.attr.state_pressed);

            mutate();
            setAlpha(enabled ? 255 : 50);
            setColorFilter(enabled && pressed ? pressedFilter : null);
            invalidateSelf();

            if (bg instanceof ShapeDrawable && !enabled) {
                ((ShapeDrawable) bg).getPaint().setShadowLayer(shadowWidth, 0, 0,
                        Color.parseColor("#40000000"));
            }

            return super.onStateChange(states);
        }

        @Override
        public boolean isStateful() {
            return true;
        }

        private AutoBgDrawable(Drawable bg, int shadowWidth) {
            super(bg, shadowWidth * 2);
            this.bg = bg;
            this.shadowWidth = shadowWidth;
        }
    }

    private static Drawable transform(Drawable bg) {
        if (bg instanceof ColorDrawable) {
            ShapeDrawable shapeBg = new ShapeDrawable(new RectShape());
            shapeBg.getPaint().setColor(((ColorDrawable) bg).getColor());
            return shapeBg;
        }
        return bg;
    }

    private AutoBg() {}

    /**
     * <p>
     * Configure a view to have an automatic background with an optional shadow behind it.
     * </p>
     *
     * <p>
     * The background becomes shadier if the view gets disabled.
     * </p>
     * <p>
     * The background gets slightly shadier if the view gets clicked.
     * </p>
     *
     * <p>
     * If <b>shadowWidth</b> is greater than 0 and drawable is a shape or color drawable it adds a
     * shadow behind the view. Shadow is not applicable to other view types.
     * </p>
     *
     * <p>
     * If it is a color drawable it also transforms it to a RectShape drawable with a specified
     * color. During the transformation any other attributes of a color drawable are lost.
     * </p>
     *
     * <p>
     * The reason why the actual drawable is is private is to prevent using the drawable a regular
     * way.
     * </p>
     *
     * <p>
     * There is a bug (feature?) in Android that remove the view padding whenever you set the
     * background.
     * </p>
     *
     * @param view the view to apply the background to
     * @param shadowWidth shadow size (can be 0)
     */
    public static void apply(View view, int shadowWidth) {
        Drawable bg = view.getBackground();
        if (bg == null) {
            return;
        }
        bg = transform(bg);
        int top = view.getPaddingTop();
        int left = view.getPaddingLeft();
        int right = view.getPaddingRight();
        int bottom = view.getPaddingBottom();

        if (shadowWidth > 0 && bg instanceof ShapeDrawable) {
            ShapeDrawable shapeBg = (ShapeDrawable) bg;
            shapeBg.getPaint().setShadowLayer(shadowWidth, 0, shadowWidth,
                    Color.parseColor("#68000000"));
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            // In some cases the theming of Android makes views look really ugly with auto shadows
            ViewCompat.setClipBounds(view, new Rect(0, 0, 0, 0));
        } else {
            shadowWidth = 0;
        }

        view.setBackground(new AutoBgDrawable(bg, shadowWidth));
        view.setPadding(left, top, right, bottom);
    }

}
