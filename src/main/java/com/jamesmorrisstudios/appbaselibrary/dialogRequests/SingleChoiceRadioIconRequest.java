package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by James on 7/22/2015.
 */
public class SingleChoiceRadioIconRequest {
    public final String title;
    @DrawableRes public final int[] itemsIds;
    public final Uri[] itemsUri;
    public final DialogInterface.OnClickListener onPositive;
    public final DialogInterface.OnClickListener onNegative;
    public DialogInterface.OnClickListener onNeutral = null;
    public String textPositive = null;
    public String textNegative = null;
    public String textNeutral = null;

    public SingleChoiceRadioIconRequest(@NonNull String title, @DrawableRes @NonNull int[] itemsIds, @NonNull DialogInterface.OnClickListener onPositive, @Nullable DialogInterface.OnClickListener onNegative) {
        this.title = title;
        this.itemsIds = itemsIds;
        this.itemsUri = null;
        this.onPositive = onPositive;
        this.onNegative = onNegative;
    }

    public SingleChoiceRadioIconRequest(@NonNull String title, @DrawableRes @NonNull Uri[] itemsUri, @NonNull DialogInterface.OnClickListener onPositive, @Nullable DialogInterface.OnClickListener onNegative) {
        this.title = title;
        this.itemsIds = null;
        this.itemsUri = itemsUri;
        this.onPositive = onPositive;
        this.onNegative = onNegative;
    }

    public void setPositiveText(@NonNull String textPositive) {
        this.textPositive = textPositive;
    }

    public void setNegativeText(@NonNull String textNegative) {
        this.textNegative = textNegative;
    }

    public void AddNeutralAction(@NonNull String textNeutral, @NonNull DialogInterface.OnClickListener onNeutral) {
        this.textNeutral = textNeutral;
        this.onNeutral = onNeutral;
    }
}
