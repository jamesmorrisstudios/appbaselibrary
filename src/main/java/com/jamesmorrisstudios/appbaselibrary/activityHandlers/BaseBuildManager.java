package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.Bus;
import com.jamesmorrisstudios.appbaselibrary.activities.BaseActivity;

/**
 * Abstract base for activity handlers.
 * <p/>
 * Created by James on 12/7/2015.
 */
public abstract class BaseBuildManager {
    private BaseBuildManagerListener listener;

    /**
     * Attach to the containing activity. Call from onCreate
     *
     * @param listener Activity implementing the build manager listener.
     */
    public final void attach(@NonNull final BaseBuildManagerListener listener) {
        this.listener = listener;
        Bus.register(this);
    }

    /**
     * Detach from activity. Call from onDestroy
     */
    public final void detach() {
        this.listener = null;
        Bus.unregister(this);
    }

    /**
     * NEVER store the result from this function.
     *
     * @return The containing activity
     */
    @NonNull
    protected final BaseActivity getActivity() {
        return listener.getActivity();
    }

    /**
     * Listener to retrieve the attached activity.
     */
    public interface BaseBuildManagerListener {

        /**
         * NEVER store the result from this function.
         * @return The base Activity
         */
        @NonNull
        BaseActivity getActivity();
    }

}
