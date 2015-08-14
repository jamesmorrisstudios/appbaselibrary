package com.jamesmorrisstudios.appbaselibrary.listAdapters;

/**
 * Created by James on 7/10/2015.
 */
public class BaseRecycleDummyItem extends BaseRecycleContainer {


    /**
     * Constructor for dummy item
     *
     */
    public BaseRecycleDummyItem() {
        super(false, true);
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
