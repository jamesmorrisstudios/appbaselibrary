package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.dialogs.EditTextDialogBuilder;

/**
 * Edit text prompt dialog
 *
 * Created by James on 12/22/2015.
 */
public final class EditTextRequest extends AbstractDialogRequest{
    public final String title, message, text, hint;
    public final EditTextDialogBuilder.OnEditTextDialogListener listener;

    /**
     * Constructor
     *
     * @param title Dialog title
     * @param text Current text for the edit text view
     * @param hint Hint text for the edit text view
     * @param listener Listener
     */
    public EditTextRequest(@NonNull final String title, @NonNull final String text, @NonNull final String hint, @NonNull final EditTextDialogBuilder.OnEditTextDialogListener listener) {
        this.title = title;
        this.message = null;
        this.text = text;
        this.hint = hint;
        this.listener = listener;
    }

    /**
     * Constructor
     *
     * @param title Dialog title
     * @param message Dialog message text
     * @param text Current text for the edit text view
     * @param hint Hint text for the edit text view
     * @param listener Listener
     */
    public EditTextRequest(@NonNull final String title, @NonNull final String message, @NonNull final String text, @NonNull final String hint, @NonNull final EditTextDialogBuilder.OnEditTextDialogListener listener) {
        this.title = title;
        this.message = message;
        this.text = text;
        this.hint = hint;
        this.listener = listener;
    }

}
