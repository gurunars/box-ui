package com.gurunars.android_utils.example.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.io.Serializable;


/**
 * <p>
 * Helper to persist arbitrary payload using shared preferences.
 * </p>
 *
 * <p>
 * Not for production use. Only for example applications and development purposes.
 * </p>
 *
 * @param <ItemType> type of the payload to be stored
 */
public class PersistentStorage<ItemType extends Serializable> {

    public interface PayloadChangeListener<ItemType> {
        void onChange(ItemType payload);
    }

    private final Activity activity;

    private ItemType payload;
    private String typeAlias;
    private ItemType defaultPayload;
    private PayloadChangeListener<ItemType> changeListener;

    /**
     * @param activity Android activity to be attached to
     * @param typeAlias unique string describing the payload to be used as a key when storing the
     *                  payload in shared preferences
     * @param defaultPayload payload to be used when the storage is cleared
     * @param changeListener runnable called when the payload is initially loaded and later on
     *                       whenever changed
     */
    public PersistentStorage(@NonNull Activity activity,
                             @NonNull String typeAlias,
                             @NonNull ItemType defaultPayload,
                             @NonNull PayloadChangeListener<ItemType> changeListener) {
        this.activity = activity;
        this.payload = defaultPayload;
        this.typeAlias = typeAlias;
        this.defaultPayload = defaultPayload;
        this.changeListener = changeListener;

        load();
    }

    private void load() {
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        ItemType loaded = StringSerializer.fromString(preferences.getString(typeAlias, null));

        payload = loaded != null ? loaded : payload;

        changeListener.onChange(payload);
    }

    /**
     * @param payload payload to persist
     */
    public void set(@NonNull ItemType payload) {
        this.payload = payload;
        save();
    }

    private void save() {
        SharedPreferences.Editor editor = activity.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(typeAlias, StringSerializer.toString(payload));
        editor.apply();

        if (this.changeListener != null) {
            this.changeListener.onChange(payload);
        }
    }

    /**
     * Set persisted payload to the default one.
     */
    public void clear() {
        payload = defaultPayload;
        save();
    }
}
