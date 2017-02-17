package com.gurunars.crud_item_list;

import android.content.Context;
import android.graphics.drawable.shapes.RectShape;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gurunars.android_utils.ui.AutoBg;
import com.gurunars.android_utils.ui.ColoredShapeDrawable;
import com.gurunars.item_list.Item;

import icepick.Icepick;
import icepick.State;

/**
 * Widget meant for editing individual items
 *
 * @param <ItemType> implementation of Item to be manipulated via the form
 */
public abstract class ItemForm<ItemType extends Item> extends FrameLayout {

    @State int okButtonFgColor;
    @State int cancelButtonBgColor;
    @State int cancelButtonFgColor;
    @State int okButtonBgColor;
    @State boolean isCreateMenu;

    private Button save;
    private Button cancel;

    void setCancelButtonBgColor(int cancelButtonBgColor) {
        this.cancelButtonBgColor = cancelButtonBgColor;
        cancel.setBackground(new ColoredShapeDrawable(new RectShape(), cancelButtonBgColor));
        AutoBg.apply(cancel, 4);
    }

    void setCancelButtonFgColor(int cancelButtonFgColor) {
        this.cancelButtonFgColor = cancelButtonFgColor;
        cancel.setTextColor(cancelButtonFgColor);
    }

    void setOkButtonBgColor(int okButtonBgColor) {
        this.okButtonBgColor = okButtonBgColor;
        save.setBackground(new ColoredShapeDrawable(new RectShape(), okButtonBgColor));
        AutoBg.apply(save, 4);
    }

    void setOkButtonFgColor(int okButtonFgColor) {
        this.okButtonFgColor = okButtonFgColor;
        save.setTextColor(okButtonFgColor);
    }

    interface ItemConsumer<ItemType extends Item> {
        void consume(ItemType item);
    }

    private Runnable cancelListener;
    private ItemConsumer<ItemType> saveListener;

    public ItemForm(Context context) {
        this(context, null);
    }

    public ItemForm(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemForm(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.form_view, this);

        cancel = (Button) findViewById(R.id.cancel);
        save = (Button) findViewById(R.id.save);

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null) {
                    cancelListener.run();
                }
            }
        });
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveListener != null) {
                    saveListener.consume(getItem());
                }
            }
        });

        render((ViewGroup) findViewById(R.id.content));
    }

    /**
     * Configure the way the form should respond to save and cancel button clicks.
     *
     * @param saveListener called when the save button is clicked
     * @param cancelListener called when cancel button is clicked
     */
    void configureListeners(ItemConsumer<ItemType> saveListener, Runnable cancelListener) {
        this.saveListener = saveListener;
        this.cancelListener = cancelListener;
    }

    /**
     * Enable/disable save button. It is supposed to get triggered from within the form.
     *
     * @param isEnabled true if the item populated from the form can be saved
     */
    protected void setSaveActionEnabled(boolean isEnabled) {
        save.setEnabled(isEnabled);
    }

    /**
     * Populate the item to be saved based on data from the form's payload.
     *
     * @return item to be saved
     */
    protected abstract ItemType getItem();

    /**
     * Render a blank form.
     *
     * @param contentPoint full screen view group to which the form is supposed to be attached
     */
    protected abstract void render(ViewGroup contentPoint);

    /**
     * Populate the form using the payload.
     *
     * @param item payload to be used to populate the form
     */
    protected abstract void populate(@Nullable ItemType item);

    @Override public Parcelable onSaveInstanceState() {
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }

    @Override public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
        setCancelButtonBgColor(cancelButtonBgColor);
        setCancelButtonFgColor(cancelButtonFgColor);
        setOkButtonBgColor(okButtonBgColor);
        setOkButtonFgColor(okButtonFgColor);
        setCreateMenu(isCreateMenu);
    }

    void setCreateMenu(boolean createMenu) {
        isCreateMenu = createMenu;
        save.setText(createMenu ? R.string.create : R.string.update);
    }
}
