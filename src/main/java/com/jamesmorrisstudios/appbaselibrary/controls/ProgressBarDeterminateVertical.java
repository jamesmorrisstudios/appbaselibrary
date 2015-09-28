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
import android.graphics.Rect;
import android.util.AttributeSet;

public class ProgressBarDeterminateVertical extends ProgressBarDeterminate {

    public ProgressBarDeterminateVertical(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // SETTERS

    public void setProgress(int progress, Canvas canvas) {
        if (getHeight() == 0) {
            pendindProgress = progress;
        } else {
            this.progress = progress;
            if (progress > max)
                progress = max;
            if (progress < min)
                progress = min;
            int totalHeight = max - min;
            double progressPercent = (double) progress / (double) totalHeight;
            int progressHeight = (int) (getHeight() * progressPercent);

            Rect rect;

            int heightSplit = Math.round(canvas.getHeight() * 1.0f / colors.length);
            int minHeight = canvas.getHeight()-progressHeight;

            for(int i=0; i<colors.length; i++) {
                paint.setColor(colors[i]);
                int heightStart = Math.max(canvas.getHeight() - heightSplit * (i+1), minHeight);
                int heightEnd = Math.max(canvas.getHeight() - heightSplit * i, minHeight);
                rect = new Rect(0, heightStart, canvas.getWidth(), heightEnd);
                canvas.drawRect(rect, paint);
            }
            pendindProgress = -1;
        }
    }

}
