package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.dialogs.DatePickerMultiDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.time.DateItem;

/**
 * Created by James on 11/3/2015.
 */
public class SingleDatePickerRequest {
    public final DateItem startDate, endDate, selectedDate;
    public final DatePickerMultiDialogBuilder.SingleDatePickerListener singleListener;

    public SingleDatePickerRequest(@NonNull DateItem startDate, @NonNull DateItem endDate, @NonNull DateItem selectedDate, @NonNull DatePickerMultiDialogBuilder.SingleDatePickerListener singleListener) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.selectedDate = selectedDate;
        this.singleListener = singleListener;
    }
}
