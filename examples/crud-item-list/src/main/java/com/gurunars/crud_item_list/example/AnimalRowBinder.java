package com.gurunars.crud_item_list.example;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.gurunars.item_list.SelectableItem;

class AnimalRowBinder implements com.gurunars.item_list.ItemViewBinder<TextView, SelectableItem<AnimalItem>> {

    @Override
    public TextView getView(Context context) {
        TextView text = new TextView(context);
        int padding = context.getResources().getDimensionPixelOffset(R.dimen.padding);
        text.setPadding(padding, padding, padding, padding);
        return text;
    }

    @Override
    public void bind(TextView view, SelectableItem<AnimalItem> item,
                     @Nullable SelectableItem<AnimalItem> previousItem) {
        view.setBackgroundColor(ContextCompat.getColor(view.getContext(),
                item.isSelected() ? com.gurunars.crud_item_list.R.color.Red :
                        com.gurunars.crud_item_list.R.color.White));
        view.setText(item.toString());
        view.setContentDescription("I"+item.getId());

        // Animate created and updated items
        if (previousItem == null) {
            animate(view);
        }
    }

    private void animate(final View view) {
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

}
