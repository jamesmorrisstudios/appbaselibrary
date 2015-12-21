package com.jamesmorrisstudios.appbaselibrary.items;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * Text and Icon combo item
 * <p/>
 * Created by James on 11/11/2015.
 */
public final class IconItem {
    public final String text;
    @DrawableRes
    public final int icon;

    /**
     * @param text Text
     * @param icon Icon resource id
     */
    public IconItem(@NonNull String text, @DrawableRes Integer icon) {
        this.text = text;
        this.icon = icon;
    }

    /**
     * @return Text
     */
    @NonNull
    @Override
    public String toString() {
        return text;
    }

}
