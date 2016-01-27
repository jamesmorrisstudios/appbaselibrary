package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jamesmorrisstudios.appbaselibrary.colorpicker.builder.ColorPickerClickListener;

/**
 * Request to build a color picker dialog.
 * Call execute to display the dialog
 * <p/>
 * Created by James on 6/29/2015.
 */
public final class ColorPickerRequest extends AbstractDialogRequest {
    public final int initialColor;
    public final ColorPickerClickListener onColorPickerClickListener;
    public final DialogInterface.OnClickListener onNegative, onDisable;

    /**
     * @param initialColor               Starting color
     * @param onColorPickerClickListener Color picker listener
     * @param onNegative                 On cancel listener (may be null)
     * @param onDisable                  On disable listener (may be null)
     */
    public ColorPickerRequest(final int initialColor, @NonNull final ColorPickerClickListener onColorPickerClickListener, @NonNull final DialogInterface.OnClickListener onNegative, @Nullable final DialogInterface.OnClickListener onDisable) {
        this.initialColor = initialColor;
        this.onColorPickerClickListener = onColorPickerClickListener;
        this.onNegative = onNegative;
        this.onDisable = onDisable;
    }

}
