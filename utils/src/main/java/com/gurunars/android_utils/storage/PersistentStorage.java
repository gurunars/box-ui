package com.gurunars.android_utils.storage;

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
 * @param <PayloadType> type of the payload to be stored
 */
public class PersistentStorage<PayloadType extends Serializable> {

    public interface PayloadChangeListener<PayloadType> {
        void onChange(PayloadType payload);
    }

    private final Activity activity;

    private PayloadType payload;
    private String typeAlias;
    private PayloadType defaultPayload;
    private PayloadChangeListener<PayloadType> changeListener;

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
                             @NonNull PayloadType defaultPayload,
                             @NonNull PayloadChangeListener<PayloadType> changeListener) {
        this.activity = activity;
        this.payload = defaultPayload;
        this.typeAlias = typeAlias;
        this.defaultPayload = defaultPayload;
        this.changeListener = changeListener;

        load();
    }

    private void load() {
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        PayloadType loaded = StringSerializer.fromString(preferences.getString(typeAlias, null));

        payload = loaded != null ? loaded : payload;

        changeListener.onChange(payload);
    }

    /**
     * @param payload payload to persist
     */
    public void set(@NonNull PayloadType payload) {
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
