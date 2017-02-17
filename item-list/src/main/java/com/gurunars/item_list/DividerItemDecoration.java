package com.gurunars.item_list;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;


class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    DividerItemDecoration(Context context, Drawable divider) {
        mDivider = divider == null ? ContextCompat.getDrawable(context, R.drawable.divider) : divider;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            // Skip the footer and empty view

            if (child.getTag(ItemViewBinderFooter.FOOTER_TYPE) != null ||
                child.getTag(ItemViewBinderEmpty.EMPTY_TYPE) != null) {
                continue;
            }

            int top = child.getBottom() +
                    ((RecyclerView.LayoutParams) child.getLayoutParams()).bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
    }
}