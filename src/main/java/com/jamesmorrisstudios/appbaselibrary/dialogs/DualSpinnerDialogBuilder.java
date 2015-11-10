package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.jamesmorrisstudios.appbaselibrary.R;

import java.util.List;

/**
 * Created by James on 11/10/2015.
 */
public class DualSpinnerDialogBuilder {
    private AlertDialog.Builder builder;
    private AppCompatSpinner firstSpinner, secondSpinner;
    private List<String> first, second;
    private int firstSelected, secondSelected;
    private DualSpinnerListener listener = null;

    private DualSpinnerDialogBuilder(@NonNull Context context, int style) {
        builder = new AlertDialog.Builder(context, style);
        LayoutInflater vi= LayoutInflater.from(context);
        LinearLayout mainView = (LinearLayout) vi.inflate(R.layout.dual_spinner_layout, null);
        firstSpinner = (AppCompatSpinner) mainView.findViewById(R.id.firstSpinner);
        secondSpinner = (AppCompatSpinner) mainView.findViewById(R.id.secondSpinner);
        builder.setView(mainView);
    }

    public static DualSpinnerDialogBuilder with(@NonNull Context context, int style) {
        return new DualSpinnerDialogBuilder(context, style);
    }

    public DualSpinnerDialogBuilder setTitle(@NonNull String title) {
        builder.setTitle(title);
        return this;
    }

    public DualSpinnerDialogBuilder setFirst(@NonNull List<String> first, int firstSelected) {
        this.first = first;
        this.firstSelected = firstSelected;
        return this;
    }

    public DualSpinnerDialogBuilder setSecond(@NonNull List<String> second, int secondSelected) {
        this.second = second;
        this.secondSelected = secondSelected;
        return this;
    }

    public DualSpinnerDialogBuilder setListener(@NonNull DualSpinnerListener listener) {
        this.listener = listener;
        return this;
    }

    public AlertDialog build() {
        ArrayAdapter<String> firstAdapter = new ArrayAdapter<>(builder.getContext(), R.layout.support_simple_spinner_dropdown_item, first);
        firstAdapter.setDropDownViewResource(R.layout.simple_drop_down_item);
        firstSpinner.setAdapter(firstAdapter);
        firstSpinner.setSelection(firstSelected);

        ArrayAdapter<String> secondAdapter = new ArrayAdapter<>(builder.getContext(), R.layout.support_simple_spinner_dropdown_item, second);
        secondAdapter.setDropDownViewResource(R.layout.simple_drop_down_item);
        secondSpinner.setAdapter(secondAdapter);
        secondSpinner.setSelection(secondSelected);

        builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null) {
                    listener.onSelection(firstSpinner.getSelectedItemPosition(), secondSpinner.getSelectedItemPosition());
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null) {
                    listener.onCancel();
                }
            }
        });
        return builder.create();
    }

    public interface DualSpinnerListener {
        void onSelection(int firstSelected, int secondSelected);
        void onCancel();
    }

}
