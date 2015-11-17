package com.jamesmorrisstudios.appbaselibrary.activities;

import android.content.Intent;
import android.os.Bundle;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;

/**
 * Created by James on 11/14/2015.
 */
public class RestartActivity extends BaseThemedActivity {

    private String page = null;
    private int scrollY = -1;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyTheme();
        setLocale();
        updateImmersiveMode(true);
        super.onCreate(savedInstanceState);
        processIntents();
    }

    /**
     * Activity resume
     */
    @Override
    public void onResume() {
        super.onResume();
        startLauncher();
    }

    private void startLauncher() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent i = AppBase.getContext().getPackageManager().getLaunchIntentForPackage(AppBase.getContext().getPackageName());
                if (page != null) {
                    i.putExtra("PAGE", page);
                }
                if (scrollY != -1) {
                    i.putExtra("SCROLL_Y", scrollY);
                }
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AppBase.getContext().startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        processIntents();
    }

    private void processIntents() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        if(intent.hasExtra("PAGE")) {
            page = intent.getStringExtra("PAGE");
            intent.removeExtra("PAGE");
        }

        if(intent.hasExtra("SCROLL_Y")) {
            scrollY = intent.getIntExtra("SCROLL_Y", -1);
            intent.removeExtra("SCROLL_Y");
        }
    }

}
