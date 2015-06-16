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

/**
 * Reminder view holder for use in RecyclerView
 */
public abstract class BaseRecycleNoHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    //Click listener
    private cardClickListener mListener;

    /**
     * Constructor
     *
     * @param view      Parent view
     * @param mListener Click listener. Null if none desired
     */
    public BaseRecycleNoHeaderViewHolder(@NonNull View view, @Nullable cardClickListener mListener) {
        super(view);
        this.mListener = mListener;
        initItem(view);
    }

    protected abstract void initItem(View view);

    protected abstract void bindItem(BaseRecycleItem item, boolean expanded);

    public final void toggleExpanded() {
        mListener.toggleExpanded(getLayoutPosition());
    }

    /**
     * Binds the given data to this view.
     *
     * @param data Data to bind
     */
    public void bindItem(@NonNull final BaseRecycleNoHeaderContainer data, boolean expanded) {
        bindItem(data.getItem(), expanded);
    }

    /**
     * @param v The view that was clicked
     */
    @Override
    public void onClick(@NonNull View v) {
        if (mListener != null) {
            mListener.cardClicked(getLayoutPosition());
        }
    }

    /**
     * Card click listener interface
     */
    public interface cardClickListener {
        void cardClicked(int position);

        void toggleExpanded(int position);
    }

}
