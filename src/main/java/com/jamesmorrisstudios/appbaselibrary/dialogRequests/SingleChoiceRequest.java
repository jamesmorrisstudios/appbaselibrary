package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jamesmorrisstudios.appbaselibrary.items.IconItem;

/**
 * Request to build a single choice dialog.
 * Call execute to display the dialog
 * <p/>
 * Created by James on 7/22/2015.
 */
public final class SingleChoiceRequest extends AbstractDialogRequest {
    public final String title;
    public final String[] items;
    public final IconItem[] iconItems;
    public final boolean allowCancel;
    public final DialogInterface.OnClickListener clickListener;
    public final DialogInterface.OnClickListener onNegative;

    /**
     * @param title         Title
     * @param items         list of items
     * @param allowCancel   allow the dialog to be canceled with back button
     * @param clickListener selection listener
     * @param onNegative    onNegative
     */
    public SingleChoiceRequest(@NonNull final String title, @NonNull final String[] items, final boolean allowCancel, @NonNull final DialogInterface.OnClickListener clickListener, @Nullable final DialogInterface.OnClickListener onNegative) {
        this.title = title;
        this.items = items;
        this.iconItems = null;
        this.allowCancel = allowCancel;
        this.clickListener = clickListener;
        this.onNegative = onNegative;
    }

    /**
     * @param title         Title
     * @param iconItems     list of icon/string combo items
     * @param allowCancel   allow the dialog to be canceled with back button
     * @param clickListener selection listener
     * @param onNegative    onNegative
     */
    public SingleChoiceRequest(@NonNull final String title, @NonNull final IconItem[] iconItems, final boolean allowCancel, @NonNull final DialogInterface.OnClickListener clickListener, @Nullable final DialogInterface.OnClickListener onNegative) {
        this.title = title;
        this.items = null;
        this.iconItems = iconItems;
        this.allowCancel = allowCancel;
        this.clickListener = clickListener;
        this.onNegative = onNegative;
    }
}
