package com.jamesmorrisstudios.appbaselibrary.controls;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jamesmorrisstudios.appbaselibrary.R;


/**
 * Created by James on 5/4/2015.
 */
public class TintedImageView extends ImageView {

    public TintedImageView(Context context) {
        super(context);
    }

    public TintedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyBackgroundTint();
    }

    public TintedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyBackgroundTint();
    }

    private void applyBackgroundTint() {
        if (getBackground() != null) {
            Drawable background = getBackground();
            setImageBitmap(null);
            setImageResource(android.R.color.transparent);
            setImageDrawable(background);
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            // We can now set a tint
            DrawableCompat.setTint(drawable, getResources().getColor(R.color.tintColor));
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_OVER);
        }
        super.setImageDrawable(drawable);
    }

}
