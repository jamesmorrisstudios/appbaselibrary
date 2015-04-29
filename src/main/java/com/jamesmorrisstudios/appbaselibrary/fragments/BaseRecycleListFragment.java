package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.listAdapters.BaseRecycleAdapter;
import com.jamesmorrisstudios.appbaselibrary.listAdapters.BaseRecycleContainer;
import com.jamesmorrisstudios.appbaselibrary.listAdapters.BaseRecycleItem;
import com.jamesmorrisstudios.materialuilibrary.controls.ButtonFloat;
import com.jamesmorrisstudios.materialuilibrary.recyclemanager.LayoutManager;
import com.jamesmorrisstudios.utilitieslibrary.Utils;

import java.util.ArrayList;

/**
 * Created by James on 4/29/2015.
 */
public abstract class BaseRecycleListFragment extends BaseFragment implements BaseRecycleAdapter.OnItemClickListener {
    private boolean isRefreshing = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BaseRecycleAdapter mAdapter = null;
    private TextView noDataText;
    private ButtonFloat fab;

    /**
     * Required empty public constructor
     */
    public BaseRecycleListFragment() {}

    /**
     * @param inflater Inflater
     * @param container Root container
     * @param savedInstanceState Saved instance state
     * @return This fragments top view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycle, container, false);
        fab = (ButtonFloat) view.findViewById(R.id.buttonAddNew);
        fab.setBackgroundColor(getResources().getColor(R.color.primaryColorAccent));
        noDataText = (TextView) view.findViewById(R.id.empty_view);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    fab.hide();
                } else if (dy < 0) {
                    fab.show();
                }
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primaryColorAccent);
        mSwipeRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Utils.removeGlobalLayoutListener(mSwipeRefreshLayout, this);
                        if (isRefreshing) {
                            mSwipeRefreshLayout.setRefreshing(true);
                        }
                    }
                });
        mSwipeRefreshLayout.setEnabled(false);
        isRefreshing = true;
        return view;
    }

    /**
     * View creation done
     * @param view This fragments main view
     * @param savedInstanceState Saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int mHeaderDisplay = getResources().getInteger(R.integer.default_header_display);
        boolean mAreMarginsFixed = getResources().getBoolean(R.bool.default_margins_fixed);
        ViewHolder mViews = new ViewHolder(view);
        mViews.initViews(new LayoutManager(getActivity()));
        mAdapter = getAdapter(mHeaderDisplay, this);
        mAdapter.setMarginsFixed(mAreMarginsFixed);
        mAdapter.setHeaderDisplay(mHeaderDisplay);
        mViews.setAdapter(mAdapter);
        startDataLoad();
    }

    protected abstract BaseRecycleAdapter getAdapter(int headerMode, @NonNull BaseRecycleAdapter.OnItemClickListener mListener);
    protected abstract void startDataLoad();
    protected abstract void itemClicked(BaseRecycleItem item);

    protected final void applyData(ArrayList<BaseRecycleContainer> data) {
        if(mAdapter != null && data != null && !data.isEmpty()) {
            mAdapter.setItems(data);
            hideNoDataText();
        } else {
            showNoDataText();
        }
        endRefresh();
    }

    /**
     * Finish up a refresh and hide the spinner
     */
    private void endRefresh () {
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                isRefreshing = false;
            }
        }, 500);
    }

    /**
     * Show the no data text display
     */
    private void showNoDataText() {
        noDataText.setVisibility(View.VISIBLE);
    }

    /**
     * Hide the no data text display
     */
    private void hideNoDataText() {
        noDataText.setVisibility(View.GONE);
    }

    /**
     * @param item Clicked reminder item
     */
    @Override
    public void itemClicked(@NonNull BaseRecycleContainer item) {
        itemClicked(item.getItem());
    }

    /**
     * View holder class
     */
    private static class ViewHolder {
        private final RecyclerView mRecyclerView;

        /**
         * Constructor
         * @param view View to set
         */
        public ViewHolder(@NonNull View view) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        }

        /**
         * @param lm Set with layout manager
         */
        public void initViews(@NonNull LayoutManager lm) {
            mRecyclerView.setLayoutManager(lm);
        }

        /**
         * Set the adapter
         * @param adapter Adapter
         */
        public void setAdapter(@NonNull RecyclerView.Adapter<?> adapter) {
            mRecyclerView.setAdapter(adapter);
        }
    }

}
