package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * App Restart Request
 * <p/>
 * Created by James on 12/8/2015.
 */
public final class RestartAppRequest extends AbstractActivityRequest {
    public final String pageTag;
    public final int scrollY;
    public final Bundle bundle;

    /**
     * Constructor
     *
     * @param pageTag Load to page tag
     * @param scrollY ScrollY position
     * @param bundle  Bundle
     */
    public RestartAppRequest(@Nullable String pageTag, int scrollY, @Nullable Bundle bundle) {
        this.pageTag = pageTag;
        this.scrollY = scrollY;
        this.bundle = bundle;
    }

}
