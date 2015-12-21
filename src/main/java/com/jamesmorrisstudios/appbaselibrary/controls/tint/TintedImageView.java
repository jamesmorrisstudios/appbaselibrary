package com.jamesmorrisstudios.appbaselibrary.controls.tint;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jamesmorrisstudios.appbaselibrary.UtilsTheme;


/**
 * Image view that tints its color based on the app icon tint color
 * <p/>
 * Created by James on 5/4/2015.
 */
public final class TintedImageView extends ImageView {

    /**
     * Constructor
     *
     * @param context Context
     * @param attrs   Attributes
     */
    public TintedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyBackgroundTint();
    }

    /**
     * Constructor
     *
     * @param context      Context
     * @param attrs        Attributes
     * @param defStyleAttr Def style
     */
    public TintedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyBackgroundTint();
    }

    /**
     * Retrieve the background and apply the tint
     */
    private void applyBackgroundTint() {
        if (getBackground() != null) {
            Drawable background = getBackground();
            setImageBitmap(null);
            setImageResource(android.R.color.transparent);
            setImageDrawable(background);
        }
    }

    /**
     * Set the new tinted drawable background
     *
     * @param drawable Drawable to tint
     */
    @Override
    public final void setImageDrawable(Drawable drawable) {
        if (drawable != null) {
            Drawable clone = drawable.mutate();
            clone = DrawableCompat.wrap(clone);
            DrawableCompat.setTint(clone, UtilsTheme.getTintColor());
            DrawableCompat.setTintMode(clone, PorterDuff.Mode.SRC_OVER);
            super.setImageDrawable(clone);
        }
    }

}
