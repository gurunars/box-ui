package com.gurunars.box.ui.components.listcontroller

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.RelativeLayout
import com.gurunars.box.core.*
import com.gurunars.box.ui.*
import com.gurunars.box.ui.components.Icon
import com.gurunars.box.ui.components.floatmenu.contextualFloatMenu
import com.gurunars.box.ui.components.iconView
import com.gurunars.box.ui.components.listview.WithId
import kotlinx.coroutines.*

fun <T: WithId> Context.withListController(
    items: IObservableValue<List<T>>,
    // TODO: there should be a way to paste stuff if the clipboard contains valid items
    // TODO: clear clipboard button
    selectedItems: IObservableValue<Set<Any>>,
    sortable: Boolean=true,
    serializer: ClipboardSerializer<T>?=null,
    inner: () -> View
): View {
    val menu = relativeLayout {
        id = R.id.contextualMenu

        val state = merge(items, selectedItems).bind { ListState(first, second) }

        var actionJob: Job? = null
        val viewScope = CoroutineScope(Dispatchers.Unconfined)

        addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(p0: View?) {
                actionJob?.cancel()
            }
            override fun onViewAttachedToWindow(p0: View?) {}
        })

        var canDo = true

        fun actionIcon(
            icon: Int,
            id: Int,
            action: Action<T>,
            visible: Boolean = true,
            init: RelativeLayout.LayoutParams.() -> Unit
        ) {
            if (!visible) {
                return
            }

            iconView(
                ObservableValue(Icon(
                    icon = icon,
                    bgColor = themeColor(R.attr.colorSecondary) ?: Color.RED,
                    fgColor = themeColor(R.attr.colorOnSecondary) ?: Color.WHITE
                )),
                action.canPerform(state)
            ).apply {
                this.id = id
                onClick {
                    actionJob = viewScope.launch {
                        if (!canDo) {
                            return@launch
                        }
                        canDo = false
                        actionJob?.cancelAndJoin()
                        action.perform(state.get()).apply {
                            items.set(all)
                            selectedItems.set(selected)
                        }
                        canDo = true
                    }
                }
                elevation = 7f
            }.layoutParams {
                width = dip(45)
                height = dip(45)
                init()
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

    return contextualFloatMenu(
        inner(),
        menu,
        selectedItems.bind(
            { size > 0 },
            { if (it) this else setOf<T>() }
        )
    )

}
