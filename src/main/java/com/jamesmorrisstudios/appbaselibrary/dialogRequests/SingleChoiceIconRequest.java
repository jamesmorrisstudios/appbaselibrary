package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.dialogs.SingleChoiceIconDialogBuilder;

/**
 * Created by James on 7/22/2015.
 */
public class SingleChoiceIconRequest {
    public final String title;
    @DrawableRes public final int[] itemsIds;
    public final Uri[] itemsUri;
    public final SingleChoiceIconDialogBuilder.OptionPickerListener onOptionPickedListener;
    public String textNeutral = null;
    public DialogInterface.OnClickListener onNeutral = null;

    public SingleChoiceIconRequest(@NonNull String title, @DrawableRes @NonNull int[] itemsIds, @NonNull SingleChoiceIconDialogBuilder.OptionPickerListener onOptionPickedListener) {
        this.title = title;
        this.itemsIds = itemsIds;
        this.itemsUri = null;
        this.onOptionPickedListener = onOptionPickedListener;
    }

    public SingleChoiceIconRequest(@NonNull String title, @DrawableRes @NonNull Uri[] itemsUri, @NonNull SingleChoiceIconDialogBuilder.OptionPickerListener onOptionPickedListener) {
        this.title = title;
        this.itemsIds = null;
        this.itemsUri = itemsUri;
        this.onOptionPickedListener = onOptionPickedListener;
    }

    public void AddNeutralAction(@NonNull String text, @NonNull DialogInterface.OnClickListener listener) {
        this.textNeutral = text;
        this.onNeutral = listener;
    }
}
