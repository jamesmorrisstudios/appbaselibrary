package com.jamesmorrisstudios.appbaselibrary.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.jamesmorrisstudios.appbaselibrary.R;


public abstract class InternalButtonCircleSubBase extends RelativeLayout {
    final static String MATERIALDESIGNXML = "http://schemas.android.com/apk/res-auto";
    final int backgroundColor = -1;
    // Indicate if user touched this view the last time
    public boolean isLastTouch = false;
    int beforeBackground;
    boolean animation = false;

    public InternalButtonCircleSubBase(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setBackgroundColor(backgroundColor);
        invalidate();
    }

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
        animation = true;
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
        animation = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (animation)
            invalidate();
    }

}
