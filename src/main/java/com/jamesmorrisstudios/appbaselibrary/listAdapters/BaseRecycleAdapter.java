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
public abstract class BaseRecycleAdapter extends RecyclerView.Adapter<BaseRecycleViewHolder>
        implements ItemTouchHelperAdapter {
    public static final String TAG = "BaseRecycleAdapter";
    private static final int VIEW_TYPE_HEADER = 0x03;
    private static final int VIEW_TYPE_CONTENT = 0x00;
    private final ArrayList<LineItem> mItems;
    private OnItemClickListener mListener;
    private int expandedPosition = -1;
    private boolean hasDummyItem = false;

    /**
     * Constructor
     *
     * @param mListener Item Click listener
     */
    public BaseRecycleAdapter(@NonNull OnItemClickListener mListener) {
        this.mListener = mListener;
        mItems = new ArrayList<>();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        fromPosition = UtilsMath.inBoundsInt(0, mItems.size() - 1, fromPosition);
        toPosition = UtilsMath.inBoundsInt(0, mItems.size() - 1, toPosition);

        LineItem prev = mItems.remove(fromPosition);
        mItems.add(toPosition, prev);
        mListener.itemMoved(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Sets the items to this list
     * This will override anything previously set
     *
     * @param items List of items to set.
     */
    public final void setItems(@NonNull ArrayList<BaseRecycleContainer> items) {
        ArrayList<LineItem> mItemsTemp = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            mItemsTemp.add(new LineItem(items.get(i)));
        }
        while (!mItems.isEmpty()) {
            mItems.remove(0);
        }
        mItems.addAll(mItemsTemp);
        notifyDataSetChanged();
    }

    public final void addItems(@NonNull ArrayList<BaseRecycleContainer> items) {
        ArrayList<LineItem> mItemsTemp = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            mItemsTemp.add(new LineItem(items.get(i)));
        }
        int indexStart = mItems.size();
        mItems.addAll(mItemsTemp);
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
    public BaseRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        boolean isHeader = false;
        View view;
        if(viewType == VIEW_TYPE_CONTENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(getItemResId(), parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(getHeaderResId(), parent, false);
            isHeader = true;
        }

        return getViewHolder(view, isHeader, new BaseRecycleViewHolder.cardClickListener() {
            @Override
            public void cardClicked(int position) {
                mListener.itemClicked(position);
                if(position >= 0 && position < mItems.size()) {
                    mListener.itemClicked(mItems.get(position).data);
                }
            }

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

    protected abstract BaseRecycleViewHolder getViewHolder(@NonNull View view, boolean isHeader, @Nullable BaseRecycleViewHolder.cardClickListener mListener);

    @LayoutRes
    protected abstract int getItemResId();

    @LayoutRes
    protected abstract int getHeaderResId();

    public final ArrayList<LineItem> getItems() {
        return mItems;
    }

    /**
     * Binds a view holder data set in the given position
     *
     * @param holder   Holder to bind
     * @param position Position
     */
    @Override
    public void onBindViewHolder(@NonNull BaseRecycleViewHolder holder, int position) {
        final LineItem item = mItems.get(position);
        holder.bindItem(item.data, position == expandedPosition);

        if(item.data.isHeader) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams)holder.itemView.getLayoutParams();
            if(params != null) {
                params.setFullSpan(true);
            }
        }
    }

    /**
     * @return Number of items in view holder
     */
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mItems.get(position).data.isHeader) {
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_CONTENT;
    }

    /**
     * Reminder click interface
     */
    public interface OnItemClickListener {
        void itemClicked(BaseRecycleContainer item);
        void itemClicked(int position);
        void itemMoved(int fromPosition, int toPosition);
    }

}
