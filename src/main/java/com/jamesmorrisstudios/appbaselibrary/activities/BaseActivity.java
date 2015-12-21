package com.jamesmorrisstudios.appbaselibrary.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.jamesmorrisstudios.appbaselibrary.Bus;
import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.UtilsAppBase;
import com.jamesmorrisstudios.appbaselibrary.UtilsDisplay;
import com.jamesmorrisstudios.appbaselibrary.UtilsTheme;
import com.jamesmorrisstudios.appbaselibrary.UtilsVersion;
import com.jamesmorrisstudios.appbaselibrary.activityHandlers.ActivityResultManager;
import com.jamesmorrisstudios.appbaselibrary.activityHandlers.BaseBuildManager;
import com.jamesmorrisstudios.appbaselibrary.activityHandlers.DialogBuildManager;
import com.jamesmorrisstudios.appbaselibrary.activityHandlers.RestartAppRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.ReleaseNotesDialogRequest;
import com.jamesmorrisstudios.appbaselibrary.fragments.BaseFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.BaseMainFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.HelpFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.LicenseFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.SettingsFragment;
import com.jamesmorrisstudios.appbaselibrary.items.FragmentItem;
import com.jamesmorrisstudios.appbaselibrary.items.FragmentLoadRequest;
import com.jamesmorrisstudios.appbaselibrary.sound.Sounds;

import java.util.ArrayList;

/**
 * Primary activity. This handles getting the toolbar up and running and fragment loading. Add fragments in OnCreate
 * <p/>
 * Created by James on 11/14/2015.
 */
public abstract class BaseActivity extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener,
        BaseBuildManager.BaseBuildManagerListener,
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View splashScreen;
    private Toolbar toolbar;
    private ProgressBar spinner;
    private FloatingActionButton fab;
    private DialogBuildManager dialogBuildManager;
    private ActivityResultManager activityResultManager;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean clearingBackStack = false;
    private ArrayList<FragmentItem> fragmentItems = new ArrayList<>();
    private FragmentLoadRequest loadRequest = new FragmentLoadRequest();

    /**
     * Called on the first launch after a version change.
     * Called after onFirstLaunchActions on a new install
     *
     * @param majorChanged True if major version has changed
     * @param major        New major version
     * @param minorChanged True if major version has changed
     * @param minor        New minor version
     * @param auxChanged   True if major version has changed
     * @param aux          New aux version
     * @param firstLaunch  True if this was the first launch as you may or may not want to take actions depending.
     */
    protected abstract void onVersionChanged(boolean majorChanged, int major, boolean minorChanged, int minor, boolean auxChanged, int aux, boolean firstLaunch);

    /**
     * Called only on the users first launch of the app.
     * This is called in onResume
     */
    protected abstract void onFirstLaunchActions();

    /**
     * @return Process launch intent
     */
    protected abstract boolean processIntents();

    /**
     * @return True if sound will be used by the app
     */
    protected abstract boolean enableSound();

    /**
     * Use this in place of onCreate.
     * Add all fragments here with the addFragment function.
     *
     * @param savedInstanceState Saved instance state
     */
    protected void OnCreate(@Nullable Bundle savedInstanceState) {
        addFragment(SettingsFragment.TAG, SettingsFragment.class, BaseMainFragment.TAG);
        addFragment(HelpFragment.TAG, HelpFragment.class, BaseMainFragment.TAG);
        addFragment(LicenseFragment.TAG, LicenseFragment.class, HelpFragment.TAG);
    }

    /**
     * Override OnCreate(Bundle savedInstanceState) instead of this.
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        UtilsTheme.applyTheme(this);
        UtilsAppBase.applyLocale();
        UtilsDisplay.updateImmersiveMode(this, true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OnCreate(savedInstanceState);
        OnPostCreate(savedInstanceState);
    }

    /**
     * Setup the toolbar and all helper classes.
     * Load the main fragment if the stack is empty
     *
     * @param savedInstanceState savedInstanceState
     */
    private void OnPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Get Views
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FabEvent.CLICKED.post();
            }
        });
        splashScreen = findViewById(R.id.splashScreen);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        initToolAndNavigationBar();
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        dialogBuildManager = new DialogBuildManager();
        activityResultManager = new ActivityResultManager();
        dialogBuildManager.attach(this);
        activityResultManager.attach(this);
        UtilsDisplay.toggleShowToolbar(toolbar, true, true);
        //UtilsDisplay.toggleToolbarOverlay(getFragmentContainer(), getTheme(), false);
        if (!hasBackStack()) {
            loadFragment(BaseMainFragment.TAG);
        }
        processIntentsBase();
    }

    /**
     * Activity start
     */
    @Override
    public void onStart() {
        super.onStart();
        if (enableSound()) {
            Sounds.getInstance().onStart();
        }
        UtilsDisplay.hideSplashScreen(splashScreen, false);
    }

    /**
     * Activity resume
     */
    @Override
    public void onResume() {
        super.onResume();
        boolean firstLaunch = UtilsAppBase.isFirstLaunch();
        if (firstLaunch) {
            UtilsAppBase.setFirstLaunchComplete();
            onFirstLaunchActions();
        }
        int major = UtilsVersion.getVersionMajor();
        int majorOld = UtilsVersion.getOldVersionMajor();
        int minor = UtilsVersion.getVersionMinor();
        int minorOld = UtilsVersion.getOldVersionMinor();
        int aux = UtilsVersion.getVersionAux();
        int auxOld = UtilsVersion.getOldVersionAux();
        if (major != majorOld || minor != minorOld || aux != auxOld) {
            onVersionChanged(major == majorOld, major, minor == minorOld, minor, aux == auxOld, aux, firstLaunch);
            onBaseVersionChanged(major == majorOld, major, minor == minorOld, minor, aux == auxOld, aux, firstLaunch);
            UtilsVersion.updateVersion();
        }
    }

    /**
     * Called on the first launch after a version change.
     * Called after onFirstLaunchActions on a new install
     *
     * @param majorChanged True if major version has changed
     * @param major        New major version
     * @param minorChanged True if major version has changed
     * @param minor        New minor version
     * @param auxChanged   True if major version has changed
     * @param aux          New aux version
     * @param firstLaunch  True if this was the first launch as you may or may not want to take actions depending.
     */
    private void onBaseVersionChanged(boolean majorChanged, int major, boolean minorChanged, int minor, boolean auxChanged, int aux, boolean firstLaunch) {
        if (majorChanged || minorChanged) {
            new ReleaseNotesDialogRequest().show();
        }
    }

    /**
     * Activity stop
     */
    @Override
    public void onStop() {
        super.onStop();
        if (enableSound()) {
            Sounds.getInstance().onStop();
        }
    }

    /**
     * Activity destroy
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        dialogBuildManager.detach();
        activityResultManager.detach();
    }

    /**
     * Activity callback result for popup actions.
     * Responses are given to the activity result manager to handle
     *
     * @param requestCode Request code
     * @param resultCode  Result code status
     * @param intent      Result intent
     */
    public void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        activityResultManager.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * Permission request callbacks for Android 6.0
     * Responses are given to the activity result manager to handle
     *
     * @param requestCode  Request code
     * @param permissions  Permission list
     * @param grantResults Grant result list
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        activityResultManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Bus handled actions. Events are forwarded from the activity result manager.
     *
     * @param event App Base Event
     */
    public final void onAppBaseEvent(@NonNull AppBaseEvent event) {
        switch (event) {
            case SHOW_SPINNER:
                spinner.setVisibility(View.VISIBLE);
                break;
            case HIDE_SPINNER:
                spinner.setVisibility(View.GONE);
                break;
            case SHOW_TOOLBAR_INSTANT:
                UtilsDisplay.toggleShowToolbar(toolbar, true, true);
                break;
            case HIDE_TOOLBAR_INSTANT:
                UtilsDisplay.toggleShowToolbar(toolbar, false, true);
                break;
            case SHOW_TOOLBAR_ANIM:
                UtilsDisplay.toggleShowToolbar(toolbar, true, false);
                break;
            case HIDE_TOOLBAR_ANIM:
                UtilsDisplay.toggleShowToolbar(toolbar, false, false);
                break;
            case TOOLBAR_OVERLAY_ENABLE:
                //UtilsDisplay.toggleToolbarOverlay(getFragmentContainer(), getTheme(), true);
                break;
            case TOOLBAR_OVERLAY_DISABLE:
                //UtilsDisplay.toggleToolbarOverlay(getFragmentContainer(), getTheme(), false);
                break;
            case BACK_TO_MAIN_FRAGMENT:
                clearBackStack();
                loadFragment(BaseMainFragment.TAG);
                break;
            case BACK_ONE_FRAGMENT:
                onBackPressed();
                break;
            case HIDE_KEYBOARD:
                Utils.hideKeyboard(this);
                break;
            case HIDE_TOOLBAR_TITLE:
                ActionBar actionbar1 = getSupportActionBar();
                if (actionbar1 != null) {
                    actionbar1.setDisplayShowTitleEnabled(false);
                }
                break;
            case SHOW_TOOLBAR_TITLE:
                ActionBar actionbar2 = getSupportActionBar();
                if (actionbar2 != null) {
                    actionbar2.setDisplayShowTitleEnabled(true);
                }
                break;
            case SETTINGS_CLICKED:
                loadFragment(SettingsFragment.TAG);
                break;
            case LICENSE_CLICKED:
                loadFragment(LicenseFragment.TAG);
                break;
            case HELP_CLICKED:
                loadFragment(HelpFragment.TAG);
                break;
            case SETTINGS_CHANGED:
                onSettingsChanged();
                break;
            case SET_TOOLBAR_TITLE:
                toolbar.setTitle(event.text);
                break;
        }
    }

    /**
     * Restart app to page given by the request.
     * Event is forwarded from the activity result manager.
     *
     * @param request Restart Request
     */
    public final void restartApp(@NonNull RestartAppRequest request) {
        Utils.restartApp(this, request.pageTag, request.scrollY, request.bundle);
    }

    /**
     * App settings changed.
     * Override this and be sure to call super
     */
    @CallSuper
    protected void onSettingsChanged() {
        UtilsAppBase.applyFirstDayOfWeek();
        UtilsDisplay.updateImmersiveMode(this, true);
        if (enableSound()) {
            Sounds.getInstance().reloadSettings();
        }
    }

    /**
     * Intent was recieved
     *
     * @param intent Launch Intent
     */
    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntentsBase();
    }

    /**
     * Process launch intents. Called from onNewIntent and onCreate
     */
    private void processIntentsBase() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        if (intent.hasExtra("PAGE")) {
            UtilsDisplay.hideSplashScreen(splashScreen, true);
            String page = intent.getStringExtra("PAGE");
            int scrollY = intent.getIntExtra("SCROLL_Y", -1);
            Bundle bundle = intent.getBundleExtra("EXTRAS");
            loadFragment(page, bundle, null, scrollY);
            return;
        }
        processIntents();
    }

    /**
     * Setup the toolbar and the navigation drawer
     */
    private void initToolAndNavigationBar() {
        toolbar.setPopupTheme(UtilsTheme.getToolbarPopupStyle());
        toolbar.setTitle(getString(R.string.app_name_short));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        //Init navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.BLANK, R.string.BLANK) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (loadRequest.active) {
                    loadFragment(loadRequest.tag, loadRequest.bundle, loadRequest.object, loadRequest.scrollY);
                    loadRequest.clearData();
                }
                syncState();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                super.onDrawerSlide(drawerView, 0);
                loadRequest.clearData();
                syncState();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the animation
            }

        };

        // Set the drawer toggle as the DrawerListener
        drawer.setDrawerListener(mDrawerToggle);

        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerToggle.isDrawerIndicatorEnabled()) {
                    drawer.openDrawer(GravityCompat.START);
                } else {
                    onBackPressed();
                }
            }
        });

        mDrawerToggle.syncState();
    }

    /**
     * Override to provide navigation drawer actions
     *
     * @param item Menu item
     * @return True if consumed action
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * Override to provide navigation drawer button handlers
     *
     * @param item Menu item
     * @return True if consumed action
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_settings) {
            item.setChecked(false);
            loadFragment(SettingsFragment.TAG);
        } else if (item.getItemId() == R.id.navigation_help) {
            item.setChecked(false);
            loadFragment(HelpFragment.TAG);
        }
        drawer.closeDrawers();
        return true;
    }

    /**
     * Ensure the navigation drawer is in sync
     *
     * @param savedInstanceState Saved instance state
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        updateDrawerEnable();
        mDrawerToggle.syncState();
    }

    /**
     * Keep the navigation drawer working on a configuration change
     *
     * @param newConfig configuration
     */
    @Override
    public void onConfigurationChanged(@Nullable Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment backstack changed.
     */
    @Override
    public void onBackStackChanged() {
        if (!clearingBackStack) {
            updateDrawerEnable();
        }
    }

    /**
     * Check if we are at the top page and execute the up button as needed
     */
    public final void updateDrawerEnable() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            FragmentItem item = topBackStack();
            if (item != null) {
                setDrawerState(false);
            } else {
                setDrawerState(true);
            }
        }
    }

    /**
     * @param isEnabled True to enable nav drawer.
     */
    private void setDrawerState(boolean isEnabled) {
        mDrawerToggle.setDrawerIndicatorEnabled(isEnabled);
        if (isEnabled) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        mDrawerToggle.syncState();
    }

    /**
     * @return True if the backstack size is greater then 0
     */
    public final boolean hasBackStack() {
        return getSupportFragmentManager().getBackStackEntryCount() > 0;
    }

    /**
     * @return Fragment item at the top of the back stack. Null if none (or main).
     */
    @Nullable
    public FragmentItem topBackStack() {
        if (!hasBackStack()) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getFragmentItem(tag);
    }

    /**
     * Clear the backstack
     */
    public final void clearBackStack() {
        clearingBackStack = true;
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().executePendingTransactions();
        clearingBackStack = false;
    }

    /**
     * Called when the android back button was pressed
     */
    @Override
    public final void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else {
            if (backPressed()) {
                super.onBackPressed();
            }
        }
    }

    /**
     * Called when the android back button or up button was pressed
     * Signal to fragments that need it that the use clicked back and is leaving them
     */
    private boolean backPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if (f instanceof BaseFragment) {
            BaseFragment fragment = (BaseFragment) f;
            if (fragment.goBackInternal()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Window focus has changed
     *
     * @param hasFocus True if app is focused
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        UtilsDisplay.updateImmersiveMode(this, hasFocus);
    }

    /**
     * Adds a fragment to the list. You must add a fragment to this before it can be loaded
     *
     * @param tag   Use the Fragment.TAG for the tag
     * @param clazz Use Fragment.class
     */
    protected final void addFragment(@NonNull String tag, @NonNull Class clazz, @Nullable String parentTag) {
        for (int i = 0; i < fragmentItems.size(); i++) {
            if (fragmentItems.get(i).tag.equals(tag)) {
                fragmentItems.remove(i);
                break;
            }
        }
        fragmentItems.add(new FragmentItem(tag, clazz, parentTag));
    }

    /**
     * Gets the fragment from the fragment manager or creates it if it doesn't exist.
     *
     * @param tag Use the Fragment.TAG for the tag
     * @return Instance of the fragment.
     */
    @Nullable
    private FragmentItem getFragmentItem(@NonNull String tag) {
        for (FragmentItem item : fragmentItems) {
            if (tag.contains(item.tag)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Load the fragment into view
     *
     * @param tag Use the Fragment.TAG for the tag
     */
    protected final void loadFragment(@NonNull String tag) {
        loadFragment(tag, null);
    }

    /**
     * Load the fragment into view
     *
     * @param tag    Use the Fragment.TAG for the tag
     * @param bundle Extra bundle of data to pass to the fragment
     */
    protected final void loadFragment(@NonNull String tag, @Nullable Bundle bundle) {
        loadFragment(tag, bundle, null);
    }

    /**
     * Load the fragment into view
     *
     * @param tag    Use the Fragment.TAG for the tag
     * @param bundle Extra bundle of data to pass to the fragment
     * @param object Generic object to pass to the fragment
     */
    protected final void loadFragment(@NonNull String tag, @Nullable Bundle bundle, @Nullable Object object) {
        loadFragment(tag, bundle, object, -1);
    }

    /**
     * Load the fragment into view
     *
     * @param tag     Use the Fragment.TAG for the tag
     * @param bundle  Extra bundle of data to pass to the fragment
     * @param object  Generic object to pass to the fragment
     * @param scrollY Starting scroll Y position
     */
    protected final void loadFragment(@NonNull String tag, @Nullable Bundle bundle, @Nullable Object object, int scrollY) {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            loadRequest.tag = tag;
            loadRequest.bundle = bundle;
            loadRequest.object = object;
            loadRequest.scrollY = scrollY;
            loadRequest.active = true;
            return;
        }

        FragmentItem item = getFragmentItem(tag);
        if (item == null) {
            return;
        }
        BaseFragment fragment = item.getFragment(this);
        if (fragment == null) {
            return;
        }
        fragment.setBundle(bundle);
        fragment.setObject(object);
        fragment.setScrollY(scrollY);
        loadFragment(fragment, item);
    }

    /**
     * Loads the given fragment into the container view.
     *
     * @param fragment Fragment to add
     * @param item     Fragment Item
     */
    private void loadFragment(@NonNull BaseFragment fragment, @NonNull FragmentItem item) {
        if (!isFragmentUIActive(fragment)) {
            Log.v("BaseActivity", "LoadFragment: " + item.tag);
            if (!item.isMain()) {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .setCustomAnimations(R.anim.right_to_center, R.anim.center_to_left)
                        .addToBackStack(item.tag)
                        .replace(R.id.container, fragment, item.tag)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                //.setCustomAnimations(R.anim.right_to_center, R.anim.center_to_left)
                        .replace(R.id.container, fragment, item.tag)
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
    private boolean isFragmentUIActive(@NonNull BaseFragment fragment) {
        return fragment.isAdded() && !fragment.isDetached() && !fragment.isRemoving();
    }

    /**
     * @param event Fab Event
     */
    public final void onFabEvent(@NonNull BaseActivity.FabEvent event) {
        switch (event) {
            case CLICKED:

                break;
            case SHOW:
                fab.show();
                break;
            case HIDE:
                fab.hide();
                break;
            case SET_ICON:
                fab.setImageResource(event.icon);
                break;
        }
    }

    /**
     * NEVER store the result of this.
     *
     * @return This activity
     */
    @Override
    @NonNull
    public final BaseActivity getActivity() {
        return this;
    }

    /**
     * Fab action event
     */
    public enum FabEvent {
        CLICKED,
        SHOW,
        HIDE,
        SET_ICON;

        @DrawableRes
        public int icon;

        public FabEvent setIcon(@DrawableRes int icon) {
            this.icon = icon;
            return this;
        }

        /**
         * Send the action event
         */
        public final void post() {
            Bus.postEnum(this);
        }

    }

    /**
     * Events that trigger an action in the App Base Activity
     */
    public enum AppBaseEvent {
        SHOW_SPINNER,
        HIDE_SPINNER,
        SHOW_TOOLBAR_INSTANT,
        HIDE_TOOLBAR_INSTANT,
        SHOW_TOOLBAR_ANIM,
        HIDE_TOOLBAR_ANIM,
        TOOLBAR_OVERLAY_ENABLE,
        TOOLBAR_OVERLAY_DISABLE,
        BACK_TO_MAIN_FRAGMENT,
        BACK_ONE_FRAGMENT,
        HIDE_KEYBOARD,
        HIDE_TOOLBAR_TITLE,
        SHOW_TOOLBAR_TITLE,
        SETTINGS_CLICKED,
        LICENSE_CLICKED,
        HELP_CLICKED,
        SETTINGS_CHANGED,
        SET_TOOLBAR_TITLE;

        public String text;

        /**
         * Send the action event
         */
        public final void post() {
            Bus.postEnum(this);
        }

        /**
         * @param text Text to set
         * @return this event
         */
        public final AppBaseEvent text(String text) {
            this.text = text;
            return this;
        }
    }

}
