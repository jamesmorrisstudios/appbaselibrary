package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Snackbar request
 * <p/>
 * Created by James on 12/10/2015.
 */
public final class SnackbarRequest extends AbstractActivityRequest {
    public final String text, actionText;
    public final View.OnClickListener actionListener;
    public final SnackBarDuration duration;

    /**
     * Create a text only snackbar
     *
     * @param text     Text
     * @param duration Duration
     */
    public SnackbarRequest(@NonNull String text, @NonNull SnackBarDuration duration) {
        this.text = text;
        this.duration = duration;
        this.actionText = null;
        this.actionListener = null;
    }

    /**
     * Create a text with action snackbar
     *
     * @param text           Text
     * @param duration       Duration
     * @param actionText     Action text
     * @param actionListener Action listener
     */
    public SnackbarRequest(@NonNull String text, @NonNull SnackBarDuration duration, @NonNull String actionText, @NonNull View.OnClickListener actionListener) {
        this.text = text;
        this.duration = duration;
        this.actionText = actionText;
        this.actionListener = actionListener;
    }

    /**
     * Snackbar popup duration.
     * Can be swiped away by the user
     */
    public enum SnackBarDuration {
        SHORT,
        LONG,
        INDEFINITE;

        /**
         * @return Duration integer
         */
        public int getDuration() {
            switch (this) {
                case SHORT:
                    return Snackbar.LENGTH_SHORT;
                case LONG:
                    return Snackbar.LENGTH_LONG;
                case INDEFINITE:
                    return Snackbar.LENGTH_INDEFINITE;
                default:
                    return Snackbar.LENGTH_SHORT;
            }
        }
    }

}
