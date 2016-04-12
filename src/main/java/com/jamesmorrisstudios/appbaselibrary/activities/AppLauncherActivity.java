package com.jamesmorrisstudios.appbaselibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;

/**
 * Handles restarting the App and can restore the previous page and scroll location if supported by that page.
 * <p/>
 * Created by James on 11/14/2015.
 */
public final class AppLauncherActivity extends AppCompatActivity {

    /**
     * On Creation of the activity
     *
     * @param savedInstanceState Unused
     */
    @Override
    protected final void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("AppLauncherActivity", "onCreate");
        processIntents();
    }

    /**
     * Process the starting intent.
     */
    private void processIntents() {
        final Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        String page = null;
        if (intent.hasExtra("PAGE")) {
            page = intent.getStringExtra("PAGE");
            intent.removeExtra("PAGE");
        }
        int scrollY = -1;
        if (intent.hasExtra("SCROLL_Y")) {
            scrollY = intent.getIntExtra("SCROLL_Y", -1);
            intent.removeExtra("SCROLL_Y");
        }
        Bundle bundle = null;
        if (intent.hasExtra("EXTRAS")) {
            bundle = intent.getBundleExtra("EXTRAS");
        }
        Intent i = AppBase.getContext().getPackageManager().getLaunchIntentForPackage(AppBase.getContext().getPackageName());
        if (page != null) {
            i.putExtra("PAGE", page);
        }
        if (scrollY != -1) {
            i.putExtra("SCROLL_Y", scrollY);
        }
        if (bundle != null) {
            i.putExtra("EXTRAS", bundle);
        }
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AppBase.getContext().startActivity(i);
        finish();
    }

}
