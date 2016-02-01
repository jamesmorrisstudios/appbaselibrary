package com.jamesmorrisstudios.appbaselibrary;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * Created by James on 1/20/2016.
 */
public class UtilsIcon {

    @DrawableRes
    public static int getIconAuto(@NonNull final Icon icon) {
        return getIcon(icon, IconColor.AUTO);
    }

    @DrawableRes
    public static int getIconWhite(@NonNull final Icon icon) {
        return getIcon(icon, IconColor.WHITE);
    }

    @DrawableRes
    public static int getIconBlack(@NonNull final Icon icon) {
        return getIcon(icon, IconColor.BLACK);
    }

    @DrawableRes
    public static int getIcon(@NonNull final Icon icon, @NonNull final IconColor iconColor) {
        switch (icon) {
            case ADD:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_add_white_24dp;
                        } else {
                            return R.drawable.ic_add_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_add_black_24dp;
                    case WHITE:
                        return R.drawable.ic_add_white_24dp;
                }
            case CHECK:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_check_white_24dp;
                        } else {
                            return R.drawable.ic_check_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_check_black_24dp;
                    case WHITE:
                        return R.drawable.ic_check_white_24dp;
                }
            case CLEAR:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_clear_white_24dp;
                        } else {
                            return R.drawable.ic_clear_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_clear_black_24dp;
                    case WHITE:
                        return R.drawable.ic_clear_white_24dp;
                }
            case CONTENT_COPY:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_content_copy_white_24dp;
                        } else {
                            return R.drawable.ic_content_copy_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_content_copy_black_24dp;
                    case WHITE:
                        return R.drawable.ic_content_copy_white_24dp;
                }
            case CREATE:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_create_white_24dp;
                        } else {
                            return R.drawable.ic_create_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_create_black_24dp;
                    case WHITE:
                        return R.drawable.ic_create_white_24dp;
                }
            case DELETE:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_delete_white_24dp;
                        } else {
                            return R.drawable.ic_delete_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_delete_black_24dp;
                    case WHITE:
                        return R.drawable.ic_delete_white_24dp;
                }
            case DO_NOT_DISTURB:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_do_not_disturb_white_24dp;
                        } else {
                            return R.drawable.ic_do_not_disturb_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_do_not_disturb_black_24dp;
                    case WHITE:
                        return R.drawable.ic_do_not_disturb_white_24dp;
                }
            case DRAFTS:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_drafts_white_24dp;
                        } else {
                            return R.drawable.ic_drafts_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_drafts_black_24dp;
                    case WHITE:
                        return R.drawable.ic_drafts_white_24dp;
                }
            case EVENT_AVAILABLE:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_event_available_white_24dp;
                        } else {
                            return R.drawable.ic_event_available_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_event_available_black_24dp;
                    case WHITE:
                        return R.drawable.ic_event_available_white_24dp;
                }
            case EVENT:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_event_white_24dp;
                        } else {
                            return R.drawable.ic_event_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_event_black_24dp;
                    case WHITE:
                        return R.drawable.ic_event_white_24dp;
                }
            case EVENT_BUSY:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_event_busy_white_24dp;
                        } else {
                            return R.drawable.ic_event_busy_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_event_busy_black_24dp;
                    case WHITE:
                        return R.drawable.ic_event_busy_white_24dp;
                }
            case HELP:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_help_white_24dp;
                        } else {
                            return R.drawable.ic_help_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_help_black_24dp;
                    case WHITE:
                        return R.drawable.ic_help_white_24dp;
                }
            case HOME:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_home_white_24dp;
                        } else {
                            return R.drawable.ic_home_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_home_black_24dp;
                    case WHITE:
                        return R.drawable.ic_home_white_24dp;
                }
            case IMPORT_EXPORT:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_import_export_white_24dp;
                        } else {
                            return R.drawable.ic_import_export_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_import_export_black_24dp;
                    case WHITE:
                        return R.drawable.ic_import_export_white_24dp;
                }
            case MAP:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_map_white_24dp;
                        } else {
                            return R.drawable.ic_map_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_map_black_24dp;
                    case WHITE:
                        return R.drawable.ic_map_white_24dp;
                }
            case MY_LOCATION:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_my_location_white_24dp;
                        } else {
                            return R.drawable.ic_my_location_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_my_location_black_24dp;
                    case WHITE:
                        return R.drawable.ic_my_location_white_24dp;
                }
            case NOTIFICATIONS_OFF:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_notifications_off_white_24dp;
                        } else {
                            return R.drawable.ic_notifications_off_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_notifications_off_black_24dp;
                    case WHITE:
                        return R.drawable.ic_notifications_off_white_24dp;
                }
            case NOTIFICATIONS_ON:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_notifications_on_white_24dp;
                        } else {
                            return R.drawable.ic_notifications_on_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_notifications_on_black_24dp;
                    case WHITE:
                        return R.drawable.ic_notifications_on_white_24dp;
                }
            case PLAY_ARROW:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_play_arrow_white_24dp;
                        } else {
                            return R.drawable.ic_play_arrow_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_play_arrow_black_24dp;
                    case WHITE:
                        return R.drawable.ic_play_arrow_white_24dp;
                }
            case PRO:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_pro_white_24dp;
                        } else {
                            return R.drawable.ic_pro_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_pro_black_24dp;
                    case WHITE:
                        return R.drawable.ic_pro_white_24dp;
                }
            case SAVE:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_save_white_24dp;
                        } else {
                            return R.drawable.ic_save_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_save_black_24dp;
                    case WHITE:
                        return R.drawable.ic_save_white_24dp;
                }
            case SEARCH:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_search_white_24dp;
                        } else {
                            return R.drawable.ic_search_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_search_black_24dp;
                    case WHITE:
                        return R.drawable.ic_search_white_24dp;
                }
            case SETTINGS:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_settings_white_24dp;
                        } else {
                            return R.drawable.ic_settings_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_settings_black_24dp;
                    case WHITE:
                        return R.drawable.ic_settings_white_24dp;
                }
            case SHARE:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_share_white_24dp;
                        } else {
                            return R.drawable.ic_share_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_share_black_24dp;
                    case WHITE:
                        return R.drawable.ic_share_white_24dp;
                }
            case SNOOZE:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_snooze_white_24dp;
                        } else {
                            return R.drawable.ic_snooze_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_snooze_black_24dp;
                    case WHITE:
                        return R.drawable.ic_snooze_white_24dp;
                }
            case SWAP_HORIZ:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_swap_horiz_white_24dp;
                        } else {
                            return R.drawable.ic_swap_horiz_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_swap_horiz_black_24dp;
                    case WHITE:
                        return R.drawable.ic_swap_horiz_white_24dp;
                }
            case VIBRATION:
                switch (iconColor) {
                    case AUTO:
                        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
                            return R.drawable.ic_vibration_white_24dp;
                        } else {
                            return R.drawable.ic_vibration_black_24dp;
                        }
                    case BLACK:
                        return R.drawable.ic_vibration_black_24dp;
                    case WHITE:
                        return R.drawable.ic_vibration_white_24dp;
                }
        }
        return 0;
    }

    public enum Icon {
        ADD, CHECK, CLEAR, CONTENT_COPY, CREATE, DELETE, DO_NOT_DISTURB, DRAFTS, EVENT_AVAILABLE, EVENT,
        EVENT_BUSY, HELP, HOME, IMPORT_EXPORT, MAP, MY_LOCATION, NOTIFICATIONS_OFF, NOTIFICATIONS_ON,
        PLAY_ARROW, PRO, SAVE, SEARCH, SETTINGS, SHARE, SNOOZE, SWAP_HORIZ, VIBRATION
    }

    public enum IconColor {
        AUTO, BLACK, WHITE
    }

}
