package com.gurunars.crud_item_list


import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.gurunars.android_utils.IconView
import com.gurunars.android_utils.iconView
import com.gurunars.databinding.BindableField
import com.gurunars.item_list.Item
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.*


internal class ContextualMenu<ItemType: Item> constructor(
        context: Context,
        actionIcon: BindableField<CrudItemListView.IconColorBundle>,
        isLeftHanded: BindableField<Boolean>,
        isSortable: BindableField<Boolean>,
        items: BindableField<List<ItemType>>,
        selectedItems: BindableField<Set<ItemType>>,
        itemEditListener: (item: ItemType) -> Unit
) : FrameLayout(context) {

    init {

        isLeftHanded.onChange {
            contentDescription = if (it) "LEFT HANDED" else "RIGHT HANDED"
        }

        relativeLayout {
            fullSize()
            R.id.menuContainer

            iconView {
                id=R.id.moveUp
                icon.set(IconView.Icon(icon=R.drawable.ic_move_up))
                setTag(R.id.action, ActionMoveUp<ItemType>())
            }.lparams {
                isLeftHanded.onChange {
                    if (it) {
                        alignParentLeft()
                    } else {
                        alignParentRight()
                    }
                }
                isSortable.onChange {
                    visibility = if (it) View.VISIBLE else View.GONE
                }
                above(R.id.moveDown)
                bottomMargin=dip(5)
                leftMargin=dip(23)
                rightMargin=dip(23)
            }

            iconView {
                id=R.id.moveDown
                icon.set(IconView.Icon(icon=R.drawable.ic_move_down))
                setTag(R.id.action, ActionMoveDown<ItemType>())
            }.lparams {
                alignParentBottom()
                isLeftHanded.onChange {
                    if (it) {
                        alignParentLeft()
                    } else {
                        alignParentRight()
                    }
                }
                isSortable.onChange {
                    visibility = if (it) View.VISIBLE else View.GONE
                }
                bottomMargin=dip(85)
                leftMargin=dip(23)
                rightMargin=dip(23)
            }

            iconView {
                id=R.id.delete
                icon.set(IconView.Icon(icon=R.drawable.ic_delete))
                setTag(R.id.action, ActionDelete<ItemType>())
            }.lparams {
                alignParentBottom()
                isLeftHanded.onChange {
                    if (it) {
                        rightOf(R.id.selectAll)
                    } else {
                        leftOf(R.id.selectAll)
                    }
                }
                bottomMargin=dip(23)
                leftMargin=dip(5)
                rightMargin=dip(5)
            }


            iconView {
                id=R.id.selectAll
                icon.set(IconView.Icon(icon=R.drawable.ic_select_all))
                setTag(R.id.action, ActionSelectAll<ItemType>())
            }.lparams {
                alignParentBottom()
                isLeftHanded.onChange {
                    if (it) {
                        rightOf(R.id.edit)
                    } else {
                        leftOf(R.id.edit)
                    }
                }
                bottomMargin=dip(23)
                leftMargin=dip(5)
                rightMargin=dip(5)
            }

            iconView {
                id=R.id.edit
                icon.set(IconView.Icon(icon=R.drawable.ic_edit))
                setTag(R.id.action, ActionEdit({ payload: ItemType -> itemEditListener(payload) }))
            }.lparams {
                alignParentBottom()
                isLeftHanded.onChange {
                    if (it) {
                        alignParentLeft()
                        leftMargin=dip(85)
                        rightMargin=dip(5)
                    } else {
                        alignParentRight()
                        leftMargin=dip(5)
                        rightMargin=dip(85)
                    }
                }
                bottomMargin=dip(23)
            }

        }.applyRecursively { when(it) {
            is IconView -> {
                (it.layoutParams as RelativeLayout.LayoutParams).apply {
                    topMargin=dip(5)
                    width=dip(45)
                    height=dip(45)
                }
                val action = it.getTag(R.id.action) as Action<ItemType>

                fun configureAbility() {
                    it.isEnabled = action.canPerform(items.get(), selectedItems.get())
                }

                items.onChange { configureAbility() }
                selectedItems.onChange { configureAbility() }

                it.setOnClickListener {
                    val pair = action.perform(items.get(), selectedItems.get())
                    items.set(pair.first)
                    selectedItems.set(pair.second)
                }

                val iconView = it

                actionIcon.onChange {
                    iconView.icon.set(iconView.icon.get().copy(
                        bgColor = it.bgColor,
                        fgColor = it.fgColor
                    ))
                }
            }
        } }
    }

}
