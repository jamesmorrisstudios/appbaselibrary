package com.jamesmorrisstudios.appbaselibrary.dialogHelper;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.dialogs.SingleChoiceIconDialogBuilder;

/**
 * Created by James on 7/22/2015.
 */
public class SingleChoiceIconRequest {
    public final String title;
    @DrawableRes public final int[] items;
    public final SingleChoiceIconDialogBuilder.OptionPickerListener onOptionPickedListener;

    public SingleChoiceIconRequest(@NonNull String title, @DrawableRes @NonNull int[] items, @NonNull SingleChoiceIconDialogBuilder.OptionPickerListener onOptionPickedListener) {
        this.title = title;
        this.items = items;
        this.onOptionPickedListener = onOptionPickedListener;
    }
}
