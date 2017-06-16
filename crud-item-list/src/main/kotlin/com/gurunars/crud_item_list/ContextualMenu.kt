package com.gurunars.crud_item_list


import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.gurunars.android_utils.IconView
import com.gurunars.android_utils.iconView
import com.gurunars.databinding.bindableField
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.*


internal class ContextualMenu constructor(context: Context) : FrameLayout(context) {

    val isLeftHanded = bindableField(false)
    val isSortable = bindableField(true)

    init {

        isLeftHanded.onChange {
            contentDescription = if (it) "LEFT HANDED" else "RIGHT HANDED"
        }

        relativeLayout {
            fullSize()
            R.id.menuContainer

            iconView {
                id=R.id.moveUp
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
            }
        } }
    }

}
