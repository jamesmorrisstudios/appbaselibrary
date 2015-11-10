package com.jamesmorrisstudios.appbaselibrary;

import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by James on 11/9/2015.
 */
public class RingtoneItem {
    public final Utils.RingtoneType type;
    public final String name;
    public final Uri uri;
    public final boolean isDefault;
    public final boolean isNone;
    public boolean selected = false;

    public RingtoneItem(@NonNull Utils.RingtoneType type, @NonNull String name, @NonNull Uri uri) {
        this.type = type;
        this.name = name;
        this.uri = uri;
        this.isDefault = false;
        this.isNone = false;
    }

    public RingtoneItem(@NonNull Utils.RingtoneType type, @NonNull String name, @NonNull Uri uri, boolean isDefault) {
        this.type = type;
        this.name = name;
        this.uri = uri;
        this.isDefault = isDefault;
        this.isNone = false;
    }

    public RingtoneItem(@NonNull Utils.RingtoneType type, @NonNull String name) {
        this.type = type;
        this.name = name;
        this.uri = null;
        this.isDefault = false;
        this.isNone = true;
    }
}
