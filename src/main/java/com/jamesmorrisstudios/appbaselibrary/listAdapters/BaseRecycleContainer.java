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

/**
 * Container for items that abstracts it for use in the recyclerView
 * Houses either a header or an item
 * <p/>
 * Created by James on 3/31/2015.
 */
public abstract class BaseRecycleContainer {
    public final boolean isHeader;

    /**
     * Constructor defining if header or not
     */
    public BaseRecycleContainer(boolean isHeader) {
        this.isHeader = isHeader;
    }

    /**
     * Cannot return null
     *
     * @return The header object
     */
    @NonNull
    public abstract BaseRecycleItem getHeader();

    /**
     * Cannot return null
     *
     * @return The row item object
     */
    @NonNull
    public abstract BaseRecycleItem getItem();

    /**
     * @return Filter Text
     */
    @NonNull
    public abstract String getFilterText();

}
