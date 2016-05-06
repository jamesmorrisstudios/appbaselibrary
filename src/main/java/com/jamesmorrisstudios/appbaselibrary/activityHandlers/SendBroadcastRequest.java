package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by James on 5/5/2016.
 */
public class SendBroadcastRequest extends AbstractActivityRequest {
    public final Intent intent;

    /**
     * Starts the activity indicated in the intent.
     *
     * @param intent Activity to start
     */
    public SendBroadcastRequest(@NonNull final Intent intent) {
        this.intent = intent;
    }

}
