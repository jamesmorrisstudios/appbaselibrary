package com.jamesmorrisstudios.appbaselibrary.dialogHelper;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by James on 6/29/2015.
 */
public class RingtoneRequest {
    public final Uri currentTone;
    public final String title;
    public final RingtoneRequestListener listener;

    public RingtoneRequest(@Nullable Uri currentTone, @NonNull String title, @NonNull RingtoneRequestListener listener) {
        this.currentTone = currentTone;
        this.title = title;
        this.listener = listener;
    }

    public interface RingtoneRequestListener {
        void ringtoneResponse(@Nullable Uri currentTone, @Nullable String title);
    }

}
