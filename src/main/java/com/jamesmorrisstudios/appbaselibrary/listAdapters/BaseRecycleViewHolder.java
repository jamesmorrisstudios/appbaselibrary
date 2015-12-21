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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jamesmorrisstudios.appbaselibrary.touchHelper.ItemTouchHelperViewHolder;

/**
 * Reminder view holder for use in RecyclerView
 */
public abstract class BaseRecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {
    private cardClickListener mListener;
    private boolean isHeader;

    /**
     * Constructor
     *
     * @param view      Parent view
     * @param mListener Click listener. Null if none desired
     */
    public BaseRecycleViewHolder(@NonNull View view, boolean isHeader, @Nullable cardClickListener mListener) {
        super(view);
        this.isHeader = isHeader;
        this.mListener = mListener;
        if (isHeader) {
            initHeader(view);
        } else {
            initItem(view);
        }
    }

    /**
     * TODO possibly add some kinda of item highlight when dragging to rearrange
     */
    @Override
    public final void onItemSelected() {

    }

    /**
     * TODO possibly add some kinda of item highlight when dragging to rearrange
     */
    @Override
    public final void onItemClear() {

    }

    /**
     * Init the header view
     *
     * @param view Header view
     */
    protected abstract void initHeader(@NonNull View view);

    /**
     * Init the item view
     *
     * @param view Item view
     */
    protected abstract void initItem(@NonNull View view);

    /**
     * Bind the header to the data source
     *
     * @param headerItem Header data source
     * @param expanded   True if expanded view
     */
    protected abstract void bindHeader(@NonNull BaseRecycleItem headerItem, boolean expanded);

    /**
     * Bind the item to the data source
     *
     * @param item     Item data source
     * @param expanded True if expanded view
     */
    protected abstract void bindItem(@NonNull BaseRecycleItem item, boolean expanded);

    /**
     * Toggle expanded vs compact view
     */
    public final void toggleExpanded() {
        mListener.toggleExpanded(getLayoutPosition());
    }

    /**
     * Binds the given data to this view.
     *
     * @param data Data to bind
     */
    public final void bindItem(@NonNull final BaseRecycleContainer data, boolean expanded) {
        if (isHeader) {
            bindHeader(data.getHeader(), expanded);
        } else {
            bindItem(data.getItem(), expanded);
        }
    }

    /**
     * @param view The view that was clicked
     */
    @Override
    public final void onClick(@NonNull View view) {
        if (mListener != null) {
            mListener.cardClicked(getLayoutPosition());
        }
    }

    /**
     * Card click listener interface
     */
    public interface cardClickListener {

        /**
         * @param position Clicked card position
         */
        void cardClicked(int position);

        /**
         * @param position Card that should toggle expanded view
         */
        void toggleExpanded(int position);
    }

}
