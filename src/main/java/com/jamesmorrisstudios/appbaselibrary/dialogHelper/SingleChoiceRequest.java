package com.jamesmorrisstudios.appbaselibrary.dialogHelper;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by James on 7/22/2015.
 */
public class SingleChoiceRequest {
    public final String title;
    public final String[] items;
    public final DialogInterface.OnClickListener clickListener;
    public final DialogInterface.OnClickListener onNegative;

    public SingleChoiceRequest(@NonNull String title, @NonNull String[] items, @NonNull DialogInterface.OnClickListener clickListener, @Nullable DialogInterface.OnClickListener onNegative) {
        this.title = title;
        this.items = items;
        this.clickListener = clickListener;
        this.onNegative = onNegative;
    }
}
