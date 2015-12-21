package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.dialogs.SingleChoiceIconDialogBuilder;

/**
 * Request to build a single choice icon dialog.
 * Call execute to display the dialog
 * <p/>
 * Created by James on 7/22/2015.
 */
public final class SingleChoiceIconRequest extends AbstractDialogRequest {
    public final String title;
    @DrawableRes
    public final int[] itemsIds;
    public final Uri[] itemsUri;
    public final SingleChoiceIconDialogBuilder.OptionPickerListener onOptionPickedListener;
    public String textNeutral = null;
    public DialogInterface.OnClickListener onNeutral = null;
    public int backgroundColor = -1;

    /**
     * @param title                  Title
     * @param itemsIds               Id list of icons
     * @param onOptionPickedListener selection listener
     */
    public SingleChoiceIconRequest(@NonNull String title, @DrawableRes @NonNull int[] itemsIds, @NonNull SingleChoiceIconDialogBuilder.OptionPickerListener onOptionPickedListener) {
        this.title = title;
        this.itemsIds = itemsIds;
        this.itemsUri = null;
        this.onOptionPickedListener = onOptionPickedListener;
    }

    /**
     * @param title                  Title
     * @param itemsUri               Uri list of icons
     * @param onOptionPickedListener selection listener
     */
    public SingleChoiceIconRequest(@NonNull String title, @DrawableRes @NonNull Uri[] itemsUri, @NonNull SingleChoiceIconDialogBuilder.OptionPickerListener onOptionPickedListener) {
        this.title = title;
        this.itemsIds = null;
        this.itemsUri = itemsUri;
        this.onOptionPickedListener = onOptionPickedListener;
    }

    /**
     * Add a neutral item
     *
     * @param text     Text of button
     * @param listener onNeutral listener
     */
    public final void addNeutralAction(@NonNull String text, @NonNull DialogInterface.OnClickListener listener) {
        this.textNeutral = text;
        this.onNeutral = listener;
    }

    /**
     * @param backgroundColor Set a background color for each icon
     */
    public final void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
