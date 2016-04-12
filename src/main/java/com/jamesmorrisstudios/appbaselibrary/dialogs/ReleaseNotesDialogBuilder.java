package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.fragments.HelpFragment;

/**
 * Displays current app release notes
 * <p/>
 * Created by James on 7/22/2015.
 */
public final class ReleaseNotesDialogBuilder {
    private AlertDialog.Builder builder;
    private ScrollView mainView;
    private String content;

    /**
     * @param context Context
     * @param style   Style
     */
    private ReleaseNotesDialogBuilder(@NonNull final Context context, final int style) {
        builder = new AlertDialog.Builder(context, style);
        mainView = new ScrollView(context);
        builder.setView(mainView);
    }

    /**
     * Builder
     *
     * @param context Context
     * @param style   Style
     * @return Dialog Builder
     */
    @NonNull
    public static ReleaseNotesDialogBuilder with(@NonNull final Context context, final int style) {
        return new ReleaseNotesDialogBuilder(context, style);
    }

    /**
     * @param title String title
     * @return Dialog Builder
     */
    @NonNull
    public final ReleaseNotesDialogBuilder setTitle(@NonNull final String title) {
        builder.setTitle(title);
        return this;
    }

    /**
     * @param content String content
     * @return Dialog Builder
     */
    @NonNull
    public final ReleaseNotesDialogBuilder setContent(@NonNull final String content) {
        this.content = content;
        return this;
    }

    /**
     * Builds the dialog
     *
     * @return Alert dialog. Must call execute
     */
    @NonNull
    public final AlertDialog build() {
        builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        Context context = builder.getContext();
        buildReleaseNotesPage(context);
        return builder.create();
    }

    /**
     * Build the dialog
     *
     * @param context Context
     */
    private void buildReleaseNotesPage(@NonNull final Context context) {
        LayoutInflater vi = LayoutInflater.from(context);
        View view = vi.inflate(R.layout.dialog_release_notes, null);
        TextView notes = (TextView) view.findViewById(R.id.notes);
        notes.setText(content);
        Button btnRate = (Button) view.findViewById(R.id.btn_rate);
        Button btnSupport = (Button) view.findViewById(R.id.btn_pro_upgrade);
        Button btnTranslate = (Button) view.findViewById(R.id.btn_translate);
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpFragment.HelpEvent.GOTO_RATE.post();
                Utils.openLink(AppBase.getContext().getString(R.string.store_link));
            }
        });
        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpFragment.HelpEvent.GOTO_PRO_UPGRADE.post();
                Utils.openLink(AppBase.getContext().getString(R.string.store_link_pro));
            }
        });
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpFragment.HelpEvent.GOTO_TRANSLATE.post();
                Utils.openLink(AppBase.getContext().getString(R.string.help_link_translate));
            }
        });
        mainView.addView(view);
    }

}
