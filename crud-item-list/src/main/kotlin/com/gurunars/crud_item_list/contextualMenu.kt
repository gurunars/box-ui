package com.gurunars.crud_item_list

import android.content.Context
import android.widget.RelativeLayout
import com.gurunars.android_utils.iconView
import com.gurunars.box.IBox
import com.gurunars.box.box
import com.gurunars.box.ui.*
import com.gurunars.item_list.Item


internal fun <ItemType : Item> Context.contextualMenu(
    sortable: Boolean,
    actionIcon: IconColorBundle,
    items: IBox<List<ItemType>>,
    selectedItems: IBox<Set<ItemType>>,
    serializer: ClipboardSerializer<ItemType>?
) = with<RelativeLayout> {
    fullSize()
    id = R.id.contextualMenu

    fun actionIcon(
        icon: Int,
        id: Int,
        action: Action<ItemType>,
        visible: Boolean = true,
        init: RelativeLayout.LayoutParams.() -> Unit
    ) {
        if (!visible) {
            return
        }

        iconView(
            actionIcon.icon(icon).box,
            action.canPerform(items, selectedItems)
        ).apply {
            this.id = id
            onClick {
                action.perform(items.get(), selectedItems.get()) { first, second ->
                    items.set(first)
                    selectedItems.set(second)
                }
            }
            layout(this@with) {
                width = dip(45)
                height = dip(45)
                init()
            }
        }
    }

    actionIcon(
        R.drawable.ic_move_up,
        R.id.moveUp,
        ActionMoveUp(),
        sortable
    ) {
        alignInParent(HorizontalAlignment.RIGHT)
        alignWithRespectTo(
            R.id.moveDown,
            verticalPosition = VerticalPosition.ABOVE
        )
        margin = Bounds(
            bottom = dip(5),
            left = dip(23),
            right = dip(23)
        )
    }

    actionIcon(
        R.drawable.ic_move_down,
        R.id.moveDown,
        ActionMoveDown(),
        sortable
    ) {
        alignInParent(
            horizontalAlignment = HorizontalAlignment.RIGHT,
            verticalAlignment = VerticalAlignment.BOTTOM
        )
        margin = Bounds(
            top = dip(5),
            bottom = dip(85),
            left = dip(23),
            right = dip(23)
        )
    }

    actionIcon(
        R.drawable.ic_delete,
        R.id.delete,
        ActionDelete()
    ) {
        alignInParent(
            verticalAlignment = VerticalAlignment.BOTTOM
        )
        alignWithRespectTo(R.id.selectAll, HorizontalPosition.LEFT_OF)
        margin = Bounds(
            bottom = dip(23),
            left = dip(5),
            right = dip(5)
        )
    }

    actionIcon(
        R.drawable.ic_select_all,
        R.id.selectAll,
        ActionSelectAll()
    ) {
        alignInParent(
            horizontalAlignment = HorizontalAlignment.RIGHT,
            verticalAlignment = VerticalAlignment.BOTTOM
        )
        margin = Bounds(
            bottom = dip(23),
            left = dip(5),
            right = dip(85)
        )
    }

    actionIcon(
        R.drawable.ic_copy,
        R.id.copy,
        ActionCopyToClipboard(context, serializer),
        serializer != null
    ) {
        alignInParent(
            horizontalAlignment = HorizontalAlignment.RIGHT,
            verticalAlignment = VerticalAlignment.BOTTOM
        )
        margin = Bounds(
            bottom = dip(75),
            right = dip(75)
        )
    }

    actionIcon(
        R.drawable.ic_paste,
        R.id.paste,
        ActionPasteFromClipboard(this, context, serializer),
        serializer != null
    ) {
        alignWithRespectTo(
            R.id.copy,
            HorizontalPosition.LEFT_OF,
            VerticalPosition.ABOVE
        )
        margin = Bounds(
            bottom = dip(-7),
            right = dip(-7)
        )
    }

}
