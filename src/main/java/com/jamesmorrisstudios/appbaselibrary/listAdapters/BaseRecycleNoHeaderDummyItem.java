package com.jamesmorrisstudios.appbaselibrary.listAdapters;

/**
 * Created by James on 7/10/2015.
 */
public class BaseRecycleNoHeaderDummyItem extends BaseRecycleNoHeaderContainer {

    /**
     * Constructor for dummy item
     *
     */
    public BaseRecycleNoHeaderDummyItem() {
        super(true);
    }

    @Override
    public BaseRecycleItem getItem() {
        return null;
    }
}
