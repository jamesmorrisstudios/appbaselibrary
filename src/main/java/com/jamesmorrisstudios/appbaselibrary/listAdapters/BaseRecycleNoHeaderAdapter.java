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

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Reminder adapter class to manage the recyclerView
 */
public abstract class BaseRecycleNoHeaderAdapter extends RecyclerView.Adapter<BaseRecycleNoHeaderViewHolder> {
    public static final String TAG = "BaseRecycleAdapter";
    private final ArrayList<LineItem> mItems;
    private OnItemClickListener mListener;
    private int expandedPosition = -1;

    /**
     * Constructor
     *
     * @param mListener Item Click listener
     */
    public BaseRecycleNoHeaderAdapter(@NonNull OnItemClickListener mListener) {
        this.mListener = mListener;
        mItems = new ArrayList<>();
    }

    /**
     * Sets the items to this list
     * This will override anything previously set
     *
     * @param items List of items to set.
     */
    public final void setItems(@NonNull ArrayList<BaseRecycleNoHeaderContainer> items) {
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

    /**
     * Creates a view holder for one of the item views
     *
     * @param parent   Parent view
     * @param viewType Type of view
     * @return The reminder view holder
     */
    @Override
    public BaseRecycleNoHeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemResId(), parent, false);
        return getViewHolder(view, new BaseRecycleNoHeaderViewHolder.cardClickListener() {
            @Override
            public void cardClicked(int position) {
                mListener.itemClicked(mItems.get(position).reminder);
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

    protected abstract BaseRecycleNoHeaderViewHolder getViewHolder(@NonNull View view, @Nullable BaseRecycleNoHeaderViewHolder.cardClickListener mListener);

    @LayoutRes
    protected abstract int getItemResId();

    /**
     * Binds a view holder data set in the given position
     *
     * @param holder   Holder to bind
     * @param position Position
     */
    @Override
    public void onBindViewHolder(@NonNull BaseRecycleNoHeaderViewHolder holder, int position) {
        final LineItem item = mItems.get(position);
        holder.bindItem(item.reminder, position == expandedPosition);
    }

    /**
     * @return Number of items in view holder
     */
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * Reminder click interface
     */
    public interface OnItemClickListener {
        void itemClicked(BaseRecycleNoHeaderContainer item);
    }

    /**
     * Individual line item for each item in recyclerView. These are recycled.
     */
    private static class LineItem {
        public BaseRecycleNoHeaderContainer reminder;

        /**
         * Constructor
         *
         * @param reminder Reminder line item data
         */
        public LineItem(@NonNull BaseRecycleNoHeaderContainer reminder) {
            this.reminder = reminder;
        }
    }
}
