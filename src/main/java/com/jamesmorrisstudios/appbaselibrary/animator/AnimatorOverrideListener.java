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
import android.support.annotation.NonNull;

/**
 * Customer listener that implements some methods so only the desired ones are left
 * This shortens code and makes it more readable elsewhere
 * <p/>
 * Created by James on 4/11/2015.
 */
public abstract class AnimatorOverrideListener implements Animator.AnimatorListener {

    /**
     * Does nothing. Just hides this from the listener to shorten code length.
     * Override as needed.
     *
     * @param animation Animation
     */
    @Override
    public void onAnimationStart(@NonNull Animator animation) {

    }

    /**
     * Does nothing. Just hides this from the listener to shorten code length.
     * Override as needed.
     *
     * @param animation Animation
     */
    @Override
    public void onAnimationEnd(@NonNull Animator animation) {

    }

    /**
     * Does nothing. Just hides this from the listener to shorten code length.
     * Override as needed.
     *
     * @param animation Animation
     */
    @Override
    public void onAnimationCancel(@NonNull Animator animation) {

    }

    /**
     * Does nothing. Just hides this from the listener to shorten code length.
     * Override as needed.
     *
     * @param animation Animation
     */
    @Override
    public void onAnimationRepeat(@NonNull Animator animation) {

    }
}
