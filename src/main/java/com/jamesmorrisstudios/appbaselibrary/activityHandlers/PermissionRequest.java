package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import android.Manifest;
import android.os.Build;
import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.Utils;

/**
 * Created by James on 12/5/2015.
 */
public class PermissionRequest {
    public final AppPermission[] permissions;
    public final int requestCode;
    public final OnRequestPermissionListener listener;

    public PermissionRequest(@NonNull AppPermission permission, @NonNull OnRequestPermissionListener listener) {
        this.permissions = new AppPermission[]{permission};
        this.listener = listener;
        this.requestCode = Utils.generateUniqueInt();
    }

    public PermissionRequest(@NonNull AppPermission[] permissions, @NonNull OnRequestPermissionListener listener) {
        this.permissions = permissions;
        this.listener = listener;
        this.requestCode = Utils.generateUniqueInt();
    }

    public final String[] getRequestedPermissions() {
        int validCount = 0;
        for(AppPermission item : permissions) {
            if(item.getPermission() != null) {
                validCount++;
            }
        }
        String[] permList = new String[validCount];
        validCount = 0;
        for(AppPermission item : permissions) {
            if(item.getPermission() != null) {
                permList[validCount] = item.getPermission();
                validCount++;
            }
        }
        return permList;
    }

    public enum AppPermission {
        /*Calendar*/
        READ_CALENDAR,
        WRITE_CALENDAR,
        /*Camera*/
        CAMERA,
        /*Contacts*/
        READ_CONTACTS,
        WRITE_CONTACTS,
        GET_ACCOUNTS,
        /*Location*/
        ACCESS_FINE_LOCATION,
        ACCESS_COARSE_LOCATION,
        /*Microphone*/
        RECORD_AUDIO,
        /*Phone*/
        READ_PHONE_STATE,
        CALL_PHONE,
        READ_CALL_LOG,
        WRITE_CALL_LOG,
        ADD_VOICEMAIL,
        USE_SIP,
        PROCESS_OUTGOING_CALLS,
        /*Sensors*/
        BODY_SENSORS,
        /*SMS*/
        SEND_SMS,
        RECEIVE_SMS,
        READ_SMS,
        RECEIVE_WAP_PUSH,
        RECEIVE_MMS,
        /*Storage*/
        READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE;

        public String getPermission() {
            switch (this) {
                /*Calendar*/
                case READ_CALENDAR:
                    return Manifest.permission.READ_CALENDAR;
                case WRITE_CALENDAR:
                    return Manifest.permission.WRITE_CALENDAR;
                /*Camera*/
                case CAMERA:
                    return Manifest.permission.CAMERA;
                /*Contacts*/
                case READ_CONTACTS:
                    return Manifest.permission.READ_CONTACTS;
                case WRITE_CONTACTS:
                    return Manifest.permission.WRITE_CONTACTS;
                case GET_ACCOUNTS:
                    return Manifest.permission.GET_ACCOUNTS;
                /*Location*/
                case ACCESS_FINE_LOCATION:
                    return Manifest.permission.ACCESS_FINE_LOCATION;
                case ACCESS_COARSE_LOCATION:
                    return Manifest.permission.ACCESS_COARSE_LOCATION;
                /*Microphone*/
                case RECORD_AUDIO:
                    return Manifest.permission.RECORD_AUDIO;
                /*Phone*/
                case READ_PHONE_STATE:
                    return Manifest.permission.READ_PHONE_STATE;
                case CALL_PHONE:
                    return Manifest.permission.CALL_PHONE;
                case READ_CALL_LOG:
                    if(Build.VERSION.SDK_INT >= 16) {
                        return Manifest.permission.READ_CALL_LOG;
                    } else {
                        return null;
                    }
                case WRITE_CALL_LOG:
                    if(Build.VERSION.SDK_INT >= 16) {
                        return Manifest.permission.WRITE_CALL_LOG;
                    } else {
                        return null;
                    }
                case ADD_VOICEMAIL:
                    return Manifest.permission.ADD_VOICEMAIL;
                case USE_SIP:
                    return Manifest.permission.USE_SIP;
                case PROCESS_OUTGOING_CALLS:
                    return Manifest.permission.PROCESS_OUTGOING_CALLS;
                /*Sensors*/
                case BODY_SENSORS:
                    if(Build.VERSION.SDK_INT >= 20) {
                        return Manifest.permission.BODY_SENSORS;
                    } else {
                        return null;
                    }
                /*SMS*/
                case SEND_SMS:
                    return Manifest.permission.SEND_SMS;
                case RECEIVE_SMS:
                    return Manifest.permission.RECEIVE_SMS;
                case READ_SMS:
                    return Manifest.permission.READ_SMS;
                case RECEIVE_WAP_PUSH:
                    return Manifest.permission.RECEIVE_WAP_PUSH;
                case RECEIVE_MMS:
                    return Manifest.permission.RECEIVE_MMS;
                /*Storage*/
                case READ_EXTERNAL_STORAGE:
                    if(Build.VERSION.SDK_INT >= 16) {
                        return Manifest.permission.READ_EXTERNAL_STORAGE;
                    } else {
                        return null;
                    }
                case WRITE_EXTERNAL_STORAGE:
                    return Manifest.permission.WRITE_EXTERNAL_STORAGE;
            }
            return null;
        }
    }

    public interface OnRequestPermissionListener {
        void permissionGranted();
        void permissionDenied();
    }

}
