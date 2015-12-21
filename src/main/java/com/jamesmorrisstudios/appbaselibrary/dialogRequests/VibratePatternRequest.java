package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.dialogs.VibratePatternDialog;

/**
 * Vibrate pattern builder request
 * <p/>
 * Created by James on 11/25/2015.
 */
public class VibratePatternRequest extends AbstractDialogRequest {
    public final String title;
    public final long[] pattern;
    public final VibratePatternDialog.VibratePatternListener listener;

    /**
     * @param title    Pattern Title
     * @param pattern  Existing pattern. Even length array
     * @param listener Listener
     */
    public VibratePatternRequest(@NonNull String title, @NonNull long[] pattern, @NonNull VibratePatternDialog.VibratePatternListener listener) {
        this.title = title;
        this.pattern = pattern;
        this.listener = listener;
    }

}
