package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.Bus;
import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.UtilsVersion;
import com.jamesmorrisstudios.appbaselibrary.activities.BaseActivity;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.ReleaseNotesDialogRequest;

/**
 * Help screen fragment
 * <p/>
 * Created by James on 4/29/2015.
 */
public final class HelpFragment extends BaseFragment {
    public static final String TAG = "HelpFragment";

    /**
     * Create the view
     *
     * @param inflater           Inflater object
     * @param container          Container view
     * @param savedInstanceState Saved instance state
     * @return This fragments top view
     */
    @NonNull
    @Override
    public final View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        Button readHow = (Button) view.findViewById(R.id.howToUseRead);
        Button watchHow = (Button) view.findViewById(R.id.howToUseWatch);
        Button license = (Button) view.findViewById(R.id.helpLicense);
        TextView version = (TextView) view.findViewById(R.id.versionName);
        TextView versionType = (TextView) view.findViewById(R.id.versionType);
        ImageView logo = (ImageView) view.findViewById(R.id.studio_logo);
        Button rateNow = (Button) view.findViewById(R.id.btn_rateNow);
        Button moreBy = (Button) view.findViewById(R.id.btn_moreBy);
        Button beta = (Button) view.findViewById(R.id.btn_beta);
        Button support = (Button) view.findViewById(R.id.btn_pro_upgrade);
        Button translate = (Button) view.findViewById(R.id.btn_translate);
        Button releaseNotes = (Button) view.findViewById(R.id.btn_release_notes);
        readHow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpEvent.READ_TUTORIAL.post();
                Utils.openLink(getResources().getString(R.string.tutorial_link_read));
            }
        });
        watchHow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpEvent.WATCH_TUTORIAL.post();
                Utils.openLink(getResources().getString(R.string.tutorial_link_watch));
            }
        });
        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.AppBaseEvent.LICENSE_CLICKED.post();
            }
        });
        if (UtilsVersion.isPro()) {
            versionType.setText(R.string.pro);
        } else {
            versionType.setText(R.string.free);
        }
        version.setText(UtilsVersion.getVersionName() +" ("+UtilsVersion.getVersionCode()+") " + UtilsVersion.getVersionType());
        rateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpEvent.GOTO_RATE.post();
                Utils.openLink(getResources().getString(R.string.store_link));
            }
        });
        moreBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpEvent.GOTO_MORE.post();
                Utils.openLink(getResources().getString(R.string.store_link_all));
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpEvent.GOTO_SUPPORT.post();
                Utils.openLink(getResources().getString(R.string.store_link_pro));
            }
        });
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpEvent.GOTO_TRANSLATE.post();
                Utils.openLink(getResources().getString(R.string.help_link_translate));
            }
        });
        beta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLink(getResources().getString(R.string.store_link_beta));
            }
        });
        releaseNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpEvent.GOTO_RELEASE_NOTES.post();
                new ReleaseNotesDialogRequest().show();
            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLink(getResources().getString(R.string.help_about_site));
            }
        });
        return view;
    }

    /**
     * Override this to set custom text for the toolbar
     *
     * @return Toolbar title text.
     */
    @NonNull
    protected final String getToolbarTitle() {
        return getString(R.string.help_and_feedback);
    }

    /**
     * Unused
     */
    @Override
    protected final void afterViewCreated() {
        hideFab();
    }

    /**
     * Unused
     * @return 0
     */
    @Override
    protected int getOptionsMenuRes() {
        return 0;
    }

    /**
     * @return False
     */
    @Override
    protected boolean usesOptionsMenu() {
        return false;
    }

    /**
     * @return true
     */
    @Override
    public final boolean showToolbarTitle() {
        return true;
    }

    /**
     * Register bus listener if used
     */
    @Override
    protected final void registerBus() {

    }

    /**
     * Unregister bus listener if used
     */
    @Override
    protected final void unregisterBus() {

    }

    /**
     * Unused
     *
     * @param bundle bundle
     */
    @Override
    protected final void saveState(@NonNull final Bundle bundle) {

    }

    /**
     * Unused
     *
     * @param bundle bundle
     */
    @Override
    protected final void restoreState(@NonNull final Bundle bundle) {

    }

    /**
     * Help action events
     */
    public enum HelpEvent {
        READ_TUTORIAL,
        WATCH_TUTORIAL,
        GOTO_RATE,
        GOTO_MORE,
        GOTO_SUPPORT,
        GOTO_TRANSLATE,
        GOTO_RELEASE_NOTES;

        /**
         * Send the event
         */
        public final void post() {
            Bus.postEnum(this);
        }
    }
}
