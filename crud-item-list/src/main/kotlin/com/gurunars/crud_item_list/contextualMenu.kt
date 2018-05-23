package com.gurunars.crud_item_list

import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import com.gurunars.android_utils.iconView
import com.gurunars.box.IBox
import com.gurunars.box.box
import com.gurunars.box.merge
import com.gurunars.box.ui.*
import com.gurunars.item_list.Item

internal fun <ItemType : Item> Context.contextualMenu(
    openForm: (item: ItemType) -> Unit,
    sortable: Boolean,
    actionIcon: IconColorBundle,
    items: IBox<List<ItemType>>,
    selectedItems: IBox<Set<ItemType>>,
    serializer: ClipboardSerializer<ItemType>?
) = with<RelativeLayout> {
    fullSize()
    id = R.id.contextualMenu

    fun configureIcon(
        icon: Int,
        id: Int,
        action: Action<ItemType>,
        init: View.() -> Unit
    ): IBox<Boolean> {
        val enabled = true.box
        val iconView = iconView(actionIcon.icon(icon).box, enabled)
        iconView.layout(this@with)
        iconView.init()
        iconView.apply {
            this.id = id
            (layoutParams as RelativeLayout.LayoutParams).apply {
                width = dip(45)
                height = dip(45)
            }
            @Suppress("UNCHECKED_CAST")
            merge(items, selectedItems).onChange {
                action.canPerform(it.first, it.second, { enabled.set(it) })
            }

            onClick {
                action.perform(items.get(), selectedItems.get(), { first, second ->
                    items.set(first)
                    selectedItems.set(second)
                })
            }
        }
        return enabled
    }

    configureIcon(
        R.drawable.ic_move_up,
        R.id.moveUp,
        ActionMoveUp()
    ) {
        setIsVisible(sortable)

        layout(this@with) {
            alignInParent(HorizontalAlignment.RIGHT)
            alignWithRespectTo(
                R.id.moveDown,
                verticalPosition = VerticalPosition.ABOVE
            )
            margin=Bounds(
                bottom=dip(5),
                left=dip(23),
                right=dip(23)
            )
        }
    }

    configureIcon(
        R.drawable.ic_move_down,
        R.id.moveDown,
        ActionMoveDown()
    ) {
        setIsVisible(sortable)

        layout(this@with) {
            alignInParent(
                horizontalAlignment=HorizontalAlignment.RIGHT,
                verticalAlignment = VerticalAlignment.BOTTOM
            )
            margin=Bounds(
                top=dip(5),
                bottom=dip(85),
                left=dip(23),
                right=dip(23)
            )
        }
    }

    configureIcon(
        R.drawable.ic_edit,
        R.id.edit,
        ActionEdit(openForm)
    ) {
        layout(this@with) {
            alignInParent(
                verticalAlignment = VerticalAlignment.BOTTOM
            )
            alignWithRespectTo(R.id.delete, HorizontalPosition.LEFT_OF)
            margin=Bounds(
                bottom=dip(23),
                left=dip(5),
                right=dip(5)
            )
        }
    }

    configureIcon(
        R.drawable.ic_delete,
        R.id.delete,
        ActionDelete()
    ) {
        layout(this@with) {
            alignInParent(
                verticalAlignment = VerticalAlignment.BOTTOM
            )
            alignWithRespectTo(R.id.selectAll, HorizontalPosition.LEFT_OF)
            margin=Bounds(
                bottom=dip(23),
                left=dip(5),
                right=dip(5)
            )
        }
    }

    configureIcon(
        R.drawable.ic_select_all,
        R.id.selectAll,
        ActionSelectAll()
    ) {
        layout(this@with) {
            alignInParent(
                horizontalAlignment = HorizontalAlignment.RIGHT,
                verticalAlignment = VerticalAlignment.BOTTOM
            )
            margin=Bounds(
                bottom=dip(23),
                left=dip(5),
                right=dip(85)
            )
        }
    }

    if (serializer == null) {
        return@with
    }

    configureIcon(
        R.drawable.ic_copy,
        R.id.copy,
        ActionCopyToClipboard(context, serializer)
    ) {
        layout(this@with) {
            alignInParent(
                horizontalAlignment = HorizontalAlignment.RIGHT,
                verticalAlignment = VerticalAlignment.BOTTOM
            )
            margin=Bounds(
                bottom=dip(75),
                right=dip(75)
            )
        }
    }

    val pasteAction = ActionPasteFromClipboard(context, serializer)
    val canPaste = configureIcon(
        R.drawable.ic_paste,
        R.id.paste,
        pasteAction
    ) {
        layout(this@with) {
            alignWithRespectTo(R.id.copy, HorizontalPosition.LEFT_OF, VerticalPosition.ABOVE)
            margin=Bounds(
                bottom=dip(-7),
                right=dip(-7)
            )
        }
    }

    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    val onClipChange = {
        pasteAction.canPerform(items.get(), selectedItems.get(), { canPaste.set(it) })
    }

    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewDetachedFromWindow(v: View?) {
            clipboard.removePrimaryClipChangedListener(onClipChange)
        }

        override fun onViewAttachedToWindow(v: View?) {
            clipboard.addPrimaryClipChangedListener(onClipChange)
        }
    })
}
