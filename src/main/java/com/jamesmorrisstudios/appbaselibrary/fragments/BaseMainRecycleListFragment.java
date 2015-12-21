package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.activities.BaseActivity;

/**
 * Base implementation of the main fragment that houses a recycler view as the entire view. Extend from this for the launch fragment
 * <p/>
 * <p/>
 * Created by James on 4/29/2015.
 */
public abstract class BaseMainRecycleListFragment extends BaseRecycleListFragment {

    /**
     * @return True if we have a search view for the recycleList
     */
    protected abstract boolean includeSearch();

    /**
     * Override if manually using the search text. Recycle list is automatically updated
     */
    protected void searchTextChanged(@Nullable String text) {

    }

    /**
     * Override if manually using the search text. Recycle list is automatically updated
     */
    protected void searchTextSubmitted(@Nullable String text) {

    }

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
        if (includeSearch()) {
            inflater.inflate(R.menu.menu_main_recycle, menu);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    setFilterText(query);
                    searchTextSubmitted(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    setFilterText(newText);
                    searchTextChanged(newText);
                    return false;
                }
            });
        } else {
            inflater.inflate(R.menu.menu_main, menu);
        }
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
