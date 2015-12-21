package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.activities.BaseActivity;

/**
 * Base implementation of the main fragment. Extend from this for the launch fragment
 * <p/>
 * Created by James on 4/29/2015.
 */
public abstract class BaseMainFragment extends BaseFragment {
    public static final String TAG = "BaseMainFragment";

    /**
     * @param savedInstanceState Saved instance state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * @param menu     Menu
     * @param inflater Inflate
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * @param item Selected item
     * @return True if action consumed
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            BaseActivity.AppBaseEvent.HELP_CLICKED.post();
        } else if (item.getItemId() == R.id.action_settings) {
            BaseActivity.AppBaseEvent.SETTINGS_CLICKED.post();
        }
        return super.onOptionsItemSelected(item);
    }

}
