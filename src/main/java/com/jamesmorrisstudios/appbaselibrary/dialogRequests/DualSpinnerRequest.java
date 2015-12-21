package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.dialogs.DualSpinnerDialogBuilder;

import java.util.List;

/**
 * Request to build a dual spinner dialog.
 * Call execute to display the dialog
 * <p/>
 * Created by James on 11/10/2015.
 */
public final class DualSpinnerRequest extends AbstractDialogRequest {
    public final String title;
    public final List<String> first, second;
    public final int firstSelected, secondSelected;
    public final int[][] firstRestrictingSecond, secondRestrictingFirst;
    public final DualSpinnerDialogBuilder.DualSpinnerListener listener;

    /**
     * @param title          Title
     * @param first          List of first spinner
     * @param firstSelected  First spinner selected index
     * @param second         List of second spinner
     * @param secondSelected Second spinner selected index
     * @param listener       Listener
     */
    public DualSpinnerRequest(@NonNull String title, @NonNull List<String> first, int firstSelected, @NonNull List<String> second, int secondSelected, @NonNull DualSpinnerDialogBuilder.DualSpinnerListener listener) {
        this.title = title;
        this.first = first;
        this.second = second;
        this.firstSelected = firstSelected;
        this.secondSelected = secondSelected;
        this.firstRestrictingSecond = null;
        this.secondRestrictingFirst = null;
        this.listener = listener;
    }

    /**
     * Note: firstRestrictingSecond should be the same length as first spinner array.
     * The index of the selected item in the first spinner matches to the index of firstRestrictingSecond array.
     * The 2d array [0] item is the first allowed index in second spinner and [1] item is the max allowed index in the second spinner.
     *
     * @param title                  Title
     * @param first                  List of first spinner
     * @param firstSelected          First spinner selected index
     * @param firstRestrictingSecond Restrictions of allowed values in the second spinner based on first spinner selection
     * @param second                 List of second spinner
     * @param secondSelected         Second spinner selected index
     * @param listener               Listener
     */
    public DualSpinnerRequest(@NonNull String title, @NonNull List<String> first, int firstSelected, @NonNull int[][] firstRestrictingSecond, @NonNull List<String> second, int secondSelected, @NonNull DualSpinnerDialogBuilder.DualSpinnerListener listener) {
        this.title = title;
        this.first = first;
        this.second = second;
        this.firstSelected = firstSelected;
        this.secondSelected = secondSelected;
        this.firstRestrictingSecond = firstRestrictingSecond;
        this.secondRestrictingFirst = null;
        this.listener = listener;
    }

    /**
     * Note: secondRestrictingFirst should be the same length as second spinner array.
     * The index of the selected item in the second spinner matches to the index of secondRestrictingFirst array.
     * The 2d array [0] item is the first allowed index in first spinner and [1] item is the max allowed index in the first spinner.
     *
     * @param title                  Title
     * @param first                  List of first spinner
     * @param firstSelected          First spinner selected index
     * @param second                 List of second spinner
     * @param secondSelected         Second spinner selected index
     * @param secondRestrictingFirst Restrictions of allowed values in the first spinner based on second spinner selection
     * @param listener               Listener
     */
    public DualSpinnerRequest(@NonNull String title, @NonNull List<String> first, int firstSelected, @NonNull List<String> second, int secondSelected, @NonNull int[][] secondRestrictingFirst, @NonNull DualSpinnerDialogBuilder.DualSpinnerListener listener) {
        this.title = title;
        this.first = first;
        this.second = second;
        this.firstSelected = firstSelected;
        this.secondSelected = secondSelected;
        this.firstRestrictingSecond = null;
        this.secondRestrictingFirst = secondRestrictingFirst;
        this.listener = listener;
    }

}
