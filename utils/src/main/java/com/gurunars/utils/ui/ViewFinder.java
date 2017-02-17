package com.gurunars.utils.ui;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;


/**
 * Helper class to fetch child views by ID without casting them to proper datatypes
 */
public class ViewFinder {

    /**
     * Runtime exception raised when the view was not found but was required to be non-null.
     */
    public static class NotFound extends RuntimeException {
        private NotFound() {
            super();
        }
    }

    private ViewFinder() {}

    /**
     * @throws NotFound when child view could not be found
     */
    @NonNull
    public static <ViewType extends View> ViewType findViewById(@NonNull Activity parent, @IdRes int id) {
        return _findViewById(parent, id);
    }

    @Nullable
    public static <ViewType extends View> ViewType findOptionalViewById(@NonNull Activity parent, @IdRes int id) {
        return _findOptionalViewById(parent, id);
    }

    /**
     * @throws NotFound when child view could not be found
     */
    @NonNull
    public static <ViewType extends View> ViewType findViewById(@NonNull View parent, @IdRes int id) {
        return _findViewById(parent, id);
    }

    @Nullable
    public static <ViewType extends View> ViewType findOptionalViewById(@NonNull View parent, @IdRes int id) {
        return _findOptionalViewById(parent, id);
    }

    @NonNull
    private static <ViewType extends View, ParentType> ViewType _findViewById(@NonNull ParentType parent, @IdRes int id) {
        ViewType returnValue = _findOptionalViewById(parent, id);
        if (returnValue == null) {
            throw new NotFound();
        }
        return returnValue;
    }

    @Nullable
    private static <ViewType extends View, ParentType> ViewType _findOptionalViewById(@NonNull ParentType parent, @IdRes int id) {
        if (parent instanceof Activity) {
            return (ViewType) ((Activity) parent).findViewById(id);
        } else if (parent instanceof View) {
            return (ViewType) ((View) parent).findViewById(id);
        } else {
            return null;
        }
    }

}
