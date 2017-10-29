package com.gurunars.crud_item_list

import android.content.Context
import android.widget.RelativeLayout
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.*
import com.gurunars.databinding.onChange
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.floatmenu.MenuComponent
import com.gurunars.item_list.Item
import com.gurunars.item_list.SelectableItemContainer
import org.jetbrains.anko.above
import org.jetbrains.anko.applyRecursively
import org.jetbrains.anko.dip
import org.jetbrains.anko.relativeLayout

class DefaultSelectionManager<ItemType : Item>(
    private val selectableItemContainer: SelectableItemContainer<ItemType>
) : MenuComponent, SelectableItemContainer<ItemType> by selectableItemContainer {

    val actionIcon = BindableField(IconColorBundle())
    val sortable = BindableField(false)

    private val floatMenu = FloatMenu(selectableItemContainer, menu()).apply {
        hasOverlay.set(false)
    }

    override val isOpen = floatMenu.isOpen
    override val isLeftHanded = floatMenu.isLeftHanded
    override val openIcon = floatMenu.openIcon
    override val closeIcon = floatMenu.closeIcon

    override fun Context.render() = floatMenu.render(this)

    fun menu(): Component = component {

        fun RelativeLayout.LayoutParams.isLeftHanded(flag: Boolean) {
            alignInParent(
                if (flag)
                    HorizontalAlignment.LEFT
                else
                    HorizontalAlignment.RIGHT
            )
        }

        fun RelativeLayout.LayoutParams.alignHorizontallyAroundElement(id: Int, flag: Boolean) {
            alignWithRespectTo(id,
                if (flag)
                    HorizontalPosition.RIGHT_OF
                else
                    HorizontalPosition.LEFT_OF
            )
        }

        relativeLayout {
            fullSize()
            R.id.menuContainer

            isLeftHanded.onChange {
                requestLayout()
                contentDescription = if (it) "LEFT HANDED" else "RIGHT HANDED"
            }

            IconView(context).add(this) {
                id = R.id.moveUp
                icon.set(IconView.Icon(icon = R.drawable.ic_move_up))
                setTag(R.id.action, ActionMoveUp<ItemType>())
                isVisible(sortable)
            }.lparams {
                isLeftHanded.onChange(listener = this::isLeftHanded)
                above(R.id.moveDown)
                bottomMargin = dip(5)
                leftMargin = dip(23)
                rightMargin = dip(23)
            }

            IconView(context).add(this) {
                id = R.id.moveDown
                icon.set(IconView.Icon(icon = R.drawable.ic_move_down))
                setTag(R.id.action, ActionMoveDown<ItemType>())
                isVisible(sortable)
            }.lparams {
                alignInParent(verticalAlignment = VerticalAlignment.BOTTOM)
                isLeftHanded.onChange(listener = this::isLeftHanded)
                bottomMargin = dip(85)
                leftMargin = dip(23)
                rightMargin = dip(23)
            }

            IconView(context).add(this) {
                id = R.id.delete
                icon.set(IconView.Icon(icon = R.drawable.ic_delete))
                setTag(R.id.action, ActionDelete<ItemType>())
            }.lparams {
                alignInParent(verticalAlignment = VerticalAlignment.BOTTOM)
                isLeftHanded.onChange { alignHorizontallyAroundElement(R.id.selectAll, it) }
                bottomMargin = dip(23)
                leftMargin = dip(5)
                rightMargin = dip(5)
            }

            IconView(context).add(this) {
                id = R.id.selectAll
                icon.set(IconView.Icon(icon = R.drawable.ic_select_all))
                setTag(R.id.action, ActionSelectAll<ItemType>())
            }.lparams {
                alignInParent(verticalAlignment = VerticalAlignment.BOTTOM)
                isLeftHanded.onChange { alignHorizontallyAroundElement(R.id.edit, it) }
                bottomMargin = dip(23)
                leftMargin = dip(5)
                rightMargin = dip(5)
            }

        }.applyRecursively {
            when (it) {
                is IconView -> {
                    (it.layoutParams as RelativeLayout.LayoutParams).apply {
                        topMargin = dip(5)
                        width = dip(45)
                        height = dip(45)
                    }
                    @Suppress("UNCHECKED_CAST")
                    val action = it.getTag(R.id.action) as Action<ItemType>

                    listOf(items, selectedItems).onChange {
                        it.isEnabled = action.canPerform(items.get(), selectedItems.get())
                    }

                    it.setOnClickListener {
                        action.perform(items.get(), selectedItems.get()).apply {
                            items.set(first)
                            selectedItems.set(second)
                        }
                    }

                    actionIcon.onChange { icon ->
                        it.icon.set(it.icon.get().copy(
                            bgColor = icon.bgColor,
                            fgColor = icon.fgColor
                        ))
                    }
                }
            }
        }
    }
}