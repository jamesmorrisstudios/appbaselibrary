package com.jamesmorrisstudios.appbaselibrary.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.fragments.BaseFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.BaseMainFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.BaseMainRecycleListFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.HelpFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.LicenseFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.SettingsFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.TutorialFragment;
import com.jamesmorrisstudios.materialuilibrary.dialogs.ColorSelector;
import com.jamesmorrisstudios.materialuilibrary.dialogs.MaterialDialog;
import com.jamesmorrisstudios.materialuilibrary.dialogs.time.TimePickerDialog;

/**
 * Base level activity implementation. This handles getting the toolbar up and running and includes a main fragment page
 * along with help and settings fragments. Extend the mainFragment for your main page
 *
 * Created by James on 4/29/2015.
 */
public abstract class BaseLauncherActivity extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener,
        BaseMainFragment.OnMenuItemClickedListener,
        BaseMainRecycleListFragment.OnMenuItemClickedListener,
        HelpFragment.OnHelpSubPageListener,
        BaseFragment.OnDialogListener,
        BaseFragment.OnUtilListener {

    private boolean clearingBackStack = false;

    /**
     * The fragment is changing. This is called right after the fragment is notified
     */
    protected abstract void onFragmentChangeStart();

    /**
     * The fragment was just changed
     */
    protected abstract void onFragmentChangeEnd();

    /**
     * Create this activity
     * @param savedInstanceState Saved instance state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_short_name));
        setSupportActionBar(toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();
        if(!hasBackStack()) {
            loadMainFragment();
        }
    }

    /**
     * Activity start
     */
    @Override
      public void onStart() {
        super.onStart();
    }

    /**
     * Activity resume
     */
    @Override
    public void onResume() {
        super.onResume();
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
    }

    /**
     * Fragment backstack changed.
     */
    @Override
    public void onBackStackChanged() {
        if(!clearingBackStack) {
            shouldDisplayHomeUp();
            onFragmentChangeEnd();
        }
    }

    /**
     * Check if we are at the top page and show the up button as needed
     */
    public final void shouldDisplayHomeUp(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(hasBackStack());
        }
    }

    public final boolean hasBackStack() {
        return getSupportFragmentManager().getBackStackEntryCount() > 0;
    }

    public final void clearBackStack() {
        clearingBackStack = true;
        while(hasBackStack()) {
            getSupportFragmentManager().popBackStack();
        }
        clearingBackStack = false;
    }

    /**
     * The up button was pressed so pop one off the backstack
     * and notify the visible fragment that it is being left
     * @return Always true
     */
    @Override
    public boolean onSupportNavigateUp() {
        backUpPressed();
        getSupportFragmentManager().popBackStack();
        return true;
    }

    /**
     * Called when the android back button was pressed
     * notify the visible fragment that it is being left
     */
    @Override
    public void onBackPressed() {
        backUpPressed();
        super.onBackPressed();
    }

    /**
     * Called when the android back button or up button was pressed
     * Signal to fragments that need it that the use clicked back and is leaving them
     */
    protected void backUpPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if (f instanceof BaseFragment) {
            ((BaseFragment) f).onBack();
        }
        onFragmentChangeStart();
    }

    /**
     * Gets the tutorial fragment from the fragment manager.
     * Creates the fragment if it does not exist yet.
     * @return The fragment
     */
    @NonNull
    protected final TutorialFragment getTutorialFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        TutorialFragment fragment = (TutorialFragment) fragmentManager.findFragmentByTag(TutorialFragment.TAG);
        if (fragment == null) {
            fragment = new TutorialFragment();
        }
        return fragment;
    }

    /**
     * Loads the tutorial fragment into the main view
     */
    protected final void loadTutorialFragment() {
        TutorialFragment fragment = getTutorialFragment();
        loadFragment(fragment, TutorialFragment.TAG, true);
        getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * Gets the license fragment from the fragment manager.
     * Creates the fragment if it does not exist yet.
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
     * @return The fragment
     */
    @NonNull
    protected final SettingsFragment getSettingsFragment() {
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
    protected final void loadSettingsFragment() {
        SettingsFragment fragment = getSettingsFragment();
        loadFragment(fragment, SettingsFragment.TAG, true);
        getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * Gets the main list fragment from the fragment manager.
     * Creates the fragment if it does not exist yet.
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
     * @param fragment Fragment to add
     * @param tag Tag to give the fragment
     * @param addBackStack True to add to backstack for back and up navigation
     */
    protected final void loadFragment(@NonNull BaseFragment fragment, @NonNull String tag, boolean addBackStack) {
        if (!isFragmentUIActive(fragment)) {
            onFragmentChangeStart();
            if(addBackStack) {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(tag)
                        .replace(R.id.container, fragment, tag)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.container, fragment, tag)
                        .commit();
            }
        }
    }

    /**
     * Checks if the UI of a fragment is active
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
    public void onSettingsClicked() {
        loadSettingsFragment();
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
     * Tutorial button clicked
     */
    @Override
    public void onTutorialClicked() {
        loadTutorialFragment();
    }

    /**
     * Create a time picker dialog
     * @param listener Return listener
     * @param hour Start hour
     * @param minute Start minute
     * @param is24Hour True if 24 hour mode
     */
    @Override
    public void createTimePickerDialog(@NonNull TimePickerDialog.OnTimeSetListener listener, int hour, int minute, boolean is24Hour) {
        TimePickerDialog time = new TimePickerDialog();
        time.initialize(listener, hour, minute, is24Hour);
        time.show(getFragmentManager(), "TimePickerDialog");
    }

    @Override
    public void createPromptDialog(@NonNull String title, @NonNull String content, MaterialDialog.ButtonCallback callback) {
        new MaterialDialog.Builder(this)
                .title(title)
                .content(content)
                .callback(callback)
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .show();
    }

    @Override
    public void createColorPickerDialog(int intialColor, ColorSelector.OnColorSelectedListener onColorSelectedListener) {
        ColorSelector colorSelector = new ColorSelector(this, intialColor, onColorSelectedListener);
        colorSelector.show();
    }

}
