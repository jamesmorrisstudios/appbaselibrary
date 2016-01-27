package com.jamesmorrisstudios.appbaselibrary.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
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
    public FABBehavior(@NonNull final Context context, @NonNull final AttributeSet attrs) {
    }

    /**
     * @param parent     Coordinator layout
     * @param child      FAB
     * @param dependency Dependency view
     * @return true if depends on
     */
    @Override
    public final boolean layoutDependsOn(@NonNull final CoordinatorLayout parent, @NonNull final FloatingActionButton child, @NonNull final View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    /**
     * @param parent     Coordinator layout
     * @param child      FAB
     * @param dependency Dependency view
     * @return true
     */
    @Override
    public final boolean onDependentViewChanged(@NonNull final CoordinatorLayout parent, @NonNull final FloatingActionButton child, @NonNull final View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }

}
