package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.UtilsDisplay;
import com.jamesmorrisstudios.appbaselibrary.UtilsTheme;
import com.jamesmorrisstudios.appbaselibrary.listAdapters.BaseRecycleAdapter;
import com.jamesmorrisstudios.appbaselibrary.listAdapters.BaseRecycleContainer;
import com.jamesmorrisstudios.appbaselibrary.touchHelper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;

/**
 * Base implementation of the Recycler view control
 * <p/>
 * Created by James on 4/29/2015.
 */
public abstract class BaseRecycleListFragment extends BaseFragment implements BaseRecycleAdapter.OnRecycleAdapterEventsListener {
    protected BaseRecycleAdapter mAdapter = null;
    private boolean isRefreshing = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView noDataText;
    private RecyclerView mRecyclerView;
    private String filterText = null;

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
        if (supportsHeaders()) {
            StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(getNumberColumns(), StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(llm);
        } else {
            GridLayoutManager llm = new GridLayoutManager(getContext(), getNumberColumns());
            mRecyclerView.setLayoutManager(llm);
        }
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(UtilsTheme.getBackgroundColor());
        mSwipeRefreshLayout.setColorSchemeColors(UtilsTheme.decodeAttrColor(R.attr.colorAccent));
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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    onScrolledToEnd();
                } else if (dy < 0) {
                    onScrolledUp();
                } else if (dy > 0) {
                    onScrolledDown();
                }
            }
        });
        mSwipeRefreshLayout.setEnabled(false);
        isRefreshing = true;
        return view;
    }

    /**
     * Unused
     */
    private void onScrolledUp() {

    }

    /**
     * Unused
     */
    private void onScrolledDown() {

    }

    /**
     * Load more data as we reached the end of the current set
     */
    private void onScrolledToEnd() {
        startMoreDataLoad();
    }

    /**
     * Defaults to getNumberColumnsNarrow
     *
     * @return Number of columns to execute
     */
    protected int getNumberColumns() {
        return getNumberColumnsNarrow();
    }

    /**
     * @return Number of columns to use with wide data
     */
    protected final int getNumberColumnsWide() {
        switch (UtilsDisplay.getOrientation()) {
            case PORTRAIT:
                switch (UtilsDisplay.getScreenSize()) {
                    case SMALL:
                        return 1;
                    case NORMAL:
                        return 1;
                    case LARGE:
                        return 1;
                    case XLARGE:
                        return 2;
                    case UNDEFINED:
                        return 1;
                    default:
                        return 1;
                }
            case LANDSCAPE:
                switch (UtilsDisplay.getScreenSize()) {
                    case SMALL:
                        return 1;
                    case NORMAL:
                        return 1;
                    case LARGE:
                        return 2;
                    case XLARGE:
                        return 2;
                    case UNDEFINED:
                        return 2;
                    default:
                        return 2;
                }
        }
        return 1;
    }

    /**
     * @return Number of columns to use with narrow data
     */
    protected final int getNumberColumnsNarrow() {
        switch (UtilsDisplay.getOrientation()) {
            case PORTRAIT:
                switch (UtilsDisplay.getScreenSize()) {
                    case SMALL:
                        return 1;
                    case NORMAL:
                        return 1;
                    case LARGE:
                        return 1;
                    case XLARGE:
                        return 2;
                    case UNDEFINED:
                        return 1;
                    default:
                        return 1;
                }
            case LANDSCAPE:
                switch (UtilsDisplay.getScreenSize()) {
                    case SMALL:
                        return 1;
                    case NORMAL:
                        return 2;
                    case LARGE:
                        return 2;
                    case XLARGE:
                        return 2;
                    case UNDEFINED:
                        return 2;
                    default:
                        return 2;
                }
        }
        return 1;
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
        ViewHolder mViews = new ViewHolder(view);
        mAdapter = getAdapter(this);
        mViews.setAdapter(getAdapterToSet());
        filterText = null;
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

        if (allowReording()) {
            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
            ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        }
    }

    /**
     * @param mListener listener
     * @return List Adapter
     */
    @NonNull
    protected abstract BaseRecycleAdapter getAdapter(@NonNull BaseRecycleAdapter.OnRecycleAdapterEventsListener mListener);

    /**
     * @return List Adapter
     */
    @NonNull
    protected RecyclerView.Adapter getAdapterToSet() {
        return mAdapter;
    }

    /**
     * Load data
     *
     * @param forcedRefresh true to force
     */
    protected abstract void startDataLoad(boolean forcedRefresh);

    /**
     * Load more data if available
     */
    protected abstract void startMoreDataLoad();

    /**
     * @param item Item that was clicked on
     */
    protected abstract void itemClick(@NonNull BaseRecycleContainer item);

    /**
     * Item was moved. Adjust your data as needed
     *
     * @param fromPosition Item from position
     * @param toPosition   Item to position
     */
    protected abstract void itemMove(int fromPosition, int toPosition);

    /**
     * Enable header support
     *
     * @return True if headers are allowed
     */
    protected abstract boolean supportsHeaders();

    /**
     * Allow the user to rearrange the items
     *
     * @return True if reording is allowed
     */
    protected abstract boolean allowReording();

    /**
     * Clear and set data to the data set
     *
     * @param data Data to set
     */
    protected final void applyData(@NonNull ArrayList<BaseRecycleContainer> data) {
        if (mAdapter != null && !data.isEmpty()) {
            mAdapter.setItems(data);
            hideNoDataText();
        } else {
            mAdapter.setItems(new ArrayList<BaseRecycleContainer>());
            showNoDataText();
        }
        endRefresh();
    }

    /**
     * Add data onto the end of the current data set
     *
     * @param data Data to append
     */
    protected final void appendData(@NonNull ArrayList<BaseRecycleContainer> data) {
        if (mAdapter != null && !data.isEmpty()) {
            mAdapter.addItems(data);
            hideNoDataText();
        }
        endRefresh();
    }

    /**
     * Start a data refresh
     *
     * @param forceReload True to force even if data is already loaded
     */
    protected final void startRefresh(boolean forceReload) {
        mSwipeRefreshLayout.setRefreshing(true);
        isRefreshing = true;
        startDataLoad(forceReload);
    }

    /**
     * Enable dummy space at the bottom of the list. This allows enough space for the FAB to be below the bottom item
     *
     * @param dummyItem True for dummy space. False for none
     */
    protected final void setDummyItem(boolean dummyItem) {
        if (dummyItem) {
            mRecyclerView.setPadding(0, 0, 0, UtilsDisplay.getDipInt(92));
        } else {
            mRecyclerView.setPadding(0, 0, 0, 0);
        }
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

    /**
     * Disabled by default
     *
     * @param enable Enable or disable pull to refresh
     */
    protected final void setEnablePullToRefresh(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
        if (enable) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    startDataLoad(true);
                }
            });
        } else {
            mSwipeRefreshLayout.setOnRefreshListener(null);
        }
    }

    /**
     * Text to display if no data in view
     *
     * @param text Text
     */
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
     * @return The current filter text
     */
    @Override
    public String getFilterText() {
        return filterText;
    }

    /**
     * @param filterText Filter Text. Null or empty for no filter
     */
    public final void setFilterText(@Nullable String filterText) {
        this.filterText = filterText;
        this.mAdapter.updateFilterText();
    }

    /**
     * @param item Clicked container item
     */
    @Override
    public final void itemClicked(@NonNull BaseRecycleContainer item) {
        itemClick(item);
    }

    /**
     * Item clicked
     *
     * @param position Clicked item position
     */
    @Override
    public final void itemClicked(int position) {
        //Unused
    }

    /**
     * @param fromPosition From position
     * @param toPosition   To position
     */
    @Override
    public final void itemMoved(int fromPosition, int toPosition) {
        itemMove(fromPosition, toPosition);
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
         * Set the adapter
         *
         * @param adapter Adapter
         */
        public void setAdapter(@NonNull RecyclerView.Adapter<?> adapter) {
            mRecyclerView.setAdapter(adapter);
        }
    }

}
