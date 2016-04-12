package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jamesmorrisstudios.appbaselibrary.Utils;

/**
 * Start an activity and callback with the result.
 * The activity will be started from the current primary activity.
 * <p/>
 * Created by James on 12/7/2015.
 */
public final class StartActivityForResultRequest extends AbstractActivityRequest {
    public final Intent intent;
    public final int requestCode;
    public final OnStartActivityForResultListener listener;

    /**
     * Starts the activity indicated in the intent.
     *
     * @param intent   Intent containing activity to start.
     * @param listener Result listener
     */
    public StartActivityForResultRequest(@NonNull Intent intent, @NonNull OnStartActivityForResultListener listener) {
        this.intent = intent;
        this.requestCode = Utils.getNextRequestCode();
        this.listener = listener;
    }

    /**
     * Listener for a start activity for result request
     */
    public interface OnStartActivityForResultListener {

        /**
         * @param intent Result Intent if available
         */
        void resultOk(@Nullable Intent intent);

        /**
         * Result failed
         */
        void resultFailed();
    }

}
