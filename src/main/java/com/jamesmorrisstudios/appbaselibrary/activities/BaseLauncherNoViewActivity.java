package com.jamesmorrisstudios.appbaselibrary.activities;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.ThemeManager;
import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.activityHandlers.ActivityResultManager;
import com.jamesmorrisstudios.appbaselibrary.activityHandlers.BaseBuildManager;
import com.jamesmorrisstudios.appbaselibrary.activityHandlers.DialogBuildManager;
import com.jamesmorrisstudios.appbaselibrary.animator.AnimatorControl;
import com.jamesmorrisstudios.appbaselibrary.animator.AnimatorEndListener;
import com.jamesmorrisstudios.appbaselibrary.animator.AnimatorStartListener;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.fragments.BaseFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.BaseMainFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.BaseMainRecycleListFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.HelpFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.LicenseFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.SettingsFragment;
import com.jamesmorrisstudios.appbaselibrary.preferences.Prefs;
import com.jamesmorrisstudios.appbaselibrary.sound.Sounds;

/**
 * Base level activity implementation. This handles getting the toolbar up and running and includes a main fragment page
 * along with help and settings fragments. Extend the mainFragment for your main page
 * <p/>
 * Created by James on 4/29/2015.
 */
public abstract class BaseLauncherNoViewActivity extends BaseThemedActivity implements
        FragmentManager.OnBackStackChangedListener,
        BaseMainFragment.OnMenuItemClickedListener,
        BaseMainRecycleListFragment.OnMenuItemClickedListener,
        HelpFragment.OnHelpSubPageListener,
        BaseFragment.OnUtilListener,
        SettingsFragment.OnSettingsListener,
        BaseBuildManager.BaseBuildManagerListener {

    private FrameLayout container;
    private View splashScreen;
    private Toolbar toolbar;
    private ProgressBar spinner;

    private DialogBuildManager dialogBuildManager;
    private ActivityResultManager activityResultManager;
    private boolean clearingBackStack = false;

    /**
     * This handles all the on create work after the view has been set.
     * This is normally called from onCreate. If you are supplying your own view in onCreate
     * then you must call this manually.
     */
    protected final void initOnCreate() {
        container = (FrameLayout) findViewById(R.id.container);
        splashScreen = findViewById(R.id.splashScreen);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPopupTheme(ThemeManager.getToolbarPopupStyle());
        toolbar.setTitle(getString(R.string.app_name_short));
        setSupportActionBar(toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        dialogBuildManager = new DialogBuildManager();
        activityResultManager = new ActivityResultManager();
        dialogBuildManager.attach(this);
        activityResultManager.attach(this);
        shouldDisplayHomeUp();
        if (!hasBackStack()) {
            loadMainFragment();
        }
        processIntentsBase();
    }

    /**
     * Activity start
     */
    @Override
    public void onStart() {
        super.onStart();
        Sounds.getInstance().onStart();
        hideSplashScreen(false);
    }

    /**
     * Activity resume
     */
    @Override
    public void onResume() {
        super.onResume();
        showReleaseNotesIfNeeded();
    }

    /**
     * Activity pause
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Activity stop
     */
    @Override
    public void onStop() {
        super.onStop();
        Sounds.getInstance().onStop();
    }

    /**
     * Activity stop
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        dialogBuildManager.detach();
        activityResultManager.detach();
        Sounds.getInstance().onDestroy();
    }

    /**
     * Activity callback result for popup actions.
     *
     * @param requestCode Request code
     * @param resultCode  Result code status
     * @param intent      Result intent
     */
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        activityResultManager.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        activityResultManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onAppBaseEvent(AppBaseEvent event) {
        switch(event) {
            case SHOW_SPINNER:
                spinner.setVisibility(View.VISIBLE);
                break;
            case HIDE_SPINNER:
                spinner.setVisibility(View.GONE);
                break;
            case SHOW_TOOLBAR_INSTANT:
                toggleShowToolbar(true, true);
                break;
            case HIDE_TOOLBAR_INSTANT:
                toggleShowToolbar(false, true);
                break;
            case SHOW_TOOLBAR_ANIM:
                toggleShowToolbar(true, false);
                break;
            case HIDE_TOOLBAR_ANIM:
                toggleShowToolbar(false, false);
                break;
            case TOOLBAR_OVERLAY_ENABLE:
                toggleToolbarOverlay(true);
                break;
            case TOOLBAR_OVERLAY_DISABLE:
                toggleToolbarOverlay(false);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntentsBase();
    }

    private void processIntentsBase() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        if(intent.hasExtra("RESTART")) {
            hideSplashScreen(true);
            if (intent.hasExtra("PAGE")) {
                String page = intent.getStringExtra("PAGE");
                int scrollY = intent.getIntExtra("SCROLL_Y", -1);
                if ("SETTINGS".equals(page)) {
                    loadSettingsFragment(scrollY);
                } else {
                    restartToPage(page, scrollY);
                }
                intent.removeExtra("PAGE");
                intent.removeExtra("SCROLL_Y");
            }
        }
        processIntents();
    }

    /**
     * Fragment backstack changed.
     */
    @Override
    public void onBackStackChanged() {
        if (!clearingBackStack) {
            shouldDisplayHomeUp();
            onFragmentChangeEnd();
        }
    }

    /**
     * Check if we are at the top page and show the up button as needed
     */
    public final void shouldDisplayHomeUp() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            boolean hasBackStack = hasBackStack();
            actionBar.setDisplayHomeAsUpEnabled(hasBackStack);
            if (!hasBackStack) {
                onBackToHome();
            }
        }
    }

    public final boolean hasBackStack() {
        return getSupportFragmentManager().getBackStackEntryCount() > 0;
    }

    public final void clearBackStack() {
        clearingBackStack = true;
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().executePendingTransactions();
        clearingBackStack = false;
    }

    /**
     * The up button was pressed so pop one off the backstack
     * and notify the visible fragment that it is being left
     *
     * @return Always true
     */
    @Override
    public boolean onSupportNavigateUp() {
        if(backUpPressed()) {
            getSupportFragmentManager().popBackStack();
        }
        return true;
    }

    /**
     * Called when the android back button was pressed
     * notify the visible fragment that it is being left
     */
    @Override
    public void onBackPressed() {
        if(backUpPressed()) {
            super.onBackPressed();
        }
    }

    /**
     * Called when the android back button or up button was pressed
     * Signal to fragments that need it that the use clicked back and is leaving them
     */
    protected boolean backUpPressed() {
        Log.v("BaseActivity", "Back Up Pressed");
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if (f instanceof BaseFragment) {
            BaseFragment fragment = (BaseFragment) f;
            if(!fragment.goBackInternal()) {
                fragment.onBack();
            } else {
                return false;
            }
        }
        onFragmentChangeStart();
        Log.v("BaseActivity", "Back Up Pressed Returning True");
        return true;
    }

    private void showReleaseNotesIfNeeded() {
        String lastOpenedVersion = Prefs.getString("com.jamesmorrisstudios.appbaselibrary.APPDATA", "LAST_OPEN_VERSION");
        int lastMajor = Utils.getVersionMajor(lastOpenedVersion);
        int lastMinor = Utils.getVersionMinor(lastOpenedVersion);

        if (lastMajor != Utils.getVersionMajor() || lastMinor != Utils.getVersionMinor()) {
            //onRequestReleaseNotesDialog(); //TODO
        }
        Prefs.putString("com.jamesmorrisstudios.appbaselibrary.APPDATA", "LAST_OPEN_VERSION", Utils.getVersionName());
    }

    private void hideSplashScreen(boolean forceInstant) {
        if(forceInstant || !AppBase.getInstance().isColdLaunch()) {
            splashScreen.setVisibility(View.GONE);
        } else if(splashScreen.getVisibility() == View.VISIBLE) {
            AnimatorControl.alphaAutoStart(splashScreen, 1.0f, 0.0f, 1000, 1000, AnimatorControl.InterpolatorType.ACCELERATE, new AnimatorEndListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    splashScreen.setVisibility(View.GONE);
                }
            });
        }
        AppBase.getInstance().setHasLaunched();
    }

    /**
     * Gets the license fragment from the fragment manager.
     * Creates the fragment if it does not exist yet.
     *
     * @return The fragment
     */
    @NonNull
    protected final LicenseFragment getLicenseFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        LicenseFragment fragment = (LicenseFragment) fragmentManager.findFragmentByTag(LicenseFragment.TAG);
        if (fragment == null) {
            fragment = new LicenseFragment();
        }
        return fragment;
    }

    /**
     * Loads the lisense fragment into the main view
     */
    protected final void loadLicenseFragment() {
        LicenseFragment fragment = getLicenseFragment();
        loadFragment(fragment, LicenseFragment.TAG, true);
        getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * Gets the help fragment from the fragment manager.
     * Creates the fragment if it does not exist yet.
     *
     * @return The fragment
     */
    @NonNull
    protected final HelpFragment getHelpFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        HelpFragment fragment = (HelpFragment) fragmentManager.findFragmentByTag(HelpFragment.TAG);
        if (fragment == null) {
            fragment = new HelpFragment();
        }
        return fragment;
    }

    /**
     * Loads the help fragment into the main view
     */
    protected final void loadHelpFragment() {
        HelpFragment fragment = getHelpFragment();
        loadFragment(fragment, HelpFragment.TAG, true);
        getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * Gets the help fragment from the fragment manager.
     * Creates the fragment if it does not exist yet.
     *
     * @return The fragment
     */
    @NonNull
    protected SettingsFragment getSettingsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SettingsFragment fragment = (SettingsFragment) fragmentManager.findFragmentByTag(SettingsFragment.TAG);
        if (fragment == null) {
            fragment = new SettingsFragment();
        }
        return fragment;
    }

    /**
     * Loads the help fragment into the main view
     */
    protected void loadSettingsFragment(int scrollY) {
        SettingsFragment fragment = getSettingsFragment();
        fragment.setStartScrollY(scrollY);
        loadFragment(fragment, SettingsFragment.TAG, true);
        getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * Gets the main list fragment from the fragment manager.
     * Creates the fragment if it does not exist yet.
     *
     * @return The fragment
     */
    @NonNull
    protected abstract BaseFragment getMainFragment();

    /**
     * Loads the main list fragment into the main view
     */
    protected void loadMainFragment() {
        BaseFragment fragment = getMainFragment();
        loadFragment(fragment, BaseMainFragment.TAG, false);
        getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * Loads the given fragment into the container view.
     *
     * @param fragment     Fragment to add
     * @param tag          Tag to give the fragment
     * @param addBackStack True to add to backstack for back and up navigation
     */
    protected final void loadFragment(@NonNull BaseFragment fragment, @NonNull String tag, boolean addBackStack) {
        if (!isFragmentUIActive(fragment)) {
            onFragmentChangeStart();
            if (addBackStack) {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .addToBackStack(tag)
                        .replace(R.id.container, fragment, tag)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .replace(R.id.container, fragment, tag)
                        .commit();
            }
        }
    }

    /**
     * Checks if the UI of a fragment is active
     *
     * @param fragment Fragment to check
     * @return True if fragment is added and visible
     */
    protected final boolean isFragmentUIActive(@NonNull BaseFragment fragment) {
        return fragment.isAdded() && !fragment.isDetached() && !fragment.isRemoving();
    }

    @Override
    public void goBackFromFragment() {
        onBackPressed(); //Force a back press event
    }

    @Override
    public void goBackToMainFragment() {
        clearBackStack();
        loadMainFragment();
    }

    /**
     * Hides the keyboards if visible
     */
    @Override
    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void hideToolbarTitle() {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void showToolbarTitle() {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public void onSettingsClicked() {
        loadSettingsFragment(-1);
    }

    @Override
    public void onHelpClicked() {
        loadHelpFragment();
    }

    /**
     * License button clicked
     */
    @Override
    public void onLicenseClicked() {
        loadLicenseFragment();
    }

    /**
     * Called on settings change event
     */
    @Override
    public void onSettingsChanged() {
        updateImmersiveMode(true);
        Sounds.getInstance().reloadSettings();
    }

    /**
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        updateImmersiveMode(hasFocus);
    }

    private void toggleShowToolbar(boolean show, boolean instant) {
        if(instant) {
            if (show) {
                toolbar.setVisibility(View.VISIBLE);
            } else {
                toolbar.setVisibility(View.GONE);
            }
        } else {
            if(show) {
                AnimatorControl.translateYAutoStart(toolbar, -toolbar.getHeight(), 0, 100, 0, AnimatorControl.InterpolatorType.ACCELERLATE_DECELERATE, new AnimatorStartListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        toolbar.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                AnimatorControl.translateYAutoStart(toolbar, 0, -toolbar.getHeight(), 100, 0, AnimatorControl.InterpolatorType.ACCELERLATE_DECELERATE, new AnimatorEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toolbar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    private void toggleToolbarOverlay(boolean enable) {
        if(enable) {
            //Padding to 0
            container.setPadding(container.getPaddingLeft(), 0, container.getPaddingTop(), container.getPaddingBottom());
        } else {
            //Padding to actionbarsize
            final TypedArray styledAttributes = getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
            int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
            styledAttributes.recycle();
            container.setPadding(container.getPaddingLeft(), mActionBarSize, container.getPaddingTop(), container.getPaddingBottom());
        }
    }

    @Override
    public final void restartActivity(String page, int scrollY) {
        Utils.restartApp(this, page, scrollY);
    }

    @Override
    public BaseLauncherNoViewActivity getActivity() {
        return this;
    }

    protected abstract void restartToPage(String page, int scrollY);

    protected abstract void processIntents();

    /**
     * The fragment is changing. This is called right after the fragment is notified
     */
    protected abstract void onFragmentChangeStart();

    /**
     * The fragment was just changed
     */
    protected abstract void onFragmentChangeEnd();

    /**
     * The we just went back to the home fragment
     */
    protected abstract void onBackToHome();

    /**
     * Events that trigger an action in the App Base Activity
     */
    public enum AppBaseEvent {
        SHOW_SPINNER, HIDE_SPINNER, SHOW_TOOLBAR_INSTANT, HIDE_TOOLBAR_INSTANT, SHOW_TOOLBAR_ANIM, HIDE_TOOLBAR_ANIM,
        TOOLBAR_OVERLAY_ENABLE, TOOLBAR_OVERLAY_DISABLE
    }

}

