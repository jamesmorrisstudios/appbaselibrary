package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.Bus;
import com.jamesmorrisstudios.appbaselibrary.activities.BaseLauncherNoViewActivity;

/**
 * Created by James on 12/7/2015.
 */
public class BaseBuildManager {
    private BaseBuildManagerListener listener;

    public void attach(@NonNull BaseBuildManagerListener listener) {
        this.listener = listener;
        Bus.register(this);
    }

    public void detach() {
        this.listener = null;
        Bus.unregister(this);
    }

    protected final BaseLauncherNoViewActivity getActivity() {
        return listener.getActivity();
    }

    public interface BaseBuildManagerListener {
        BaseLauncherNoViewActivity getActivity();
    }

}
