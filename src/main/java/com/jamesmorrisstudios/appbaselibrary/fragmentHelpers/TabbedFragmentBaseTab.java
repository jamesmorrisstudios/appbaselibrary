package com.jamesmorrisstudios.appbaselibrary.fragmentHelpers;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.ViewGroup;

/**
 * Tabbed Fragment base tab
 * All tabs for the tabbed fragment have to extend this.
 * <p/>
 * Created by James on 12/8/2015.
 */
public abstract class TabbedFragmentBaseTab {

    /**
     * Get the view layout
     *
     * @return Layout resource Id
     */
    @LayoutRes
    public abstract int getViewLayoutId();

    /**
     * Get the title id
     *
     * @return String resource Id
     */
    @StringRes
    public abstract int getTitleResId();

    /**
     * FAB Clicked
     */
    public abstract void fabClicked();

    /**
     * View is now visible. Will already have called active
     */
    public abstract void viewVisible();

    /**
     * View is now invisible.
     */
    public abstract void viewInvisible();

    /**
     * View is now active. May not be visible yet
     *
     * @param view    View
     * @param context Context
     */
    public abstract void viewActive(@NonNull ViewGroup view, Context context);

    /**
     * View is now inactive. Will be invisible before this.
     */
    public abstract void viewInactive();

    /**
     * @return True to enable the FAB
     */
    public abstract boolean fabEnable();

    /**
     * @return FAB Icon resource Id
     */
    @DrawableRes
    public abstract int fabIconRes();

    /**
     * @return True if this tab requires pro
     */
    public abstract boolean requiresPro();

    /**
     * Save tab state
     *
     * @param bundle Bundle
     */
    public abstract void saveState(@NonNull Bundle bundle);

    /**
     * Restore tab state
     *
     * @param bundle Bundle
     */
    public abstract void restoreState(@NonNull Bundle bundle);

}
