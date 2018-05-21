package com.gurunars.example

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.gurunars.box.core.ObservableValue
import com.gurunars.box.core.bind
import com.gurunars.box.core.withCleanup
import com.gurunars.box.ui.*
import com.gurunars.box.ui.components.floatmenu.fabDecoratedView
import com.gurunars.box.ui.components.listcontroller.ClipboardSerializer
import com.gurunars.box.ui.components.listcontroller.withListController
import com.gurunars.box.ui.components.listview.Wrapped
import com.gurunars.box.ui.components.listview.itemView
import com.gurunars.box.ui.components.listview.selectableListView

class ActivityListController: BaseDeclarativeActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val items = ObservableValue(
            listOf(1, 2, 3, 4, 5, 6).mapIndexed { index, i -> Wrapped(index, i) }
        ).withCleanup { wrapped ->
            var count = (wrapped).maxBy { it.id as Int }?.id as Int? ?: 0
            wrapped.map {
                it.copy(id=if (it.id == -1) ++count else it.id)
            }
        }
        val selectedItems = ObservableValue(setOf<Any>())

        withListController(items, selectedItems, serializer = object: ClipboardSerializer<Wrapped<Int>> {
            override fun fromString(source: String): List<Wrapped<Int>> =
                source.lines().map { Wrapped(-1, it.toInt()) }
            override fun toString(source: List<Wrapped<Int>>): String =
                source.joinToString("\n") { it.payload.toString() }
        }) {
            fabDecoratedView({
                showToast("Clicked plus")
            }) {
                selectableListView(items, selectedItems) {
                    itemView { field ->
                        relativeLayout {
                            textView {
                                text_ = field.bind { item.payload.toString() }
                                padding = Bounds(vertical = dip(5), horizontal = dip(10))
                            }.layoutParams {
                                alignInParent(horizontalAlignment = HorizontalAlignment.LEFT)
                            }

                            field.bind { isSelected }.onChange {
                                background = if (it) {
                                    Color.RED
                                } else {
                                    Color.TRANSPARENT
                                }.drawable
                                setBorders(Bounds(bottom = 2))
                            }

                            imageView {
                                isVisible_ = field.bind { isSelected }
                                src = ContextCompat.getDrawable(context, R.drawable.ic_edit)

                                onClick {
                                    showToast("Open ${field.get().item}")
                                }

                            }.layoutParams {
                                alignInParent(horizontalAlignment = HorizontalAlignment.RIGHT)
                            }
                        }
                    }
                }
            }
        }.layoutParams {
            matchParent()
        }

    }

}