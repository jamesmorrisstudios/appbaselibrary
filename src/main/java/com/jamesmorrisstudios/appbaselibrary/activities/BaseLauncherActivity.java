package com.jamesmorrisstudios.appbaselibrary.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jamesmorrisstudios.appbaselibrary.R;

/**
 * Created by James on 5/11/2015.
 */
public abstract class BaseLauncherActivity extends BaseLauncherNoViewActivity {

    /**
     * Create this activity
     *
     * @param savedInstanceState Saved instance state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initOnCreate();
    }

}
