package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.jamesmorrisstudios.appbaselibrary.dialogs.EditTextListDialog;

import java.util.ArrayList;

/**
 * Request to build a edit text list dialog.
 * Call execute to display the dialog
 * <p/>
 * Created by James on 6/30/2015.
 */
public final class EditTextListRequest extends AbstractDialogRequest {
    public final String title;
    public final ArrayList<String> messages;
    public final EditTextListDialog.EditTextListListener onPositive;
    public final View.OnClickListener onNegative;

    /**
     * @param title      Title
     * @param messages   List of current text strings
     * @param onPositive onPositive
     * @param onNegative onNegative
     */
    public EditTextListRequest(@NonNull String title, @NonNull ArrayList<String> messages, @NonNull EditTextListDialog.EditTextListListener onPositive, @Nullable View.OnClickListener onNegative) {
        this.title = title;
        this.messages = messages;
        this.onPositive = onPositive;
        this.onNegative = onNegative;
    }

}
