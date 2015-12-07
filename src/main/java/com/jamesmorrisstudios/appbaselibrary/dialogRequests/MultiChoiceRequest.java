package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by James on 7/22/2015.
 */
public class MultiChoiceRequest {
    public final String title;
    public final String[] items;
    public final boolean[] checkedItems;
    public final DialogInterface.OnMultiChoiceClickListener clickListener;
    public final DialogInterface.OnClickListener onPositive;
    public final DialogInterface.OnClickListener onNegative;

    public MultiChoiceRequest(@NonNull String title, @NonNull String[] items, boolean[] checkedItems, @NonNull DialogInterface.OnMultiChoiceClickListener clickListener, @NonNull DialogInterface.OnClickListener onPositive, @Nullable DialogInterface.OnClickListener onNegative) {
        this.title = title;
        this.items = items;
        this.checkedItems = checkedItems;
        this.clickListener = clickListener;
        this.onPositive = onPositive;
        this.onNegative = onNegative;
    }

}
