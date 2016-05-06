package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.AutoLockOrientation;
import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.UtilsTheme;
import com.jamesmorrisstudios.appbaselibrary.activities.BaseActivity;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Handles onActivity result and onPermission result calls that were started with Bus managed calls.
 * <p/>
 * Created by James on 12/7/2015.
 */
public final class ActivityResultManager extends BaseBuildManager {
    private ArrayList<PermissionRequest> permissionRequests = new ArrayList<>();
    private ArrayList<StartActivityForResultRequest> startActivityForResultRequests = new ArrayList<>();

    /**
     * Called from the main Activity by the same function
     *
     * @param requestCode Request Code
     * @param resultCode  Result Code
     * @param intent      Result Intent. May be null
     */
    public final boolean onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent intent) {
        for (int i = 0; i < startActivityForResultRequests.size(); i++) {
            if (startActivityForResultRequests.get(i).requestCode == requestCode) {
                AutoLockOrientation.disableAutoLock(getActivity());
                if (resultCode == Activity.RESULT_OK) {
                    startActivityForResultRequests.get(i).listener.resultOk(intent);
                } else {
                    startActivityForResultRequests.get(i).listener.resultFailed();
                }
                startActivityForResultRequests.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Called from the main Activity by the same function
     *
     * @param requestCode  Request Code
     * @param permissions  Permissions list
     * @param grantResults Grant status list
     */
    public final boolean onRequestPermissionsResult(final int requestCode, @NonNull final String permissions[], @NonNull final int[] grantResults) {
        for (int i = 0; i < permissionRequests.size(); i++) {
            if (permissionRequests.get(i).requestCode == requestCode) {
                AutoLockOrientation.disableAutoLock(getActivity());
                if (grantResults.length == 0) {
                    permissionRequests.get(i).listener.permissionDenied();
                } else {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            permissionRequests.get(i).listener.permissionDenied();
                            return true;
                        }
                    }
                    permissionRequests.get(i).listener.permissionGranted();
                }
                permissionRequests.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Not called directly
     *
     * @param request Permission Request
     */
    @Subscribe
    public final void onPermissionRequest(@NonNull final PermissionRequest request) {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permList = request.getRequestedPermissions();
            if (permList.length == 0) {
                request.listener.permissionGranted();
                return;
            }
            for (String item : permList) {
                Log.v("ActivityResultManager", "Perm: " + item);
            }
            boolean anyDenied = false;
            for (String perm : permList) {
                if (getActivity().checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) {
                    if (getActivity().shouldShowRequestPermissionRationale(perm)) {
                        request.listener.shouldShowRationale();
                        return;
                    }
                    anyDenied = true;
                }
            }
            if (anyDenied) {
                permissionRequests.add(request);
                AutoLockOrientation.enableAutoLock(getActivity());
                getActivity().requestPermissions(permList, request.requestCode);
                return;
            }
        }
        request.listener.permissionGranted();
    }

    /**
     * Not called directly
     *
     * @param request Start Activity for Result Request
     */
    @Subscribe
    public final void onStartActivityForResultRequest(@NonNull final StartActivityForResultRequest request) {
        startActivityForResultRequests.add(request);
        AutoLockOrientation.enableAutoLock(getActivity());
        try {
            getActivity().startActivityForResult(request.intent, request.requestCode);
        } catch(Exception ex) {
            ex.printStackTrace();
            Log.v("ActivityResultManager", "Start Activity For Result Failed");
        }
    }

    /**
     * Not called directly
     *
     * @param request Start Activity request
     */
    @Subscribe
    public final void onStartActivityRequest(@NonNull final StartActivityRequest request) {
        try {
            getActivity().startActivity(request.intent);
        } catch(Exception ex) {
            ex.printStackTrace();
            Log.v("ActivityResultManager", "Start Activity Failed");
        }
    }

    /**
     * Not called directly
     *
     * @param request Send Broadcast request
     */
    @Subscribe
    public final void onSendBroadcastRequest(@NonNull final SendBroadcastRequest request) {
        try {
            Log.v("ActivityResultManager", "Send Broadcast");
            getActivity().sendBroadcast(request.intent);
        } catch(Exception ex) {
            ex.printStackTrace();
            Log.v("ActivityResultManager", "Send Broadcast failed");
        }
    }

    /**
     * Not called directly
     *
     * @param event App Base Event
     */
    @Subscribe
    public final void onAppBaseEvent(@NonNull final BaseActivity.AppBaseEvent event) {
        getActivity().onAppBaseEvent(event);
    }

    /**
     * Not called directly
     *
     * @param event App Base Event
     */
    @Subscribe
    public final void onFabEvent(@NonNull final BaseActivity.FabEvent event) {
        getActivity().onFabEvent(event);
    }

    /**
     * Not called directly
     *
     * @param request Request object
     */
    @Subscribe
    public final void onSnackbarRequest(@NonNull final SnackbarRequest request) {
        Snackbar snackbar;
        if (request.actionText != null && request.actionListener != null) {
            snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinatorLayout), request.text, request.duration.getDuration())
                    .setAction(request.actionText, request.actionListener)
                    .setActionTextColor(UtilsTheme.getAccentColor());
        } else {
            snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinatorLayout), request.text, request.duration.getDuration());
        }
        ViewGroup group = (ViewGroup) snackbar.getView();
        TextView textView = (TextView) group.findViewById(R.id.snackbar_text);
        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
            group.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.cardBackgroundDark));
            textView.setTextColor(getActivity().getResources().getColor(R.color.textLightMain));
        } else {
            group.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.cardBackgroundGrey));
            textView.setTextColor(getActivity().getResources().getColor(R.color.textDarkMain));
        }
        snackbar.show();
    }

    /**
     * Not called directly
     *
     * @param request Restart App Request
     */
    @Subscribe
    public final void onRestartAppRequest(@NonNull final RestartAppRequest request) {
        getActivity().restartApp(request);
    }

    /**
     * Not called directly
     *
     * @param request Finish App Request
     */
    @Subscribe
    public final void onFinishAppRequest(@NonNull final FinishAppRequest request) {
        getActivity().finish();
    }

}
