package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jamesmorrisstudios.appbaselibrary.AutoLockOrientation;
import com.jamesmorrisstudios.appbaselibrary.activities.BaseLauncherNoViewActivity;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Created by James on 12/7/2015.
 */
public class ActivityResultManager extends BaseBuildManager {
    private ArrayList<PermissionRequest> permissionRequests = new ArrayList<>();
    private ArrayList<StartActivityForResultRequest> startActivityForResultRequests = new ArrayList<>();

    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        for(int i=0; i<startActivityForResultRequests.size(); i++) {
            if(startActivityForResultRequests.get(i).requestCode == requestCode) {
                AutoLockOrientation.disableAutoLock(getActivity());
                if(resultCode == Activity.RESULT_OK) {
                    startActivityForResultRequests.get(i).listener.resultOk(intent);
                } else {
                    startActivityForResultRequests.get(i).listener.resultFailed();
                }
                startActivityForResultRequests.remove(i);
                break;
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        for(int i=0; i<permissionRequests.size(); i++) {
            if(permissionRequests.get(i).requestCode == requestCode) {
                AutoLockOrientation.disableAutoLock(getActivity());
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionRequests.get(i).listener.permissionGranted();
                } else {
                    permissionRequests.get(i).listener.permissionDenied();
                }
                permissionRequests.remove(i);
                break;
            }
        }
    }

    @Subscribe
    public void onPermissionRequest(PermissionRequest request) {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permList = request.getRequestedPermissions();
            for(String perm : permList) {
                if(getActivity().checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) {
                    permissionRequests.add(request);
                    AutoLockOrientation.enableAutoLock(getActivity());
                    getActivity().requestPermissions(permList, request.requestCode);
                    return;
                }
            }
        }
        request.listener.permissionGranted();
    }

    @Subscribe
    public void onStartActivityForResultRequest(StartActivityForResultRequest request) {
        startActivityForResultRequests.add(request);
        AutoLockOrientation.enableAutoLock(getActivity());
        Log.v("ActivityResultManager", "RequestCode: " + request.requestCode);
        getActivity().startActivityForResult(request.intent, request.requestCode);
    }

    @Subscribe
    public void onAppBaseEvent(BaseLauncherNoViewActivity.AppBaseEvent event) {
        getActivity().onAppBaseEvent(event);
    }

}
