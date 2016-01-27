package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.app.TimePickerDialog;
import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.time.TimeItem;

/**
 * Request to build a time picker dialog.
 * Call execute to display the dialog
 * <p/>
 * Created by James on 6/29/2015.
 */
public final class TimePickerRequest extends AbstractDialogRequest {
    public final OnTimePickerListener listener;
    public final TimeItem timeItem;

    /**
     * @param timeItem          Initial time
     * @param listener Callback listener
     */
    public TimePickerRequest(@NonNull final TimeItem timeItem, @NonNull final OnTimePickerListener listener) {
        this.listener = listener;
        this.timeItem = timeItem;
    }

    /**
     * Time set listener
     */
    public interface OnTimePickerListener {

        /**
         * Time was set
         * @param hourOfDay Hour of day
         * @param minute Minute
         */
        void onTimeSet(final int hourOfDay, final int minute);
    }

}
