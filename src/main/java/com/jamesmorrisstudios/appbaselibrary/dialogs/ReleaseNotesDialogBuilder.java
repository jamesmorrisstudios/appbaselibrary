package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.Bus;
import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.fragments.HelpFragment;

/**
 * Created by James on 7/22/2015.
 */
public class ReleaseNotesDialogBuilder {
    private AlertDialog.Builder builder;
    private ScrollView mainView;
    private AlertDialog dialog;

    private String content;

    private ReleaseNotesDialogBuilder(@NonNull Context context) {
        builder = new AlertDialog.Builder(context);
        mainView = new ScrollView(context);
        builder.setView(mainView);
    }

    public static ReleaseNotesDialogBuilder with(@NonNull Context context) {
        return new ReleaseNotesDialogBuilder(context);
    }

    public ReleaseNotesDialogBuilder setTitle(@NonNull String title) {
        builder.setTitle(title);
        return this;
    }

    public ReleaseNotesDialogBuilder setContent(@NonNull String content) {
        this.content = content;
        return this;
    }

    public AlertDialog build() {
        builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        Context context = builder.getContext();
        buildReleaseNotesPage(context);
        dialog = builder.create();
        return dialog;
    }

    private void buildReleaseNotesPage(@NonNull Context context) {
        Log.v("ReleaseNotes", "Building release notes page");
        LayoutInflater vi= LayoutInflater.from(context);
        View view = vi.inflate(R.layout.release_notes_dialog, null);
        TextView notes = (TextView) view.findViewById(R.id.notes);
        notes.setText(content);

        Button btnRate = (Button) view.findViewById(R.id.btn_rate);
        Button btnSupport = (Button) view.findViewById(R.id.btn_support);
        Button btnTranslate = (Button) view.findViewById(R.id.btn_translate);

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.postEnum(HelpFragment.HelpEvent.GOTO_RATE);
                Utils.openLink(AppBase.getContext().getString(R.string.store_link));
            }
        });
        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.postEnum(HelpFragment.HelpEvent.GOTO_SUPPORT);
                Utils.openLink(AppBase.getContext().getString(R.string.store_link_donation));
            }
        });
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.postEnum(HelpFragment.HelpEvent.GOTO_TRANSLATE);
                Utils.openLink(AppBase.getContext().getString(R.string.help_link_translate));
            }
        });

        mainView.addView(view);
    }

}
