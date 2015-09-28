package com.jamesmorrisstudios.appbaselibrary.colorpicker.builder;

import com.jamesmorrisstudios.appbaselibrary.colorpicker.ColorPickerView;
import com.jamesmorrisstudios.appbaselibrary.colorpicker.renderer.ColorWheelRenderer;
import com.jamesmorrisstudios.appbaselibrary.colorpicker.renderer.FlowerColorWheelRenderer;
import com.jamesmorrisstudios.appbaselibrary.colorpicker.renderer.SimpleColorWheelRenderer;

public class ColorWheelRendererBuilder {
    public static ColorWheelRenderer getRenderer(ColorPickerView.WHEEL_TYPE wheelType) {
        switch (wheelType) {
            case CIRCLE:
                return new SimpleColorWheelRenderer();
            case FLOWER:
                return new FlowerColorWheelRenderer();
        }
        throw new IllegalArgumentException("wrong WHEEL_TYPE");
    }
}