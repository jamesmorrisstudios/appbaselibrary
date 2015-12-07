package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.Utils;

/**
 * Created by James on 12/7/2015.
 */
public class StartActivityForResultRequest {
    public final Intent intent;
    public final int requestCode;
    public final OnStartActivityForResultListener listener;

    public StartActivityForResultRequest(@NonNull Intent intent, @NonNull OnStartActivityForResultListener listener) {
        this.intent = intent;
        this.requestCode = Utils.generateUniqueIntLower16();
        this.listener = listener;
    }

    public interface OnStartActivityForResultListener {
        void resultOk(Intent intent);
        void resultFailed();
    }

}
