package com.jamesmorrisstudios.appbaselibrary.controls.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * Horizontal progress bar
 */
public final class ProgressBarDeterminateHorizontal extends ProgressBarDeterminate {

    /**
     * Constructor
     *
     * @param context Context
     * @param attrs   Attributes
     */
    public ProgressBarDeterminateHorizontal(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Draw the current progress level
     *
     * @param progress Current progress amount
     * @param canvas   Canvas to draw on
     */
    public final void setProgress(int progress, Canvas canvas) {
        if (getWidth() == 0) {
            pendindProgress = progress;
        } else {
            this.progress = progress;
            if (progress > max)
                progress = max;
            if (progress < min)
                progress = min;
            int totalWidth = max - min;
            double progressPercent = (double) progress / (double) totalWidth;
            int progressWidth = (int) (getWidth() * progressPercent);
            Rect rect;
            int widthSplit = Math.round(canvas.getWidth() * 1.0f / colors.length);
            int minWidth = canvas.getWidth() - progressWidth;
            for (int i = 0; i < colors.length; i++) {
                paint.setColor(colors[i]);
                int widthStart = Math.max(canvas.getWidth() - widthSplit * (i + 1), minWidth);
                int widthEnd = Math.max(canvas.getWidth() - widthSplit * i, minWidth);
                rect = new Rect(widthStart, 0, widthEnd, canvas.getHeight());
                canvas.drawRect(rect, paint);
            }
            pendindProgress = -1;
        }
    }

}
