package com.jamesmorrisstudios.appbaselibrary;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.jamesmorrisstudios.appbaselibrary.activityHandlers.SnackbarRequest;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.PromptDialogRequest;
import com.jamesmorrisstudios.appbaselibrary.preferences.Prefs;

import java.util.regex.Pattern;

/**
 * Version utility functions
 * <p/>
 * Created by James on 12/8/2015.
 */
public final class UtilsVersion {

    /**
     * Updates the saved version name
     */
    public static void updateVersion() {
        Prefs.putString("com.jamesmorrisstudios.appbaselibrary.APPDATA", "LAST_OPEN_VERSION", UtilsVersion.getVersionName());
    }

    /**
     * Gets the formatted version string of the app in the format 1.2.3
     *
     * @return The formatted version string
     */
    @NonNull
    public static String getOldVersionName() {
        String version = Prefs.getString("com.jamesmorrisstudios.appbaselibrary.APPDATA", "LAST_OPEN_VERSION");
        if (version.isEmpty()) {
            return "0.0.0";
        }
        return version;
    }

    /**
     * Gets the formatted version string of the app in the format 1.2.3
     *
     * @return The formatted version string
     */
    @NonNull
    public static String getVersionName() {
        try {
            return AppBase.getContext().getPackageManager().getPackageInfo(AppBase.getContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Gets the formatted version code (build) number
     *
     * @return The version code (build number)
     */
    public static int getVersionCode() {
        try {
            return AppBase.getContext().getPackageManager().getPackageInfo(AppBase.getContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @param fromVersionString Version String
     * @return major version number
     */
    public static int getVersionMajor(@NonNull final String fromVersionString) {
        return getVersion(fromVersionString, 0);
    }

    /**
     * @param fromVersionString Version String
     * @return minor version number
     */
    public static int getVersionMinor(@NonNull final String fromVersionString) {
        return getVersion(fromVersionString, 1);
    }

    /**
     * @param fromVersionString Version String
     * @return aux version number
     */
    public static int getVersionAux(@NonNull final String fromVersionString) {
        return getVersion(fromVersionString, 2);
    }

    /**
     * @return major version number
     */
    public static int getVersionMajor() {
        return getVersion(getVersionName(), 0);
    }

    /**
     * @return minor version number
     */
    public static int getVersionMinor() {
        return getVersion(getVersionName(), 1);
    }

    /**
     * @return aux version number
     */
    public static int getVersionAux() {
        return getVersion(getVersionName(), 2);
    }

    /**
     * @return old major version number
     */
    public static int getOldVersionMajor() {
        return getVersion(getOldVersionName(), 0);
    }

    /**
     * @return old minor version number
     */
    public static int getOldVersionMinor() {
        return getVersion(getOldVersionName(), 1);
    }

    /**
     * @return old aux version number
     */
    public static int getOldVersionAux() {
        return getVersion(getOldVersionName(), 2);
    }

    /**
     * @param versionName  Version String
     * @param versionIndex Index of item. 0 == major, 1 == minor, or 2 == aux
     * @return version number
     */
    private static int getVersion(@NonNull final String versionName, final int versionIndex) {
        String nameSplit[] = versionName.split(Pattern.quote("."));
        if (versionName.equals("") || nameSplit.length != 3) {
            return -1;
        }
        return Utils.stringToInt(nameSplit[versionIndex], -1);
    }

    /**
     * Version type name (Alpha, Beta, Production)
     *
     * @return Alpha, Beta, or Production
     */
    @NonNull
    public static String getVersionType() {
        return AppBase.getContext().getString(R.string.version_type);
    }

    /**
     * @return True if pro version
     */
    public static boolean isPro() {
        //Log.v("UtilsVersion", "PRO Package: "+AppBase.getContext().getString(R.string.pro_package_name)+" Package: "+Utils.getPackage());
        return AppBase.getContext().getString(R.string.pro_package_name).equals(Utils.getPackage());
    }

    /**
     * Show the get pro popup dialog
     */
    public static void showProPopup() {
        showProPopup(AppBase.getContext().getString(R.string.pro_feature), AppBase.getContext().getString(R.string.feature_requires_pro));
    }

    /**
     * Show the get pro popup dialog for a limited feature. Full feature available in pro
     */
    public static void showProPopupLimited() {
        showProPopup(AppBase.getContext().getString(R.string.pro_feature), AppBase.getContext().getString(R.string.feature_requires_pro_limited));
    }

    /**
     * Show the get pro popup dialog
     */
    private static void showProPopup(@NonNull String title, @NonNull String content) {
        String positiveText = AppBase.getContext().getString(R.string.upgrade_now);
        new PromptDialogRequest(title, content, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utils.openLink(AppBase.getContext().getString(R.string.store_link_pro));
            }
        }, positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cancel
            }
        }, null).show();
    }

    /**
     * Shows the permission denied snackbar that links the user to the settings page
     */
    public static void showPermDeniedSnackbar() {
        new SnackbarRequest(AppBase.getContext().getString(R.string.permission_denied), SnackbarRequest.SnackBarDuration.INDEFINITE, AppBase.getContext().getString(R.string.enable), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openSettingsPage();
            }
        }).execute();
    }

    /**
     * Dark icon for light theme and light icon for dark theme
     *
     * @return The pro icon in the proper color for the app theme
     */
    @DrawableRes
    public static int getProIcon() {
        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
            return R.drawable.ic_pro_black_24dp;
        } else {
            return R.drawable.ic_pro_white_24dp;
        }
    }

    /**
     * Light for light text and dark for dark text
     *
     * @return The pro icon in the proper color for the app theme to go in the toolbar
     */
    @DrawableRes
    public static int getProIconToolbar() {
        if (UtilsTheme.getToolbarTheme() == UtilsTheme.ToolbarTheme.LIGHT_TEXT) {
            return R.drawable.ic_pro_white_24dp;
        } else {
            return R.drawable.ic_pro_black_24dp;
        }
    }

}
