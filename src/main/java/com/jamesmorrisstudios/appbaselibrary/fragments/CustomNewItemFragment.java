package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.UtilsTheme;
import com.jamesmorrisstudios.appbaselibrary.listeners.AfterTextChangedWatcher;
import com.nononsenseapps.filepicker.NewItemFragment;

/**
 * Customized new item fragment for the file browser to ensure dialogs are styled properly
 * <p/>
 * Created by James on 11/12/2015.
 */
public abstract class CustomNewItemFragment extends NewItemFragment {
    private OnNewFolderListener listener = null;

    /**
     * @param listener Listener
     */
    public void setListener(@NonNull final OnNewFolderListener listener) {
        this.listener = listener;
    }

    /**
     * @param savedInstanceState saved instance state
     * @return Dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), UtilsTheme.getAlertDialogStyle());
        builder.setView(R.layout.nnf_dialog_folder_name)
                .setTitle(R.string.nnf_new_folder)
                .setNegativeButton(android.R.string.cancel,
                        null)
                .setPositiveButton(android.R.string.ok,
                        null);
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog1) {
                final AlertDialog dialog = (AlertDialog) dialog1;
                final EditText editText = (EditText) dialog.findViewById(R.id.edit_text);
                Button cancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                final Button ok = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                // Start disabled
                ok.setEnabled(false);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String itemName = editText.getText().toString();
                        if (validateName(itemName)) {
                            if (listener != null) {
                                listener.onNewFolder(itemName);
                            }
                            dialog.dismiss();
                        }
                    }
                });
                editText.addTextChangedListener(new AfterTextChangedWatcher() {
                    @Override
                    public void afterTextChanged(@NonNull final Editable s) {
                        ok.setEnabled(validateName(s.toString()));
                    }
                });
            }
        });
        return dialog;
    }

}
