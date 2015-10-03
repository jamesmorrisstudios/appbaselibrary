package com.jamesmorrisstudios.appbaselibrary.activities;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.jamesmorrisstudios.appbaselibrary.Bus;
import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.animator.AnimatorControl;
import com.jamesmorrisstudios.appbaselibrary.animator.AnimatorEndListener;
import com.jamesmorrisstudios.appbaselibrary.animator.AnimatorStartListener;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.colorpicker.ColorPickerView;
import com.jamesmorrisstudios.appbaselibrary.colorpicker.OnColorSelectedListener;
import com.jamesmorrisstudios.appbaselibrary.colorpicker.builder.ColorPickerClickListener;
import com.jamesmorrisstudios.appbaselibrary.colorpicker.builder.ColorPickerDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.dialogHelper.ColorPickerRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogHelper.EditTextListRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogHelper.FileBrowserRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogHelper.MultiChoiceRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogHelper.PromptDialogRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogHelper.RingtoneRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogHelper.SingleChoiceIconRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogHelper.SingleChoiceRadioRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogHelper.SingleChoiceRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogHelper.TimePickerRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogs.EditTextListDialog;
import com.jamesmorrisstudios.appbaselibrary.dialogs.SingleChoiceIconDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.fragments.BaseFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.BaseMainFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.BaseMainRecycleListFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.HelpFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.LicenseFragment;
import com.jamesmorrisstudios.appbaselibrary.fragments.SettingsFragment;
import com.jamesmorrisstudios.appbaselibrary.preferences.Prefs;
import com.jamesmorrisstudios.appbaselibrary.sound.Sounds;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.squareup.otto.Subscribe;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;

/**
 * Base level activity implementation. This handles getting the toolbar up and running and includes a main fragment page
 * along with help and settings fragments. Extend the mainFragment for your main page
 * <p/>
 * Created by James on 4/29/2015.
 */
public abstract class BaseLauncherNoViewActivity extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener,
        BaseMainFragment.OnMenuItemClickedListener,
        BaseMainRecycleListFragment.OnMenuItemClickedListener,
        HelpFragment.OnHelpSubPageListener,
        BaseFragment.OnUtilListener,
        SettingsFragment.OnSettingsListener {

    //On Activity Result codes
    private static final int RINGTONE_RESULT = 5010;
    private static final int FILE_BROWSER_RESULT = 5011;
    //Permission Request Codes
    private static final int REQUEST_READ_STORAGE_RINGTONE = 6000;
    private static final int REQUEST_WRITE_STORAGE_FILE_BROWSER = 6001;

    private FrameLayout container;
    private Toolbar toolbar;
    private ProgressBar spinner;

    private boolean useAutoLock = false;

    //Saved requests that have to survive on activity result and/or permission request
    private RingtoneRequest ringtoneRequest = null;
    private FileBrowserRequest fileBrowserRequest = null;

    private final Object busListener = new Object() {
        @Subscribe
        public void onFileBrowserRequest(final FileBrowserRequest request) {
            BaseLauncherNoViewActivity.this.createFileBrowserDialog(request);
        }
        @Subscribe
        public void onRingtoneRequest(final RingtoneRequest request) {
            BaseLauncherNoViewActivity.this.createRingtoneDialog(request.currentTone, request.title, request.listener);
        }

        @Subscribe
        public void onColorPickerRequest(ColorPickerRequest request) {
            createColorPickerDialog(request.initialColor, request.onColorPickerClickListener, request.onNegative, request.onDisable);
        }

        @Subscribe
        public void onPromptDialogRequest(PromptDialogRequest request) {
            createPromptDialog(request.title, request.content, request.onPositive, request.onNegative);
        }

        @Subscribe
        public void onTimePickerDialogRequest(TimePickerRequest request) {
            createTimePickerDialog(request.onTimeSetListener, request.hour, request.minute, request.is24Hour);
        }

        @Subscribe
        public void onEditMessageRequest(@NonNull EditTextListRequest request) {
            showEditTextListDialog(request.messages, request.onPositive, request.onNegative);
        }

        @Subscribe
        public void onAppBaseEvent(AppBaseEvent event) {
            BaseLauncherNoViewActivity.this.onAppBaseEvent(event);
        }

        @Subscribe
        public void onSingleChoiceRequest(SingleChoiceRequest request) {
            BaseLauncherNoViewActivity.this.createSingleChoiceDialog(request.title, request.items, request.clickListener, request.onNegative);
        }

        @Subscribe
        public void onSingleChoiceRadioRequest(SingleChoiceRadioRequest request) {
            BaseLauncherNoViewActivity.this.createSingleChoiceRadioDialog(request.title, request.items, request.defaultValue, request.clickListener, request.onPositive, request.onNegative);
        }

        @Subscribe
        public void onMultiChoiceRadioRequest(MultiChoiceRequest request) {
            BaseLauncherNoViewActivity.this.createMultiChoiceDialog(request.title, request.items, request.checkedItems, request.clickListener, request.onPositive, request.onNegative);
        }

        @Subscribe
        public void onSingleChoiceIconRequest(SingleChoiceIconRequest request) {
            BaseLauncherNoViewActivity.this.createSingleChoiceIconDialog(request.title, request.items, request.onOptionPickedListener);
        }
    };
    private boolean clearingBackStack = false;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        updateImmersiveMode(true);
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

        //Ringtone picker
        if (requestCode == RINGTONE_RESULT) {
            if(resultCode == Activity.RESULT_OK && intent != null && intent.hasExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)) {
                String name = null;
                Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (uri != null) {
                    Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
                    if (ringtone != null) {
                        name = ringtone.getTitle(this);
                    }
                }
                if (ringtoneRequest != null) {
                    ringtoneRequest.listener.ringtoneResponse(uri, name);
                    ringtoneRequest = null;
                }
            }
            disableAutoLock();
        }

        //File Browser
        if(requestCode == FILE_BROWSER_RESULT) {
            if(resultCode == Activity.RESULT_OK && intent != null) {
                Uri uri = intent.getData();
                if(fileBrowserRequest != null) {
                    fileBrowserRequest.fileBrowserRequestListener.path(uri);
                    fileBrowserRequest = null;
                }
            }
            disableAutoLock();
        }
    }

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
     * This handles all the on create work after the view has been set.
     * This is normally called from onCreate. If you are supplying your own view in onCreate
     * then you must call this manually.
     */
    protected final void initOnCreate() {
        container = (FrameLayout) findViewById(R.id.container);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_short_name));
        setSupportActionBar(toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();
        if (!hasBackStack()) {
            loadMainFragment();
        }
    }

    /**
     * Activity start
     */
    @Override
    public void onStart() {
        super.onStart();
        Bus.register(busListener);
        Sounds.getInstance().onStart();
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
        Bus.unregister(busListener);
        Sounds.getInstance().onStop();
    }

    /**
     * Activity stop
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Sounds.getInstance().onDestroy();
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
    protected void loadSettingsFragment() {
        SettingsFragment fragment = getSettingsFragment();
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
     * Called on settings change event
     */
    @Override
    public void onSettingsChanged() {
        updateImmersiveMode(true);
        Sounds.getInstance().reloadSettings();
    }

    @Override
    public final void restartActivity() {
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    /**
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        updateImmersiveMode(hasFocus);
    }

    /**
     * @param hasFocus
     */
    protected final void updateImmersiveMode(boolean hasFocus) {
        if (Build.VERSION.SDK_INT >= 11) {
            int newUiOptions = 0;
            String pref = AppBase.getContext().getString(R.string.settings_pref);
            String key = AppBase.getContext().getString(R.string.pref_immersive);
            if (Prefs.getBoolean(pref, key, false)) {
                if (hasFocus) {
                    if (Build.VERSION.SDK_INT >= 16) {
                        newUiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                    }
                    if (Build.VERSION.SDK_INT >= 19) {
                        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                    }
                    getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
                }
            } else {
                if (hasFocus) {
                    if (Build.VERSION.SDK_INT >= 16) {
                        newUiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                    }
                    getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
                }
            }
        }
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

    private void toggleShowToolbar(boolean show, boolean instant) {
        if(instant) {
            if (show) {
                toolbar.setVisibility(View.VISIBLE);
            } else {
                toolbar.setVisibility(View.GONE);
            }
        } else {
            if(show) {
                AnimatorControl.translateYAutoStart(toolbar, -toolbar.getHeight(), 0, 100, 0, new AnimatorStartListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        toolbar.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                AnimatorControl.translateYAutoStart(toolbar, 0, -toolbar.getHeight(), 100, 0, new AnimatorEndListener() {
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

    public void createFileBrowserDialog(@NonNull FileBrowserRequest request) {
        fileBrowserRequest = request;
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                enableAutoLock();
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE_FILE_BROWSER);
            } else {
                createFileBrowserDialogSub();
            }
        } else {
            createFileBrowserDialogSub();
        }
    }

    public void createFileBrowserDialogSub() {
        if(fileBrowserRequest == null) {
            return;
        }
        Intent i = new Intent(this, CustomFilePickerActivity.class);
        i.putExtra(CustomFilePickerActivity.EXTRA_EXTENSION, fileBrowserRequest.extension);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false); //Not supported on the return yet
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, fileBrowserRequest.allowCreateDir);
        if (fileBrowserRequest.dirType == FileBrowserRequest.DirType.DIRECTORY) {
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
        } else {
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        }
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        try {
            enableAutoLock();
            startActivityForResult(i, FILE_BROWSER_RESULT);
        } catch (Exception ex) {
            ex.printStackTrace();
            disableAutoLock();
            Utils.toastShort(getString(R.string.failed));
        }
    }

    /**
     * Create a time picker dialog
     *
     * @param listener Return listener
     * @param hour     Start hour
     * @param minute   Start minute
     * @param is24Hour True if 24 hour mode
     */
    public void createTimePickerDialog(@NonNull TimePickerDialog.OnTimeSetListener listener, int hour, int minute, boolean is24Hour) {
        TimePickerDialog time = new TimePickerDialog();
        time.initialize(listener, hour, minute, is24Hour);
        time.show(getSupportFragmentManager(), "TimePickerDialog");
    }

    public void createPromptDialog(@NonNull String title, @NonNull String content, @NonNull DialogInterface.OnClickListener onPositive, @NonNull DialogInterface.OnClickListener onNegative) {
        new AlertDialog.Builder(this, R.style.alertDialog)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(R.string.okay, onPositive)
                .setNegativeButton(R.string.cancel, onNegative)
                .show();
    }

    public void createSingleChoiceDialog(@NonNull String title, @NonNull String[] items, @NonNull DialogInterface.OnClickListener clickListener, @Nullable DialogInterface.OnClickListener onNegative) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.alertDialog)
                .setTitle(title)
                .setItems(items, clickListener);
        if(onNegative != null) {
            builder.setNegativeButton(R.string.cancel, onNegative);
        }
        builder.show();
    }

    public void createSingleChoiceRadioDialog(@NonNull String title, @NonNull String[] items, int defaultValue, @NonNull DialogInterface.OnClickListener clickListener, @NonNull DialogInterface.OnClickListener onPositive, @Nullable DialogInterface.OnClickListener onNegative) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.alertDialog)
                .setTitle(title)
                .setPositiveButton(R.string.okay, onPositive)
                .setSingleChoiceItems(items, defaultValue, clickListener);
        if(onNegative != null) {
            builder.setNegativeButton(R.string.cancel, onNegative);
        }
        builder.show();
    }

    public void createMultiChoiceDialog(@NonNull String title, @NonNull String[] items, boolean[] checkedItems, @NonNull DialogInterface.OnMultiChoiceClickListener clickListener, @NonNull DialogInterface.OnClickListener onPositive, @Nullable DialogInterface.OnClickListener onNegative) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.alertDialog)
                .setTitle(title)
                .setPositiveButton(R.string.okay, onPositive)
                .setMultiChoiceItems(items, checkedItems, clickListener);
        if(onNegative != null) {
            builder.setNegativeButton(R.string.cancel, onNegative);
        }
        builder.show();
    }

    public void createSingleChoiceIconDialog(@NonNull String title, @NonNull @DrawableRes int[] items, @NonNull SingleChoiceIconDialogBuilder.OptionPickerListener onOptionPickedListener) {
        SingleChoiceIconDialogBuilder.with(this)
                .setTitle(title)
                .setItems(items)
                .setOnOptionPicked(onOptionPickedListener)
                .build()
                .show();
    }

    public void createColorPickerDialog(int initialColor, @NonNull ColorPickerClickListener onColorPickerClickListener, @NonNull DialogInterface.OnClickListener onNegative, @Nullable DialogInterface.OnClickListener onDisable) {
        ColorPickerDialogBuilder builder = ColorPickerDialogBuilder.with(this)
                .setTitle(getResources().getString(R.string.choose_color))
                .initialColor(initialColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .noSliders()
                .density(6)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //Unused as the onPositive call gives the same info
                    }
                })
                .setPositiveButton(getResources().getString(R.string.okay), onColorPickerClickListener)
                .setNegativeButton(getResources().getString(R.string.cancel), onNegative);
                if(onDisable != null) {
                    builder.setNeutralButton(getResources().getString(R.string.disable), onDisable);
                }
                builder.build().show();
    }

    public void createRingtoneDialog(@Nullable Uri currentTone, @NonNull String title, @NonNull RingtoneRequest.RingtoneRequestListener listener) {
        this.ringtoneRequest = new RingtoneRequest(currentTone, title, listener);
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                enableAutoLock();
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE_RINGTONE);
            } else {
                createRingtoneDialogSub();
            }
        } else {
            createRingtoneDialogSub();
        }
    }

    private void createRingtoneDialogSub() {
        if(ringtoneRequest == null) {
            return;
        }
        Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if(ringtoneRequest.currentTone != null) {
            defaultUri = ringtoneRequest.currentTone;
        }
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, ringtoneRequest.title);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, defaultUri);
        try {
            enableAutoLock();
            startActivityForResult(intent, RINGTONE_RESULT);
        } catch (Exception ex) {
            disableAutoLock();
            Utils.toastShort(getString(R.string.failed_open_link));
        }
    }

    public void showEditTextListDialog(@NonNull ArrayList<String> messages, @NonNull EditTextListDialog.EditMessageListener onPositive, @Nullable View.OnClickListener onNegative) {
        FragmentManager fm = getSupportFragmentManager();
        EditTextListDialog editTextListDialog = new EditTextListDialog();
        editTextListDialog.setData(messages, onPositive, onNegative);
        editTextListDialog.show(fm, "fragment_edit_text_list");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_STORAGE_RINGTONE:
                disableAutoLock();
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("BaseActivity", "Permission Granted for external storage");
                    createRingtoneDialogSub();
                } else {
                    Log.v("BaseActivity", "Permission Denied for external storage");
                    createRingtoneDialogSub();
                }
                break;
            case REQUEST_WRITE_STORAGE_FILE_BROWSER:
                disableAutoLock();
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("BaseActivity", "Permission Granted for external storage");
                    createFileBrowserDialogSub();
                } else {
                    Log.v("BaseActivity", "Permission Denied for external storage");
                }
                break;
        }
    }

    protected final void enableAutoLock() {
        if(useAutoLock) {
            return;
        }
        if(Utils.getOrientationLock(this) == Utils.Orientation.UNDEFINED) {
            Utils.lockOrientationCurrent(this);
            useAutoLock = true;
        } else {
            useAutoLock = false;
        }
    }

    protected final void disableAutoLock() {
        if(useAutoLock) {
            Utils.unlockOrientation(this);
            useAutoLock = false;
        }
    }

    public enum AppBaseEvent {
        SHOW_SPINNER, HIDE_SPINNER, SHOW_TOOLBAR_INSTANT, HIDE_TOOLBAR_INSTANT, SHOW_TOOLBAR_ANIM, HIDE_TOOLBAR_ANIM,
        TOOLBAR_OVERLAY_ENABLE, TOOLBAR_OVERLAY_DISABLE
    }

}

