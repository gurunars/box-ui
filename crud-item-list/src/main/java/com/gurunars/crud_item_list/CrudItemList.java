package com.gurunars.crud_item_list;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gurunars.floatmenu.AnimationListener;
import com.gurunars.floatmenu.FloatMenu;
import com.gurunars.item_list.EmptyViewBinder;
import com.gurunars.item_list.Item;
import com.gurunars.item_list.ItemViewBinder;
import com.gurunars.item_list.SelectableItemList;
import com.gurunars.item_list.SelectableItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import icepick.State;
import java8.util.function.Consumer;

/**
 * Widget to be used for manipulating a collection of items.
 */
public final class CrudItemList<ItemType extends Item> extends RelativeLayout {

    @State int actionIconFgColor;
    @State int actionIconBgColor;
    @State int contextualCloseFgColor;
    @State int contextualCloseBgColor;
    @State int createCloseFgColor;
    @State int createCloseBgColor;
    @State int openBgColor;
    @State int openFgColor;

    private ContextualMenu contextualMenu;
    private View creationMenu;

    private final UiThrottleBuffer throttleBuffer = new UiThrottleBuffer();

    private final FloatMenu floatingMenu;
    private final SelectableItemList<ItemType> itemList;

    private ListChangeListener<ItemType> listChangeListener =
            new ListChangeListener.DefaultListChangeListener<>();
    private ItemEditListener<ItemType> itemEditListener =
            new ItemEditListener.DefaultItemEditListener<>();

    private List<ItemType> items = new ArrayList<>();

    private final Map<Integer, Action<ItemType>> actions =
        new HashMap<Integer, Action<ItemType>>() {{
            put(R.id.selectAll, new ActionSelectAll<ItemType>());
            put(R.id.delete, new ActionDelete<ItemType>());
            put(R.id.edit, new ActionEdit<>(new Consumer<ItemType>() {
                @Override
                public void accept(ItemType payload) {
                    itemEditListener.onEdit(payload, false);
                }
            }));
            put(R.id.moveUp, new ActionMoveUp<ItemType>());
            put(R.id.moveDown, new ActionMoveDown<ItemType>());
    }};

    public CrudItemList(Context context) {
        this(context, null);
    }

    public CrudItemList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void confView(View view, @IdRes int id) {
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        );
        view.setId(id);
    }

    public CrudItemList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        floatingMenu = new FloatMenu(context);
        confView(floatingMenu, R.id.floatingMenu);

        itemList = new SelectableItemList<>(context);
        confView(itemList, R.id.rawItemList);

        contextualMenu = new ContextualMenu(context);
        confView(contextualMenu, R.id.contextualMenu);

        setCreationMenu(new View(context));

        floatingMenu.setContentView(itemList);
        floatingMenu.setMenuView(contextualMenu);

        addView(floatingMenu);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CrudItemList);

        int bgColor = ContextCompat.getColor(context, R.color.Black);
        int fgColor = ContextCompat.getColor(context, R.color.White);

        actionIconFgColor = a.getColor(R.styleable.CrudItemList_actionIconFgColor, bgColor);
        actionIconBgColor = a.getColor(R.styleable.CrudItemList_actionIconBgColor, fgColor);
        contextualCloseFgColor = a.getColor(R.styleable.CrudItemList_contextualCloseFgColor, bgColor);
        contextualCloseBgColor = a.getColor(R.styleable.CrudItemList_contextualCloseBgColor, fgColor);
        createCloseBgColor = a.getColor(R.styleable.CrudItemList_createCloseBgColor, bgColor);
        createCloseFgColor = a.getColor(R.styleable.CrudItemList_createCloseFgColor, fgColor);
        openBgColor = a.getColor(R.styleable.CrudItemList_openBgColor, bgColor);
        openFgColor = a.getColor(R.styleable.CrudItemList_openFgColor, fgColor);

        a.recycle();

        setLeftHanded(false);

        itemList.setSelectionChangeListener(new Runnable() {
            @Override
            public void run() {
                if (itemList.getSelectedItems().isEmpty()) {
                    floatingMenu.close();
                } else {
                    setUpContextualMenu();
                    floatingMenu.open();
                }
            }
        });

        floatingMenu.setOnCloseListener(new AnimationListener() {
            @Override
            public void onStart(int projectedDuration) {
                itemList.setSelectedItems(new HashSet<ItemType>());
            }

            @Override
            public void onFinish() {
                setUpCreationMenu();
            }
        });

        for (final Map.Entry<Integer, Action<ItemType>> entry: actions.entrySet()) {
            contextualMenu.findViewById(entry.getKey()).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Set<ItemType> selectedItems = itemList.getSelectedItems();
                    Action<ItemType> action = entry.getValue();
                    if (!action.canPerform(items, selectedItems)) {
                        return;
                    }
                    action.perform(items, selectedItems);
                    itemList.setItems(items);
                    itemList.setSelectedItems(selectedItems);
                    setUpActions();
                    throttleBuffer.call(new Runnable() {
                        @Override
                        public void run() {
                            listChangeListener.onChange(items);
                        }
                    });
                }
            });
        }

        reload();
    }

    private void reload() {
        floatingMenu.setOpenIconBgColor(openBgColor);
        floatingMenu.setOpenIconFgColor(openFgColor);
        if (itemList.getSelectedItems().isEmpty()) {
            setUpCreationMenu();
        } else {
            setUpContextualMenu();
        }
    }

    /**
     * @param leftHanded flag specifying if the menu should be left handed or not
     */
    public void setLeftHanded(boolean leftHanded) {
        floatingMenu.setLeftHanded(leftHanded);
        contextualMenu.setLeftHanded(leftHanded);
    }

    private void setUpActions() {
        for (Map.Entry<Integer, Action<ItemType>> entry: actions.entrySet()) {
            contextualMenu.findViewById(entry.getKey()).setEnabled(
                    entry.getValue().canPerform(items, itemList.getSelectedItems())
            );
        }
    }

    private void setUpContextualMenu() {
        floatingMenu.setMenuView(contextualMenu);
        floatingMenu.setCloseIconFgColor(contextualCloseFgColor);
        floatingMenu.setCloseIconBgColor(contextualCloseBgColor);
        floatingMenu.setHasOverlay(false);
        setUpActions();
    }

    private void setUpCreationMenu() {
        floatingMenu.setMenuView(creationMenu);
        floatingMenu.setCloseIconFgColor(createCloseFgColor);
        floatingMenu.setCloseIconBgColor(createCloseBgColor);
        floatingMenu.setHasOverlay(true);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);

    }

    @Override
    protected void onDetachedFromWindow() {
        throttleBuffer.shutdown();
        super.onDetachedFromWindow();
    }

    /**
     * Set a collection of items to be shown.
     *
     * If the collection of items is different - the diff shall be animated.
     *
     * @param items a new collection to be shown
     */
    public void setItems(List<ItemType> items) {
        this.items = items;
        itemList.setItems(items);
        setUpActions();
    }

    /**
     * Close creation or contextual menu.
     */
    public void close() {
        floatingMenu.close();
    }

    /**
     * Map item type to view binder responsible for rending items of this type.
     *
     * @param itemType type of the Items
     * @param itemViewBinder row renderer for the items of a given type
     */
    public void registerItemType(
            Enum itemType,
            ItemViewBinder<? extends View, SelectableItem<ItemType>> itemViewBinder) {
        itemList.registerItemViewBinder(itemType, itemViewBinder);
    }

    /**
     * Set the renderer to be employed when the list contains no items.
     *
     * @param emptyViewBinder renderer for the empty list
     */
    public void setEmptyViewBinder(EmptyViewBinder emptyViewBinder) {
        itemList.setEmptyViewBinder(emptyViewBinder);
    }

    /**
     * @param listChangeListener callback to be executed whenever the list gets changed within
     *                           the widget
     */
    public void setListChangeListener(ListChangeListener<ItemType> listChangeListener) {
        this.listChangeListener = listChangeListener;
    }

    /**
     * @param creationMenu menu to be show when creation mode is active (no items selected) and the
     *                     menu is open
     */
    public void setCreationMenu(View creationMenu) {
        this.creationMenu = creationMenu;
        confView(creationMenu, R.id.creationMenu);
        reload();
    }

    public void setActionIconFgColor(int actionIconFgColor) {
        this.actionIconFgColor = actionIconFgColor;
        reload();
    }

    public void setActionIconBgColor(int actionIconBgColor) {
        this.actionIconBgColor = actionIconBgColor;
        reload();
    }

    public void setContextualCloseFgColor(int contextualCloseFgColor) {
        this.contextualCloseFgColor = contextualCloseFgColor;
        reload();
    }

    public void setContextualCloseBgColor(int contextualCloseBgColor) {
        this.contextualCloseBgColor = contextualCloseBgColor;
        reload();
    }

    public void setCreateCloseFgColor(int createCloseFgColor) {
        this.createCloseFgColor = createCloseFgColor;
        reload();
    }

    public void setCreateCloseBgColor(int createCloseBgColor) {
        this.createCloseBgColor = createCloseBgColor;
        reload();
    }

    public void setOpenBgColor(int openBgColor) {
        this.openBgColor = openBgColor;
        reload();
    }

    public void setOpenFgColor(int openFgColor) {
        this.openFgColor = openFgColor;
        reload();
    }

    /**
     * @param itemEditListener a listener for the cases when a new item has to be created or when
     *                         the existing one has to be edited
     */
    public void setItemEditListener(final ItemEditListener<ItemType> itemEditListener) {
        this.itemEditListener = itemEditListener;
    }

    public void setSortable(boolean sortable) {
        contextualMenu.setSortable(sortable);
    }
}
