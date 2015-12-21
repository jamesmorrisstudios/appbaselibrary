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

package com.jamesmorrisstudios.appbaselibrary.controls.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.jamesmorrisstudios.appbaselibrary.math.UtilsMath;

/**
 * Abstract implemented of determinate progress bar
 */
public abstract class ProgressBarDeterminate extends View {
    protected int max = 100;
    protected int min = 0;
    protected int progress = 0;
    protected Paint paint = new Paint();
    protected int[] colors = new int[]{Color.parseColor("#1E88E5")};
    protected int pendindProgress = -1;

    /**
     * Constructor
     *
     * @param context Context
     * @param attrs   Attributes
     */
    public ProgressBarDeterminate(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Draw the progress bar when this is called
     *
     * @param progress Current progress amount
     * @param canvas   Canvas to draw on
     */
    public abstract void setProgress(int progress, Canvas canvas);

    /**
     * @param canvas Canvas to draw on
     */
    @Override
    protected final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pendindProgress != -1) {
            setProgress(pendindProgress, canvas);
        }
    }

    /**
     * @param max The max value
     */
    public final void setMax(int max) {
        this.max = max;
    }

    /**
     * @param min The min value
     */
    public final void setMin(int min) {
        this.min = min;
    }

    /**
     * @return The current progress state
     */
    public final int getProgress() {
        return progress;
    }

    /**
     * This value will be forced into the min-max range. Set those first.
     *
     * @param progress Set current progress level.
     */
    public final void setProgress(int progress) {
        pendindProgress = UtilsMath.inBoundsInt(min, max, progress);
        this.invalidate();
    }

    /**
     * Colors will be evenly split across the entire length of the progress bar. Use one color for a solid color
     *
     * @param colors The colors of the background
     */
    public final void setColors(int[] colors) {
        this.colors = colors;
    }

}
