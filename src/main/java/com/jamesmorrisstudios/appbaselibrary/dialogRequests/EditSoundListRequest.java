package com.jamesmorrisstudios.appbaselibrary.dialogRequests;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.jamesmorrisstudios.appbaselibrary.dialogs.EditSoundListDialog;
import com.jamesmorrisstudios.appbaselibrary.items.SoundItem;

import java.util.ArrayList;

/**
 * Created by James on 5/2/2016.
 */
public class EditSoundListRequest extends AbstractDialogRequest {
    public final String title;
    public final ArrayList<SoundItem> items;
    public final EditSoundListDialog.EditSoundListListener onPositive;
    public final View.OnClickListener onNegative;
    public int freeLimit = -1;

    /**
     * @param title      Title
     * @param items   List of current sound items
     * @param onPositive onPositive
     * @param onNegative onNegative
     */
    public EditSoundListRequest(@NonNull final String title, @NonNull final ArrayList<SoundItem> items, @NonNull final EditSoundListDialog.EditSoundListListener onPositive, @Nullable final View.OnClickListener onNegative) {
        this.title = title;
        this.items = items;
        this.onPositive = onPositive;
        this.onNegative = onNegative;
    }

    /**
     * @param freeLimit Number allowed without pro. -1 (default) for no limit.
     * @return instance
     */
    public final EditSoundListRequest setFreeLimit(int freeLimit) {
        this.freeLimit = freeLimit;
        return this;
    }
}
