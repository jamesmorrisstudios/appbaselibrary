package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.dialogs.DatePickerMultiDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.time.DateItem;

import java.util.ArrayList;

/**
 * Request to build a multi date picker dialog.
 * Call execute to display the dialog
 * <p/>
 * Created by James on 11/3/2015.
 */
public final class MultiDatePickerRequest extends AbstractDialogRequest {
    public final DateItem startDate, endDate;
    public final ArrayList<DateItem> selectedDates;
    public final DatePickerMultiDialogBuilder.MultiDatePickerListener multiListener;

    /**
     * @param startDate     Earliest allowed date
     * @param endDate       Latest allowed date
     * @param selectedDates Currently selected dates
     * @param multiListener Listener
     */
    public MultiDatePickerRequest(@NonNull final DateItem startDate, @NonNull final DateItem endDate, @NonNull final ArrayList<DateItem> selectedDates, @NonNull final DatePickerMultiDialogBuilder.MultiDatePickerListener multiListener) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.selectedDates = selectedDates;
        this.multiListener = multiListener;
    }

}
