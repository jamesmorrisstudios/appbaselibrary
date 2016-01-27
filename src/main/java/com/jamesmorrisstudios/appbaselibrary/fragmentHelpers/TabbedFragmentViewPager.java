package com.jamesmorrisstudios.appbaselibrary.fragmentHelpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Tabbed Fragment view pager.
 * <p/>
 * Created by James on 12/8/2015.
 */
public final class TabbedFragmentViewPager extends PagerAdapter {
    private Context mContext;
    private TabbedFragmentBaseTab[] tabs = null;

    /**
     * Constructor
     *
     * @param context Context
     * @param tabs    Tab Array
     */
    public TabbedFragmentViewPager(@NonNull final Context context, @NonNull final TabbedFragmentBaseTab[] tabs) {
        mContext = context;
        this.tabs = tabs;
    }

    /**
     * Instantiate tab
     *
     * @param collection Parent view
     * @param position   Tab Position
     * @return Tab View
     */
    @NonNull
    @Override
    public ViewGroup instantiateItem(@NonNull final ViewGroup collection, final int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(tabs[position].getViewLayoutId(), null);
        collection.addView(layout);
        tabs[position].viewActive(layout, mContext);
        return layout;
    }

    /**
     * Destroy the tab view
     *
     * @param collection Parent view
     * @param position   Tab position
     * @param view       Tab view
     */
    @Override
    public void destroyItem(@NonNull final ViewGroup collection, final int position, @NonNull final Object view) {
        tabs[position].viewInactive();
        collection.removeView((View) view);
    }

    /**
     * @return Number of tabs
     */
    @Override
    public int getCount() {
        return tabs.length;
    }

    /**
     * @param view   View
     * @param object Object
     * @return True if equal
     */
    @Override
    public boolean isViewFromObject(@NonNull final View view, @NonNull final Object object) {
        return view == object;
    }

    /**
     * @param position Tab Position
     * @return Tab Title
     */
    @NonNull
    @Override
    public CharSequence getPageTitle(final int position) {
        return mContext.getString(tabs[position].getTitleResId());
    }

}
