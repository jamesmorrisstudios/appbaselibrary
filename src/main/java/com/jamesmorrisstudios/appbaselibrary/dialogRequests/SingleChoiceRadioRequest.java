package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Request to build a single choice radio dialog.
 * Call execute to display the dialog
 * <p/>
 * Created by James on 7/22/2015.
 */
public final class SingleChoiceRadioRequest extends AbstractDialogRequest {
    public final String title;
    public final String[] items;
    public final int defaultValue;
    public final DialogInterface.OnClickListener clickListener;
    public final DialogInterface.OnClickListener onPositive;
    public final DialogInterface.OnClickListener onNegative;

    /**
     * @param title         Title
     * @param items         array of items
     * @param defaultValue  Currently selected item
     * @param clickListener selected but not confirmed listener
     * @param onPositive    onPositive
     * @param onNegative    onNegative
     */
    public SingleChoiceRadioRequest(@NonNull final String title, @NonNull final String[] items, final int defaultValue, @NonNull final DialogInterface.OnClickListener clickListener, @NonNull final DialogInterface.OnClickListener onPositive, @Nullable final DialogInterface.OnClickListener onNegative) {
        this.title = title;
        this.items = items;
        this.defaultValue = defaultValue;
        this.clickListener = clickListener;
        this.onPositive = onPositive;
        this.onNegative = onNegative;
    }

}
