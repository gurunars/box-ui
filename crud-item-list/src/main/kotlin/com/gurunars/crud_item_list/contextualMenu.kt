package com.gurunars.crud_item_list

import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import com.gurunars.android_utils.iconView
import com.gurunars.databinding.IBox
import com.gurunars.databinding.android.HorizontalAlignment
import com.gurunars.databinding.android.HorizontalPosition
import com.gurunars.databinding.android.VerticalAlignment
import com.gurunars.databinding.android.VerticalPosition
import com.gurunars.databinding.android.add
import com.gurunars.databinding.android.alignInParent
import com.gurunars.databinding.android.alignWithRespectTo
import com.gurunars.databinding.android.fullSize
import com.gurunars.box.ui.onClick
import com.gurunars.databinding.android.setIsVisible
import com.gurunars.databinding.box
import com.gurunars.databinding.onChange
import com.gurunars.item_list.Item
import org.jetbrains.anko.above
import org.jetbrains.anko.dip
import org.jetbrains.anko.relativeLayout

internal fun <ItemType : Item> Context.contextualMenu(
    openForm: (item: ItemType) -> Unit,
    sortable: Boolean,
    actionIcon: IconColorBundle,
    items: IBox<List<ItemType>>,
    selectedItems: IBox<Set<ItemType>>,
    serializer: ClipboardSerializer<ItemType>?
) = relativeLayout {
    fullSize()
    id = R.id.contextualMenu

    fun configureIcon(icon: Int, init: View.() -> Unit): IBox<Boolean> {
        val enabled = true.box
        val iconView = iconView(actionIcon.icon(icon).box, enabled)
        iconView.add(this@relativeLayout)
        iconView.init()
        iconView.apply {
            (layoutParams as RelativeLayout.LayoutParams).apply {
                width = dip(45)
                height = dip(45)
            }
            @Suppress("UNCHECKED_CAST")
            val action = getTag(R.id.action) as Action<ItemType>

            listOf(items, selectedItems).onChange {
                action.canPerform(items.get(), selectedItems.get(), { enabled.set(it) })
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

    configureIcon(R.drawable.ic_move_up) {
        id = R.id.moveUp
        setTag(R.id.action, ActionMoveUp<ItemType>())
        setIsVisible(sortable)
        lparams {
            alignInParent(HorizontalAlignment.RIGHT)
            above(R.id.moveDown)
            bottomMargin = dip(5)
            leftMargin = dip(23)
            rightMargin = dip(23)
        }
    }

    configureIcon(R.drawable.ic_move_down) {
        id = R.id.moveDown
        setTag(R.id.action, ActionMoveDown<ItemType>())
        setIsVisible(sortable)
        lparams {
            alignInParent(
                horizontalAlignment = HorizontalAlignment.RIGHT,
                verticalAlignment = VerticalAlignment.BOTTOM
            )
            topMargin = dip(5)
            bottomMargin = dip(85)
            leftMargin = dip(23)
            rightMargin = dip(23)
        }
    }

    configureIcon(R.drawable.ic_edit) {
        id = R.id.edit
        setTag(R.id.action, ActionEdit(openForm))
        lparams {
            alignInParent(verticalAlignment = VerticalAlignment.BOTTOM)
            alignWithRespectTo(R.id.delete, HorizontalPosition.LEFT_OF)
            bottomMargin = dip(23)
            leftMargin = dip(5)
            rightMargin = dip(5)
        }
    }

    configureIcon(R.drawable.ic_delete) {
        id = R.id.delete
        setTag(R.id.action, ActionDelete<ItemType>())
        lparams {
            alignInParent(verticalAlignment = VerticalAlignment.BOTTOM)
            alignWithRespectTo(R.id.selectAll, HorizontalPosition.LEFT_OF)
            bottomMargin = dip(23)
            leftMargin = dip(5)
            rightMargin = dip(5)
        }
    }

    configureIcon(R.drawable.ic_select_all) {
        id = R.id.selectAll
        setTag(R.id.action, ActionSelectAll<ItemType>())
        lparams {
            alignInParent(
                horizontalAlignment = HorizontalAlignment.RIGHT,
                verticalAlignment = VerticalAlignment.BOTTOM
            )
            leftMargin = dip(5)
            rightMargin = dip(85)
            bottomMargin = dip(23)
        }
    }

    if (serializer == null) {
        return@relativeLayout
    }

    configureIcon(R.drawable.ic_copy) {
        id = R.id.copy
        setTag(R.id.action, ActionCopyToClipboard(context, serializer))
        lparams {
            alignInParent(
                horizontalAlignment = HorizontalAlignment.RIGHT,
                verticalAlignment = VerticalAlignment.BOTTOM
            )
            rightMargin = dip(75)
            bottomMargin = dip(75)
        }
    }

    val pasteAction = ActionPasteFromClipboard(context, serializer)
    val canPaste = configureIcon(R.drawable.ic_paste) {
        id = R.id.paste
        setTag(R.id.action, pasteAction)

        lparams {
            rightMargin = dip(-7)
            bottomMargin = dip(-7)
            alignWithRespectTo(R.id.copy, HorizontalPosition.LEFT_OF, VerticalPosition.ABOVE)
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
