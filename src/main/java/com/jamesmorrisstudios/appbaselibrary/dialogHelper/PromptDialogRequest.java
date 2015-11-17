package com.jamesmorrisstudios.appbaselibrary.dialogHelper;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by James on 6/29/2015.
 */
public class PromptDialogRequest {
    public final String title, content;
    public final DialogInterface.OnClickListener onPositive, onNegative;
    public final String positiveText, negativeText;

    public PromptDialogRequest(@NonNull String title, @NonNull String content, DialogInterface.OnClickListener onPositive, DialogInterface.OnClickListener onNegative) {
        this.title = title;
        this.content = content;
        this.onPositive = onPositive;
        this.onNegative = onNegative;
        this.positiveText = null;
        this.negativeText = null;
    }

    public PromptDialogRequest(@NonNull String title, @NonNull String content, DialogInterface.OnClickListener onPositive, @Nullable String positiveText, DialogInterface.OnClickListener onNegative, @Nullable String negativeText) {
        this.title = title;
        this.content = content;
        this.onPositive = onPositive;
        this.onNegative = onNegative;
        this.positiveText = positiveText;
        this.negativeText = negativeText;
    }

}
