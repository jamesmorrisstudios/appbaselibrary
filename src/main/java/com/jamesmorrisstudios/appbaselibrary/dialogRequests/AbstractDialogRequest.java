package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import com.jamesmorrisstudios.appbaselibrary.Bus;

/**
 * Base implementation of the dialog request
 * <p/>
 * Created by James on 12/7/2015.
 */
public abstract class AbstractDialogRequest {

    /**
     * Show the current dialog
     */
    public final void show() {
        Bus.postObject(this);
    }

}
