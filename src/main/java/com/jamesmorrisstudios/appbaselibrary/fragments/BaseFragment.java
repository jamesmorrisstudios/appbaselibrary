package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jamesmorrisstudios.appbaselibrary.Bus;
import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.activities.BaseActivity;
import com.jamesmorrisstudios.appbaselibrary.filewriting.FileWriter;
import com.squareup.otto.Subscribe;

/**
 * Base app fragment
 * <p/>
 * Created by James on 4/29/2015.
 */
public abstract class BaseFragment extends Fragment {
    /**
     * Listener handler
     */
    private final Object object = new Object() {
        @Subscribe
        public void onFabEvent(BaseActivity.FabEvent event) {
            if (event == BaseActivity.FabEvent.CLICKED) {
                BaseFragment.this.fabClicked();
            }
        }
    };
    protected int startScrollY = -1;
    protected Bundle startBundle = null;
    protected Object startObject = null;
    private boolean fabAutoHide = true;

    /**
     * @return True to show the toolbar title
     */
    protected abstract boolean showToolbarTitle();

    /**
     * Register with the bus listener if needed
     */
    protected abstract void registerBus();

    /**
     * unregister with the bus listener if needed
     */
    protected abstract void unregisterBus();

    /**
     * Save instance state
     *
     * @param bundle bundle
     */
    protected abstract void saveState(@NonNull Bundle bundle);

    /**
     * restore instance state
     *
     * @param bundle bundle
     */
    protected abstract void restoreState(@NonNull Bundle bundle);

    /**
     * After view created. Setup fragment here
     */
    protected abstract void afterViewCreated();

    /**
     * Returns true if there is an internal back within this fragment.
     * If true the fragment should go back to its previous page and the fragment will not be removed from view
     *
     * @return true if we have an internal back in the fragment. False if we should move to previous fragment
     */
    public boolean goBackInternal() {
        return false;
    }

    /**
     * Call before fragment is attached to activity
     *
     * @param scrollY Set start scrollY position
     */
    public final void setScrollY(int scrollY) {
        this.startScrollY = scrollY;
    }

    /**
     * Call before fragment is attached to activity
     *
     * @param bundle Set starting data bundle
     */
    public final void setBundle(@Nullable Bundle bundle) {
        this.startBundle = bundle;
    }

    /**
     * Call before fragment is attached to activity
     *
     * @param object Set starting data object
     */
    public final void setObject(@Nullable Object object) {
        this.startObject = object;
    }

    /**
     * On Start
     */
    public void onStart() {
        super.onStart();
    }

    /**
     * Save instance state
     *
     * @param bundle bundle
     */
    @Override
    public void onSaveInstanceState(@Nullable Bundle bundle) {
        if (bundle != null) {
            saveState(bundle);
        }
        super.onSaveInstanceState(bundle);
    }

    /**
     * On view being destroyed
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterBus();
        Bus.unregister(object);
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
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
        if (showToolbarTitle()) {
            BaseActivity.AppBaseEvent.SHOW_TOOLBAR_TITLE.post();
            BaseActivity.AppBaseEvent.SET_TOOLBAR_TITLE.text(getToolbarTitle()).post();
        } else {
            BaseActivity.AppBaseEvent.HIDE_TOOLBAR_TITLE.post();
        }
        registerBus();
        Bus.register(object);
        afterViewCreated();
    }
    //R.anim.right_to_center, R.anim.center_to_left, R.anim.left_to_center, R.anim.center_to_right

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim == 0) {
            if (enter) {
                return AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_center);
            } else {
                return AnimationUtils.loadAnimation(getActivity(), R.anim.center_to_right);
            }
        } else {
            return AnimationUtils.loadAnimation(getActivity(), nextAnim);
        }
    }

    /**
     * Override this if you care about fab clicks
     */
    protected void fabClicked() {

    }

    /**
     * @param resourceId Resource id of the FAB icon
     */
    protected final void setFabIcon(@DrawableRes int resourceId) {
        BaseActivity.FabEvent.SET_ICON.setIcon(resourceId).post();
    }

    /**
     * Only shows the fab is autoHide is enabled
     */
    protected final void showFabAuto() {
        if (fabAutoHide) {
            showFab();
        }
    }

    /**
     * Only hides the fab is autoHide is enabled
     */
    protected final void hideFabAuto() {
        if (fabAutoHide) {
            hideFab();
        }
    }

    /**
     * Shows the fab
     */
    protected final void showFab() {
        BaseActivity.FabEvent.SHOW.post();
    }

    /**
     * Hides the fab
     */
    protected final void hideFab() {
        BaseActivity.FabEvent.HIDE.post();
    }

    /**
     * Must call hideFabAuto and showFabAuto on scroll events for this to work.
     *
     * @param fabAutoHide True to auto hide fab on scroll
     */
    protected final void setFabAutoHide(boolean fabAutoHide) {
        this.fabAutoHide = fabAutoHide;
        if (!fabAutoHide) {
            showFab();
        }
    }

    /**
     * Override this to set custom text for the toolbar
     *
     * @return Toolbar title text.
     */
    @NonNull
    protected String getToolbarTitle() {
        return getString(R.string.app_name_short);
    }

    /**
     * TODO update implementation
     * <p/>
     * Share the current view contained by the fragment
     *
     * @param chooserTitle Title text
     * @param shareText    Shared text
     */
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
                //TODO use a FileProvider instead
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

}
