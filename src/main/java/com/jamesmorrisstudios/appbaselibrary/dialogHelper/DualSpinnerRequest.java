package com.jamesmorrisstudios.appbaselibrary.dialogHelper;

import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.dialogs.DualSpinnerDialogBuilder;

import java.util.List;

/**
 * Created by James on 11/10/2015.
 */
public class DualSpinnerRequest {
    public final String title;
    public final List<String> first, second;
    public final int firstSelected, secondSelected;
    public final DualSpinnerDialogBuilder.DualSpinnerListener listener;

    public DualSpinnerRequest(@NonNull String title, @NonNull List<String> first, int firstSelected, @NonNull List<String> second, int secondSelected, @NonNull DualSpinnerDialogBuilder.DualSpinnerListener listener) {
        this.title = title;
        this.first = first;
        this.second = second;
        this.firstSelected = firstSelected;
        this.secondSelected = secondSelected;
        this.listener = listener;
    }
}
