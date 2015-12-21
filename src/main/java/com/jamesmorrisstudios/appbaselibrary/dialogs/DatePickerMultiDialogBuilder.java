package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.UtilsTheme;
import com.jamesmorrisstudios.appbaselibrary.time.DateItem;
import com.jamesmorrisstudios.appbaselibrary.time.UtilsTime;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;

/**
 * Allows selection of one or more dates depending upon configuration.
 * <p/>
 * Created by James on 11/4/2015.
 */
public final class DatePickerMultiDialogBuilder {
    private AlertDialog.Builder builder;
    private CalendarPickerView mainView;
    private DateItem startDate, endDate, selectedDate;
    private ArrayList<DateItem> selectedDates = new ArrayList<>();
    private SingleDatePickerListener singleListener = null;
    private MultiDatePickerListener multiListener = null;

    /**
     * Private constructor. Use the Builder pattern
     *
     * @param context Context
     * @param style   Style
     */
    private DatePickerMultiDialogBuilder(@NonNull Context context, int style) {
        builder = new AlertDialog.Builder(context, style);
        LayoutInflater vi = LayoutInflater.from(context);
        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
            mainView = (CalendarPickerView) vi.inflate(R.layout.dialog_date_picker_layout, null);
        } else {
            mainView = (CalendarPickerView) vi.inflate(R.layout.dialog_date_picker_dark_layout, null);
        }
        builder.setView(mainView);
    }

    /**
     * Start of builder
     *
     * @param context Activity context
     * @param style   Style
     * @return The dialog builder
     */
    public static DatePickerMultiDialogBuilder with(@NonNull Context context, int style) {
        return new DatePickerMultiDialogBuilder(context, style);
    }

    /**
     * @param startDate Earliest allowed date
     * @param endDate   Latest allowed date
     * @return The dialog builder
     */
    public final DatePickerMultiDialogBuilder setDateRange(@NonNull DateItem startDate, @NonNull DateItem endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    /**
     * Must be within the start and end dates.
     * This will allow a single date to be chosen by the user.
     *
     * @param selectedDate   Currently selected date. Must include one.
     * @param singleListener Date picked listener
     * @return The dialog builder
     */
    public final DatePickerMultiDialogBuilder setSelectedDate(@NonNull DateItem selectedDate, @NonNull SingleDatePickerListener singleListener) {
        this.selectedDate = selectedDate;
        this.singleListener = singleListener;
        return this;
    }

    /**
     * All dates must be within the start and end date
     * This will allow the user to select multiple dates.
     *
     * @param selectedDates List of selected dates. Empty list for none.
     * @param multiListener Date list picked listener
     * @return The dialog builder
     */
    public final DatePickerMultiDialogBuilder setSelectedDates(@NonNull ArrayList<DateItem> selectedDates, @NonNull MultiDatePickerListener multiListener) {
        this.selectedDates = selectedDates;
        this.multiListener = multiListener;
        return this;
    }

    /**
     * Builds the dialog
     *
     * @return The alert dialog. Call execute to display it
     */
    public final AlertDialog build() {
        if (selectedDate != null) {
            mainView.init(UtilsTime.dateItemToDate(startDate), UtilsTime.dateItemToDate(endDate))
                    .inMode(CalendarPickerView.SelectionMode.SINGLE)
                    .withSelectedDate(UtilsTime.dateItemToDate(selectedDate));
        } else if (selectedDates != null) {
            mainView.init(UtilsTime.dateItemToDate(startDate), UtilsTime.dateItemToDate(endDate))
                    .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
                    .withSelectedDates(UtilsTime.dateItemArrToDateArr(selectedDates));
        }
        builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (singleListener != null) {
                    singleListener.onSelection(UtilsTime.dateToDateItem(mainView.getSelectedDate()));
                }
                if (multiListener != null) {
                    multiListener.onSelection(UtilsTime.dateArrToDateItemArr(mainView.getSelectedDates()));
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (singleListener != null) {
                    singleListener.onCancel();
                }
                if (multiListener != null) {
                    multiListener.onCancel();
                }
            }
        });
        return builder.create();
    }

    /**
     * Single date chosen listener
     */
    public interface SingleDatePickerListener {

        /**
         * @param selectedDate The users selected date
         */
        void onSelection(@NonNull DateItem selectedDate);

        /**
         * Canceled the dialog
         */
        void onCancel();
    }

    /**
     * Date list chosen listener
     */
    public interface MultiDatePickerListener {

        /**
         * @param selectedDates List of the users selected dates. Empty list for none.
         */
        void onSelection(@NonNull ArrayList<DateItem> selectedDates);

        /**
         * Canceled the dialog
         */
        void onCancel();
    }

}
