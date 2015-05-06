package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.utilitieslibrary.preferences.Preferences;

/**
 * Created by James on 4/29/2015.
 */
public class SettingsFragment extends BaseFragment {
    public static final String TAG = "SettingsFragment";

    /**
     * Required empty public constructor
     */
    public SettingsFragment() {
    }

    /**
     * @param savedInstanceState Saved instance state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        LinearLayout settingsContainer = (LinearLayout) view.findViewById(R.id.settings_container);

        TypedArray settings = getResources().obtainTypedArray(R.array.settings_array);
        for (int i = 0; i < settings.length(); i++) {
            int id = settings.getResourceId(i, 0);
            if (id > 0) {
                TypedArray item = getResources().obtainTypedArray(id);
                if (item.length() == 4) {
                    int idType = item.getResourceId(0, 0);
                    int idKey = item.getResourceId(1, 0);
                    int idPrimary = item.getResourceId(2, 0);
                    int idSecondary = item.getResourceId(3, 0);
                    boolean type = getResources().getBoolean(idType);
                    String key = getResources().getString(idKey);
                    String primary = getResources().getString(idPrimary);
                    String secondary = getResources().getString(idSecondary);
                    addBooleanSettingsItem(settingsContainer, primary, secondary, key);
                }
                item.recycle();
            }
        }
        settings.recycle();
        return view;
    }

    private void addBooleanSettingsItem(@NonNull LinearLayout container, @NonNull String primary, @NonNull String secondary, final @NonNull String key) {
        View item = getActivity().getLayoutInflater().inflate(R.layout.settings_item_boolean, null);
        TextView primaryItem = (TextView) item.findViewById(R.id.primary);
        TextView secondaryItem = (TextView) item.findViewById(R.id.secondary);
        SwitchCompat switchItem = (SwitchCompat) item.findViewById(R.id.switchItem);
        switchItem.setChecked(Preferences.getBoolean(getString(R.string.settings_pref), key, true));
        switchItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Preferences.putBoolean(getString(R.string.settings_pref), key, isChecked);
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
    protected void afterViewCreated() {

    }

}
