package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.animation.Animator;
import android.app.Activity;
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

import com.jamesmorrisstudios.appbaselibrary.filewriting.FileWriter;
import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.animator.AnimatorControl;
import com.jamesmorrisstudios.appbaselibrary.animator.AnimatorStartEndListener;

/**
 * Base app fragment
 * <p/>
 * Created by James on 4/29/2015.
 */
public abstract class BaseFragment extends Fragment {
    protected OnUtilListener utilListener;
    private FloatingActionButton fab;
    private transient boolean animRunning = false;
    private float fabShownPos, fabHidePos;
    private boolean fabAutoHide = true;

    public abstract void onBack();

    public abstract boolean showToolbarTitle();

    protected abstract void saveState(Bundle bundle);

    protected abstract void restoreState(Bundle bundle);

    /**
     * @param savedInstanceState Saved instance state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onStart() {
        super.onStart();
        if (showToolbarTitle()) {
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
        utilListener = null;
    }

    private void initFab() {
        if (!(getView() instanceof ViewGroup)) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) getView();
        RelativeLayout relativeLayout = null;
        if (viewGroup instanceof RelativeLayout) {
            relativeLayout = (RelativeLayout) viewGroup;
        } else if (viewGroup.getChildCount() == 1) {
            if (viewGroup.getChildAt(0) instanceof RelativeLayout) {
                relativeLayout = (RelativeLayout) viewGroup.getChildAt(0);
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
            Log.v("BaseFragment", "Got Here " + (fab != null));
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

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        saveState(bundle);
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
        if(savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
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
            if (fab != null) {
                fab.setVisibility(View.VISIBLE);
            }
        } else {
            if (fab != null) {
                fab.setVisibility(View.GONE);
            }
        }
    }

    protected final void setFabIcon(@DrawableRes int resourceId) {
        if (fab != null) {
            fab.setImageResource(resourceId);
        }
    }

    protected final void showFabAuto() {
        if(fabAutoHide) {
            showFab();
        }
    }

    protected final void hideFabAuto() {
        if(fabAutoHide) {
            hideFab();
        }
    }

    protected final void showFab() {
        if (fab == null) {
            return;
        }
        if (fabShownPos == 0 || fabHidePos == 0) {
            fabShownPos = fab.getY();
            fabHidePos = fabShownPos + Utils.getDipInt(24) + fab.getHeight();
        }
        if (animRunning || fab.getY() == fabShownPos) {
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
            fabShownPos = fab.getY();
            fabHidePos = fabShownPos + Utils.getDipInt(24) + fab.getHeight();
        }
        if (animRunning || fab.getY() == fabHidePos) {
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

    protected final void setFabAutoHide(boolean fabAutoHide) {
        this.fabAutoHide = fabAutoHide;
        if(!fabAutoHide) {
            showFab();
        }
    }

    protected final void shareText(@NonNull String chooserTitle, @NonNull String shareText) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(share, chooserTitle));
    }

    protected final void shareView(@NonNull final String chooserTitle, @Nullable final String shareText) {
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
                if (image.getWidth() > maxEdge || image.getHeight() > maxEdge) {
                    float scaleFactor = Math.min(maxEdge * 1.0f / image.getWidth(), maxEdge * 1.0f / image.getHeight());
                    int width = Math.round(image.getWidth() * scaleFactor);
                    int height = Math.round(image.getHeight() * scaleFactor);
                    image = Bitmap.createScaledBitmap(image, width, height, true);
                }
                FileWriter.writeImage("ShareImage.png", image, FileWriter.FileLocation.CACHE);
                return Uri.fromFile(FileWriter.getFile("ShareImage.png", FileWriter.FileLocation.CACHE));
            }

            @Override
            protected void onPostExecute(Uri uri) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_STREAM, uri);
                if (shareText == null || shareText.isEmpty()) {
                    share.setType("image/png");
                } else {
                    share.setType("*/*");
                    share.putExtra(Intent.EXTRA_TEXT, shareText);
                }
                startActivity(Intent.createChooser(share, chooserTitle));
            }
        };
        taskShare.execute(imageSrc);
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
