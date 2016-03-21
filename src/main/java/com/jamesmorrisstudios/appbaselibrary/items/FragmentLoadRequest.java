package com.jamesmorrisstudios.appbaselibrary.items;

import android.os.Bundle;

/**
 * Fragment load request for when a fragment load has to be delayed due to existing animation or other
 * <p/>
 * Created by James on 12/15/2015.
 */
public final class FragmentLoadRequest {
    public boolean active = false;
    public String tag = null;
    public Bundle bundle = null;
    public int scrollY = -1;

    /**
     * Clear the load back to deactivated state
     */
    public final void clearData() {
        this.active = false;
        this.tag = null;
        this.bundle = null;
        this.scrollY = -1;
    }

}
