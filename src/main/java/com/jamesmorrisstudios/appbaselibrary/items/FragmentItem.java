package com.jamesmorrisstudios.appbaselibrary.items;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.jamesmorrisstudios.appbaselibrary.fragments.BaseFragment;

/**
 * Fragment item. Build and add this before you can load a fragment into view.
 * <p/>
 * Created by James on 12/8/2015.
 */
public final class FragmentItem {
    public final String tag, parentTag;
    public final Class clazz;

    /**
     * Constructor.
     *
     * @param tag       Fragment tag. Used to then launch this fragment later.
     * @param clazz     Class of the fragment to load
     * @param parentTag Fragment Tag of parent object. Use null if it doesn't matter what is the parent
     */
    public FragmentItem(@NonNull final String tag, @NonNull final Class clazz, @Nullable final String parentTag) {
        this.tag = tag;
        this.clazz = clazz;
        this.parentTag = parentTag;
    }

    /**
     * @return True if this is the main fragment
     */
    public final boolean isMain() {
        return tag.equals(BaseFragment.TAG_MAIN_FRAGMENT);
    }

    /**
     * Gets the fragment from the fragment manager or creates it if it doesn't exist.
     *
     * @param activity Activity caller
     * @return Instance of the fragment.
     */
    @Nullable
    public final BaseFragment getFragment(@NonNull final AppCompatActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        BaseFragment fragment = (BaseFragment) fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            try {
                return (BaseFragment) clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fragment;
    }

}
