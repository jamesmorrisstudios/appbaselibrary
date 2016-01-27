package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.dialogs.DatePickerMultiDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.time.DateItem;

/**
 * Request to build a single date picker dialog.
 * Call execute to display the dialog
 * <p/>
 * Created by James on 11/3/2015.
 */
public final class SingleDatePickerRequest extends AbstractDialogRequest {
    public final DateItem startDate, endDate, selectedDate;
    public final DatePickerMultiDialogBuilder.SingleDatePickerListener singleListener;

    /**
     * @param startDate      earliest allowed date
     * @param endDate        latest allowed date
     * @param selectedDate   Currently selected date
     * @param singleListener selection listener
     */
    public SingleDatePickerRequest(@NonNull final DateItem startDate, @NonNull final DateItem endDate, @NonNull final DateItem selectedDate, @NonNull final DatePickerMultiDialogBuilder.SingleDatePickerListener singleListener) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.selectedDate = selectedDate;
        this.singleListener = singleListener;
    }
}
