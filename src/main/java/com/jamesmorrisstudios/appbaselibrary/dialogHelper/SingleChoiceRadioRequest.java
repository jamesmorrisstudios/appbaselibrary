package com.jamesmorrisstudios.appbaselibrary.dialogHelper;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by James on 7/22/2015.
 */
public class SingleChoiceRadioRequest {
    public final String title;
    public final String[] items;
    public final int defaultValue;
    public final DialogInterface.OnClickListener clickListener;
    public final DialogInterface.OnClickListener onPositive;
    public final DialogInterface.OnClickListener onNegative;

    public SingleChoiceRadioRequest(@NonNull String title, @NonNull String[] items, int defaultValue, @NonNull DialogInterface.OnClickListener clickListener, @NonNull DialogInterface.OnClickListener onPositive, @Nullable DialogInterface.OnClickListener onNegative) {
        this.title = title;
        this.items = items;
        this.defaultValue = defaultValue;
        this.clickListener = clickListener;
        this.onPositive = onPositive;
        this.onNegative = onNegative;
    }

}
