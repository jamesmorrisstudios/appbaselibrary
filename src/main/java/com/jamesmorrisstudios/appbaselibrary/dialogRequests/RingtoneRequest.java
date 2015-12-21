package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Request to build a ringtone dialog.
 * Call execute to display the dialog
 * <p/>
 * Created by James on 6/29/2015.
 */
public final class RingtoneRequest extends AbstractDialogRequest {
    public final Uri currentTone;
    public final String title;
    public final RingtoneRequestListener listener;

    /**
     * @param currentTone Uri of current tone. Null if none
     * @param title       Title of dialog
     * @param listener    Listener
     */
    public RingtoneRequest(@Nullable Uri currentTone, @NonNull String title, @NonNull RingtoneRequestListener listener) {
        this.currentTone = currentTone;
        this.title = title;
        this.listener = listener;
    }

    /**
     * Ringtone callback listener
     */
    public interface RingtoneRequestListener {

        /**
         * @param currentTone Uri of selected tone. Null if none
         * @param name        name of selected tone. Null if none.
         */
        void ringtoneResponse(@Nullable Uri currentTone, @Nullable String name);
    }

}
