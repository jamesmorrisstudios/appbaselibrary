package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.support.annotation.NonNull;

/**
 * Created by James on 12/23/2015.
 */
public final class ImageRequest extends AbstractDialogRequest {
    public final String imagePath;

    public ImageRequest(@NonNull final String imagePath) {
        this.imagePath = imagePath;
    }

}
