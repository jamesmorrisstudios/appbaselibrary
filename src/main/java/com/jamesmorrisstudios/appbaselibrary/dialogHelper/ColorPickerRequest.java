package com.jamesmorrisstudios.appbaselibrary.dialogHelper;

import android.support.annotation.NonNull;

import com.jamesmorrisstudios.utilitieslibrary.dialogs.colorpicker.builder.ColorPickerClickListener;

/**
 * Created by James on 6/29/2015.
 */
public class ColorPickerRequest {
    public final int initialColor;
    public final ColorPickerClickListener onColorPickerClickListener;

    public ColorPickerRequest(int initialColor, @NonNull ColorPickerClickListener onColorPickerClickListener) {
        this.initialColor = initialColor;
        this.onColorPickerClickListener = onColorPickerClickListener;
    }

}
