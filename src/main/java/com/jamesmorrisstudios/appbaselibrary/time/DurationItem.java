package com.jamesmorrisstudios.appbaselibrary.time;

import com.google.gson.annotations.SerializedName;

/**
 * Created by James on 1/5/2016.
 */
public class DurationItem {
    @SerializedName("duration")
    public long duration;

    public DurationItem(final long duration) {
        this.duration = duration;
    }

}
