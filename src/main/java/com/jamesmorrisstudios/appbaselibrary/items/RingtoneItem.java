package com.jamesmorrisstudios.appbaselibrary.items;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.UtilsRingtone;

/**
 * Ringtone Container Item
 * <p/>
 * Created by James on 11/9/2015.
 */
public final class RingtoneItem {
    public final UtilsRingtone.RingtoneType type;
    public final String name;
    public final Uri uri;
    public final boolean isDefault;
    public final boolean isNone;
    public boolean selected = false;

    /**
     * @param type Ringtone Type
     * @param name Ringtone Name
     */
    public RingtoneItem(@NonNull UtilsRingtone.RingtoneType type, @NonNull String name) {
        this.type = type;
        this.name = name;
        this.uri = null;
        this.isDefault = false;
        this.isNone = true;
    }

    /**
     * @param type Ringtone Type
     * @param name Ringtone Name
     * @param uri  Uri path of tone
     */
    public RingtoneItem(@NonNull UtilsRingtone.RingtoneType type, @NonNull String name, @NonNull Uri uri) {
        this.type = type;
        this.name = name;
        this.uri = uri;
        this.isDefault = false;
        this.isNone = false;
    }

    /**
     * @param type      Ringtone Type
     * @param name      Ringtone Name
     * @param uri       Uri path of tone
     * @param isDefault True if default tone
     */
    public RingtoneItem(@NonNull UtilsRingtone.RingtoneType type, @NonNull String name, @NonNull Uri uri, boolean isDefault) {
        this.type = type;
        this.name = name;
        this.uri = uri;
        this.isDefault = isDefault;
        this.isNone = false;
    }
}
