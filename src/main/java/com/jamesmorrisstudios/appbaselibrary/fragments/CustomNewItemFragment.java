package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.ThemeManager;
import com.nononsenseapps.filepicker.NewItemFragment;

/**
 * Created by James on 11/12/2015.
 */
public abstract class CustomNewItemFragment extends NewItemFragment  {

    private OnNewFolderListener listener = null;

    public void setListener(final OnNewFolderListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), ThemeManager.getAlertDialogStyle());
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

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(final CharSequence s, final int start,
                                                  final int count, final int after) {
                    }

                    @Override
                    public void onTextChanged(final CharSequence s, final int start,
                                              final int before, final int count) {
                    }

                    @Override
                    public void afterTextChanged(final Editable s) {
                        ok.setEnabled(validateName(s.toString()));
                    }
                });
            }
        });


        return dialog;
    }

}
