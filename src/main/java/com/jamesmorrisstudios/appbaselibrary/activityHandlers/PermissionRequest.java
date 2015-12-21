package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import android.Manifest;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jamesmorrisstudios.appbaselibrary.Utils;

/**
 * App permission requests for android 6.0+ compatibility.
 * If on an older platform callbacks will be called instantly with a success response.
 * <p/>
 * Created by James on 12/5/2015.
 */
public final class PermissionRequest extends AbstractActivityRequest {
    public final AppPermission[] permissions;
    public final int requestCode;
    public final OnPermissionRequestListener listener;

    /**
     * @param permission Single permission to request
     * @param listener   Callback listener
     */
    public PermissionRequest(@NonNull AppPermission permission, @NonNull OnPermissionRequestListener listener) {
        this.permissions = new AppPermission[]{permission};
        this.listener = listener;
        this.requestCode = Utils.generateUniqueInt();
    }

    /**
     * You must also include the desired permission in the manifest or else the app will crash.
     *
     * @param permissions Array of permissions to request. Will fail if any are denied.
     * @param listener    Callback listener
     */
    public PermissionRequest(@NonNull AppPermission[] permissions, @NonNull OnPermissionRequestListener listener) {
        this.permissions = permissions;
        this.listener = listener;
        this.requestCode = Utils.generateUniqueInt();
    }

    /**
     * Gets an array of all the requested permissions trimming out those not needed for the current platform.
     *
     * @return String array of requested permissions. May be zero length.
     */
    @NonNull
    public final String[] getRequestedPermissions() {
        int validCount = 0;
        for (AppPermission item : permissions) {
            if (item.getPermission() != null) {
                validCount++;
            }
        }
        String[] permList = new String[validCount];
        validCount = 0;
        for (AppPermission item : permissions) {
            if (item.getPermission() != null) {
                permList[validCount] = item.getPermission();
                validCount++;
            }
        }
        return permList;
    }

    /**
     * List of all supported app permissions.
     * Some are available only on specific android versions. They should still always be asked for as this system automatically handles when they are not needed.
     * Permissions are grouped into categories. The prompt will include the containing categories and not each specific permission.
     */
    public enum AppPermission {
        /*Calendar*/
        /**
         * Category Calendar
         * Read the users calendar
         */
        READ_CALENDAR,
        /**
         * Category Calendar
         * Write to the users calendar
         */
        WRITE_CALENDAR,
        /*Camera*/
        /**
         * Category Camera
         * Access the users camera to take pictures
         */
        CAMERA,
        /*Contacts*/
        /**
         * Category Contacts
         * Read the users contacts
         */
        READ_CONTACTS,
        /**
         * Category Contacts
         * Write to the users contacts
         */
        WRITE_CONTACTS,
        /**
         * Category Contacts
         * Retrieve the accounts on the device
         */
        GET_ACCOUNTS,
        /*Location*/
        /**
         * Category Location
         * Access the fine (GPS) location
         */
        ACCESS_FINE_LOCATION,
        /**
         * Category Location
         * Access the coarse (network) location
         */
        ACCESS_COARSE_LOCATION,
        /*Microphone*/
        /**
         * Category Microphone
         * Record audio
         */
        RECORD_AUDIO,
        /*Phone*/
        /**
         * Category Phone
         * Read the phone state and identity information. Avoid if at all possible
         */
        READ_PHONE_STATE,
        /**
         * Category Phone
         * Call a phone number
         */
        CALL_PHONE,
        /**
         * Category Phone
         * Read the users call history
         */
        READ_CALL_LOG,
        /**
         * Category Phone
         * Write to the users call history
         */
        WRITE_CALL_LOG,
        /**
         * Category Phone
         * Add a voicemail to the user
         */
        ADD_VOICEMAIL,
        /**
         * Category Phone
         * Use SIP
         */
        USE_SIP,
        /**
         * Category Phone
         * ?
         */
        PROCESS_OUTGOING_CALLS,
        /*Sensors*/
        /**
         * Category Sensors
         * Access sensor data such as step info
         */
        BODY_SENSORS,
        /*SMS*/
        /**
         * Category SMS
         * Send an SMS message
         */
        SEND_SMS,
        /**
         * Category SMS
         * Receive an SMS message
         */
        RECEIVE_SMS,
        /**
         * Category SMS
         * Read SMS messages
         */
        READ_SMS,
        /**
         * Category SMS
         * Receive WAP push
         */
        RECEIVE_WAP_PUSH,
        /**
         * Category SMS
         * Receive an MMS message
         */
        RECEIVE_MMS,
        /*Storage*/
        /**
         * Category Storage
         * Read from external storage
         */
        READ_EXTERNAL_STORAGE,
        /**
         * Category Storage
         * Write to external storage
         */
        WRITE_EXTERNAL_STORAGE;

        /**
         * @return The string representation of the permission. Null if not available on this platform.
         */
        @Nullable
        public final String getPermission() {
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
                    if (Build.VERSION.SDK_INT >= 16) {
                        return Manifest.permission.READ_CALL_LOG;
                    } else {
                        return null;
                    }
                case WRITE_CALL_LOG:
                    if (Build.VERSION.SDK_INT >= 16) {
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
                    if (Build.VERSION.SDK_INT >= 20) {
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
                    if (Build.VERSION.SDK_INT >= 16) {
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

    /**
     * Listener for a permission request
     */
    public interface OnPermissionRequestListener {

        /**
         * Permission was granted for all requested permissions
         */
        void permissionGranted();

        /**
         * Permission was denied for one or more requested permissions
         */
        void permissionDenied();
    }

}
