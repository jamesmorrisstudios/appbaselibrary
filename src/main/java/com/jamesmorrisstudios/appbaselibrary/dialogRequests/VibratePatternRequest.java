package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.dialogs.VibratePatternDialog;

/**
 * Vibrate pattern builder request
 * <p/>
 * Created by James on 11/25/2015.
 */
public final class VibratePatternRequest extends AbstractDialogRequest {
    public final String title;
    public final long[] pattern;
    public final VibratePatternDialog.VibratePatternListener listener;

    /**
     * @param title    Pattern Title
     * @param pattern  Existing pattern. Even length array
     * @param listener Listener
     */
    public VibratePatternRequest(@NonNull final String title, @NonNull final long[] pattern, @NonNull final VibratePatternDialog.VibratePatternListener listener) {
        this.title = title;
        this.pattern = pattern;
        this.listener = listener;
    }

}
