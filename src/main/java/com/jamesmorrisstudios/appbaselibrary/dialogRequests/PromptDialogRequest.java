package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Request to build a prompt dialog.
 * Call execute to display the dialog
 * <p/>
 * Created by James on 6/29/2015.
 */
public final class PromptDialogRequest extends AbstractDialogRequest {
    public final String title, content;
    public final DialogInterface.OnClickListener onPositive, onNegative;
    public final String positiveText, negativeText;

    /**
     * Prompt with only ok/cancel buttons
     *
     * @param title      Title
     * @param content    Content text
     * @param onPositive onPositive
     * @param onNegative onNegative
     */
    public PromptDialogRequest(@NonNull final String title, @NonNull final String content, @NonNull final DialogInterface.OnClickListener onPositive, @NonNull final DialogInterface.OnClickListener onNegative) {
        this.title = title;
        this.content = content;
        this.onPositive = onPositive;
        this.onNegative = onNegative;
        this.positiveText = null;
        this.negativeText = null;
    }

    /**
     * Prompt with custom button text
     *
     * @param title        Title
     * @param content      Content text
     * @param onPositive   onPositive
     * @param positiveText positive text
     * @param onNegative   onNegative
     * @param negativeText negative text
     */
    public PromptDialogRequest(@NonNull final String title, @NonNull final String content, @NonNull final DialogInterface.OnClickListener onPositive, @Nullable String positiveText, @NonNull DialogInterface.OnClickListener onNegative, @Nullable String negativeText) {
        this.title = title;
        this.content = content;
        this.onPositive = onPositive;
        this.onNegative = onNegative;
        this.positiveText = positiveText;
        this.negativeText = negativeText;
    }

}
