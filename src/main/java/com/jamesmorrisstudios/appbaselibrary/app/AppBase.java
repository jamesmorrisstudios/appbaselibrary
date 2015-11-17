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

package com.jamesmorrisstudios.appbaselibrary.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.ThemeManager;

/**
 * Top level application class. If you override application in your app that uses this library you must extend this class!
 * <p/>
 * Created by James on 7/7/2014.
 */
public class AppBase extends Application {
    private static AppBase instance;

    /**
     * Gets the Application level Context.
     * NEVER hold a reference to this as that can cause a memory leak
     *
     * @return Application Context
     */
    @NonNull
    public static Context getContext() {
        return instance.getApplicationContext();
    }

    /**
     * Gets the Application instance
     * NEVER hold a reference to this.
     * @return Application instance
     */
    @NonNull
    public static AppBase getInstance() {
        return instance;
    }

    /**
     * Initial create for the application.
     * Sets application level context
     */
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        applyTheme();
    }

    /**
     * Set current theme to the application
     */
    public final void applyTheme() {
        setTheme(ThemeManager.getAppStyle());
        setTheme(ThemeManager.getToolbarStyle());
        setTheme(ThemeManager.getAccentColorStyle());
        setTheme(ThemeManager.getPrimaryColorStyle());
    }

}
