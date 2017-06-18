package com.gurunars.crud_item_list.example

import android.animation.ValueAnimator
import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView

import com.gurunars.item_list.SelectableItem

internal class AnimalRowBinder : com.gurunars.item_list.ItemViewBinder<TextView, SelectableItem<AnimalItem>> {

    override fun getView(context: Context): TextView {
        val text = TextView(context)
        val padding = context.resources.getDimensionPixelOffset(R.dimen.padding)
        text.setPadding(padding, padding, padding, padding)
        return text
    }

    override fun bind(view: TextView, item: SelectableItem<AnimalItem>,
                      previousItem: SelectableItem<AnimalItem>?) {
        view.setBackgroundColor(ContextCompat.getColor(view.context,
                if (item.isSelected)
                    com.gurunars.crud_item_list.R.color.Red
                else
                    com.gurunars.crud_item_list.R.color.White))
        view.setText(item.toString())
        view.contentDescription = "I" + item.getId()

        // Animate created and updated items
        if (previousItem == null) {
            animate(view)
        }
    }

    private fun animate(view: View) {
        view.clearAnimation()
        val anim = ValueAnimator()
        anim.setFloatValues(1.0.toFloat(), 0.0.toFloat(), 1.0.toFloat())
        anim.addUpdateListener { animation -> view.alpha = animation.animatedValue as Float }
        anim.duration = 1300
        anim.start()
    }

}
