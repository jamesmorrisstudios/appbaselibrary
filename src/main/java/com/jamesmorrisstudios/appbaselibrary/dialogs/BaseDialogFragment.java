package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.support.v4.app.DialogFragment;

/**
 * Base dialog fragment
 *
 * Created by James on 12/21/2015.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    /**
     * Dismiss dialog on pause
     */
    public final void onPause() {
        dismiss();
        super.onPause();
    }

}
