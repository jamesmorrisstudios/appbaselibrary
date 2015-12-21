package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.R;

/**
 * License fragment
 * <p/>
 * Created by James on 4/29/2015.
 */
public final class LicenseFragment extends BaseFragment {
    public static final String TAG = "LicenseFragment";

    /**
     * Required empty public constructor
     */
    public LicenseFragment() {
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
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NestedScrollView view = (NestedScrollView) inflater.inflate(R.layout.fragment_license, container, false);
        LinearLayout contView = (LinearLayout) view.findViewById(R.id.license_container);
        TypedArray licensesLibrary = getResources().obtainTypedArray(R.array.help_license_library);
        TypedArray codeApp = getResources().obtainTypedArray(R.array.help_license);
        buildLicense(licensesLibrary, codeApp, contView);
        licensesLibrary.recycle();
        codeApp.recycle();
        return view;
    }

    /**
     * Builds a license section given the typed array for library and app specitic sections
     * and the container for this section
     *
     * @param arrayLibrary Library general license list
     * @param arrayApp     App specific license list
     * @param container    Container view for this section
     */
    private void buildLicense(@NonNull TypedArray arrayLibrary, @NonNull TypedArray arrayApp, @NonNull LinearLayout container) {
        boolean found = false;
        for (int i = 0; i < arrayApp.length(); i++) {
            int id = arrayApp.getResourceId(i, 0);
            if (id > 0) {
                String[] arr = getResources().getStringArray(id);
                if (arr.length == 3) {
                    addLicenseItem(container, arr[0], arr[1], arr[2]);
                    found = true;
                }
            }
        }
        for (int i = 0; i < arrayLibrary.length(); i++) {
            int id = arrayLibrary.getResourceId(i, 0);
            if (id > 0) {
                String[] arr = getResources().getStringArray(id);
                if (arr.length == 3) {
                    addLicenseItem(container, arr[0], arr[1], arr[2]);
                    found = true;
                }
            }
        }
        if (found) {
            container.setVisibility(View.VISIBLE);
        } else {
            container.setVisibility(View.GONE);
        }
    }

    /**
     * Creates an individual license item
     *
     * @param container    Container for the item
     * @param title        Title of the item
     * @param link_site    Link to web site
     * @param link_license Link to license file
     */
    private void addLicenseItem(@NonNull LinearLayout container, @NonNull String title, @NonNull String link_site, @NonNull String link_license) {
        LinearLayout licenseItem = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.fragment_license_item, null);
        TextView textTitle = (TextView) licenseItem.findViewById(R.id.license_title);
        textTitle.setText(title);
        TextView textSite = (TextView) licenseItem.findViewById(R.id.license_site);
        textSite.setText(link_site);
        TextView textLicense = (TextView) licenseItem.findViewById(R.id.license_license);
        textLicense.setText(link_license);
        container.addView(licenseItem);
    }

    /**
     * Override this to set custom text for the toolbar
     *
     * @return Toolbar title text.
     */
    @NonNull
    protected String getToolbarTitle() {
        return getString(R.string.licenses);
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
    protected final void saveState(@NonNull Bundle bundle) {

    }

    /**
     * Unused
     *
     * @param bundle bundle
     */
    @Override
    protected final void restoreState(@NonNull Bundle bundle) {

    }

    /**
     * Register bus listener if used
     */
    @Override
    protected void registerBus() {

    }

    /**
     * Unregister bus listener if used
     */
    @Override
    protected void unregisterBus() {

    }

    /**
     * Unused
     */
    @Override
    protected final void afterViewCreated() {
        hideFab();
    }

}
