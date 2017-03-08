package com.gurunars.item_list;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

class ItemViewBinderString<PayloadType extends Payload> implements ItemViewBinder<PayloadType> {

    @Override
    public View getView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.string, null);
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
    public void bind(View itemView, Item<PayloadType> item, Item<PayloadType> previousItem) {
        ((TextView) itemView).setText(item.toString());
        if (previousItem != null) {
            animateUpdate(itemView);
        }
    }

}
