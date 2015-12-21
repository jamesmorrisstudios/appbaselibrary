package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.UtilsTheme;
import com.jamesmorrisstudios.appbaselibrary.UtilsVersion;
import com.jamesmorrisstudios.appbaselibrary.fragmentHelpers.TabbedFragmentBaseTab;
import com.jamesmorrisstudios.appbaselibrary.fragmentHelpers.TabbedFragmentViewPager;

/**
 * TODO Early implementation
 * <p/>
 * Created by James on 12/8/2015.
 */
public abstract class TabbedFragment extends BaseFragment {
    public static final String TAG = "TabbedFragment";
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabbedFragmentBaseTab[] tabs = null;
    private int selectedTab = 0;

    /**
     * Get tabs
     *
     * @return Array of Tabs
     */
    @NonNull
    protected abstract TabbedFragmentBaseTab[] getTabs();

    /**
     * Get tab mode
     *
     * @return Tab Mode MODE_FIXED, or MODE_SCROLLABLE
     */
    protected abstract int getTabMode();

    /**
     * @param inflater           Inflater
     * @param container          Root container
     * @param savedInstanceState Saved instance state
     * @return This fragments top view
     */
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabbed, container, false);
    }

    /**
     * Setup the page change handler to update each tab when it is selected or deselected
     */
    private void setupPageChangeHandler() {
        tabs[viewPager.getCurrentItem()].viewVisible();
        updateFab();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabs[selectedTab].viewInvisible();
                selectedTab = position;
                tabs[viewPager.getCurrentItem()].viewVisible();
                updateFab();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Signal the tab that the fab was clicked
     */
    @Override
    protected final void fabClicked() {
        if (tabsValid()) {
            tabs[viewPager.getCurrentItem()].fabClicked();
        }
    }

    /**
     * Update the fab for the current tab
     */
    private void updateFab() {
        if (tabs[viewPager.getCurrentItem()].fabEnable()) {
            setFabIcon(tabs[viewPager.getCurrentItem()].fabIconRes());
            showFab();
        } else {
            hideFab();
        }
    }

    /**
     * Save tab state
     *
     * @param bundle bundle
     */
    @Override
    protected void saveState(@NonNull Bundle bundle) {
        if (tabs == null) {
            return;
        }
        for (int i = 0; i < tabs.length; i++) {
            Bundle bundleTab = new Bundle();
            tabs[i].saveState(bundleTab);
            bundle.putBundle("TAB_" + i, bundleTab);
        }
    }

    /**
     * Restore tab state
     *
     * @param bundle bundle
     */
    @Override
    protected void restoreState(@NonNull Bundle bundle) {
        if (tabs == null) {
            tabs = getTabs();
        }
        for (int i = 0; i < tabs.length; i++) {
            Bundle bundleTab = bundle.getBundle("TAB_" + i);
            if (bundleTab != null) {
                tabs[i].restoreState(bundleTab);
            }
        }
    }

    /**
     * Setup the view pager and tabs
     */
    @Override
    protected void afterViewCreated() {
        View view = getView();
        if (view == null) {
            return;
        }
        if (tabs == null) {
            tabs = getTabs();
        }
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        TabbedFragmentViewPager pager = new TabbedFragmentViewPager(getContext(), tabs);
        viewPager.setAdapter(pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setTabMode(getTabMode());
        tabLayout.setupWithViewPager(viewPager);
        if (tabsValid()) {
            setupPageChangeHandler();
            setTabPro();
        }
    }

    /**
     * Update apps with the pro icon if needed
     */
    private void setTabPro() {
        if (!tabsValid() || UtilsVersion.isPro()) {
            return;
        }
        for (int i = 0; i < tabs.length; i++) {
            if (tabs[i].requiresPro()) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (UtilsTheme.getToolbarTheme() == UtilsTheme.ToolbarTheme.DARK_TEXT) {
                    if (tab != null) {
                        tab.setIcon(R.drawable.ic_pro_black_24dp);
                    }
                } else {
                    if (tab != null) {
                        tab.setIcon(R.drawable.ic_pro_white_24dp);
                    }
                }
            }
        }
    }

    /**
     * @return True if the tabs are valid
     */
    private boolean tabsValid() {
        if (tabs == null || tabs.length < 1) {
            return false;
        }
        for (TabbedFragmentBaseTab tab : tabs) {
            if (tab == null) {
                return false;
            }
        }
        return true;
    }
}
