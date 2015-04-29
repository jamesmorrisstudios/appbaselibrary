package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.jamesmorrisstudios.materialuilibrary.dialogs.ColorSelector;
import com.jamesmorrisstudios.materialuilibrary.dialogs.MaterialDialog;
import com.jamesmorrisstudios.materialuilibrary.dialogs.time.TimePickerDialog;

/**
 * Base app fragment
 *
 * Created by James on 4/29/2015.
 */
public abstract class BaseFragment extends Fragment {
    protected OnDialogListener dialogListener;
    protected OnUtilListener utilListener;

    public abstract void onBack();

    /**
     * @param activity Activity to attach to
     */
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            dialogListener = (OnDialogListener) activity;
            utilListener = (OnUtilListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDialogListener and OnUtilListener");
        }
    }

    /**
     * Detach from activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        dialogListener = null;
        utilListener = null;
    }

    /**
     *
     */
    public interface OnDialogListener {

        /**
         * Build a new time picker dialog
         * @param listener Return listener
         * @param hour Start hour
         * @param minute Start minute
         * @param is24Hour True if 24 hour mode
         */
        void createTimePickerDialog(@NonNull TimePickerDialog.OnTimeSetListener listener, int hour, int minute, boolean is24Hour);

        /**
         * Build a ok/cancel prompt
         * @param title Title of the prompt
         * @param content Content text
         * @param callback Callback listener
         */
        void createPromptDialog(@NonNull String title, @NonNull String content, MaterialDialog.ButtonCallback callback);

        void createColorPickerDialog(int intialColor, ColorSelector.OnColorSelectedListener onColorSelectedListener);
    }

    /**
     *
     */
    public interface OnUtilListener {

        /**
         * Go back from this fragment
         */
        void goBackFromFragment();

        /**
         * Hides the keyboard
         */
        void hideKeyboard();
    }

}
