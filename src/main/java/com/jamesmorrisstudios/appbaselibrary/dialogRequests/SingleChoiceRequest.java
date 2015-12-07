package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jamesmorrisstudios.appbaselibrary.IconItem;

/**
 * Created by James on 7/22/2015.
 */
public class SingleChoiceRequest {
    public final String title;
    public final String[] items;
    public final IconItem[] iconItems;
    public final boolean allowCancel;
    public final DialogInterface.OnClickListener clickListener;
    public final DialogInterface.OnClickListener onNegative;

    public SingleChoiceRequest(@NonNull String title, @NonNull String[] items, boolean allowCancel, @NonNull DialogInterface.OnClickListener clickListener, @Nullable DialogInterface.OnClickListener onNegative) {
        this.title = title;
        this.items = items;
        this.iconItems = null;
        this.allowCancel = allowCancel;
        this.clickListener = clickListener;
        this.onNegative = onNegative;
    }

    public SingleChoiceRequest(@NonNull String title, @NonNull IconItem[] iconItems, boolean allowCancel, @NonNull DialogInterface.OnClickListener clickListener, @Nullable DialogInterface.OnClickListener onNegative) {
        this.title = title;
        this.items = null;
        this.iconItems = iconItems;
        this.allowCancel = allowCancel;
        this.clickListener = clickListener;
        this.onNegative = onNegative;
    }
}
