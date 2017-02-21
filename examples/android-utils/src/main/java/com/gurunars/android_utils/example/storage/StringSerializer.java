package com.gurunars.android_utils.example.storage;


import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class StringSerializer {

    /** Read the object from Base64 string. */
    @Nullable
    static <ObjType extends Serializable> ObjType fromString(@Nullable String serializedObject ) {
        if (serializedObject == null) {
            return null;
        }
        try {
            return (ObjType) new ObjectInputStream(
                new Base64InputStream(
                    new ByteArrayInputStream(
                        serializedObject.getBytes()), Base64.NO_PADDING | Base64.NO_WRAP)
            ).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Write the object to a Base64 string. */
    @Nullable
    static String toString(@Nullable Serializable obj ) {
        if (obj == null) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(
                    new Base64OutputStream(baos, Base64.NO_PADDING
                            | Base64.NO_WRAP));
            oos.writeObject(obj);
            oos.close();
            return baos.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
