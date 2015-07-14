package com.jamesmorrisstudios.appbaselibrary.dialogHelper;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jamesmorrisstudios.utilitieslibrary.dialogs.colorpicker.builder.ColorPickerClickListener;

/**
 * Created by James on 6/29/2015.
 */
public class ColorPickerRequest {
    public final int initialColor;
    public final ColorPickerClickListener onColorPickerClickListener;
    public final DialogInterface.OnClickListener onNegative, onDisable;

    public ColorPickerRequest(int initialColor, @NonNull ColorPickerClickListener onColorPickerClickListener, @NonNull DialogInterface.OnClickListener onNegative, @Nullable DialogInterface.OnClickListener onDisable) {
        this.initialColor = initialColor;
        this.onColorPickerClickListener = onColorPickerClickListener;
        this.onNegative = onNegative;
        this.onDisable = onDisable;
    }

}
