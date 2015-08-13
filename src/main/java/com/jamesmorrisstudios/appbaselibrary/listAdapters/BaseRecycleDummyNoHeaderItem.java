package com.jamesmorrisstudios.appbaselibrary.listAdapters;

/**
 * Created by James on 7/10/2015.
 */
public class BaseRecycleDummyNoHeaderItem extends BaseRecycleNoHeaderContainer {


    /**
     * Constructor for dummy item
     *
     */
    public BaseRecycleDummyNoHeaderItem() {
        super(true);
    }

    @Override
    public BaseRecycleItem getHeader() {
        return null;
    }

    @Override
    public BaseRecycleItem getItem() {
        return null;
    }
}
