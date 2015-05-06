package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jamesmorrisstudios.appbaselibrary.R;

/**
 * Created by James on 4/29/2015.
 */
public abstract class BaseMainRecycleListFragment extends BaseRecycleListFragment {

    protected OnMenuItemClickedListener menuItemListener;

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
            menuItemListener.onHelpClicked();
        } else if (item.getItemId() == R.id.action_settings) {
            menuItemListener.onSettingsClicked();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Attach to the activity
     *
     * @param activity Activity to attach to
     */
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            menuItemListener = (OnMenuItemClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMenuItemClickedListener");
        }
    }

    /**
     * Detach from activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        menuItemListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnMenuItemClickedListener {

        /**
         * Add new clicker
         */
        void onSettingsClicked();

        /**
         * Help clicked
         */
        void onHelpClicked();
    }

}
