package com.jamesmorrisstudios.appbaselibrary.layouts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * This class will set its width to match whatever its height is.
 * <p/>
 * Created by James on 10/14/2014.
 */
public class SquareHeightView extends View {

    /**
     * @param context Local context
     */
    public SquareHeightView(@NonNull final Context context) {
        super(context);
    }

    /**
     * @param context Local context
     * @param attrs   Attribute set
     */
    public SquareHeightView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context  Local context
     * @param attrs    Attribute set
     * @param defStyle Style def
     */
    public SquareHeightView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyle) {
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
