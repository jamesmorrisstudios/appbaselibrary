package com.jamesmorrisstudios.appbaselibrary;

import android.animation.Animator;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.jamesmorrisstudios.appbaselibrary.animator.AnimatorControl;
import com.jamesmorrisstudios.appbaselibrary.animator.AnimatorEndListener;
import com.jamesmorrisstudios.appbaselibrary.animator.AnimatorStartListener;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.math.vectors.IntVector2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Display orientated display functions
 * <p/>
 * Created by James on 12/8/2015.
 */
public final class UtilsDisplay {

    /**
     * Gets the screensize as an intVector
     * This should work with all devices android 4.0+
     *
     * @return The screensize
     */
    @NonNull
    public static IntVector2 getDisplaySize() {
        int width = 0, height = 0;
        final DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) AppBase.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Method mGetRawH, mGetRawW;
        try {
            // For JellyBean 4.2 (API 17) and onward
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealMetrics(metrics);

                width = metrics.widthPixels;
                height = metrics.heightPixels;
            } else {
                mGetRawH = Display.class.getMethod("getRawHeight");
                mGetRawW = Display.class.getMethod("getRawWidth");

                try {
                    width = (Integer) mGetRawW.invoke(display);
                    height = (Integer) mGetRawH.invoke(display);
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        }
        return new IntVector2(width, height);
    }

    /**
     * Gets the screensize as if the device is in portrait view.
     * If in portrait this and getDisplaySize will return the same thing.
     *
     * @return The screensize formatted as if in portrait
     */
    @NonNull
    public static IntVector2 getDisplaySizeAsPortrait() {
        IntVector2 size = getDisplaySize();
        if (size.y >= size.x) {
            return size;
        } else {
            int x = size.x;
            size.x = size.y;
            size.y = x;
            return size;
        }
    }

    /**
     * Converts a dip value into a pixel value
     * Usually you want to use getDipInt unless you are performing additional calculations with the result.
     *
     * @param dp Dip value
     * @return Pixel value
     */
    public static float getDip(final int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, AppBase.getContext().getResources().getDisplayMetrics());
    }

    /**
     * Converts a dip value into a pixel value rounded to the nearest int
     * This is typically the desired choice as pixels are only in ints
     *
     * @param dip Dip value
     * @return Pixel value
     */
    public static int getDipInt(final int dip) {
        return Math.round(getDip(dip));
    }

    /**
     * Gets the current device orientation.
     * There are some reports that this may return the wrong result on some devices
     * but I have not found any yet. I may update this will a fallback screensize check
     *
     * @return The device orientation
     */
    @NonNull
    public static Orientation getOrientation() {
        switch (AppBase.getContext().getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_UNDEFINED:
                return Orientation.UNDEFINED;
            case Configuration.ORIENTATION_PORTRAIT:
                return Orientation.PORTRAIT;
            case Configuration.ORIENTATION_LANDSCAPE:
                return Orientation.LANDSCAPE;
            default:
                return Orientation.UNDEFINED;
        }
    }

    /**
     * Gets the screen size bucket category
     *
     * @return The screensize
     */
    @NonNull
    public static ScreenSize getScreenSize() {
        int screenLayout = AppBase.getContext().getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;
        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return ScreenSize.SMALL;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return ScreenSize.NORMAL;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return ScreenSize.LARGE;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                return ScreenSize.XLARGE;
            default:
                return ScreenSize.UNDEFINED;
        }

    }

    /**
     * Gets the height of the status bar if visible.
     * Must pass in if immersive is enabled as that effects the height.
     *
     * @param immersiveEnabled If immersive mode is enabled
     * @return The height of the status bar in pixels.
     */
    public static int getStatusHeight(final boolean immersiveEnabled) {
        if (immersiveEnabled) {
            return 0;
        } else {
            Resources resources = AppBase.getContext().getResources();
            int result = 0;
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId);
            }
            return result;
        }
    }

    /**
     * @return Action bar height
     */
    public static int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        if (AppBase.getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, AppBase.getContext().getResources().getDisplayMetrics());
        }
        return 0;
    }

    /**
     * Gets the height of the navigation bar
     *
     * @param immersiveEnabled If immersive mode is enabled
     * @return The height of the navigation bar in pixels.
     */
    public static int getNavigationHeight(final boolean immersiveEnabled) {
        if (immersiveEnabled) {
            return 0;
        } else {
            Resources resources = AppBase.getContext().getResources();
            int id = resources.getIdentifier(
                    getOrientation() == Orientation.PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape",
                    "dimen", "android");
            if (id > 0) {
                return resources.getDimensionPixelSize(id);
            }
            return 0;
        }
    }

    /**
     * Gets the location of the navigation bar.
     * Note that not all devices have an onscreen navigation bar.
     * This will report side for those without so always check the height of the bar before assuming its there.
     *
     * @return The location of the navigation bar
     */
    @NonNull
    public static NavigationBarLocation getNavigationBarLocation() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) AppBase.getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int viewHeight = displaymetrics.heightPixels;
        if (getDisplaySize().y != viewHeight) {
            return NavigationBarLocation.BOTTOM;
        } else {
            return NavigationBarLocation.SIDE;
        }
    }

    /**
     * Locks the device window in the current mode.
     * If current mode is unspecificed this does nothing.
     */
    public static void lockOrientationCurrent(@NonNull final AppCompatActivity activity) {
        if (getOrientation() == Orientation.PORTRAIT) {
            lockOrientationPortrait(activity);
        } else if (getOrientation() == Orientation.LANDSCAPE) {
            lockOrientationLandscape(activity);
        }
    }

    /**
     * Locks the device window in landscape mode.
     */
    public static void lockOrientationLandscape(@NonNull final AppCompatActivity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * Locks the device window in portrait mode.
     */
    public static void lockOrientationPortrait(@NonNull final AppCompatActivity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Allows user to freely use portrait or landscape mode.
     */
    public static void unlockOrientation(@NonNull final AppCompatActivity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    /**
     * Gets the current orientation lock status. Undefined if allowed to rotate freely
     */
    @NonNull
    public static Orientation getOrientationLock(@NonNull final AppCompatActivity activity) {
        switch (activity.getRequestedOrientation()) {
            case ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED:
                return Orientation.UNDEFINED;
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                return Orientation.LANDSCAPE;
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                return Orientation.PORTRAIT;
            default:
                return Orientation.UNDEFINED;
        }
    }

    /**
     * Hides the splashscreen
     *
     * @param splashScreen Splashscreen view
     * @param forceInstant True to force instant, false to animate
     */
    public static void hideSplashScreen(@NonNull final View splashScreen, final boolean forceInstant) {
        if (forceInstant || !AppBase.getInstance().isColdLaunch()) {
            splashScreen.setVisibility(View.GONE);
        } else if (splashScreen.getVisibility() == View.VISIBLE) {
            AnimatorControl.alphaAutoStart(splashScreen, 1.0f, 0.0f, 1000, 1000, AnimatorControl.InterpolatorType.ACCELERATE, new AnimatorEndListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    splashScreen.setVisibility(View.GONE);
                }
            });
        }
        AppBase.getInstance().setHasLaunched();
    }

    /**
     * Toggles showing the toolbar
     *
     * @param toolbar Toolbar view
     * @param show    True to show, false to hide
     * @param instant True for instant, false to animate
     */
    public static void toggleShowToolbar(@NonNull final View toolbar, final boolean show, final boolean instant) {
        if (instant) {
            if (show) {
                toolbar.setVisibility(View.VISIBLE);
            } else {
                toolbar.setVisibility(View.GONE);
            }
        } else {
            if (show) {
                AnimatorControl.translateYAutoStart(toolbar, -toolbar.getHeight(), 0, 100, 0, AnimatorControl.InterpolatorType.ACCELERLATE_DECELERATE, new AnimatorStartListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        toolbar.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                AnimatorControl.translateYAutoStart(toolbar, 0, -toolbar.getHeight(), 100, 0, AnimatorControl.InterpolatorType.ACCELERLATE_DECELERATE, new AnimatorEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toolbar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    /**
     * Toggles setting the toolbar to overlay mode
     *
     * @param container Toolbar's parent viewGroup
     * @param theme     Activity Theme
     * @param enable    True to enable overlay, false to disable overlay
     */
    /*
    public static void toggleToolbarOverlay(@NonNull ViewGroup container, @NonNull Resources.Theme theme, boolean enable) {
        if (enable) {
            //Padding to 0
            container.setPadding(container.getPaddingLeft(), 0, container.getPaddingTop(), container.getPaddingBottom());
        } else {
            //Padding to actionbarsize
            final TypedArray styledAttributes = theme.obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
            int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
            styledAttributes.recycle();
            container.setPadding(container.getPaddingLeft(), mActionBarSize, container.getPaddingRight(), container.getPaddingBottom());
        }
    }
    */

    /**
     * Reset immersive mode back to the user set value. This will set it to the opposite value and then back.
     * @param activity Activity
     */
    public static void resetImmersiveMode(@NonNull final AppCompatActivity activity, final boolean hasFocus) {
        if(UtilsAppBase.useImmersiveMode()) {
            updateImmersiveMode(activity, hasFocus, false);
            updateImmersiveMode(activity, hasFocus, true);
        } else {
            updateImmersiveMode(activity, hasFocus, true);
            updateImmersiveMode(activity, hasFocus, false);
        }
    }

    /**
     * Update Immersive mode for the given activity to the user set value
     *
     * @param activity Activity
     * @param hasFocus True if the app has focus, false if not
     */
    public static void updateImmersiveMode(@NonNull final AppCompatActivity activity, final boolean hasFocus) {
        updateImmersiveMode(activity, hasFocus, UtilsAppBase.useImmersiveMode());
    }

    /**
     * Update Immersive mode for the given activity
     *
     * @param activity Activity
     * @param hasFocus True if the app has focus, false if not
     * @param useImmersiveMode true to enable immersive mode and false to disable.
     */
    public static void updateImmersiveMode(@NonNull final AppCompatActivity activity, final boolean hasFocus, boolean useImmersiveMode) {
        int newUiOptions = 0;
        if (useImmersiveMode) {
            if (hasFocus) {
                if (Build.VERSION.SDK_INT >= 16) {
                    newUiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                }
                if (Build.VERSION.SDK_INT >= 19) {
                    newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                }
                activity.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        } else {
            if (hasFocus) {
                if (Build.VERSION.SDK_INT >= 16) {
                    newUiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                }
                activity.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        }
    }

    /**
     * Device orientations
     */
    public enum Orientation {
        UNDEFINED, PORTRAIT, LANDSCAPE
    }

    /**
     * Android defined screen size categories
     */
    public enum ScreenSize {
        SMALL, NORMAL, LARGE, XLARGE, UNDEFINED
    }

    /**
     * Location of the navigation bar.
     */
    public enum NavigationBarLocation {
        BOTTOM, SIDE
    }

}
