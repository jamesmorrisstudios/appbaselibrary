package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import com.jamesmorrisstudios.appbaselibrary.Bus;

/**
 * Base implementation of the activity request
 * <p/>
 * Created by James on 12/7/2015.
 */
public abstract class AbstractActivityRequest {

    /**
     * Execute this action
     */
    public final void execute() {
        Bus.postObject(this);
    }

}
