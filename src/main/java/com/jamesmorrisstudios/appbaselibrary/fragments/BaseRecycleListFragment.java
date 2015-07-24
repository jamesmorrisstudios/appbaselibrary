package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.listAdapters.BaseRecycleAdapter;
import com.jamesmorrisstudios.appbaselibrary.listAdapters.BaseRecycleContainer;
import com.jamesmorrisstudios.appbaselibrary.listAdapters.BaseRecycleDummyItem;
import com.jamesmorrisstudios.utilitieslibrary.Utils;
import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayList;

/**
 * Created by James on 4/29/2015.
 */
public abstract class BaseRecycleListFragment extends BaseFragment implements BaseRecycleAdapter.OnItemClickListener {
    private boolean isRefreshing = false, dummyItem = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BaseRecycleAdapter mAdapter = null;
    private TextView noDataText;
    private RecyclerView mRecyclerView;

    /**
     * Required empty public constructor
     */
    public BaseRecycleListFragment() {
    }

    /**
     * @param inflater           Inflater
     * @param container          Root container
     * @param savedInstanceState Saved instance state
     * @return This fragments top view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycle, container, false);
        noDataText = (TextView) view.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent);
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
     *
     * @param view               This fragments main view
     * @param savedInstanceState Saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int mHeaderDisplay = LayoutManager.LayoutParams.HEADER_INLINE;  //getResources().getInteger(R.integer.default_header_display); //LayoutManager.LayoutParams.HEADER_INLINE;
        boolean mAreMarginsFixed = false;
        ViewHolder mViews = new ViewHolder(view);
        mViews.initViews(new LayoutManager(getActivity()));
        mAdapter = getAdapter(mHeaderDisplay, this);
        mAdapter.setMarginsFixed(mAreMarginsFixed);
        mAdapter.setHeaderDisplay(mHeaderDisplay);
        mViews.setAdapter(mAdapter);
        startDataLoad(false);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    hideFabAuto();
                } else if (dy < 0) {
                    showFabAuto();
                }
            }
        });
    }

    protected abstract BaseRecycleAdapter getAdapter(int headerMode, @NonNull BaseRecycleAdapter.OnItemClickListener mListener);

    protected abstract void startDataLoad(boolean forcedRefresh);

    protected abstract void itemClick(@NonNull BaseRecycleContainer item);

    protected final void applyData(ArrayList<BaseRecycleContainer> data) {
        if (mAdapter != null && data != null && !data.isEmpty()) {
            if(dummyItem) {
                data.add(new BaseRecycleDummyItem());
            }
            mAdapter.setItems(data);
            hideNoDataText();
        } else {
            mAdapter.setItems(new ArrayList<BaseRecycleContainer>());
            showNoDataText();
        }
        endRefresh();
    }

    protected final void startRefresh(boolean forceReload) {
        hideNoDataText();
        mSwipeRefreshLayout.setRefreshing(true);
        isRefreshing = true;
        startDataLoad(forceReload);
    }

    protected final void setDummyItem(boolean dummyItem) {
        this.dummyItem = dummyItem;
    }

    /**
     * Finish up a refresh and hide the spinner
     */
    private void endRefresh() {
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                isRefreshing = false;
            }
        }, 500);
    }

    protected final void setEnablePullToRefresh(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
        if (enable) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    hideNoDataText();
                    startDataLoad(true);
                }
            });
        } else {
            mSwipeRefreshLayout.setOnRefreshListener(null);
        }
    }

    protected final void setNoDataText(@NonNull String text) {
        noDataText.setText(text);
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
        Log.v("BaseRecycleListFragment", "Item Clicked");
        itemClick(item);
    }

    /**
     * View holder class
     */
    private static class ViewHolder {
        private final RecyclerView mRecyclerView;

        /**
         * Constructor
         *
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
         *
         * @param adapter Adapter
         */
        public void setAdapter(@NonNull RecyclerView.Adapter<?> adapter) {
            mRecyclerView.setAdapter(adapter);
        }
    }

}
