package com.jamesmorrisstudios.appbaselibrary.controls.progress;

/*
 * Copyright (C) 2015 James Morris Studios <james.morris.studios@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.jamesmorrisstudios.appbaselibrary.UtilsTheme;


/**
 * Determinate progress that is circular.
 * <p/>
 * derivative of ProgressBarDeterminate from https://github.com/navasmdc/MaterialDesignLibrary
 * Created by James on 3/31/2015.
 */
public final class CircleProgressDeterminate extends View {
    private long progress = 0;
    private long max = 100;
    private Paint paint = new Paint();
    private RectF rect, rectInner;

    /**
     * Constructor
     *
     * @param context Context
     * @param attrs   Attributes
     */
    public CircleProgressDeterminate(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
    }

    /**
     * @param canvas Canvas to draw on
     */
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        float indent = getWidth() * 0.02f;
        float indentInner = getWidth() * 0.04f;
        if (rect == null || rectInner == null) {
            rect = new RectF(indent, indent, getWidth() - indent, getHeight() - indent);
            rectInner = new RectF(indentInner, indentInner, getWidth() - indentInner, getHeight() - indentInner);
        }
        //Draw the border circle
        paint.setStrokeWidth(getWidth() * 0.02f);
        paint.setColor(UtilsTheme.getIconColor());
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rect, 0, 360, false, paint);
        //Draw the progress portion
        paint.setColor(UtilsTheme.getAccentColor());
        paint.setStrokeWidth(getWidth() * 0.04f);
        canvas.drawArc(rectInner, -90, 1.0f * progress / max * 360, false, paint);
    }

    /**
     * Sets the progress
     *
     * @param progress Progress value from 0 to max
     */
    public final void setProgress(long progress) {
        this.progress = progress;
        this.invalidate();
    }

    /**
     * Sets the Max value
     *
     * @param max Maximum value
     */
    public final void setMax(long max) {
        this.max = max;
        if (progress > max) {
            progress = max;
        }
        this.invalidate();
    }

}
