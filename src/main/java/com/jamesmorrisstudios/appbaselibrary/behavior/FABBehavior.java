package com.jamesmorrisstudios.appbaselibrary.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

/**
 * Fab behavior to fix issue with it not staying above the snackbar
 * <p/>
 * Created by James on 12/11/2015.
 */
public final class FABBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    /**
     * Constructor
     *
     * @param context Context
     * @param attrs   Attributes
     */
    public FABBehavior(Context context, AttributeSet attrs) {
    }

    /**
     * @param parent     Coordinator layout
     * @param child      FAB
     * @param dependency Dependency view
     * @return true if depends on
     */
    @Override
    public final boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    /**
     * @param parent     Coordinator layout
     * @param child      FAB
     * @param dependency Dependency view
     * @return true
     */
    @Override
    public final boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }

}
