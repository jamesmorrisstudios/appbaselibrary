package com.jamesmorrisstudios.appbaselibrary.dialogHelper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.jamesmorrisstudios.appbaselibrary.dialogs.EditTextListDialog;

import java.util.ArrayList;

/**
 * Created by James on 6/30/2015.
 */
public class EditTextListRequest {
    public final ArrayList<String> messages;
    public final EditTextListDialog.EditMessageListener onPositive;
    public final View.OnClickListener onNegative;

    public EditTextListRequest(@NonNull ArrayList<String> messages, @NonNull EditTextListDialog.EditMessageListener onPositive, @Nullable View.OnClickListener onNegative) {
        this.messages = messages;
        this.onPositive = onPositive;
        this.onNegative = onNegative;
    }

}
