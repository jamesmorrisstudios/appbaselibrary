/*
 * Copyright (c) 2015.  James Morris Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jamesmorrisstudios.appbaselibrary.listAdapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamesmorrisstudios.appbaselibrary.math.UtilsMath;
import com.jamesmorrisstudios.appbaselibrary.touchHelper.ItemTouchHelperAdapter;

import java.util.ArrayList;

/**
 * Reminder adapter class to manage the recyclerView
 */
public abstract class BaseRecycleAdapter extends RecyclerView.Adapter<BaseRecycleViewHolder> implements ItemTouchHelperAdapter {
    private static final int VIEW_TYPE_HEADER = 0x03;
    private static final int VIEW_TYPE_CONTENT = 0x00;
    private final ArrayList<LineItem> allItems = new ArrayList<>();
    private final ArrayList<LineItem> visibleItems = new ArrayList<>();
    private OnRecycleAdapterEventsListener mListener;
    private int expandedPosition = -1;

    /**
     * Constructor
     *
     * @param mListener Item Click listener
     */
    public BaseRecycleAdapter(@NonNull final OnRecycleAdapterEventsListener mListener) {
        this.mListener = mListener;
    }

    /**
     * item move can only happen when visibleItems == allItems
     *
     * @param fromPosition The start position of the moved item.
     * @param toPosition   Then end position of the moved item.
     */
    @Override
    public final void onItemMove(final int fromPosition, final int toPosition) {
        final int fromPos = UtilsMath.inBoundsInt(0, visibleItems.size() - 1, fromPosition);
        final int toPos = UtilsMath.inBoundsInt(0, visibleItems.size() - 1, toPosition);
        LineItem prev = visibleItems.remove(fromPos);
        LineItem prev2 = allItems.remove(fromPos);
        visibleItems.add(toPos, prev);
        allItems.add(toPos, prev2);
        mListener.itemMoved(fromPos, toPos);
        notifyItemMoved(fromPos, toPos);
    }

    /**
     * item dismiss can only happen when visibleItems == allItems
     *
     * @param position The position of the item dismissed.
     */
    @Override
    public final void onItemDismiss(final int position) {
        visibleItems.remove(position);
        allItems.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * @return True if the adapter currently allows moving of items
     */
    @Override
    public final boolean allowMove() {
        return mListener.getFilterText() == null || mListener.getFilterText().isEmpty();
    }

    /**
     * @param items Append items to the current list
     */
    public final void addItems(@NonNull final ArrayList<BaseRecycleContainer> items) {
        ArrayList<LineItem> mItemsTemp = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            mItemsTemp.add(new LineItem(items.get(i)));
        }
        int indexStart = allItems.size();
        allItems.addAll(mItemsTemp);
        updateVisibleItems();
        notifyItemRangeInserted(indexStart, mItemsTemp.size());
    }

    /**
     * Creates a view holder for one of the item views
     *
     * @param parent   Parent view
     * @param viewType Type of view
     * @return The container view holder
     */
    @Override
    public final BaseRecycleViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        boolean isHeader = false;
        View view;
        if (viewType == VIEW_TYPE_CONTENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(getItemResId(), parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(getHeaderResId(), parent, false);
            isHeader = true;
        }
        return getViewHolder(view, isHeader, new BaseRecycleViewHolder.cardClickListener() {
            /**
             * @param position Clicked card position
             */
            @Override
            public void cardClicked(int position) {
                mListener.itemClicked(position);
                if (position >= 0 && position < visibleItems.size()) {
                    mListener.itemClicked(visibleItems.get(position).data);
                }
            }

            /**
             * @param position Card that should toggle expanded view
             */
            @Override
            public void toggleExpanded(int position) {
                if (expandedPosition == position) { //This item was expanded
                    expandedPosition = -1;
                    notifyItemChanged(position);
                } else if (expandedPosition >= 0) { //A Different item was expanded
                    int prev = expandedPosition;
                    expandedPosition = position;
                    notifyItemChanged(prev);
                    notifyItemChanged(expandedPosition);
                } else { //No items were expanded
                    expandedPosition = position;
                    notifyItemChanged(expandedPosition);
                }
            }
        });
    }

    /**
     * Retrieve the custom view holder
     *
     * @param view      View
     * @param isHeader  if a header view
     * @param mListener Item listener
     * @return Class extending BaseRecycleViewHolder
     */
    @NonNull
    protected abstract BaseRecycleViewHolder getViewHolder(@NonNull final View view, final boolean isHeader, @NonNull final BaseRecycleViewHolder.cardClickListener mListener);

    /**
     * Call to update the adapters filtering text
     */
    public final void updateFilterText() {
        updateVisibleItems();
        notifyDataSetChanged();
    }

    /**
     * @return The item layout id
     */
    @LayoutRes
    protected abstract int getItemResId();

    /**
     * @return The header layout id
     */
    @LayoutRes
    protected abstract int getHeaderResId();

    /**
     * @return List of all items
     */
    @NonNull
    public final ArrayList<LineItem> getItems() {
        return allItems;
    }

    /**
     * Sets the items to this list
     * This will override anything previously set
     *
     * @param items List of items to set.
     */
    public final void setItems(@NonNull final ArrayList<BaseRecycleContainer> items) {
        ArrayList<LineItem> mItemsTemp = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            mItemsTemp.add(new LineItem(items.get(i)));
        }
        while (!allItems.isEmpty()) {
            allItems.remove(0);
        }
        allItems.addAll(mItemsTemp);
        updateVisibleItems();
        notifyDataSetChanged();
    }

    /**
     * Updates what items are currently visible based on the users filter text
     */
    private void updateVisibleItems() {
        visibleItems.clear();
        if (mListener.getFilterText() == null || mListener.getFilterText().isEmpty()) {
            visibleItems.addAll(allItems);
        } else {
            for (LineItem item : allItems) {
                if (item.data.getFilterText().toLowerCase().contains(mListener.getFilterText().toLowerCase())) {
                    visibleItems.add(item);
                }
            }
        }
    }

    /**
     * Binds a view holder data set in the given position
     *
     * @param holder   Holder to bind
     * @param position Position
     */
    @Override
    public final void onBindViewHolder(@NonNull final BaseRecycleViewHolder holder, final int position) {
        final LineItem item = visibleItems.get(position);
        holder.bindItem(item.data, position == expandedPosition);
        if (item.data.isHeader) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            if (params != null) {
                params.setFullSpan(true);
            }
        }
    }

    /**
     * @return Number of items in view holder
     */
    @Override
    public final int getItemCount() {
        return visibleItems.size();
    }

    /**
     * @param position Position of item
     * @return View type (header or item)
     */
    @Override
    public final int getItemViewType(final int position) {
        if (visibleItems.get(position).data.isHeader) {
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_CONTENT;
    }

    /**
     * Reminder click interface
     */
    public interface OnRecycleAdapterEventsListener {

        /**
         * @return Filter Text. Null for none
         */
        @Nullable
        String getFilterText();

        /**
         * @param item Clicked item
         */
        void itemClicked(@NonNull final BaseRecycleContainer item);

        /**
         * @param position Clicked item position
         */
        void itemClicked(final int position);

        /**
         * Item was moved in list. Update the source data
         * <p/>
         * Remove item fromPosition
         * Add item toPosition
         *
         * @param fromPosition From position
         * @param toPosition   To position
         */
        void itemMoved(final int fromPosition, final int toPosition);
    }

}
