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

package com.jamesmorrisstudios.appbaselibrary.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Static animator handler. This class creates many of the common ObjectAnimator use cases.
 * <p/>
 * Created by James on 12/7/2014.
 */
public final class AnimatorControl {

    /**
     * Provide the field to animate on.
     *
     * @param field            The field to animate on
     * @param view             The view to run the animation on
     * @param start            The starting value
     * @param end              The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param interpolatorType The type of interpolator to use
     * @return The ObjectAnimator. You must still start it.
     */
    @NonNull
    public static ObjectAnimator animateCustomField(@NonNull String field, @NonNull View view, float start, float end, long duration, long startDelay, @NonNull InterpolatorType interpolatorType) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, field, start, end);
        anim.setInterpolator(interpolatorType.getInterpolator());
        if (duration != 0) {
            anim.setDuration(duration);
        }
        if (startDelay != 0) {
            anim.setStartDelay(startDelay);
        }
        return anim;
    }

    /**
     * Provide the field to animate on.
     *
     * @param field            The field to animate on
     * @param view             The view to run the animation on
     * @param start            The starting value
     * @param end              The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param interpolatorType The type of interpolator to use
     * @param animatorListener Animation listener.
     */
    public static void animateCustomFieldAutoStart(@NonNull String field, @NonNull View view, float start, float end, long duration, long startDelay, @NonNull InterpolatorType interpolatorType, @Nullable Animator.AnimatorListener animatorListener) {
        ObjectAnimator anim = animateCustomField(field, view, start, end, duration, startDelay, interpolatorType);
        if (animatorListener != null) {
            anim.addListener(animatorListener);
        }
        anim.start();
    }

    /**
     * @param view             The view to run the animation on
     * @param startX           The starting value
     * @param endX             The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param interpolatorType The type of interpolator to use
     * @return The ObjectAnimator. You must still start it.
     */
    @NonNull
    public static ObjectAnimator translateX(@NonNull View view, float startX, float endX, long duration, long startDelay, @NonNull InterpolatorType interpolatorType) {
        return animateCustomField("translationX", view, startX, endX, duration, startDelay, interpolatorType);
    }

    /**
     * @param view             The view to run the animation on
     * @param startX           The starting value
     * @param endX             The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param interpolatorType The type of interpolator to use
     * @param animatorListener The listener for animation callbacks
     */
    public static void translateXAutoStart(@NonNull View view, float startX, float endX, long duration, long startDelay, @NonNull InterpolatorType interpolatorType, @Nullable Animator.AnimatorListener animatorListener) {
        animateCustomFieldAutoStart("translationX", view, startX, endX, duration, startDelay, interpolatorType, animatorListener);
    }

    /**
     * @param view             The view to run the animation on
     * @param startY           The starting value
     * @param endY             The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param interpolatorType The type of interpolator to use
     * @return The ObjectAnimator. You must still start it.
     */
    @NonNull
    public static ObjectAnimator translateY(@NonNull View view, float startY, float endY, long duration, long startDelay, @NonNull InterpolatorType interpolatorType) {
        return animateCustomField("translationY", view, startY, endY, duration, startDelay, interpolatorType);
    }

    /**
     * @param view             The view to run the animation on
     * @param startY           The starting value
     * @param endY             The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param interpolatorType The type of interpolator to use
     * @param animatorListener The listener for animation callbacks
     */
    public static void translateYAutoStart(@NonNull View view, float startY, float endY, long duration, long startDelay, @NonNull InterpolatorType interpolatorType, @Nullable Animator.AnimatorListener animatorListener) {
        animateCustomFieldAutoStart("translationY", view, startY, endY, duration, startDelay, interpolatorType, animatorListener);
    }

    /**
     * @param view             The view to run the animation on
     * @param startAngle       The starting value
     * @param endAngle         The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param interpolatorType The type of interpolator to use
     * @return The ObjectAnimator. You must still start it.
     */
    @NonNull
    public static ObjectAnimator rotation(@NonNull View view, float startAngle, float endAngle, long duration, long startDelay, @NonNull InterpolatorType interpolatorType) {
        return animateCustomField("rotation", view, startAngle, endAngle, duration, startDelay, interpolatorType);
    }

    /**
     * @param view             The view to run the animation on
     * @param startAngle       The starting value
     * @param endAngle         The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param interpolatorType The type of interpolator to use
     * @param animatorListener The listener for animation callbacks
     */
    public static void rotationAutoStart(@NonNull View view, float startAngle, float endAngle, long duration, long startDelay, @NonNull InterpolatorType interpolatorType, @Nullable Animator.AnimatorListener animatorListener) {
        animateCustomFieldAutoStart("rotation", view, startAngle, endAngle, duration, startDelay, interpolatorType, animatorListener);
    }

    /**
     * @param view             The view to run the animation on
     * @param startScale       The starting value
     * @param endScale         The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param interpolatorType The type of interpolator to use
     * @return The ObjectAnimator. You must still start it.
     */
    @NonNull
    public static ObjectAnimator scaleX(@NonNull View view, float startScale, float endScale, long duration, long startDelay, @NonNull InterpolatorType interpolatorType) {
        return animateCustomField("scaleX", view, startScale, endScale, duration, startDelay, interpolatorType);
    }

    /**
     * @param view             The view to run the animation on
     * @param startScale       The starting value
     * @param endScale         The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param interpolatorType The type of interpolator to use
     * @param animatorListener The listener for animation callbacks
     */
    public static void scaleXAutoStart(@NonNull View view, float startScale, float endScale, long duration, long startDelay, @NonNull InterpolatorType interpolatorType, @Nullable Animator.AnimatorListener animatorListener) {
        animateCustomFieldAutoStart("scaleX", view, startScale, endScale, duration, startDelay, interpolatorType, animatorListener);
    }

    /**
     * @param view             The view to run the animation on
     * @param startScale       The starting value
     * @param endScale         The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param interpolatorType The type of interpolator to use
     * @return The ObjectAnimator. You must still start it.
     */
    @NonNull
    public static ObjectAnimator scaleY(@NonNull View view, float startScale, float endScale, long duration, long startDelay, @NonNull InterpolatorType interpolatorType) {
        return animateCustomField("scaleY", view, startScale, endScale, duration, startDelay, interpolatorType);
    }

    /**
     * @param view             The view to run the animation on
     * @param startScale       The starting value
     * @param endScale         The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param interpolatorType The type of interpolator to use
     * @param animatorListener The listener for animation callbacks
     */
    public static void scaleYAutoStart(@NonNull View view, float startScale, float endScale, long duration, long startDelay, @NonNull InterpolatorType interpolatorType, @Nullable Animator.AnimatorListener animatorListener) {
        animateCustomFieldAutoStart("scaleY", view, startScale, endScale, duration, startDelay, interpolatorType, animatorListener);
    }

    /**
     * @param view             The view to run the animation on
     * @param startAlpha       The starting value
     * @param endAlpha         The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param interpolatorType The type of interpolator to use
     * @return The ObjectAnimator. You must still start it.
     */
    @NonNull
    public static ObjectAnimator alpha(@NonNull View view, float startAlpha, float endAlpha, long duration, long startDelay, @NonNull InterpolatorType interpolatorType) {
        return animateCustomField("alpha", view, startAlpha, endAlpha, duration, startDelay, interpolatorType);
    }

    /**
     * @param view             The view to run the animation on
     * @param startAlpha       The starting value
     * @param endAlpha         The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param interpolatorType The type of interpolator to use
     * @param animatorListener The listener for animation callbacks
     */
    public static void alphaAutoStart(@NonNull View view, float startAlpha, float endAlpha, long duration, long startDelay, @NonNull InterpolatorType interpolatorType, @Nullable Animator.AnimatorListener animatorListener) {
        animateCustomFieldAutoStart("alpha", view, startAlpha, endAlpha, duration, startDelay, interpolatorType, animatorListener);
    }

    /**
     * Interpolator types
     */
    public enum InterpolatorType {
        /**
         * An interpolator where the rate of change starts and ends slowly but accelerates through the middle.
         */
        ACCELERLATE_DECELERATE,
        /**
         * An interpolator where the rate of change starts out slowly and and then accelerates.
         */
        ACCELERATE,
        /**
         * An interpolator where the change starts backward then flings forward.
         */
        ANTICIPATE,
        /**
         * An interpolator where the change starts backward then flings forward and overshoots the target value and finally goes back to the final value.
         */
        ANTICIPATE_OVERSHOOT,
        /**
         * An interpolator where the change bounces at the end.
         */
        BOUNCE,
        /**
         * An interpolator where the rate of change starts out quickly and and then decelerates.
         */
        DECELERATE,
        /**
         * An interpolator which accelerates fast and keeps accelerating until the end.
         */
        FAST_OUT_LINEAR_IN,
        /**
         * An interpolator which accelerates fast but decelerates slowly.
         */
        FAST_OUT_SLOW_IN,
        /**
         * An interpolator where the rate of change is constant
         */
        LINEAR,
        /**
         * An interpolator which starts with a peak non-zero velocity and decelerates slowly.
         */
        LINEAR_OUT_SLOW_IN,
        /**
         * An interpolator where the change flings forward and overshoots the last value then comes back.
         */
        OVERSHOOT;

        /**
         * @return Interpolator class
         */
        public final Interpolator getInterpolator() {
            switch (this) {
                case ACCELERLATE_DECELERATE:
                    return new AccelerateDecelerateInterpolator();
                case ACCELERATE:
                    return new AccelerateInterpolator();
                case ANTICIPATE:
                    return new AnticipateInterpolator();
                case ANTICIPATE_OVERSHOOT:
                    return new AnticipateOvershootInterpolator();
                case BOUNCE:
                    return new BounceInterpolator();
                case DECELERATE:
                    return new DecelerateInterpolator();
                case FAST_OUT_LINEAR_IN:
                    return new FastOutLinearInInterpolator();
                case FAST_OUT_SLOW_IN:
                    return new FastOutSlowInInterpolator();
                case LINEAR:
                    return new LinearInterpolator();
                case LINEAR_OUT_SLOW_IN:
                    return new LinearOutSlowInInterpolator();
                case OVERSHOOT:
                    return new OvershootInterpolator();
            }
            return new LinearInterpolator();
        }
    }

}
