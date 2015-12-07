package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.app.Activity;
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
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.ReleaseNotesDialogRequest;

/**
 * Created by James on 4/29/2015.
 */
public class HelpFragment extends BaseFragment {
    public static final String TAG = "HelpFragment";

    private OnHelpSubPageListener subPageListener;

    /**
     * Required empty constructor
     */
    public HelpFragment() {
    }

    public enum HelpEvent {
        READ_TUTORIAL, WATCH_TUTORIAL, GOTO_RATE, GOTO_MORE, GOTO_SUPPORT, GOTO_TRANSLATE, GOTO_RELEASE_NOTES
    }

    /**
     * Create the view
     *
     * @param inflater           Inflater object
     * @param container          Container view
     * @param savedInstanceState Saved instance state
     * @return This fragments top view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                Bus.postEnum(HelpEvent.READ_TUTORIAL);
                Utils.openLink(getResources().getString(R.string.tutorial_link_read));
            }
        });
        watchHow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.postEnum(HelpEvent.WATCH_TUTORIAL);
                Utils.openLink(getResources().getString(R.string.tutorial_link_watch));
            }
        });
        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subPageListener.onLicenseClicked();
            }
        });
        if(Utils.isPro()) {
            versionType.setText(R.string.pro);
        } else {
            versionType.setText(R.string.free);
        }
        version.setText(Utils.getVersionName() + " " + Utils.getVersionType());
        rateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.postEnum(HelpEvent.GOTO_RATE);
                Utils.openLink(getResources().getString(R.string.store_link));
            }
        });
        moreBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.postEnum(HelpEvent.GOTO_MORE);
                Utils.openLink(getResources().getString(R.string.store_link_all));
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.postEnum(HelpEvent.GOTO_SUPPORT);
                Utils.openLink(getResources().getString(R.string.store_link_pro));
            }
        });
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.postEnum(HelpEvent.GOTO_TRANSLATE);
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
                Bus.postEnum(HelpEvent.GOTO_RELEASE_NOTES);
                Bus.postObject(new ReleaseNotesDialogRequest());
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
     * Attach to the activity
     *
     * @param activity Activity to attach
     */
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            subPageListener = (OnHelpSubPageListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHelpSubPageListener");
        }
    }

    /**
     * Detach from activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        subPageListener = null;
    }

    @Override
    protected void afterViewCreated() {

    }

    @Override
    public void onBack() {

    }

    @Override
    public boolean showToolbarTitle() {
        return true;
    }

    @Override
    protected void saveState(Bundle bundle) {

    }

    @Override
    protected void restoreState(Bundle bundle) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnHelpSubPageListener {

        /**
         * License fragment button clicked
         */
        void onLicenseClicked();
    }
}
