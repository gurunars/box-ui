package com.gurunars.item_list;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class ItemViewBinderString<ItemType extends Item> implements ItemViewBinder<TextView, ItemType> {

    @Override
    public TextView getView(Context context) {
        TextView view = new TextView(context);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return view;
    }

    private void animateUpdate(final View view) {
        view.clearAnimation();
        ValueAnimator anim = new ValueAnimator();
        anim.setFloatValues((float) 1.0, (float) 0.0, (float) 1.0);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        anim.setDuration(1300);
        anim.start();
    }

    @Override
    public void bind(TextView itemView, ItemType item, ItemType previousItem) {
        itemView.setText(item.toString());
        if (previousItem != null) {
            animateUpdate(itemView);
        }
    }

}
