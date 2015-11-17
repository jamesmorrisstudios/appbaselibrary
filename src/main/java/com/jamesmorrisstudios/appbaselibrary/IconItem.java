package com.jamesmorrisstudios.appbaselibrary;

import android.support.annotation.DrawableRes;

/**
 * Created by James on 11/11/2015.
 */
public class IconItem {
    public final String text;
    @DrawableRes public final int icon;

    public IconItem(String text, @DrawableRes Integer icon) {
        this.text = text;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return text;
    }

}
