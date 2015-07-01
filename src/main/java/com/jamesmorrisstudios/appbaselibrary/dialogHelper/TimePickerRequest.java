package com.jamesmorrisstudios.appbaselibrary.dialogHelper;

import android.support.annotation.NonNull;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

/**
 * Created by James on 6/29/2015.
 */
public class TimePickerRequest {
    public final TimePickerDialog.OnTimeSetListener onTimeSetListener;
    public final int hour, minute;
    public final boolean is24Hour;

    public TimePickerRequest(@NonNull TimePickerDialog.OnTimeSetListener onTimeSetListener, int hour, int minute, boolean is24Hour) {
        this.onTimeSetListener = onTimeSetListener;
        this.hour = hour;
        this.minute = minute;
        this.is24Hour = is24Hour;
    }

}
