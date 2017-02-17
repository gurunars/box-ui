package com.gurunars.crud_item_list;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gurunars.android_utils.ui.ViewFinder;
import com.gurunars.floatmenu.AnimationListener;
import com.gurunars.floatmenu.FloatMenu;
import com.gurunars.item_list.EmptyViewBinder;
import com.gurunars.item_list.Item;
import com.gurunars.item_list.ItemList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java8.util.function.Consumer;

/**
 * Widget to be used for manipulating a collection of items.
 */
public class CrudItemList<ItemType extends Item> extends RelativeLayout {

    private Map<Enum, ItemFormSupplier<ItemType>> formBinders = new HashMap<>();

    private final int contextualCloseFgColor;
    private final int contextualCloseBgColor;

    private final int createCloseFgColor;
    private final int createCloseBgColor;
    private final int openBgColor;
    private final int openFgColor;

    private boolean sortable = true;
    private boolean leftHanded = false;
    private boolean isNew = true;

    private ItemType editableItem;

    private final CollectionManager<ItemType> collectionManager;

    private final UiThrottleBuffer throttleBuffer = new UiThrottleBuffer();
    private final ScheduledRunner scheduledRunner = new ScheduledRunner();

    private final ViewGroup creationMenuPlaceholder;
    private final ViewGroup formPlaceholder;
    private final ContextualMenu contextualMenu;

    private final FloatMenu floatingMenu;
    private final ItemList<SelectableItem<ItemType>> itemList;

    private ListChangeListener<ItemType> listChangeListener;

    public CrudItemList(Context context) {
        this(context, null);
    }

    public CrudItemList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CrudItemList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.crud_item_list, this);

        floatingMenu = ViewFinder.findViewById(this, R.id.floatingMenu);
        floatingMenu.setMenuView(inflate(context, R.layout.raw_menu_layout, null));
        floatingMenu.setContentView(inflate(context, R.layout.raw_item_list, null));

        contextualMenu = ViewFinder.findViewById(this, R.id.contextualMenu);
        creationMenuPlaceholder = ViewFinder.findViewById(this, R.id.creationMenuPlaceholder);
        itemList = ViewFinder.findViewById(this, R.id.itemList);
        formPlaceholder = ViewFinder.findViewById(this, R.id.formPlaceholder);

        collectionManager = new CollectionManager<>(new Consumer<List<SelectableItem<ItemType>>>() {
            @Override
            public void accept(final List<SelectableItem<ItemType>> selectableItems) {
                itemList.setItems(selectableItems);
                if (collectionManager.hasSelection()) {
                    setUpContextualMenu();
                    contextualMenu.setUpContextualButtons(
                            collectionManager.canEdit(),
                            collectionManager.canMoveUp(),
                            collectionManager.canMoveDown(),
                            collectionManager.canDelete(),
                            collectionManager.canSelectAll());
                    floatingMenu.open();
                } else {
                    floatingMenu.close();
                }
            }
        }, new Consumer<List<ItemType>>() {
            @Override
            public void accept(final List<ItemType> items) {
                throttleBuffer.call(new Runnable() {
                    @Override
                    public void run() {
                        if (listChangeListener != null) {
                            listChangeListener.onChange(items);
                        }
                    }
                });
            }
        });

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CrudItemList);

        int bgColor = ContextCompat.getColor(context, R.color.Black);
        int fgColor = ContextCompat.getColor(context, R.color.White);

        int actionIconFgColor = a.getColor(R.styleable.CrudItemList_actionIconFgColor, bgColor);
        int actionIconBgColor = a.getColor(R.styleable.CrudItemList_actionIconBgColor, fgColor);

        contextualCloseFgColor = a.getColor(R.styleable.CrudItemList_contextualCloseFgColor, bgColor);
        contextualCloseBgColor = a.getColor(R.styleable.CrudItemList_contextualCloseBgColor, fgColor);

        createCloseBgColor = a.getColor(R.styleable.CrudItemList_createCloseBgColor, bgColor);
        createCloseFgColor = a.getColor(R.styleable.CrudItemList_createCloseFgColor, fgColor);
        openBgColor = a.getColor(R.styleable.CrudItemList_openBgColor, bgColor);
        openFgColor = a.getColor(R.styleable.CrudItemList_openFgColor, fgColor);

        a.recycle();

        setLeftHanded(leftHanded);
        setSortable(sortable);

        floatingMenu.setOnCloseListener(new AnimationListener() {
            @Override
            public void onStart(int projectedDuration) {
                if (collectionManager.hasSelection()) {
                    scheduledRunner.stop();
                    collectionManager.unselectAll();
                }
            }

            @Override
            public void onFinish() {
                setUpCreationMenu();
            }
        });

        floatingMenu.setOpenIconBgColor(openBgColor);
        floatingMenu.setOpenIconFgColor(openFgColor);
        contextualMenu.setIconBgColor(actionIconBgColor);
        contextualMenu.setIconFgColor(actionIconFgColor);
        contextualMenu.setMenuListener(new ContextualMenu.MenuListener() {
            @Override
            public void delete() {
                collectionManager.deleteSelected();
            }

            @Override
            public void edit() {
                collectionManager.triggerConsumption();
            }

            @Override
            public void moveUp(boolean isActive) {
                if (isActive) {
                    scheduledRunner.start(new Runnable() {
                        @Override
                        public void run() {
                            collectionManager.moveSelectionUp();
                        }
                    });
                } else {
                    scheduledRunner.stop();
                }
            }

            @Override
            public void moveDown(boolean isActive) {
                if (isActive) {
                    scheduledRunner.start(new Runnable() {
                        @Override
                        public void run() {
                            collectionManager.moveSelectionDown();
                        }
                    });
                } else {
                    scheduledRunner.stop();
                }
            }

            @Override
            public void selectAll() {
                collectionManager.selectAll();
            }
        });

        collectionManager.setItemConsumer(new Consumer<ItemType>() {
            @Override
            public void accept(ItemType item) {
                showForm(item, false);
            }
        });

        setUpCreationMenu();
    }

    /**
     * @param leftHanded flag specifying if the menu should be left handed or not
     */
    public void setLeftHanded(boolean leftHanded) {
        floatingMenu.setLeftHanded(leftHanded);
        contextualMenu.setLeftHanded(leftHanded);
    }

    private void setUpContextualMenu() {
        creationMenuPlaceholder.setVisibility(GONE);
        contextualMenu.setVisibility(VISIBLE);
        floatingMenu.setCloseIconFgColor(contextualCloseFgColor);
        floatingMenu.setCloseIconBgColor(contextualCloseBgColor);
        floatingMenu.setHasOverlay(false);
    }

    private void setUpCreationMenu() {
        creationMenuPlaceholder.setVisibility(VISIBLE);
        contextualMenu.setVisibility(GONE);
        floatingMenu.setCloseIconFgColor(createCloseFgColor);
        floatingMenu.setCloseIconBgColor(createCloseBgColor);
        floatingMenu.setHasOverlay(true);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putBoolean("leftHanded", leftHanded);
        bundle.putBoolean("sortable", sortable);
        bundle.putSerializable("editableItem", editableItem);
        bundle.putSerializable("selectedItems", collectionManager.saveState());
        bundle.putBoolean("isNew", isNew);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle localState = (Bundle) state;
        super.onRestoreInstanceState(localState.getParcelable("superState"));
        setLeftHanded(localState.getBoolean("leftHanded", false));
        setSortable(localState.getBoolean("sortable", true));
        collectionManager.loadState(localState.getSerializable("selectedItems"));
        showForm(
                (ItemType) localState.getSerializable("editableItem"),
                localState.getBoolean("isNew", false)
        );
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
        collectionManager.setItems(items);
    }

    /**
     * Close creation or contextual menu.
     */
    public void close() {
        floatingMenu.close();
        editableItem = null;
        formPlaceholder.removeAllViews();
        formPlaceholder.setVisibility(GONE);
    }

    private void showForm(ItemType editableItem, boolean isNew) {
        if (editableItem == null || formPlaceholder.getChildCount() != 0) {
            return;
        }
        this.editableItem = editableItem;
        this.isNew = isNew;

        floatingMenu.close();
        ItemFormSupplier<ItemType> binder = formBinders.get(editableItem.getType());
        ItemForm<ItemType> form = binder.supply(getContext());
        form.populate(editableItem);
        form.setCreateMenu(isNew);
        form.configureListeners(new ItemForm.ItemConsumer<ItemType>() {
            @Override
            public void consume(ItemType item) {
                collectionManager.setItem(item);
                close();
            }
        }, new Runnable() {  // cancel
            @Override
            public void run() {
                close();
            }
        });

        form.setCancelButtonBgColor(createCloseBgColor);
        form.setCancelButtonFgColor(createCloseFgColor);
        form.setOkButtonBgColor(openBgColor);
        form.setOkButtonFgColor(openFgColor);

        formPlaceholder.addView(form);
        formPlaceholder.setVisibility(VISIBLE);

    }

    /**
     * Map item type to view binder responsible for rending items of this type.
     *
     * @param itemType type of the Items
     * @param itemViewBinder row renderer for the items of a given type
     * @param menuItemViewId id of the clickable item in creation menu
     * @param itemFormSupplier supplier of the form to edit items of a given type
     * @param newItemSupplier supplier of blank new items
     */
    public void registerItemType(
            final Enum itemType,
            SelectableItemViewBinder<ItemType> itemViewBinder,
            int menuItemViewId,
            ItemFormSupplier<ItemType> itemFormSupplier,
            final NewItemSupplier<ItemType> newItemSupplier
            ) {
        formBinders.put(itemType, itemFormSupplier);
        itemList.registerItemViewBinder(itemType,
                new ClickableItemViewBinder<>(itemViewBinder, collectionManager));
        View itemCreationTrigger = findViewById(menuItemViewId);
        if (itemCreationTrigger != null) {
            itemCreationTrigger.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showForm(newItemSupplier.supply(), true);
                        }
                    });
        }
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
     * @param sortable if false - move up and down buttons are disabled
     */
    public void setSortable(boolean sortable) {
        this.sortable = sortable;
        contextualMenu.setSortable(sortable);
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
        creationMenuPlaceholder.removeAllViews();
        creationMenuPlaceholder.addView(creationMenu);
    }

}
