package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.utilitieslibrary.Bus;
import com.jamesmorrisstudios.utilitieslibrary.Utils;
import com.jamesmorrisstudios.utilitieslibrary.controls.ButtonFlat;

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
        READ_TUTORIAL, WATCH_TUTORIAL, GOTO_TWITTER, GOTO_FB, GOTO_GPLUS, GOTO_RATE, GOTO_MORE;
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
        ButtonFlat readHow = (ButtonFlat) view.findViewById(R.id.howToUseRead);
        ButtonFlat watchHow = (ButtonFlat) view.findViewById(R.id.howToUseWatch);
        ButtonFlat license = (ButtonFlat) view.findViewById(R.id.helpLicense);
        TextView version = (TextView) view.findViewById(R.id.versionName);
        ImageButton btnTwitter = (ImageButton) view.findViewById(R.id.btn_twitter);
        ImageButton btnFB = (ImageButton) view.findViewById(R.id.btn_fb);
        ImageButton btnGPlus = (ImageButton) view.findViewById(R.id.btn_gplus);
        ButtonFlat rateNow = (ButtonFlat) view.findViewById(R.id.btn_rateNow);
        ButtonFlat moreBy = (ButtonFlat) view.findViewById(R.id.btn_moreBy);
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
        version.setText(Utils.getVersionName());
        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.postEnum(HelpEvent.GOTO_TWITTER);
                Utils.openLink(getResources().getString(R.string.help_link_twitter));
            }
        });
        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.postEnum(HelpEvent.GOTO_FB);
                Utils.openLink(getResources().getString(R.string.help_link_fb));
            }
        });
        btnGPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.postEnum(HelpEvent.GOTO_GPLUS);
                Utils.openLink(getResources().getString(R.string.help_link_gPlus));
            }
        });
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
