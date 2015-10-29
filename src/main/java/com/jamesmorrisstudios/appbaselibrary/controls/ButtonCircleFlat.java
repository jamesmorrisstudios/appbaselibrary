/*
 * Copyright (c) 2015.  James Morris Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jamesmorrisstudios.appbaselibrary.controls;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.ThemeManager;
import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;

public class ButtonCircleFlat extends InternalButtonCircleBase {
    TextView textButton;
    private boolean active = false;
    private Paint paint = new Paint();
    private RectF rect = null;

    public ButtonCircleFlat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void setDefaultProperties() {
        minHeight = 30;
        minWidth = 30;
        rippleSize = 3;
        // Min size
        setMinimumHeight(Utils.getDipInt(minHeight));
        setMinimumWidth(Utils.getDipInt(minWidth));
        setBackgroundResource(R.drawable.background_transparent);
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if(ThemeManager.getAppTheme() == ThemeManager.AppTheme.LIGHT) {
            if (active) {
                getTextView().setTextColor(AppBase.getContext().getResources().getColor(R.color.textLightMain));
            } else {
                getTextView().setTextColor(AppBase.getContext().getResources().getColor(R.color.textDarkMain));
            }
        } else {
            if (active) {
                getTextView().setTextColor(AppBase.getContext().getResources().getColor(R.color.textDarkMain));
            } else {
                getTextView().setTextColor(AppBase.getContext().getResources().getColor(R.color.textLightMain));
            }
        }
        this.invalidate();
    }

    @Override
    protected void setAttributes(@NonNull AttributeSet attrs) {
        // Set text button
        String text = null;
        int textResource = attrs.getAttributeResourceValue(ANDROIDXML, "text", -1);
        if (textResource != -1) {
            text = getResources().getString(textResource);
        } else {
            text = attrs.getAttributeValue(ANDROIDXML, "text");
        }
        if (text == null) {
            text = "";
        }
        this.setGravity(Gravity.CENTER);

        textButton = new TextView(getContext());
        textButton.setText(text.toUpperCase());
        textButton.setGravity(Gravity.CENTER);
        textButton.setTextColor(backgroundColor);
        textButton.setTypeface(null, Typeface.BOLD);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        //params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        textButton.setLayoutParams(params);
        addView(textButton);

        if(ThemeManager.getAppTheme() == ThemeManager.AppTheme.LIGHT) {
            setBackgroundColor(getResources().getColor(R.color.iconDark));
        } else {
            setBackgroundColor(getResources().getColor(R.color.iconLight));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (x != -1) {
            paint.setAntiAlias(true);
            paint.setColor(makePressColor());
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, radius, paint);
            if (radius > getHeight() / rippleSize)
                radius += rippleSpeed;
            if (radius >= getWidth() / 2.0f) {
                x = -1;
                y = -1;
                radius = getHeight() / rippleSize;
                if (onClickListener != null && clickAfterRipple)
                    onClickListener.onClick(this);
            }
            invalidate();
        } else if (getActive()) {
            paint.setAntiAlias(true);
            paint.setColor(backgroundColor);

            float left, top, right, bottom;
            left = 0;
            top = 0;
            right = getWidth();
            bottom = getHeight();
            if (getHeight() > getWidth()) {
                top = (getHeight() - getWidth()) / 2.0f;
                bottom = bottom - top;
            } else if (getWidth() > getHeight()) {
                left = (getWidth() - getHeight()) / 2.0f;
                right = right - left;
            }
            if (rect == null || rect.width() != right - left || rect.height() != bottom - top) {
                rect = new RectF(left, top, right, bottom);
            }
            canvas.drawOval(rect, paint);
        }
    }

    @Override
    public void setActivated(boolean activated) {
        super.setActivated(activated);
        invalidate();
    }

    // Set color of background
    public void setBackgroundColor(int color) {
        backgroundColor = color;
        if (isEnabled())
            beforeBackground = backgroundColor;
        textButton.setTextColor(color);
    }

    @NonNull
    @Override
    public TextView getTextView() {
        return textButton;
    }

    @NonNull
    public String getText() {
        return textButton.getText().toString();
    }

    public void setText(String text) {
        textButton.setText(text.toUpperCase());
    }

}
