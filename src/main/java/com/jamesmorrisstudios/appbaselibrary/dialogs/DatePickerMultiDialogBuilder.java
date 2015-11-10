package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.ThemeManager;
import com.jamesmorrisstudios.appbaselibrary.time.DateItem;
import com.jamesmorrisstudios.appbaselibrary.time.UtilsTime;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;

/**
 * Created by James on 11/4/2015.
 */
public class DatePickerMultiDialogBuilder {
    private AlertDialog.Builder builder;
    private CalendarPickerView mainView;
    private DateItem startDate, endDate, selectedDate;
    private ArrayList<DateItem> selectedDates = new ArrayList<>();
    private SingleDatePickerListener singleListener = null;
    private MultiDatePickerListener multiListener = null;
    private AlertDialog dialog;

    private DatePickerMultiDialogBuilder(@NonNull Context context, int style) {
        builder = new AlertDialog.Builder(context, style);
        LayoutInflater vi= LayoutInflater.from(context);
        if(ThemeManager.getAppTheme() == ThemeManager.AppTheme.LIGHT) {
            mainView = (CalendarPickerView) vi.inflate(R.layout.date_picker_layout, null);
        } else {
            mainView = (CalendarPickerView) vi.inflate(R.layout.date_picker_dark_layout, null);
        }
        builder.setView(mainView);
    }

    public static DatePickerMultiDialogBuilder with(@NonNull Context context, int style) {
        return new DatePickerMultiDialogBuilder(context, style);
    }

    public DatePickerMultiDialogBuilder setDateRange(@NonNull DateItem startDate, @NonNull DateItem endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    public DatePickerMultiDialogBuilder setSelectedDate(@NonNull DateItem selectedDate, @NonNull SingleDatePickerListener singleListener) {
        this.selectedDate = selectedDate;
        this.singleListener = singleListener;
        return this;
    }

    public DatePickerMultiDialogBuilder setSelectedDates(@NonNull ArrayList<DateItem> selectedDates, @NonNull MultiDatePickerListener multiListener) {
        this.selectedDates = selectedDates;
        this.multiListener = multiListener;
        return this;
    }

    public AlertDialog build() {
        if(selectedDate != null) {
            mainView.init(UtilsTime.dateItemToDate(startDate), UtilsTime.dateItemToDate(endDate))
                    .inMode(CalendarPickerView.SelectionMode.SINGLE)
                    .withSelectedDate(UtilsTime.dateItemToDate(selectedDate));
        } else if(selectedDates != null) {
            mainView.init(UtilsTime.dateItemToDate(startDate), UtilsTime.dateItemToDate(endDate))
                    .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
                    .withSelectedDates(UtilsTime.dateItemArrToDateArr(selectedDates));
        }
        builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(singleListener != null) {
                    singleListener.onSelection(UtilsTime.dateToDateItem(mainView.getSelectedDate()));
                }
                if(multiListener != null) {
                    multiListener.onSelection(UtilsTime.dateArrToDateItemArr(mainView.getSelectedDates()));
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(singleListener != null) {
                    singleListener.onCancel();
                }
                if(multiListener != null) {
                    multiListener.onCancel();
                }
            }
        });
        dialog = builder.create();
        return dialog;
    }

    public interface SingleDatePickerListener {
        void onSelection(@NonNull DateItem selectedDate);
        void onCancel();
    }

    public interface MultiDatePickerListener {
        void onSelection(@NonNull ArrayList<DateItem> selectedDates);
        void onCancel();
    }

}
