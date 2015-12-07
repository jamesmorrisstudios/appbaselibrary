package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.dialogs.DatePickerMultiDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.time.DateItem;

import java.util.ArrayList;

/**
 * Created by James on 11/3/2015.
 */
public class MultiDatePickerRequest {
    public final DateItem startDate, endDate;
    public final ArrayList<DateItem> selectedDates;
    public final DatePickerMultiDialogBuilder.MultiDatePickerListener multiListener;

    public MultiDatePickerRequest(@NonNull DateItem startDate, @NonNull DateItem endDate, @NonNull ArrayList<DateItem> selectedDates, @NonNull DatePickerMultiDialogBuilder.MultiDatePickerListener multiListener) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.selectedDates = selectedDates;
        this.multiListener = multiListener;
    }
}
