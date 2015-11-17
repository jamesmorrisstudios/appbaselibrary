package com.jamesmorrisstudios.appbaselibrary.dialogHelper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jamesmorrisstudios.appbaselibrary.dialogs.DualSpinnerDialogBuilder;

import java.util.HashMap;
import java.util.List;

/**
 * Created by James on 11/10/2015.
 */
public class DualSpinnerRequest {
    public final String title;
    public final List<String> first, second;
    public final int firstSelected, secondSelected;
    public final int[] firstRestrictions, secondRestrictions;
    public final DualSpinnerDialogBuilder.DualSpinnerListener listener;

    public DualSpinnerRequest(@NonNull String title, @NonNull List<String> first, int firstSelected, @NonNull List<String> second, int secondSelected, @NonNull DualSpinnerDialogBuilder.DualSpinnerListener listener) {
        this.title = title;
        this.first = first;
        this.second = second;
        this.firstSelected = firstSelected;
        this.secondSelected = secondSelected;
        this.firstRestrictions = null;
        this.secondRestrictions = null;
        this.listener = listener;
    }

    public DualSpinnerRequest(@NonNull String title, @NonNull List<String> first, int firstSelected, @Nullable int[] firstRestrictions, @NonNull List<String> second, int secondSelected, @Nullable int[] secondRestrictions, @NonNull DualSpinnerDialogBuilder.DualSpinnerListener listener) {
        this.title = title;
        this.first = first;
        this.second = second;
        this.firstSelected = firstSelected;
        this.secondSelected = secondSelected;
        this.firstRestrictions = firstRestrictions;
        this.secondRestrictions = secondRestrictions;
        this.listener = listener;
    }
}
