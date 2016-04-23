package com.jamesmorrisstudios.appbaselibrary;

import android.app.Application;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.preferences.Prefs;

/**
 * Gets the theme and styles that the user currently has selected
 * <p/>
 * Created by James on 10/29/2015.
 */
public final class UtilsTheme {

    /**
     * Apply the given theme to the app
     * @param activity Activity
     * @param style Style resource Id of the theme.
     */
    public static void applyTheme(@NonNull final AppCompatActivity activity, @StyleRes int style) {
        activity.setTheme(style);
    }

    /**
     * Applies the theme from the activity
     *
     * @param activity Activity context
     */
    public static void applyTheme(@NonNull final AppCompatActivity activity) {
        AppBase.getInstance().applyTheme();
        activity.setTheme(UtilsTheme.getAppTheme().getStyle());
        activity.setTheme(UtilsTheme.getToolbarTheme().getStyle());
        activity.setTheme(UtilsTheme.getTabLayoutStyle());
        activity.setTheme(UtilsTheme.getAccentColorStyle());
        activity.setTheme(UtilsTheme.getPrimaryColorStyle());
    }

    /**
     * Applies the theme from the Application
     *
     * @param app Application Context
     */
    public static void applyTheme(@NonNull final Application app) {
        app.setTheme(UtilsTheme.getAppTheme().getStyle());
        app.setTheme(UtilsTheme.getToolbarTheme().getStyle());
        app.setTheme(UtilsTheme.getTabLayoutStyle());
        app.setTheme(UtilsTheme.getAccentColorStyle());
        app.setTheme(UtilsTheme.getPrimaryColorStyle());
    }

    /**
     * @return The current app theme
     */
    public static AppTheme getAppTheme() {
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        String key = AppBase.getContext().getString(R.string.pref_theme);
        switch (Prefs.getInt(pref, key, 0)) {
            case 0: //Light
                return AppTheme.LIGHT;
            case 1: //Dark
                return AppTheme.DARK;
            default:
                return AppTheme.LIGHT;
        }
    }

    /**
     * @return The toolbar theme
     */
    public static ToolbarTheme getToolbarTheme() {
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        String key = AppBase.getContext().getString(R.string.pref_toolbar_text_color);
        switch (Prefs.getInt(pref, key, 0)) {
            case 0:
                return ToolbarTheme.LIGHT_TEXT;
            case 1:
                return ToolbarTheme.DARK_TEXT;
            default:
                return ToolbarTheme.LIGHT_TEXT;
        }
    }

    /**
     * @return Accent color theme
     */
    public static AccentColorTheme getAccentColorTheme() {
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        String key = AppBase.getContext().getString(R.string.pref_accent_color);
        switch (Prefs.getInt(pref, key, 9)) {
            case 0:
                return AccentColorTheme.RED;
            case 1:
                return AccentColorTheme.PINK;
            case 2:
                return AccentColorTheme.PURPLE;
            case 3:
                return AccentColorTheme.DEEP_PURPLE;
            case 4:
                return AccentColorTheme.INDIGO;
            case 5:
                return AccentColorTheme.BLUE;
            case 6:
                return AccentColorTheme.LIGHT_BLUE;
            case 7:
                return AccentColorTheme.CYAN;
            case 8:
                return AccentColorTheme.TEAL;
            case 9:
                return AccentColorTheme.GREEN;
            case 10:
                return AccentColorTheme.LIGHT_GREEN;
            case 11:
                return AccentColorTheme.LIME;
            case 12:
                return AccentColorTheme.YELLOW;
            case 13:
                return AccentColorTheme.AMBER;
            case 14:
                return AccentColorTheme.ORANGE;
            case 15:
                return AccentColorTheme.DEEP_ORANGE;
            default:
                return AccentColorTheme.RED;
        }
    }

    /**
     * @return Primary color theme
     */
    public static PrimaryColorTheme getPrimaryColorTheme() {
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        String key = AppBase.getContext().getString(R.string.pref_primary_color);
        switch (Prefs.getInt(pref, key, 5)) {
            case 0:
                return PrimaryColorTheme.RED;
            case 1:
                return PrimaryColorTheme.PINK;
            case 2:
                return PrimaryColorTheme.PURPLE;
            case 3:
                return PrimaryColorTheme.DEEP_PURPLE;
            case 4:
                return PrimaryColorTheme.INDIGO;
            case 5:
                return PrimaryColorTheme.BLUE;
            case 6:
                return PrimaryColorTheme.LIGHT_BLUE;
            case 7:
                return PrimaryColorTheme.CYAN;
            case 8:
                return PrimaryColorTheme.TEAL;
            case 9:
                return PrimaryColorTheme.GREEN;
            case 10:
                return PrimaryColorTheme.LIGHT_GREEN;
            case 11:
                return PrimaryColorTheme.LIME;
            case 12:
                return PrimaryColorTheme.YELLOW;
            case 13:
                return PrimaryColorTheme.AMBER;
            case 14:
                return PrimaryColorTheme.ORANGE;
            case 15:
                return PrimaryColorTheme.DEEP_ORANGE;
            case 16:
                return PrimaryColorTheme.BROWN;
            case 17:
                return PrimaryColorTheme.GREY;
            case 18:
                return PrimaryColorTheme.BLUE_GREY;
            case 19:
                return PrimaryColorTheme.DARK_GREY;
            case 20:
                return PrimaryColorTheme.WHITE;
            default:
                return PrimaryColorTheme.RED;
        }
    }

    /**
     * @return Toolbar popup style
     */
    public static int getToolbarPopupStyle() {
        switch (getAppTheme()) {
            case LIGHT:
                switch (getToolbarTheme()) {
                    case LIGHT_TEXT:
                        return R.style.toolbarPopupLight;
                    case DARK_TEXT:
                        return R.style.toolbarPopupLightDarkText;
                }
            case DARK:
                switch (getToolbarTheme()) {
                    case LIGHT_TEXT:
                        return R.style.toolbarPopupDark;
                    case DARK_TEXT:
                        return R.style.toolbarPopupDarkDarkText;
                }
        }
        return -1;
    }

    /**
     * @return Tab layout style
     */
    public static int getTabLayoutStyle() {
        switch (getAppTheme()) {
            case LIGHT:
                switch (getToolbarTheme()) {
                    case LIGHT_TEXT:
                        return R.style.tabLayoutLight;
                    case DARK_TEXT:
                        return R.style.tabLayoutLightDarkText;
                }
            case DARK:
                switch (getToolbarTheme()) {
                    case LIGHT_TEXT:
                        return R.style.tabLayoutDark;
                    case DARK_TEXT:
                        return R.style.tabLayoutDarkDarkText;
                }
        }
        return -1;
    }

    /**
     * @return Accent color style
     */
    public static int getAccentColorStyle() {
        switch (getAccentColorTheme()) {
            case RED:
                return R.style.accent_red;
            case PINK:
                return R.style.accent_pink;
            case PURPLE:
                return R.style.accent_purple;
            case DEEP_PURPLE:
                return R.style.accent_deep_purple;
            case INDIGO:
                return R.style.accent_indigo;
            case BLUE:
                return R.style.accent_blue;
            case LIGHT_BLUE:
                return R.style.accent_light_blue;
            case CYAN:
                return R.style.accent_cyan;
            case TEAL:
                return R.style.accent_teal;
            case GREEN:
                return R.style.accent_green;
            case LIGHT_GREEN:
                return R.style.accent_light_green;
            case LIME:
                return R.style.accent_lime;
            case YELLOW:
                return R.style.accent_yellow;
            case AMBER:
                return R.style.accent_amber;
            case ORANGE:
                return R.style.accent_orange;
            case DEEP_ORANGE:
                return R.style.accent_deep_orange;
            default:
                return R.style.accent_red;
        }
    }

    /**
     * @return Primary color style
     */
    public static int getPrimaryColorStyle() {
        switch (getPrimaryColorTheme()) {
            case RED:
                return R.style.primary_red;
            case PINK:
                return R.style.primary_pink;
            case PURPLE:
                return R.style.primary_purple;
            case DEEP_PURPLE:
                return R.style.primary_deep_purple;
            case INDIGO:
                return R.style.primary_indigo;
            case BLUE:
                return R.style.primary_blue;
            case LIGHT_BLUE:
                return R.style.primary_light_blue;
            case CYAN:
                return R.style.primary_cyan;
            case TEAL:
                return R.style.primary_teal;
            case GREEN:
                return R.style.primary_green;
            case LIGHT_GREEN:
                return R.style.primary_light_green;
            case LIME:
                return R.style.primary_lime;
            case YELLOW:
                return R.style.primary_yellow;
            case AMBER:
                return R.style.primary_amber;
            case ORANGE:
                return R.style.primary_orange;
            case DEEP_ORANGE:
                return R.style.primary_deep_orange;
            case BROWN:
                return R.style.primary_brown;
            case GREY:
                return R.style.primary_grey;
            case BLUE_GREY:
                return R.style.primary_blue_grey;
            case DARK_GREY:
                return R.style.primary_dark_grey;
            case WHITE:
                return R.style.primary_white;
            default:
                return R.style.primary_red;
        }
    }

    /**
     * @return Alert dialog style
     */
    public static int getAlertDialogStyle() {
        switch (getAppTheme()) {
            case LIGHT:
                switch (getAccentColorTheme()) {
                    case RED:
                        return R.style.alertDialogLightRed;
                    case PINK:
                        return R.style.alertDialogLightPink;
                    case PURPLE:
                        return R.style.alertDialogLightPurple;
                    case DEEP_PURPLE:
                        return R.style.alertDialogLightDeepPurple;
                    case INDIGO:
                        return R.style.alertDialogLightIndigo;
                    case BLUE:
                        return R.style.alertDialogLightBlue;
                    case LIGHT_BLUE:
                        return R.style.alertDialogLightLightBlue;
                    case CYAN:
                        return R.style.alertDialogLightCyan;
                    case TEAL:
                        return R.style.alertDialogLightTeal;
                    case GREEN:
                        return R.style.alertDialogLightGreen;
                    case LIGHT_GREEN:
                        return R.style.alertDialogLightLightGreen;
                    case LIME:
                        return R.style.alertDialogLightLime;
                    case YELLOW:
                        return R.style.alertDialogLightYellow;
                    case AMBER:
                        return R.style.alertDialogLightAmber;
                    case ORANGE:
                        return R.style.alertDialogLightOrange;
                    case DEEP_ORANGE:
                        return R.style.alertDialogLightDeepOrange;
                    default:
                        return R.style.alertDialogLightRed;
                }
            case DARK:
                switch (getAccentColorTheme()) {
                    case RED:
                        return R.style.alertDialogDarkRed;
                    case PINK:
                        return R.style.alertDialogDarkPink;
                    case PURPLE:
                        return R.style.alertDialogDarkPurple;
                    case DEEP_PURPLE:
                        return R.style.alertDialogDarkDeepPurple;
                    case INDIGO:
                        return R.style.alertDialogDarkIndigo;
                    case BLUE:
                        return R.style.alertDialogDarkBlue;
                    case LIGHT_BLUE:
                        return R.style.alertDialogDarkLightBlue;
                    case CYAN:
                        return R.style.alertDialogDarkCyan;
                    case TEAL:
                        return R.style.alertDialogDarkTeal;
                    case GREEN:
                        return R.style.alertDialogDarkGreen;
                    case LIGHT_GREEN:
                        return R.style.alertDialogDarkLightGreen;
                    case LIME:
                        return R.style.alertDialogDarkLime;
                    case YELLOW:
                        return R.style.alertDialogDarkYellow;
                    case AMBER:
                        return R.style.alertDialogDarkAmber;
                    case ORANGE:
                        return R.style.alertDialogDarkOrange;
                    case DEEP_ORANGE:
                        return R.style.alertDialogDarkDeepOrange;
                    default:
                        return R.style.alertDialogDarkRed;
                }
        }
        return -1;
    }

    /**
     * @return Icon Color
     */
    public static int getIconColor() {
        if (getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
            return AppBase.getContext().getResources().getColor(R.color.iconDark);
        } else {
            return AppBase.getContext().getResources().getColor(R.color.iconLight);
        }
    }

    /**
     * @return Icon Tint Color
     */
    public static int getTintColor() {
        if (getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
            return AppBase.getContext().getResources().getColor(R.color.tintColorLight);
        } else {
            return AppBase.getContext().getResources().getColor(R.color.tintColorDark);
        }
    }

    /**
     * @return Background color
     */
    public static int getBackgroundColor() {
        if (getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
            return AppBase.getContext().getResources().getColor(R.color.backgroundLight);
        } else {
            return AppBase.getContext().getResources().getColor(R.color.backgroundDark);
        }
    }

    /**
     * @return Background Grey color
     */
    public static int getBackgroundGreyColor() {
        return AppBase.getContext().getResources().getColor(R.color.backgroundGrey);
    }

    /**
     * @return Primary Color
     */
    public static int getPrimaryColor() {
        return decodeAttrColor(R.attr.colorPrimary);
    }

    /**
     * @return Primary Dark Color
     */
    public static int getPrimaryDarkColor() {
        return decodeAttrColor(R.attr.colorPrimaryDark);
    }

    /**
     * @return Primary Light Color
     */
    public static int getPrimaryLightColor() {
        return decodeAttrColor(R.attr.colorPrimaryLight);
    }

    /**
     * @return Accent Color
     */
    public static int getAccentColor() {
        return decodeAttrColor(R.attr.colorAccent);
    }

    /**
     * Decode a color attribute
     *
     * @param attribute Color Attribute
     * @return Decoded Color
     */
    public static int decodeAttrColor(final int attribute) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = AppBase.getContext().obtainStyledAttributes(typedValue.data, new int[]{attribute});
        ColorStateList stateList = a.getColorStateList(0);
        a.recycle();
        if (stateList == null) {
            return -1;
        }
        return stateList.getDefaultColor();
    }

    /**
     * Primary app themes
     */
    public enum AppTheme {
        DARK,
        LIGHT;

        /**
         * @return Style
         */
        public final int getStyle() {
            switch (this) {
                case LIGHT:
                    return R.style.AppThemeLight;
                case DARK:
                    return R.style.AppThemeDark;
                default:
                    return R.style.AppThemeLight;
            }
        }
    }

    /**
     * Toolbar themes
     */
    public enum ToolbarTheme {
        DARK_TEXT,
        LIGHT_TEXT;

        /**
         * @return Style
         */
        public final int getStyle() {
            switch (getAppTheme()) {
                case LIGHT:
                    switch (this) {
                        case LIGHT_TEXT:
                            return R.style.toolbarLight;
                        case DARK_TEXT:
                            return R.style.toolbarLightDarkText;
                    }
                case DARK:
                    switch (this) {
                        case LIGHT_TEXT:
                            return R.style.toolbarDark;
                        case DARK_TEXT:
                            return R.style.toolbarDarkDarkText;
                    }
            }
            return -1;
        }
    }

    /**
     * Accent colors
     */
    public enum AccentColorTheme {
        RED, PINK, PURPLE, DEEP_PURPLE, INDIGO, BLUE, LIGHT_BLUE, CYAN,
        TEAL, GREEN, LIGHT_GREEN, LIME, YELLOW, AMBER, ORANGE, DEEP_ORANGE
    }

    /**
     * Primary colors
     */
    public enum PrimaryColorTheme {
        RED, PINK, PURPLE, DEEP_PURPLE, INDIGO, BLUE, LIGHT_BLUE, CYAN,
        TEAL, GREEN, LIGHT_GREEN, LIME, YELLOW, AMBER, ORANGE, DEEP_ORANGE,
        BROWN, GREY, BLUE_GREY, DARK_GREY, WHITE
    }

}
