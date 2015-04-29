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
import com.jamesmorrisstudios.materialuilibrary.controls.ButtonFlat;
import com.jamesmorrisstudios.utilitieslibrary.Utils;

/**
 * Created by James on 4/29/2015.
 */
public class HelpFragment extends BaseFragment {
    public static final String TAG = "HelpFragment";

    private OnHelpSubPageListener subPageListener;

    /**
     * Required empty constructor
     */
    public HelpFragment() {}

    /**
     * Create the view
     * @param inflater Inflater object
     * @param container Container view
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
        readHow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subPageListener.onTutorialClicked();
            }
        });
        watchHow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Utils.toastShort(getResources().getString(R.string.todo));
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
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.help_twitter))));
            }
        });
        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.help_fb))));
            }
        });
        btnGPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.help_gPlus))));
            }
        });
        return view;
    }

    /**
     * Attach to the activity
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
    public void onBack() {

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

        /**
         * Tutorial fragment button clicked
         */
        void onTutorialClicked();
    }
}
