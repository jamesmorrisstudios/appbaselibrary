package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.jamesmorrisstudios.appbaselibrary.R;

/**
 * Edit Text dialog builder
 * <p/>
 * Created by James on 12/22/2015.
 */
public class EditTextDialogBuilder {
    private AlertDialog.Builder builder;
    private EditText editText;

    /**
     * @param context Context
     * @param style   Style
     */
    private EditTextDialogBuilder(@NonNull final Context context, final int style) {
        builder = new AlertDialog.Builder(context, style);
        LayoutInflater vi = LayoutInflater.from(context);
        editText = (EditText) vi.inflate(R.layout.dialog_edit_text, null);
        builder.setView(editText);
    }

    /**
     * @param context Context
     * @param style   Style
     * @return Dialog Builder
     */
    @NonNull
    public static EditTextDialogBuilder with(@NonNull final Context context, final int style) {
        return new EditTextDialogBuilder(context, style);
    }

    /**
     * @param title String title
     * @return Dialog Builder
     */
    @NonNull
    public final EditTextDialogBuilder setTitle(@NonNull final String title) {
        builder.setTitle(title);
        return this;
    }

    /**
     * @param message String message
     * @return Dialog Builder
     */
    @NonNull
    public final EditTextDialogBuilder setMessage(@NonNull final String message) {
        builder.setMessage(message);
        return this;
    }

    /**
     * @param text String text
     * @param hint String hint
     * @return Dialog Builder
     */
    @NonNull
    public final EditTextDialogBuilder setText(@NonNull final String text, @NonNull final String hint) {
        editText.setText(text);
        editText.setHint(hint);
        return this;
    }

    /**
     * @param listener Option picker listener
     * @return Dialog Builder
     */
    @NonNull
    public final EditTextDialogBuilder setListener(@NonNull final OnEditTextDialogListener listener, @NonNull final String positiveText) {
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public final void onClick(DialogInterface dialog, int which) {
                listener.onCommit(editText.getText().toString());
            }
        });
        return this;
    }

    /**
     * @param listener Option picker listener
     * @return Dialog Builder
     */
    @NonNull
    public final EditTextDialogBuilder setListener(@NonNull final OnEditTextDialogListener listener, @NonNull final String positiveText, @NonNull final String negativeText) {
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public final void onClick(@NonNull final DialogInterface dialog, final int which) {
                listener.onCommit(editText.getText().toString());
            }
        });
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public final void onClick(@NonNull final DialogInterface dialog, final int which) {

            }
        });
        return this;
    }

    /**
     * Builds the dialog
     *
     * @return Alert dialog. Must call execute
     */
    @NonNull
    public final AlertDialog build() {
        return builder.create();
    }

    /**
     * Edit Text listener
     */
    public interface OnEditTextDialogListener {

        /**
         * Commit changes
         *
         * @param text Set Text
         */
        void onCommit(@NonNull final String text);

    }

}
