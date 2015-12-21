package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.time.TimeItem;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

/**
 * Request to build a time picker dialog.
 * Call execute to display the dialog
 * <p/>
 * Created by James on 6/29/2015.
 */
public final class TimePickerRequest extends AbstractDialogRequest {
    public final TimePickerDialog.OnTimeSetListener onTimeSetListener;
    public final TimeItem timeItem;

    /**
     * @param timeItem          Initial time
     * @param onTimeSetListener Callback listener
     */
    public TimePickerRequest(@NonNull TimeItem timeItem, @NonNull TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener = onTimeSetListener;
        this.timeItem = timeItem;
    }

}
