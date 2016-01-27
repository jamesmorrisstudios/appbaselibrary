package com.jamesmorrisstudios.appbaselibrary;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;

/**
 * Created by James on 1/12/2016.
 */
public class UtilsClipboard {

    @NonNull
    private static ClipboardManager getClipboardManager() {
        return (ClipboardManager) AppBase.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public static void set(@NonNull final String text) {
        ClipData clip = ClipData.newPlainText("",text);
        getClipboardManager().setPrimaryClip(clip);
    }

    @Nullable
    public static String getText() {
        ClipboardManager clipboard = getClipboardManager();
        if (clipboard.hasPrimaryClip() && (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) || clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            return item.getText().toString();
        }
        return null;
    }

}
