package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.UtilsVersion;
import com.jamesmorrisstudios.appbaselibrary.fragmentHelpers.TabbedFragmentBaseTab;
import com.jamesmorrisstudios.appbaselibrary.fragmentHelpers.TabbedFragmentViewPager;

/**
 * Tabbed fragment implementation. Extend this and use TabbedFragmentBaseTab for the tabs
 * <p/>
 * Created by James on 12/8/2015.
 */
public abstract class TabbedFragment extends BaseFragment {
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
     * @return Tab Mode TabLayout.MODE_FIXED, or TabLayout.MODE_SCROLLABLE
     */
    protected abstract int getTabMode();

    /**
     * @param inflater           Inflater
     * @param container          Root container
     * @param savedInstanceState Saved instance state
     * @return This fragments top view
     */
    @Override
    @NonNull
    public final View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabbed, container, false);
    }

    /**
     * Setup the page change handler to update each tab when it is selected or deselected
     */
    private void setupPageChangeHandler() {
        tabs[viewPager.getCurrentItem()].viewVisible();
        updateFab();
        updateMenu();
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
                updateMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        if (tabsValid()) {
            tabs[viewPager.getCurrentItem()].onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    @MenuRes
    protected int getOptionsMenuRes() {
        if (!tabsValid() || !tabs[viewPager.getCurrentItem()].menuEnable()) {
            return R.menu.base_menu_blank;
        }
        return tabs[viewPager.getCurrentItem()].menuRes();
    }

    @Override
    protected boolean usesOptionsMenu() {
        return true;
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
        if (!tabsValid()) {
            return;
        }
        if (tabs[viewPager.getCurrentItem()].fabEnable()) {
            setFabIcon(tabs[viewPager.getCurrentItem()].fabIconRes());
            showFab();
        } else {
            hideFab();
        }
    }

    /**
     * Update the fab for the current tab
     */
    private void updateMenu() {
        if (!tabsValid()) {
            return;
        }
        updateOptionsMenu();
    }

    /**
     * Save tab state
     *
     * @param bundle bundle
     */
    @Override
    protected void saveState(@NonNull final Bundle bundle) {
        if (!tabsValid()) {
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
    protected void restoreState(@NonNull final Bundle bundle) {
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
        //noinspection ResourceType
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
                if (tab != null) {
                    tab.setIcon(UtilsVersion.getProIconToolbar());
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
