package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.jamesmorrisstudios.appbaselibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 11/10/2015.
 */
public class DualSpinnerDialogBuilder {
    private AlertDialog.Builder builder;
    private AppCompatSpinner firstSpinner, secondSpinner;
    private List<String> first, second;
    private int firstSelected, secondSelected;
    private int[][] firstRestrictingSecond, secondRestrictingFirst;
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

    public DualSpinnerDialogBuilder setFirst(@NonNull List<String> first, int firstSelected, @Nullable int[][] firstRestrictions) {
        this.first = first;
        this.firstSelected = firstSelected;
        this.firstRestrictingSecond = firstRestrictions;
        return this;
    }

    public DualSpinnerDialogBuilder setSecond(@NonNull List<String> second, int secondSelected, @Nullable int[][] secondRestrictions) {
        this.second = second;
        this.secondSelected = secondSelected;
        this.secondRestrictingFirst = secondRestrictions;
        return this;
    }

    public DualSpinnerDialogBuilder setListener(@NonNull DualSpinnerListener listener) {
        this.listener = listener;
        return this;
    }

    public AlertDialog build() {
        setData(firstSelected, firstSelected, secondSelected, secondSelected);

        builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onSelection(firstSpinner.getSelectedItemPosition(), secondSpinner.getSelectedItemPosition());
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onCancel();
                }
            }
        });
        return builder.create();
    }

    private void setData(int firstSelected1, int firstSelected1Prev, int secondSelected1, int secondSelected1Prev) {
        firstSpinner.setOnItemSelectedListener(null);
        secondSpinner.setOnItemSelectedListener(null);

        List<String> firstList, secondList;

        if(firstRestrictingSecond != null) {
            secondList = new ArrayList<>();
            for(int i = firstRestrictingSecond[firstSelected1][0]; i< firstRestrictingSecond[firstSelected1][1]; i++) {
                secondList.add(second.get(i));
            }
            int prevStartIndex = firstRestrictingSecond[firstSelected1Prev][0];
            int currStartIndex = firstRestrictingSecond[firstSelected1][0];
            if(prevStartIndex != currStartIndex) {
                secondSelected1 = secondSelected1 + (prevStartIndex - currStartIndex);
            }
        } else {
            secondList = second;
        }

        if(secondRestrictingFirst != null) {
            firstList = new ArrayList<>();
            for(int i = secondRestrictingFirst[secondSelected1][0]; i<secondRestrictingFirst[secondSelected1][1]; i++) {
                firstList.add(first.get(i));
            }
            int prevStartIndex = secondRestrictingFirst[secondSelected1Prev][0];
            int currStartIndex = secondRestrictingFirst[secondSelected1][0];
            if(prevStartIndex != currStartIndex) {
                firstSelected1 = firstSelected1 + (prevStartIndex - currStartIndex);
            }
        } else {
            firstList = first;
        }

        final int firstSelected2 = Math.min(firstSelected1, firstList.size()-1);
        final int secondSelected2 = Math.min(secondSelected1, secondList.size()-1);

        ArrayAdapter<String> firstAdapter = new ArrayAdapter<>(builder.getContext(), R.layout.support_simple_spinner_dropdown_item, firstList);
        firstAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        firstSpinner.setAdapter(firstAdapter);
        firstSpinner.setSelection(firstSelected2, false);

        ArrayAdapter<String> secondAdapter = new ArrayAdapter<>(builder.getContext(), R.layout.support_simple_spinner_dropdown_item, secondList);
        secondAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        secondSpinner.setAdapter(secondAdapter);
        secondSpinner.setSelection(secondSelected2, false);


        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("DualSpinner", "Item Selected");
                if (position != firstSelected2) {
                    setData(position, firstSelected2, secondSpinner.getSelectedItemPosition(), secondSpinner.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != secondSelected2) {
                    setData(firstSpinner.getSelectedItemPosition(), firstSpinner.getSelectedItemPosition(), position, secondSelected2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public interface DualSpinnerListener {
        void onSelection(int firstSelected, int secondSelected);
        void onCancel();
    }

}
