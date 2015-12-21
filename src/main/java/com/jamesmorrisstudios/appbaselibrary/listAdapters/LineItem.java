package com.jamesmorrisstudios.appbaselibrary.listAdapters;

import android.support.annotation.NonNull;

/**
 * Recycle view line item
 * <p/>
 * Created by James on 8/13/2015.
 */
public final class LineItem {
    public BaseRecycleContainer data;

    /**
     * Constructor
     *
     * @param data Reminder line item data
     */
    public LineItem(@NonNull BaseRecycleContainer data) {
        this.data = data;
    }
}
