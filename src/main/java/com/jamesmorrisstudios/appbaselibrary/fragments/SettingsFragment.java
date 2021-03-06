package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.UtilsDisplay;
import com.jamesmorrisstudios.appbaselibrary.UtilsVersion;
import com.jamesmorrisstudios.appbaselibrary.activities.BaseActivity;
import com.jamesmorrisstudios.appbaselibrary.activityHandlers.RestartAppRequest;
import com.jamesmorrisstudios.appbaselibrary.preferences.Prefs;

/**
 * Settings Fragment
 * <p/>
 * Created by James on 4/29/2015.
 */
public final class SettingsFragment extends BaseFragment {
    public static final String TAG = "SettingsFragment";
    private transient boolean allowListener = false;
    private NestedScrollView scrollView;
    private int startScrollY = -1;

    /**
     * Create view
     *
     * @param inflater           Inflater object
     * @param container          Container view
     * @param savedInstanceState Saved instance state
     * @return The top view for this fragment
     */
    @Override
    @NonNull
    public final View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        scrollView = (NestedScrollView) inflater.inflate(R.layout.fragment_settings, container, false);
        allowListener = false;
        addSettingsOptions(scrollView);
        return scrollView;
    }

    /**
     * View has been created.
     *
     * @param v                  view
     * @param savedInstanceState Saved instance state
     */
    public final void onViewCreated(@NonNull final View v, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (startScrollY != -1) {
                    scrollView.setScrollY(startScrollY);
                    startScrollY = -1;
                }
            }
        });
    }

    /**
     * On pause
     */
    @Override
    public final void onPause() {
        super.onPause();
        allowListener = false;
    }

    /**
     * @param view Top view
     * @return Settings container layout
     */
    private LinearLayout getSettingsContainer(@NonNull final View view) {
        return (LinearLayout) view.findViewById(R.id.settings_container);
    }

    /**
     * Adds all the settings options
     *
     * @param view Top View
     */
    private void addSettingsOptions(@NonNull final View view) {
        LinearLayout settingsContainer = getSettingsContainer(view);
        TypedArray settingsTop = getResources().obtainTypedArray(R.array.settings_array);
        for (int i = 0; i < settingsTop.length(); i++) {
            int id = settingsTop.getResourceId(i, 0);
            if (id > 0) {
                TypedArray categoryItem = getResources().obtainTypedArray(id);
                if (categoryItem.length() >= 2) {
                    int idTitle = categoryItem.getResourceId(0, 0);
                    String title = getResources().getString(idTitle);
                    LinearLayout category = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.fragment_settings_category, null);
                    TextView titleView = (TextView) category.findViewById(R.id.title);
                    titleView.setText(title);
                    settingsContainer.addView(category);
                    addSettingsCategory(category, categoryItem);
                }
                categoryItem.recycle();
            }
        }
        settingsTop.recycle();
    }

    /**
     * Add a single settings category
     *
     * @param settingsContainer Container view
     * @param settingsArr       Settings typed array
     */
    private void addSettingsCategory(@NonNull final LinearLayout settingsContainer, @NonNull final TypedArray settingsArr) {
        for (int i = 1; i < settingsArr.length(); i++) {
            int id = settingsArr.getResourceId(i, 0);
            if (id > 0) {
                TypedArray item = getResources().obtainTypedArray(id);
                if (item.length() == 6) {
                    //Boolean item
                    int idType = item.getResourceId(0, 0);
                    int idKey = item.getResourceId(1, 0);
                    int idPrimary = item.getResourceId(2, 0);
                    int idSecondary = item.getResourceId(3, 0);
                    int idRestart = item.getResourceId(4, 0);
                    int idPro = item.getResourceId(5, 0);
                    boolean defaultVal = getResources().getBoolean(idType);
                    boolean restartVal = getResources().getBoolean(idRestart);
                    boolean proVal = getResources().getBoolean(idPro);
                    String key = getResources().getString(idKey);
                    String primary = getResources().getString(idPrimary);
                    String secondary = getResources().getString(idSecondary);
                    addBooleanSettingsItem(settingsContainer, primary, secondary, key, defaultVal, restartVal, proVal);
                } else if (item.length() == 7) {
                    //List item
                    int idType = item.getResourceId(0, 0);
                    int idKey = item.getResourceId(1, 0);
                    int idPrimary = item.getResourceId(2, 0);
                    int idSecondary = item.getResourceId(3, 0); //Unused but there for the count
                    int idRestart = item.getResourceId(4, 0);
                    int idPro = item.getResourceId(5, 0);
                    int idList = item.getResourceId(6, 0);
                    int defaultVal = getResources().getInteger(idType);
                    boolean restartVal = getResources().getBoolean(idRestart);
                    boolean proVal = getResources().getBoolean(idPro);
                    String key = getResources().getString(idKey);
                    String primary = getResources().getString(idPrimary);
                    String[] list = getResources().getStringArray(idList);
                    addListSettingsItem(settingsContainer, primary, key, defaultVal, list, restartVal, proVal);
                }
                item.recycle();
            }
        }
    }

    /**
     * Add a list settings item
     *
     * @param container       Container view
     * @param primary         Primary text
     * @param key             Pref Key
     * @param defaultValue    Default starting value
     * @param list            List of item choices
     * @param restartActivity True to restart the activity on change
     * @param pro             True if this setting requires pro
     */
    private void addListSettingsItem(@NonNull final LinearLayout container, @NonNull final String primary, @NonNull final String key, final int defaultValue, final String[] list, final boolean restartActivity, final boolean pro) {
        View item = getActivity().getLayoutInflater().inflate(R.layout.fragment_settings_item_list, null);
        TextView primaryItem = (TextView) item.findViewById(R.id.primary);
        if (pro && !UtilsVersion.isPro()) {
            primaryItem.setCompoundDrawablesWithIntrinsicBounds(UtilsVersion.getProIcon(), 0, 0, 0);
            primaryItem.setCompoundDrawablePadding(UtilsDisplay.getDipInt(5));
        }
        final AppCompatSpinner listItem = (AppCompatSpinner) item.findViewById(R.id.listItem);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(item.getContext(), R.layout.support_simple_spinner_dropdown_item, list);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        listItem.setAdapter(spinnerArrayAdapter);
        listItem.setSelection(Prefs.getInt(getString(R.string.settings_pref), key, defaultValue), false);
        listItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!allowListener) {
                    return;
                }
                if (pro && !UtilsVersion.isPro()) {
                    UtilsVersion.showProPopup();
                    allowListener = false;
                    listItem.setSelection(Prefs.getInt(getString(R.string.settings_pref), key, defaultValue), false);
                } else {
                    Prefs.putInt(getString(R.string.settings_pref), key, position);
                    BaseActivity.AppBaseEvent.SETTINGS_CHANGED.post();
                    if (restartActivity) {
                        new RestartAppRequest(SettingsFragment.TAG, scrollView.getScrollY(), null).execute();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    allowListener = true;
                }
                return false;
            }
        });
        primaryItem.setText(primary);
        item.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    allowListener = true;
                }
                return false;
            }
        });
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItem.performClick();
            }
        });
        container.addView(item);
    }

    /**
     * Add a boolean settings item
     *
     * @param container       Container view
     * @param primary         Primary text
     * @param secondary       Secondary text
     * @param key             Pref Key
     * @param defaultValue    Default starting value
     * @param restartActivity True to restart the activity on change
     * @param pro             True if this setting requires pro
     */
    private void addBooleanSettingsItem(@NonNull final LinearLayout container, @NonNull final String primary, @NonNull final String secondary, @NonNull final String key, final boolean defaultValue, final boolean restartActivity, final boolean pro) {
        View item;
        if (!secondary.isEmpty()) {
            item = getActivity().getLayoutInflater().inflate(R.layout.fragment_settings_item_boolean, null);
            TextView secondaryItem = (TextView) item.findViewById(R.id.secondary);
            secondaryItem.setText(secondary);
        } else {
            item = getActivity().getLayoutInflater().inflate(R.layout.fragment_settings_item_boolean_single, null);
        }
        TextView primaryItem = (TextView) item.findViewById(R.id.primary);
        if (pro && !UtilsVersion.isPro()) {
            primaryItem.setCompoundDrawablesWithIntrinsicBounds(UtilsVersion.getProIcon(), 0, 0, 0);
            primaryItem.setCompoundDrawablePadding(UtilsDisplay.getDipInt(5));
        }
        final CheckBox switchItem = (CheckBox) item.findViewById(R.id.switchItem);
        switchItem.setChecked(Prefs.getBoolean(getString(R.string.settings_pref), key, defaultValue));
        switchItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!allowListener) {
                    return;
                }
                if (pro && !UtilsVersion.isPro()) {
                    UtilsVersion.showProPopup();
                    allowListener = false;
                    switchItem.setChecked(Prefs.getBoolean(getString(R.string.settings_pref), key, defaultValue));
                } else {
                    Prefs.putBoolean(getString(R.string.settings_pref), key, isChecked);
                    BaseActivity.AppBaseEvent.SETTINGS_CHANGED.post();
                    if (restartActivity) {
                        new RestartAppRequest(SettingsFragment.TAG, scrollView.getScrollY(), null).execute();
                    }
                }
            }
        });
        switchItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    allowListener = true;
                }
                return false;
            }
        });
        primaryItem.setText(primary);
        item.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    allowListener = true;
                }
                return false;
            }
        });
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchItem.performClick();
            }
        });
        container.addView(item);
    }

    /**
     * Override this to set custom text for the toolbar
     *
     * @return Toolbar title text.
     */
    @NonNull
    protected final String getToolbarTitle() {
        return getString(R.string.settings);
    }

    /**
     * @return true
     */
    @Override
    public final boolean showToolbarTitle() {
        return true;
    }

    /**
     * Unused
     *
     * @param bundle bundle
     */
    @Override
    protected final void saveState(@NonNull final Bundle bundle) {

    }

    /**
     * Unused
     *
     * @param bundle bundle
     */
    @Override
    protected final void restoreState(@NonNull final Bundle bundle) {

    }

    /**
     * Register bus listener if used
     */
    @Override
    protected final void registerBus() {

    }

    /**
     * Unregister bus listener if used
     */
    @Override
    protected final void unregisterBus() {

    }

    @Override
    protected void setStartData(@Nullable Bundle startBundle, int startScrollY) {
        this.startScrollY = startScrollY;
    }

    /**
     * Unused
     *
     * @return 0
     */
    @Override
    protected int getOptionsMenuRes() {
        return 0;
    }

    /**
     * @return False
     */
    @Override
    protected boolean usesOptionsMenu() {
        return false;
    }

    /**
     * Unused
     */
    @Override
    protected final void afterViewCreated() {
        hideFab();
    }

}
