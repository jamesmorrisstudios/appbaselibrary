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
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Static animator handler. This class creates many of the common ObjectAnimator use cases.
 * <p/>
 * Created by James on 12/7/2014.
 */
public final class AnimatorControl {

    /**
     * Internal generic object animator builder
     *
     * @param field      The field to animate on
     * @param view       The view to run the animation on
     * @param start      The starting value
     * @param end        The ending value
     * @param duration   The duration of the animation in milliseconds
     * @param startDelay The delay before starting the animation in milliseconds
     * @return The ObjectAnimator. You must still start it.
     */
    @NonNull
    private static ObjectAnimator buildAnimatorLinear(@NonNull String field, @NonNull View view, float start, float end, long duration, long startDelay) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, field, start, end);
        anim.setInterpolator(new LinearInterpolator());
        if (duration != 0) {
            anim.setDuration(duration);
        }
        if (startDelay != 0) {
            anim.setStartDelay(startDelay);
        }
        return anim;
    }

    /**
     * @param view       The view to run the animation on
     * @param startX     The starting value
     * @param endX       The ending value
     * @param duration   The duration of the animation in milliseconds
     * @param startDelay The delay before starting the animation in milliseconds
     * @return The ObjectAnimator. You must still start it.
     */
    @NonNull
    public static ObjectAnimator translateX(@NonNull View view, float startX, float endX, long duration, long startDelay) {
        return buildAnimatorLinear("translationX", view, startX, endX, duration, startDelay);
    }

    /**
     * @param view             The view to run the animation on
     * @param startX           The starting value
     * @param endX             The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param animatorListener The listener for animation callbacks
     */
    public static void translateXAutoStart(@NonNull View view, float startX, float endX, long duration, long startDelay, @Nullable Animator.AnimatorListener animatorListener) {
        ObjectAnimator anim = translateX(view, startX, endX, duration, startDelay);
        if (animatorListener != null) {
            anim.addListener(animatorListener);
        }
        anim.start();
    }

    /**
     * @param view       The view to run the animation on
     * @param startY     The starting value
     * @param endY       The ending value
     * @param duration   The duration of the animation in milliseconds
     * @param startDelay The delay before starting the animation in milliseconds
     * @return The ObjectAnimator. You must still start it.
     */
    @NonNull
    public static ObjectAnimator translateY(@NonNull View view, float startY, float endY, long duration, long startDelay) {
        return buildAnimatorLinear("translationY", view, startY, endY, duration, startDelay);
    }

    /**
     * @param view             The view to run the animation on
     * @param startY           The starting value
     * @param endY             The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param animatorListener The listener for animation callbacks
     */
    public static void translateYAutoStart(@NonNull View view, float startY, float endY, long duration, long startDelay, @Nullable Animator.AnimatorListener animatorListener) {
        ObjectAnimator anim = translateY(view, startY, endY, duration, startDelay);
        if (animatorListener != null) {
            anim.addListener(animatorListener);
        }
        anim.start();
    }

    /**
     * @param view       The view to run the animation on
     * @param startAngle The starting value
     * @param endAngle   The ending value
     * @param duration   The duration of the animation in milliseconds
     * @param startDelay The delay before starting the animation in milliseconds
     * @return The ObjectAnimator. You must still start it.
     */
    @NonNull
    public static ObjectAnimator rotation(@NonNull View view, float startAngle, float endAngle, long duration, long startDelay) {
        return buildAnimatorLinear("rotation", view, startAngle, endAngle, duration, startDelay);
    }

    /**
     * @param view             The view to run the animation on
     * @param startAngle       The starting value
     * @param endAngle         The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param animatorListener The listener for animation callbacks
     */
    public static void rotationAutoStart(@NonNull View view, float startAngle, float endAngle, long duration, long startDelay, @Nullable Animator.AnimatorListener animatorListener) {
        ObjectAnimator anim = rotation(view, startAngle, endAngle, duration, startDelay);
        if (animatorListener != null) {
            anim.addListener(animatorListener);
        }
        anim.start();
    }

    /**
     * @param view       The view to run the animation on
     * @param startScale The starting value
     * @param endScale   The ending value
     * @param duration   The duration of the animation in milliseconds
     * @param startDelay The delay before starting the animation in milliseconds
     * @return The ObjectAnimator. You must still start it.
     */
    @NonNull
    public static ObjectAnimator scaleX(@NonNull View view, float startScale, float endScale, long duration, long startDelay) {
        return buildAnimatorLinear("scaleX", view, startScale, endScale, duration, startDelay);
    }

    /**
     * @param view             The view to run the animation on
     * @param startScale       The starting value
     * @param endScale         The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param animatorListener The listener for animation callbacks
     */
    public static void scaleXAutoStart(@NonNull View view, float startScale, float endScale, long duration, long startDelay, @Nullable Animator.AnimatorListener animatorListener) {
        ObjectAnimator anim = scaleX(view, startScale, endScale, duration, startDelay);
        if (animatorListener != null) {
            anim.addListener(animatorListener);
        }
        anim.start();
    }

    /**
     * @param view       The view to run the animation on
     * @param startScale The starting value
     * @param endScale   The ending value
     * @param duration   The duration of the animation in milliseconds
     * @param startDelay The delay before starting the animation in milliseconds
     * @return The ObjectAnimator. You must still start it.
     */
    @NonNull
    public static ObjectAnimator scaleY(@NonNull View view, float startScale, float endScale, long duration, long startDelay) {
        return buildAnimatorLinear("scaleY", view, startScale, endScale, duration, startDelay);
    }

    /**
     * @param view             The view to run the animation on
     * @param startScale       The starting value
     * @param endScale         The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param animatorListener The listener for animation callbacks
     */
    public static void scaleYAutoStart(@NonNull View view, float startScale, float endScale, long duration, long startDelay, @Nullable Animator.AnimatorListener animatorListener) {
        ObjectAnimator anim = scaleY(view, startScale, endScale, duration, startDelay);
        if (animatorListener != null) {
            anim.addListener(animatorListener);
        }
        anim.start();
    }

    /**
     * @param view       The view to run the animation on
     * @param startAlpha The starting value
     * @param endAlpha   The ending value
     * @param duration   The duration of the animation in milliseconds
     * @param startDelay The delay before starting the animation in milliseconds
     * @return The ObjectAnimator. You must still start it.
     */
    @NonNull
    public static ObjectAnimator alpha(@NonNull View view, float startAlpha, float endAlpha, long duration, long startDelay) {
        return buildAnimatorLinear("alpha", view, startAlpha, endAlpha, duration, startDelay);
    }

    /**
     * @param view             The view to run the animation on
     * @param startAlpha       The starting value
     * @param endAlpha         The ending value
     * @param duration         The duration of the animation in milliseconds
     * @param startDelay       The delay before starting the animation in milliseconds
     * @param animatorListener The listener for animation callbacks
     */
    public static void alphaAutoStart(@NonNull View view, float startAlpha, float endAlpha, long duration, long startDelay, @Nullable Animator.AnimatorListener animatorListener) {
        ObjectAnimator anim = buildAnimatorLinear("alpha", view, startAlpha, endAlpha, duration, startDelay);
        if (animatorListener != null) {
            anim.addListener(animatorListener);
        }
        anim.start();
    }

}
