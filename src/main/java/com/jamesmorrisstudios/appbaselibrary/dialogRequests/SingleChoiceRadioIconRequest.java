package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Request to build a single choice radio icon dialog.
 * Call execute to display the dialog
 * <p/>
 * Created by James on 7/22/2015.
 */
public final class SingleChoiceRadioIconRequest extends AbstractDialogRequest {
    public final String title;
    @DrawableRes
    public final int[] itemsIds;
    public final Uri[] itemsUri;
    public final DialogInterface.OnClickListener onPositive;
    public final DialogInterface.OnClickListener onNegative;
    public DialogInterface.OnClickListener onNeutral = null;
    public String textPositive = null;
    public String textNegative = null;
    public String textNeutral = null;
    public int backgroundColor = -1;

    /**
     * @param title      Title
     * @param itemsIds   array of icon resource ids
     * @param onPositive onPositive
     * @param onNegative onNegative
     */
    public SingleChoiceRadioIconRequest(@NonNull final String title, @DrawableRes @NonNull final int[] itemsIds, @NonNull final DialogInterface.OnClickListener onPositive, @Nullable final DialogInterface.OnClickListener onNegative) {
        this.title = title;
        this.itemsIds = itemsIds;
        this.itemsUri = null;
        this.onPositive = onPositive;
        this.onNegative = onNegative;
    }

    /**
     * @param title      Title
     * @param itemsUri   array of icon Uris
     * @param onPositive onPositive
     * @param onNegative onNegative
     */
    public SingleChoiceRadioIconRequest(@NonNull final String title, @DrawableRes @NonNull final Uri[] itemsUri, @NonNull final DialogInterface.OnClickListener onPositive, @Nullable final DialogInterface.OnClickListener onNegative) {
        this.title = title;
        this.itemsIds = null;
        this.itemsUri = itemsUri;
        this.onPositive = onPositive;
        this.onNegative = onNegative;
    }

    /**
     * @param textPositive positive action text
     */
    public final void setPositiveText(@NonNull final String textPositive) {
        this.textPositive = textPositive;
    }

    /**
     * @param textNegative negative action text
     */
    public final void setNegativeText(@NonNull final String textNegative) {
        this.textNegative = textNegative;
    }

    /**
     * Add a neutral button
     *
     * @param textNeutral neutral button text
     * @param onNeutral   onNeutral
     */
    public final void addNeutralAction(@NonNull final String textNeutral, @NonNull final DialogInterface.OnClickListener onNeutral) {
        this.textNeutral = textNeutral;
        this.onNeutral = onNeutral;
    }

    /**
     * @param backgroundColor Set a background color for each icon
     */
    public final void setBackgroundColor(final int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
