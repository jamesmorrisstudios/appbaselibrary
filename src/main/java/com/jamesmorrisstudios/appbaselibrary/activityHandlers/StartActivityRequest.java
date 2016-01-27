package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Start an activity with no result.
 * The activity will be started from the current primary activity.
 * <p/>
 * Created by James on 12/7/2015.
 */
public final class StartActivityRequest extends AbstractActivityRequest {
    public final Intent intent;

    /**
     * Starts the activity indicated in the intent.
     *
     * @param intent Activity to start
     */
    public StartActivityRequest(@NonNull final Intent intent) {
        this.intent = intent;
    }

}
