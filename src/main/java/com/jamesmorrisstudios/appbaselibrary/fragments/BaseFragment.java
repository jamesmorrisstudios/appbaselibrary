package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;

import com.jamesmorrisstudios.materialuilibrary.dialogs.ColorSelector;
import com.jamesmorrisstudios.materialuilibrary.dialogs.MaterialDialog;
import com.jamesmorrisstudios.materialuilibrary.dialogs.time.TimePickerDialog;
import com.jamesmorrisstudios.materialuilibrary.floatingactionbutton.FloatingActionButton;
import com.jamesmorrisstudios.utilitieslibrary.Utils;
import com.jamesmorrisstudios.utilitieslibrary.animator.AnimatorControl;

/**
 * Base app fragment
 *
 * Created by James on 4/29/2015.
 */
public abstract class BaseFragment extends Fragment {
    protected OnDialogListener dialogListener;
    protected OnUtilListener utilListener;
    private FloatingActionButton fab;
    private ObjectAnimator animShow, animHide;
    private float fabShownPos, fabHidePos;

    public abstract void onBack();

    /**
     * @param activity Activity to attach to
     */
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            dialogListener = (OnDialogListener) activity;
            utilListener = (OnUtilListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDialogListener and OnUtilListener");
        }
    }

    /**
     * Detach from activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        dialogListener = null;
        utilListener = null;
    }

    private void initFab() {
        if(getView() instanceof RelativeLayout) {
            RelativeLayout relativeLayout = (RelativeLayout) getView();
            fab = new FloatingActionButton(getActivity().getApplicationContext());
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            p.setMargins(0, 0, Utils.getDipInt(24), Utils.getDipInt(16));
            p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            fab.setLayoutParams(p);
            relativeLayout.addView(fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fabClicked();
                }
            });
        }
    }

    /**
     * View creation done
     * @param view This fragments main view
     * @param savedInstanceState Saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        afterViewCreated();
    }

    protected abstract void afterViewCreated();

    /**
     * Override this if you care about fab clicks
     */
    protected void fabClicked() {

    }

    protected final void setFabEnable(boolean enable) {
        if(enable) {
            initFab();
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
    }

    protected final void setFabIcon(@DrawableRes int resourceId) {
        fab.setIcon(resourceId);
    }

    protected void showFab() {
        if(fab == null) {
            return;
        }
        if(fabShownPos == 0 || fabHidePos == 0) {
            fabShownPos = fab.getY();
            fabHidePos = fabShownPos + Utils.getDipInt(24) + fab.getHeight();
        }
        if(animShow != null && animShow.isRunning() || fab.getY() == fabShownPos) {
            return;
        }
        animShow = AnimatorControl.translateY(fab, (fabHidePos - fabShownPos), 0, 250, 0);
        animShow.start();
    }

    protected final void hideFab() {
        if(fab == null) {
            return;
        }
        if(fabShownPos == 0 || fabHidePos == 0) {
            fabShownPos = fab.getY();
            fabHidePos = fabShownPos + Utils.getDipInt(24) + fab.getHeight();
        }
        if(animHide != null && animHide.isRunning() || fab.getY() == fabHidePos) {
            return;
        }
        animHide = AnimatorControl.translateY(fab, 0, (fabHidePos - fabShownPos), 250, 0);
        animHide.start();
    }

    /**
     *
     */
    public interface OnDialogListener {

        /**
         * Build a new time picker dialog
         * @param listener Return listener
         * @param hour Start hour
         * @param minute Start minute
         * @param is24Hour True if 24 hour mode
         */
        void createTimePickerDialog(@NonNull TimePickerDialog.OnTimeSetListener listener, int hour, int minute, boolean is24Hour);

        /**
         * Build a ok/cancel prompt
         * @param title Title of the prompt
         * @param content Content text
         * @param callback Callback listener
         */
        void createPromptDialog(@NonNull String title, @NonNull String content, MaterialDialog.ButtonCallback callback);

        void createColorPickerDialog(int intialColor, ColorSelector.OnColorSelectedListener onColorSelectedListener);
    }

    /**
     *
     */
    public interface OnUtilListener {

        /**
         * Go back from this fragment
         */
        void goBackFromFragment();

        /**
         * Hides the keyboard
         */
        void hideKeyboard();
    }

}
