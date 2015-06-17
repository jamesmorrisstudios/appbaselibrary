package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.utilitieslibrary.Utils;
import com.jamesmorrisstudios.utilitieslibrary.animator.AnimatorControl;
import com.jamesmorrisstudios.utilitieslibrary.animator.AnimatorStartEndListener;
import com.jamesmorrisstudios.utilitieslibrary.dialogs.colorpicker.builder.ColorPickerClickListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.FileWriter;


/**
 * Base app fragment
 * <p/>
 * Created by James on 4/29/2015.
 */
public abstract class BaseFragment extends Fragment {
    protected OnDialogListener dialogListener;
    protected OnUtilListener utilListener;
    private FloatingActionButton fab;
    private transient boolean animRunning = false;
    private float fabShownPos, fabHidePos;

    public abstract void onBack();
    public abstract boolean showToolbarTitle();

    /**
     * @param savedInstanceState Saved instance state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onStart() {
        super.onStart();
        if(showToolbarTitle()) {
            utilListener.showToolbarTitle();
        } else {
            utilListener.hideToolbarTitle();
        }
    }

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
        if(!(getView() instanceof ViewGroup)) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) getView();
        RelativeLayout relativeLayout = null;
        if(viewGroup instanceof RelativeLayout) {
            relativeLayout = (RelativeLayout)viewGroup;
        } else if(viewGroup.getChildCount() == 1){
            if(viewGroup.getChildAt(0) instanceof RelativeLayout) {
                relativeLayout = (RelativeLayout)viewGroup.getChildAt(0);
            }
        }
        if (relativeLayout != null) {
            int[][] states = new int[][]{
                    new int[]{android.R.attr.state_enabled}, // enabled
                    new int[]{-android.R.attr.state_enabled}, // disabled
                    new int[]{-android.R.attr.state_checked}, // unchecked
                    new int[]{android.R.attr.state_pressed}  // pressed
            };

            int[] colors = new int[]{
                    getResources().getColor(R.color.primary),
                    getResources().getColor(R.color.primaryDisabled),
                    getResources().getColor(R.color.primary),
                    getResources().getColor(R.color.primaryLight)
            };
            ColorStateList myList = new ColorStateList(states, colors);
            fab = (FloatingActionButton) getActivity().getLayoutInflater().inflate(R.layout.fab, null);
            Log.v("BaseFragment", "Got Here "+(fab != null));
            fab.setBackgroundTintList(myList);
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
        } else {
            Log.v("BaseFragment", "Parent is not RelativeLayout. unsupported fab");
        }
    }

    /**
     * View creation done
     *
     * @param view               This fragments main view
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
        if (enable) {
            Log.v("BaseFragment", "Start enable");
            initFab();
            Log.v("BaseFragment", "After Init");
            if(fab != null) {
                fab.setVisibility(View.VISIBLE);
            }
        } else {
            if(fab != null) {
                fab.setVisibility(View.GONE);
            }
        }
    }

    protected final void setFabIcon(@DrawableRes int resourceId) {
        if(fab != null) {
            fab.setImageResource(resourceId);
        }
    }

    protected void showFab() {
        if (fab == null) {
            return;
        }
        if (fabShownPos == 0 || fabHidePos == 0) {
            fabShownPos = ViewHelper.getY(fab);
            fabHidePos = fabShownPos + Utils.getDipInt(24) + fab.getHeight();
        }
        if (animRunning || ViewHelper.getY(fab) == fabShownPos) {
            return;
        }
        AnimatorControl.translateYAutoStart(fab, (fabHidePos - fabShownPos), 0, 250, 0, new AnimatorStartEndListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animRunning = false;
            }
        });
    }

    protected final void hideFab() {
        if (fab == null) {
            return;
        }
        if (fabShownPos == 0 || fabHidePos == 0) {
            fabShownPos = ViewHelper.getY(fab);
            fabHidePos = fabShownPos + Utils.getDipInt(24) + fab.getHeight();
        }
        if (animRunning || ViewHelper.getY(fab) == fabHidePos) {
            return;
        }
        AnimatorControl.translateYAutoStart(fab, 0, (fabHidePos - fabShownPos), 250, 0, new AnimatorStartEndListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animRunning = false;
            }
        });
    }

    protected final void shareView(final String title) {
        View view = getView();
        if (view == null) {
            return;
        }
        Bitmap imageSrc = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(imageSrc);
        view.draw(c);

        AsyncTask<Bitmap, Void, Uri> taskShare = new AsyncTask<Bitmap, Void, Uri>() {
            @Override
            protected Uri doInBackground(Bitmap... params) {
                Bitmap image = params[0];

                int maxEdge = 1024;
                if(image.getWidth() > maxEdge || image.getHeight() > maxEdge) {
                    float scaleFactor = Math.min(maxEdge * 1.0f / image.getWidth(), maxEdge * 1.0f / image.getHeight());
                    int width = Math.round(image.getWidth() * scaleFactor);
                    int height = Math.round(image.getHeight() * scaleFactor);
                    image = Bitmap.createScaledBitmap(image, width, height, true);
                }
                //int maxHeight = 1024;
                //if (image.getHeight() > maxHeight) {
                //    int width = Math.round((maxHeight * image.getWidth() * 1.0f) / image.getHeight());
                //     image = Bitmap.createScaledBitmap(image, width, maxHeight, true);
                //}
                com.jamesmorrisstudios.utilitieslibrary.FileWriter.writeImage("ShareImage.png", image, true);
                return Uri.fromFile(com.jamesmorrisstudios.utilitieslibrary.FileWriter.getFile("ShareImage.png", true));
            }

            @Override
            protected void onPostExecute(Uri uri) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.setType("image/png");
                startActivity(Intent.createChooser(share, title));
            }
        };
        taskShare.execute(imageSrc);
    }

    /**
     *
     */
    public interface OnDialogListener {

        /**
         * Build a new time picker dialog
         *
         * @param listener Return listener
         * @param hour     Start hour
         * @param minute   Start minute
         * @param is24Hour True if 24 hour mode
         */
        void createTimePickerDialog(@NonNull TimePickerDialog.OnTimeSetListener listener, int hour, int minute, boolean is24Hour);

        /**
         * Build a ok/cancel prompt
         *
         * @param title   Title of the prompt
         * @param content Content text
         */
        void createPromptDialog(@NonNull String title, @NonNull String content, DialogInterface.OnClickListener onPositive, DialogInterface.OnClickListener onNegative);

        void createColorPickerDialog(int intialColor, ColorPickerClickListener onColorPickerClickListener);
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

        /**
         * Hides the toolbar title
         */
        void hideToolbarTitle();

        /**
         * Shows the toolbar title
         */
        void showToolbarTitle();
    }

}
