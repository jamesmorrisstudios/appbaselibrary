/*
 * Copyright (c) 2015.   James Morris Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jamesmorrisstudios.appbaselibrary.layouts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * This class will set its width to match whatever its height is.
 * <p/>
 * Created by James on 10/14/2014.
 */
public final class SquareHeightFrameLayout extends FrameLayout {

    /**
     * @param context Local context
     */
    public SquareHeightFrameLayout(@NonNull final Context context) {
        super(context);
    }

    /**
     * @param context Local context
     * @param attrs   Attribute set
     */
    public SquareHeightFrameLayout(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context  Local context
     * @param attrs    Attribute set
     * @param defStyle Style def
     */
    public SquareHeightFrameLayout(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param widthMeasureSpec  The width spec of the view
     * @param heightMeasureSpec The height spec of the view
     */
    @Override
    public void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

}
