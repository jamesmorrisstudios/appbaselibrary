package com.jamesmorrisstudios.appbaselibrary.items;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by James on 5/2/2016.
 */
public final class SoundItem {
    @SerializedName("uri")
    public String uriPath;
    @SerializedName("name")
    public String name;

    public SoundItem(@NonNull String uriPath, @NonNull String name) {
        set(uriPath, name);
    }

    public SoundItem(@NonNull Uri uri, @NonNull String name) {
        set(uri, name);
    }

    public final void set(@NonNull String uriPath, @NonNull String name) {
        this.uriPath = uriPath;
        this.name = name;
    }

    public final void set(@NonNull Uri uri, @NonNull String name) {
        this.uriPath = uri.toString();
        this.name = name;
    }

    @NonNull
    public final Uri getUri() {
        return Uri.parse(uriPath);
    }

    /**
     * @return A copy of this object
     */
    @NonNull
    public final SoundItem copy() {
        return new SoundItem(this.uriPath, this.name);
    }

    /**
     * @param items List to copy
     * @return Deep copied list
     */
    @NonNull
    public static ArrayList<SoundItem> cloneArrayList(@NonNull final ArrayList<SoundItem> items) {
        ArrayList<SoundItem> newItems = new ArrayList<>();
        for (SoundItem item : items) {
            newItems.add(item.copy());
        }
        return newItems;
    }

}
