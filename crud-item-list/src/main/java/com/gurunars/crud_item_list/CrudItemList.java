package com.gurunars.crud_item_list;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.gurunars.floatmenu.AnimationListener;
import com.gurunars.floatmenu.FloatMenu;
import com.gurunars.item_list.EmptyViewBinder;
import com.gurunars.item_list.Item;
import com.gurunars.item_list.ItemList;

import java.util.List;

import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import java8.util.function.Consumer;

/**
 * Widget to be used for manipulating a collection of items.
 */
public class CrudItemList<ItemType extends Item> extends RelativeLayout {

    private static int ANIMATION_DURATION = 400;

    @State int actionIconFgColor;
    @State int actionIconBgColor;
    @State int contextualCloseFgColor;
    @State int contextualCloseBgColor;
    @State int createCloseFgColor;
    @State int createCloseBgColor;
    @State int openBgColor;
    @State int openFgColor;

    private final CollectionManager<ItemType> collectionManager;

    private final UiThrottleBuffer throttleBuffer = new UiThrottleBuffer();
    private final ScheduledRunner scheduledRunner = new ScheduledRunner();

    private final ViewGroup creationMenuPlaceholder;
    private final ViewGroup formPlaceholder;
    private final ContextualMenu contextualMenu;

    private final FloatMenu floatingMenu;
    private final ItemList<SelectableItem<ItemType>> itemList;

    private ListChangeListener<ItemType> listChangeListener =
            new ListChangeListener.DefaultListChangeListener<>();
    private ItemEditListener<ItemType> itemEditListener =
            new ItemEditListener.DefaultItemEditListener<>();

    public CrudItemList(Context context) {
        this(context, null);
    }

    public CrudItemList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CrudItemList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.crud_item_list, this);

        floatingMenu = ButterKnife.findById(this, R.id.floatingMenu);
        floatingMenu.setAnimationDuration(ANIMATION_DURATION);
        floatingMenu.setMenuView(inflate(context, R.layout.raw_menu_layout, null));
        floatingMenu.setContentView(inflate(context, R.layout.raw_item_list, null));

        contextualMenu = ButterKnife.findById(this, R.id.contextualMenu);
        creationMenuPlaceholder = ButterKnife.findById(this, R.id.creationMenuPlaceholder);
        itemList = ButterKnife.findById(this, R.id.itemList);
        formPlaceholder = ButterKnife.findById(this, R.id.formPlaceholder);

        collectionManager = new CollectionManager<>(new Consumer<List<SelectableItem<ItemType>>>() {
            @Override
            public void accept(final List<SelectableItem<ItemType>> selectableItems) {
                itemList.setItems(selectableItems);
                if (collectionManager.hasSelection()) {
                    setUpContextualMenu();
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
                    listChangeListener.onChange(items);
                    }
                });
            }
        });

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
        setSortable(true);

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

        collectionManager.setItemConsumer(new Consumer<ItemType>() {
            @Override
            public void accept(ItemType item) {
                onItemEdit(item, false);
            }
        });

        setUpColors();
    }

    private void setUpColors() {
        floatingMenu.setOpenIconBgColor(openBgColor);
        floatingMenu.setOpenIconFgColor(openFgColor);
        contextualMenu.setIconBgColor(actionIconBgColor);
        contextualMenu.setIconFgColor(actionIconFgColor);
        if (collectionManager.hasSelection()) {
            setUpContextualMenu();
        } else {
            setUpCreationMenu();
        }
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
        bundle.putParcelable(
                "superState", Icepick.saveInstanceState(this, super.onSaveInstanceState()));
        bundle.putSerializable("selectedItems", collectionManager.saveState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle localState = (Bundle) state;
        super.onRestoreInstanceState(
                Icepick.restoreInstanceState(this, localState.getParcelable("superState")));
        collectionManager.loadState(localState.getSerializable("selectedItems"));
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

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(ANIMATION_DURATION);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                formPlaceholder.removeAllViews();
                formPlaceholder.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        formPlaceholder.startAnimation(fadeOut);

    }

    private void onItemEdit(ItemType editableItem, boolean isNew) {
        itemEditListener.onEdit(editableItem, isNew);
    }

    /**
     * Map item type to view binder responsible for rending items of this type.
     *
     * @param itemType type of the Items
     * @param itemViewBinder row renderer for the items of a given type
     * @param menuItemViewId id of the clickable item in creation menu
     * @param newItemSupplier supplier of blank new items
     */
    public void registerItemType(
            Enum itemType,
            SelectableItemViewBinder<ItemType> itemViewBinder,
            int menuItemViewId,
            final NewItemSupplier<ItemType> newItemSupplier
            ) {
        itemList.registerItemViewBinder(itemType,
                new ClickableItemViewBinder<>(itemViewBinder, collectionManager));
        View itemCreationTrigger = findViewById(menuItemViewId);
        if (itemCreationTrigger != null) {
            itemCreationTrigger.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemEdit(newItemSupplier.supply(), true);
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

    public void setActionIconFgColor(int actionIconFgColor) {
        this.actionIconFgColor = actionIconFgColor;
        setUpColors();
    }

    public void setActionIconBgColor(int actionIconBgColor) {
        this.actionIconBgColor = actionIconBgColor;
        setUpColors();
    }

    public void setContextualCloseFgColor(int contextualCloseFgColor) {
        this.contextualCloseFgColor = contextualCloseFgColor;
        setUpColors();
    }

    public void setContextualCloseBgColor(int contextualCloseBgColor) {
        this.contextualCloseBgColor = contextualCloseBgColor;
        setUpColors();
    }

    public void setCreateCloseFgColor(int createCloseFgColor) {
        this.createCloseFgColor = createCloseFgColor;
        setUpColors();
    }

    public void setCreateCloseBgColor(int createCloseBgColor) {
        this.createCloseBgColor = createCloseBgColor;
        setUpColors();
    }

    public void setOpenBgColor(int openBgColor) {
        this.openBgColor = openBgColor;
        setUpColors();
    }

    public void setOpenFgColor(int openFgColor) {
        this.openFgColor = openFgColor;
        setUpColors();
    }

    /**
     * @param itemEditListener a listener for the cases when a new item has to be created or when
     *                         the existing one has to be edited
     */
    public void setItemEditListener(ItemEditListener<ItemType> itemEditListener) {
        this.itemEditListener = itemEditListener;
    }
}
