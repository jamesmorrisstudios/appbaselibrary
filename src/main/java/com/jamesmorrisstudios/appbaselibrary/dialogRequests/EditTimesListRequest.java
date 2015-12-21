package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.jamesmorrisstudios.appbaselibrary.dialogs.EditTimesListDialog;
import com.jamesmorrisstudios.appbaselibrary.time.TimeItem;

import java.util.ArrayList;


/**
 * Request to build a edit time list dialog.
 * Call execute to display the dialog
 * <p/>
 * Created by James on 6/30/2015.
 */
public final class EditTimesListRequest extends AbstractDialogRequest {
    public final String title;
    public final ArrayList<TimeItem> times;
    public final EditTimesListDialog.EditTimesListListener onPositive;
    public final View.OnClickListener onNegative;

    /**
     * @param title      Dialog title
     * @param times      List of current times
     * @param onPositive onPositive
     * @param onNegative onNegative
     */
    public EditTimesListRequest(@NonNull String title, @NonNull ArrayList<TimeItem> times, @NonNull EditTimesListDialog.EditTimesListListener onPositive, @Nullable View.OnClickListener onNegative) {
        this.title = title;
        this.times = times;
        this.onPositive = onPositive;
        this.onNegative = onNegative;
    }
}
