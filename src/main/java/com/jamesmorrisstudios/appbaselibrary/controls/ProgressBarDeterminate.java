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

package com.jamesmorrisstudios.appbaselibrary.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public abstract class ProgressBarDeterminate extends View {
    int max = 100;
    int min = 0;
    int progress = 0;

    Paint paint = new Paint();

    int[] colors = new int[]{Color.parseColor("#1E88E5")};

    int pendindProgress = -1;

    public ProgressBarDeterminate(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgress(int progress) {
        pendindProgress = progress;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pendindProgress != -1) {
            setProgress(pendindProgress, canvas);
        }
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getProgress() {
        return progress;
    }

    public abstract void setProgress(int progress, Canvas canvas);

    // Set color of background
    public void setColors(int[] colors) {
        this.colors = colors;
    }

}
