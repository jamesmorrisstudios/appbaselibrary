package com.jamesmorrisstudios.appbaselibrary.listAdapters;

import android.support.annotation.NonNull;

/**
 * Created by James on 8/13/2015.
 */
public class LineItem {
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
