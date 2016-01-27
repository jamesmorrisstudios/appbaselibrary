package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.jamesmorrisstudios.appbaselibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a dialog with multiple spinner dropdowns.
 * Includes advanced filtering restrictions to limit available items in the other list based upon selection.
 * <p/>
 * Created by James on 11/10/2015.
 */
public final class DualSpinnerDialogBuilder {
    private AlertDialog.Builder builder;
    private AppCompatSpinner firstSpinner, secondSpinner;
    private List<String> first, second;
    private int firstSelected, secondSelected;
    private int[][] firstRestrictingSecond, secondRestrictingFirst;
    private DualSpinnerListener listener = null;

    /**
     * Private constructor
     *
     * @param context Context
     * @param style   Style
     */
    private DualSpinnerDialogBuilder(@NonNull final Context context, final int style) {
        builder = new AlertDialog.Builder(context, style);
        LayoutInflater vi = LayoutInflater.from(context);
        LinearLayout mainView = (LinearLayout) vi.inflate(R.layout.dialog_dual_spinner_layout, null);
        firstSpinner = (AppCompatSpinner) mainView.findViewById(R.id.firstSpinner);
        secondSpinner = (AppCompatSpinner) mainView.findViewById(R.id.secondSpinner);
        builder.setView(mainView);
    }

    /**
     * Builder
     *
     * @param context Context
     * @param style   Style
     * @return Dialog builder
     */
    @NonNull
    public static DualSpinnerDialogBuilder with(@NonNull final Context context, final int style) {
        return new DualSpinnerDialogBuilder(context, style);
    }

    /**
     * @param title Dialog title
     * @return Dialog builder
     */
    @NonNull
    public final DualSpinnerDialogBuilder setTitle(@NonNull final String title) {
        builder.setTitle(title);
        return this;
    }

    /**
     * @param first             List of first spinner items
     * @param firstSelected     Selected item in first spinner
     * @param firstRestrictions Restrictions
     * @return Dialog builder
     */
    @NonNull
    public final DualSpinnerDialogBuilder setFirst(@NonNull final List<String> first, final int firstSelected, @Nullable final int[][] firstRestrictions) {
        this.first = first;
        this.firstSelected = firstSelected;
        this.firstRestrictingSecond = firstRestrictions;
        return this;
    }

    /**
     * @param second             List of second spinner items
     * @param secondSelected     Selected item in second spinner
     * @param secondRestrictions Restrictions
     * @return Dialog builder
     */
    @NonNull
    public final DualSpinnerDialogBuilder setSecond(@NonNull final List<String> second, final int secondSelected, @Nullable final int[][] secondRestrictions) {
        this.second = second;
        this.secondSelected = secondSelected;
        this.secondRestrictingFirst = secondRestrictions;
        return this;
    }

    /**
     * @param listener Dialog selection listener
     * @return Dialog builder
     */
    @NonNull
    public final DualSpinnerDialogBuilder setListener(@NonNull final DualSpinnerListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Builds the dialog
     *
     * @return Alert dialog. Must call execute
     */
    @NonNull
    public final AlertDialog build() {
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

    /**
     * Sets all the data for the spinners.
     *
     * @param firstSelected1      Currently selected item in first spinner
     * @param firstSelected1Prev  Previously selected item in first spinner
     * @param secondSelected1     Currently selected item in second spinner
     * @param secondSelected1Prev Previously selected item in second spinner
     */
    private void setData(final int firstSelected1, final int firstSelected1Prev, final int secondSelected1, final int secondSelected1Prev) {
        firstSpinner.setOnItemSelectedListener(null);
        secondSpinner.setOnItemSelectedListener(null);
        List<String> firstList, secondList;
        int firstSelected = firstSelected1;
        int secondSelected = secondSelected1;
        if (firstRestrictingSecond != null) {
            secondList = new ArrayList<>();
            for (int i = firstRestrictingSecond[firstSelected1][0]; i < firstRestrictingSecond[firstSelected1][1]; i++) {
                secondList.add(second.get(i));
            }
            int prevStartIndex = firstRestrictingSecond[firstSelected1Prev][0];
            int currStartIndex = firstRestrictingSecond[firstSelected1][0];
            if (prevStartIndex != currStartIndex) {
                secondSelected = secondSelected1 + (prevStartIndex - currStartIndex);
            }
        } else {
            secondList = second;
        }
        if (secondRestrictingFirst != null) {
            firstList = new ArrayList<>();
            for (int i = secondRestrictingFirst[secondSelected1][0]; i < secondRestrictingFirst[secondSelected1][1]; i++) {
                firstList.add(first.get(i));
            }
            int prevStartIndex = secondRestrictingFirst[secondSelected1Prev][0];
            int currStartIndex = secondRestrictingFirst[secondSelected1][0];
            if (prevStartIndex != currStartIndex) {
                firstSelected = firstSelected1 + (prevStartIndex - currStartIndex);
            }
        } else {
            firstList = first;
        }
        final int firstSelected2 = Math.min(firstSelected, firstList.size() - 1);
        final int secondSelected2 = Math.min(secondSelected, secondList.size() - 1);
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
            public void onItemSelected(@NonNull final AdapterView<?> parent, @NonNull final View view, final int position, final long id) {
                if (position != firstSelected2) {
                    setData(position, firstSelected2, secondSpinner.getSelectedItemPosition(), secondSpinner.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(@NonNull AdapterView<?> parent) {

            }
        });
        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NonNull final AdapterView<?> parent, @NonNull final View view, final int position, final long id) {
                if (position != secondSelected2) {
                    setData(firstSpinner.getSelectedItemPosition(), firstSpinner.getSelectedItemPosition(), position, secondSelected2);
                }
            }

            @Override
            public void onNothingSelected(@NonNull AdapterView<?> parent) {

            }
        });
    }

    /**
     * Dialog Spinner listener
     */
    public interface DualSpinnerListener {

        /**
         * @param firstSelected  Selected item in first spinner
         * @param secondSelected Selected item in second spinner
         */
        void onSelection(final int firstSelected, final int secondSelected);

        /**
         * Dialog canceled
         */
        void onCancel();
    }

}
