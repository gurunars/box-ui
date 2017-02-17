package com.gurunars.crud_item_list;


import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.Arrays;
import java.util.List;

import icepick.Icepick;
import icepick.State;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_UP;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.LEFT_OF;
import static android.widget.RelativeLayout.RIGHT_OF;

class ContextualMenu extends FrameLayout {

    private interface MoveAction {
        void perform(boolean isActive);
    }

    interface MenuListener {
        void delete();
        void edit();
        void moveUp(boolean isActive);
        void moveDown(boolean isActive);
        void selectAll();
    }

    private MenuListener menuListener;
    private List<CircularIconButton> buttons;

    @State int iconBgColor;
    @State int iconFgColor;
    @State boolean leftHanded = false;
    @State boolean sortable = true;

    public ContextualMenu(Context context) {
        this(context, null);
    }

    public ContextualMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContextualMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.contextual_menu, this);
        buttons = Arrays.asList(
                getButton(R.id.delete),
                getButton(R.id.edit),
                getButton(R.id.selectAll),
                getButton(R.id.moveDown),
                getButton(R.id.moveUp)
        );
    }

    private RelativeLayout.LayoutParams getParams(@IdRes int id) {
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) findViewById(id).getLayoutParams();
        params.removeRule(ALIGN_PARENT_RIGHT);
        params.removeRule(ALIGN_PARENT_LEFT);
        params.removeRule(LEFT_OF);
        params.removeRule(RIGHT_OF);
        return params;
    }

    public void setLeftHanded(boolean leftHanded) {
        this.leftHanded = leftHanded;
        RelativeLayout.LayoutParams moveUpParams = getParams(R.id.moveUp);
        RelativeLayout.LayoutParams moveDownParams = getParams(R.id.moveDown);
        RelativeLayout.LayoutParams editParams = getParams(R.id.edit);
        RelativeLayout.LayoutParams deleteParams = getParams(R.id.delete);
        RelativeLayout.LayoutParams selectAllParams = getParams(R.id.selectAll);

        moveUpParams.addRule(leftHanded ? ALIGN_PARENT_LEFT : ALIGN_PARENT_RIGHT);
        moveDownParams.addRule(leftHanded ? ALIGN_PARENT_LEFT : ALIGN_PARENT_RIGHT);

        editParams.addRule(leftHanded ? ALIGN_PARENT_LEFT : ALIGN_PARENT_RIGHT);
        editParams.leftMargin = getResources().getDimensionPixelSize(
                leftHanded ? R.dimen.largeMargin : R.dimen.smallMargin);
        editParams.rightMargin = getResources().getDimensionPixelSize(
                leftHanded ? R.dimen.smallMargin : R.dimen.largeMargin);

        selectAllParams.addRule(leftHanded ? RIGHT_OF : LEFT_OF, R.id.edit);
        deleteParams.addRule(leftHanded ? RIGHT_OF : LEFT_OF, R.id.selectAll);

        setContentDescription(leftHanded ? "LEFT HANDED" : "RIGHT HANDED");

        findViewById(R.id.menuContainer).requestLayout();
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
        findViewById(R.id.moveUp).setVisibility(sortable ? VISIBLE : GONE);
        findViewById(R.id.moveDown).setVisibility(sortable ? VISIBLE : GONE);
    }

    public void setIconFgColor(int iconFgColor) {
        this.iconFgColor = iconFgColor;
        for (CircularIconButton button : buttons) {
            button.setForegroundColor(iconFgColor);
        }
    }

    public void setIconBgColor(int iconBgColor) {
        this.iconBgColor = iconBgColor;
        for (CircularIconButton button : buttons) {
            button.setBackgroundColor(iconBgColor);
        }
    }

    public void setMenuListener(MenuListener menuListener) {
        this.menuListener = menuListener;
    }

    private CircularIconButton getButton(int id) {
        return (CircularIconButton) findViewById(id);
    }

    private void configureDeleteButton(boolean canDelete) {
        CircularIconButton delete = getButton(R.id.delete);
        delete.setEnabled(canDelete);
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuListener.delete();
            }
        });
    }

    private void configureEditButton(boolean canEdit) {
        CircularIconButton edit = getButton(R.id.edit);
        edit.setEnabled(canEdit);
        edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuListener.edit();
            }
        });
    }

    private void configureMoveButton(int buttonId, boolean isActive, final MoveAction action) {
        CircularIconButton moveDown = getButton(buttonId);
        moveDown.setEnabled(isActive);
        if (!isActive) {  // cancel the action if the button is deactivated
            action.perform(false);
        }
        moveDown.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int motion = event.getActionMasked();
                if (motion == ACTION_DOWN) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    action.perform(true);
                } else if (motion == ACTION_UP || motion == ACTION_CANCEL) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    action.perform(false);
                }
                return true;
            }
        });
    }

    private void configureMoveUpButton(boolean canMoveUp) {
        configureMoveButton(R.id.moveUp, canMoveUp, new MoveAction() {
            @Override
            public void perform(boolean isActive) {
                menuListener.moveUp(isActive);
            }
        });
    }

    private void configureMoveDownButton(boolean canMoveDown) {
        configureMoveButton(R.id.moveDown, canMoveDown, new MoveAction() {
            @Override
            public void perform(boolean isActive) {
                menuListener.moveDown(isActive);
            }
        });
    }

    private void configureSelectAllButton(boolean canSelectAll) {
        CircularIconButton selectAllB = getButton(R.id.selectAll);
        selectAllB.setEnabled(canSelectAll);
        selectAllB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuListener.selectAll();
            }
        });
    }

    public void setUpContextualButtons(
            boolean canEdit, boolean canMoveUp, boolean canMoveDown, boolean canDelete,
            boolean canSelectAll) {
        configureDeleteButton(canDelete);
        configureEditButton(canEdit);
        configureMoveUpButton(canMoveUp);
        configureMoveDownButton(canMoveDown);
        configureSelectAllButton(canSelectAll);
    }

    @Override public Parcelable onSaveInstanceState() {
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }

    @Override public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
        setIconBgColor(iconBgColor);
        setIconFgColor(iconFgColor);
        setSortable(sortable);
        setLeftHanded(leftHanded);
    }

}
