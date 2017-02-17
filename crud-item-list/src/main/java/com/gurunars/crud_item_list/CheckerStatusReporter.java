package com.gurunars.crud_item_list;

interface CheckerStatusReporter {
    void report(boolean itemsSelected, boolean canEdit, boolean canMoveUp, boolean canMoveDown,
                boolean canDelete, boolean canSelectAll);
}
