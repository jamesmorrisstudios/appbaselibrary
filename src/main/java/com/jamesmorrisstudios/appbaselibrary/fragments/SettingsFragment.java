package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.preferences.Prefs;

/**
 * Created by James on 4/29/2015.
 */
public class SettingsFragment extends BaseFragment {
    public static final String TAG = "SettingsFragment";

    private OnSettingsListener settingListener;
    private transient boolean allowListener = false;

    /**
     * Required empty public constructor
     */
    public SettingsFragment() {
    }

    /**
     * Attach to the activity
     *
     * @param activity Activity to attach
     */
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            settingListener = (OnSettingsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSettingsListener");
        }
    }

    /**
     * Detach from activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        settingListener = null;
    }

    /**
     * Create view
     *
     * @param inflater           Inflater object
     * @param container          Container view
     * @param savedInstanceState Saved instance state
     * @return The top view for this fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Log.v("SettingsFragment", "On Create View");
        allowListener = false;
        addSettingsOptions(view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        allowListener = false;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        allowListener = false;
        super.onSaveInstanceState(bundle);
    }

    protected final LinearLayout getSettingsContainer(View view) {
        return (LinearLayout) view.findViewById(R.id.settings_container);
    }

    protected final void addSettingsOptions(View view) {
        LinearLayout settingsContainer = getSettingsContainer(view);
        TypedArray settingsTop = getResources().obtainTypedArray(R.array.settings_array);
        for (int i = 0; i < settingsTop.length(); i++) {
            int id = settingsTop.getResourceId(i, 0);
            if (id > 0) {
                TypedArray categoryItem = getResources().obtainTypedArray(id);
                if(categoryItem.length() >= 2) {
                    int idTitle = categoryItem.getResourceId(0, 0);
                    String title = getResources().getString(idTitle);
                    LinearLayout category = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.settings_category_container, null);
                    TextView titleView = (TextView) category.findViewById(R.id.title);
                    titleView.setText(title);
                    settingsContainer.addView(category);
                    addSettings(category, categoryItem);
                }
                categoryItem.recycle();
            }
        }
        settingsTop.recycle();
    }

    protected void addSettings(LinearLayout settingsContainer, TypedArray settingsArr) {
        for (int i = 1; i < settingsArr.length(); i++) {
            int id = settingsArr.getResourceId(i, 0);
            if (id > 0) {
                TypedArray item = getResources().obtainTypedArray(id);
                if (item.length() == 5) {
                    //Boolean item
                    int idType = item.getResourceId(0, 0);
                    int idKey = item.getResourceId(1, 0);
                    int idPrimary = item.getResourceId(2, 0);
                    int idSecondary = item.getResourceId(3, 0);
                    int idRestart = item.getResourceId(4, 0);
                    boolean defaultVal = getResources().getBoolean(idType);
                    boolean restartVal = getResources().getBoolean(idRestart);
                    String key = getResources().getString(idKey);
                    String primary = getResources().getString(idPrimary);
                    String secondary = getResources().getString(idSecondary);
                    addBooleanSettingsItem(settingsContainer, primary, secondary, key, defaultVal, restartVal);
                } else if (item.length() == 6) {
                    //List item
                    int idType = item.getResourceId(0, 0);
                    int idKey = item.getResourceId(1, 0);
                    int idPrimary = item.getResourceId(2, 0);
                    int idSecondary = item.getResourceId(3, 0); //Unused but there for the count
                    int idRestart = item.getResourceId(4, 0);
                    int idList = item.getResourceId(5, 0);
                    int defaultVal = getResources().getInteger(idType);
                    boolean restartVal = getResources().getBoolean(idRestart);
                    String key = getResources().getString(idKey);
                    String primary = getResources().getString(idPrimary);
                    String[] list = getResources().getStringArray(idList);
                    addListSettingsItem(settingsContainer, primary, key, defaultVal, list, restartVal);
                }
                item.recycle();
            }
        }
    }

    protected void addListSettingsItem(@NonNull LinearLayout container, @NonNull String primary, final @NonNull String key, int defaultValue, String[] list, final boolean restartActivity) {
        View item = getActivity().getLayoutInflater().inflate(R.layout.settings_item_list, null);
        TextView primaryItem = (TextView) item.findViewById(R.id.primary);
        AppCompatSpinner listItem = (AppCompatSpinner) item.findViewById(R.id.listItem);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(item.getContext(), R.layout.support_simple_spinner_dropdown_item, list);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_drop_down_item);
        listItem.setAdapter(spinnerArrayAdapter);
        listItem.setSelection(Prefs.getInt(getString(R.string.settings_pref), key, defaultValue), false);
        listItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!allowListener) {
                    return;
                }
                Prefs.putInt(getString(R.string.settings_pref), key, position);
                settingListener.onSettingsChanged();
                Log.v("SettingsFragment", "Key: " + key + " SetTo: " + position);
                if (restartActivity) {
                    settingListener.restartActivity();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                allowListener = true;
                return false;
            }
        });
        primaryItem.setText(primary);
        container.addView(item);
    }

    protected void addBooleanSettingsItem(@NonNull LinearLayout container, @NonNull String primary, @NonNull String secondary, final @NonNull String key, boolean defaultValue, final boolean restartActivity) {
        View item = getActivity().getLayoutInflater().inflate(R.layout.settings_item_boolean, null);
        TextView primaryItem = (TextView) item.findViewById(R.id.primary);
        TextView secondaryItem = (TextView) item.findViewById(R.id.secondary);
        SwitchCompat switchItem = (SwitchCompat) item.findViewById(R.id.switchItem);
        switchItem.setChecked(Prefs.getBoolean(getString(R.string.settings_pref), key, defaultValue));
        switchItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!allowListener) {
                    return;
                }
                Prefs.putBoolean(getString(R.string.settings_pref), key, isChecked);
                settingListener.onSettingsChanged();
                Log.v("SettingsFragment", "Key: " + key + " SetTo: " + isChecked);
                if (restartActivity ) {
                    settingListener.restartActivity();
                }
            }
        });
        switchItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                allowListener = true;
                return false;
            }
        });
        primaryItem.setText(primary);
        secondaryItem.setText(secondary);
        container.addView(item);
    }

    @Override
    public void onBack() {

    }

    @Override
    public boolean showToolbarTitle() {
        return true;
    }

    @Override
    protected void saveState(Bundle bundle) {

    }

    @Override
    protected void restoreState(Bundle bundle) {

    }

    @Override
    protected void afterViewCreated() {

    }

    public interface OnSettingsListener {

        void restartActivity();

        /**
         * Called on settings change event
         */
        void onSettingsChanged();
    }

}
