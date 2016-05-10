package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.UtilsAppBase;
import com.jamesmorrisstudios.appbaselibrary.UtilsDisplay;
import com.jamesmorrisstudios.appbaselibrary.UtilsRingtone;
import com.jamesmorrisstudios.appbaselibrary.UtilsTheme;
import com.jamesmorrisstudios.appbaselibrary.UtilsVersion;
import com.jamesmorrisstudios.appbaselibrary.activities.CustomFilePickerActivity;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.ColorPickerRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.DualSpinnerRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.EditSoundListRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.EditTextListRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.EditTextRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.EditTimesListRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.FileBrowserRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.ImageRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.MultiChoiceRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.MultiDatePickerRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.PromptDialogRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.ReleaseNotesDialogRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.RingtoneRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.SingleChoiceIconRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.SingleChoiceRadioIconRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.SingleChoiceRadioRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.SingleChoiceRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.SingleDatePickerRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.TimePickerRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.VibratePatternRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogs.DatePickerMultiDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.dialogs.DualSpinnerDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.dialogs.EditSoundListDialog;
import com.jamesmorrisstudios.appbaselibrary.dialogs.EditTextDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.dialogs.EditTextListDialog;
import com.jamesmorrisstudios.appbaselibrary.dialogs.EditTimesListDialog;
import com.jamesmorrisstudios.appbaselibrary.dialogs.ImageDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.dialogs.ReleaseNotesDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.dialogs.SingleChoiceIconDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.dialogs.SingleChoiceRadioIconDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.dialogs.VibratePatternDialog;
import com.jamesmorrisstudios.appbaselibrary.items.IconItem;
import com.jamesmorrisstudios.appbaselibrary.items.RingtoneItem;
import com.jamesmorrisstudios.appbaselibrary.sound.SoundInstant;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.squareup.otto.Subscribe;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;

/**
 * Custom dialog build calls
 * <p/>
 * Created by James on 12/7/2015.
 */
public final class DialogBuildManager extends BaseBuildManager {

    /**
     * Not Called Directly
     * Creates a file browser dialog
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onFileBrowserRequest(@NonNull final FileBrowserRequest request) {
        Intent i = new Intent(AppBase.getContext(), CustomFilePickerActivity.class);
        i.putExtra(CustomFilePickerActivity.EXTRA_EXTENSION, request.extension);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, request.allowMultiSelect);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, request.allowCreateDir);
        if (request.dirType == FileBrowserRequest.DirType.DIRECTORY) {
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
        } else {
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        }
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.LIGHT) {
            if (UtilsTheme.getToolbarTheme() == UtilsTheme.ToolbarTheme.LIGHT_TEXT) {
                i.putExtra(CustomFilePickerActivity.EXTRA_THEME, R.style.FilePickerThemeToolLight);
            } else {
                i.putExtra(CustomFilePickerActivity.EXTRA_THEME, R.style.FilePickerThemeToolDark);
            }
        } else {
            if (UtilsTheme.getToolbarTheme() == UtilsTheme.ToolbarTheme.LIGHT_TEXT) {
                i.putExtra(CustomFilePickerActivity.EXTRA_THEME, R.style.FilePickerThemeDarkToolLight);
            } else {
                i.putExtra(CustomFilePickerActivity.EXTRA_THEME, R.style.FilePickerThemeDarkToolDark);
            }
        }
        i.putExtra(CustomFilePickerActivity.EXTRA_THEME_PRIMARY, UtilsTheme.getPrimaryColorStyle());
        i.putExtra(CustomFilePickerActivity.EXTRA_THEME_ACCENT, UtilsTheme.getAccentColorStyle());
        new StartActivityForResultRequest(i, new StartActivityForResultRequest.OnStartActivityForResultListener() {
            @Override
            public void resultOk(Intent intent) {
                if (request.allowMultiSelect) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ClipData clip = intent.getClipData();
                        if (clip != null) {
                            ArrayList<Uri> uris = new ArrayList<>();
                            for (int i = 0; i < clip.getItemCount(); i++) {
                                uris.add(clip.getItemAt(i).getUri());
                            }
                            request.onFileBrowserRequestListener.multiItemSelected(uris);
                        }
                    } else {
                        ArrayList<String> paths = intent.getStringArrayListExtra(FilePickerActivity.EXTRA_PATHS);
                        if (paths != null) {
                            ArrayList<Uri> uris = new ArrayList<>();
                            for (String path : paths) {
                                uris.add(Uri.parse(path));
                            }
                            request.onFileBrowserRequestListener.multiItemSelected(uris);
                        }
                    }
                    request.onFileBrowserRequestListener.canceled();
                } else {
                    request.onFileBrowserRequestListener.itemSelected(intent.getData());
                }
            }

            @Override
            public void resultFailed() {
                request.onFileBrowserRequestListener.canceled();
            }
        }).execute();
    }

    /**
     * Not Called Directly
     * Creates a ringtone dialog.
     * Depending upon the user setting "AppBaseUtils.useCustomRingtonePicker()" this may be the built in or system dialog.
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onRingtoneRequest(@NonNull final RingtoneRequest request) {
        new PermissionRequest(PermissionRequest.AppPermission.READ_EXTERNAL_STORAGE, new PermissionRequest.OnPermissionRequestListener() {
            @Override
            public void permissionGranted() {
                Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                if (request.currentTone != null) {
                    defaultUri = request.currentTone;
                }
                if (!UtilsAppBase.useCustomRingtonePicker()) {
                    //We are using the system ringtone picker
                    Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, request.title);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, defaultUri);
                    new StartActivityForResultRequest(intent, new StartActivityForResultRequest.OnStartActivityForResultListener() {
                        @Override
                        public void resultOk(@Nullable final Intent intent) {
                            String name = null;
                            Uri uri = null;
                            if (intent != null) {
                                uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                                if (uri != null) {
                                    Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), uri);
                                    if (ringtone != null) {
                                        name = ringtone.getTitle(getActivity());
                                    }
                                }
                            }
                            request.listener.ringtoneResponse(uri, name);
                        }

                        @Override
                        public void resultFailed() {

                        }
                    }).execute();
                } else {
                    //We are using the custom ringtone picker
                    final ArrayList<RingtoneItem> ringtones = UtilsRingtone.getRingtones(UtilsRingtone.RingtoneType.NOTIFICATION);
                    ringtones.add(0, new RingtoneItem(UtilsRingtone.RingtoneType.NOTIFICATION, getActivity().getString(R.string.none)));
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    ringtones.add(0, new RingtoneItem(UtilsRingtone.RingtoneType.NOTIFICATION, getActivity().getString(R.string.default_), uri));
                    //Set the selected item
                    int selectedItem = 1;
                    for (int i = 0; i < ringtones.size(); i++) {
                        if (ringtones.get(i).uri == null) {
                            continue;
                        }
                        if (ringtones.get(i).uri.equals(defaultUri)) {
                            ringtones.get(i).selected = true;
                            selectedItem = i;
                        }
                    }
                    String dialogTitle = getActivity().getString(R.string.select_notification);
                    String[] items = new String[ringtones.size()];
                    for (int i = 0; i < items.length; i++) {
                        items[i] = ringtones.get(i).name;
                    }
                    new SingleChoiceRadioRequest(dialogTitle, items, selectedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Selection changed
                            UtilsRingtone.ringtoneCancel();
                            if (ringtones.get(which).uri != null) {
                                SoundInstant.getInstance().loadAndPlayNotification(ringtones.get(which).uri);
                            }
                            for (int i = 0; i < ringtones.size(); i++) {
                                ringtones.get(i).selected = i == which;
                            }
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Confirmed
                            SoundInstant.getInstance().stopPlayback();
                            for (int i = 0; i < ringtones.size(); i++) {
                                if (ringtones.get(i).selected) {
                                    request.listener.ringtoneResponse(ringtones.get(i).uri, ringtones.get(i).name);
                                }
                            }
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Canceled
                            SoundInstant.getInstance().stopPlayback();
                        }
                    }).show();
                }
            }

            @Override
            public void permissionDenied() {
                UtilsVersion.showPermDeniedSnackbar(false);
            }

            @Override
            public void shouldShowRationale() {
                UtilsVersion.showPermDeniedSnackbar(true);
            }
        }).execute();
    }

    /**
     * Not Called Directly
     * Creates a color picker dialog
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onColorPickerRequest(@NonNull final ColorPickerRequest request) {
        ColorPickerDialogBuilder builder = ColorPickerDialogBuilder.with(getActivity(), UtilsTheme.getAlertDialogStyle())
                .setTitle(getActivity().getResources().getString(R.string.choose_color))
                .initialColor(request.initialColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(6)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //Unused as the onPositive call gives the same info
                    }
                })
                .showLightnessSlider(request.showLightnessSlider)
                .showAlphaSlider(false)
                .setPositiveButton(getActivity().getResources().getString(R.string.okay), request.onColorPickerClickListener)
                .setNegativeButton(getActivity().getResources().getString(R.string.cancel), request.onNegative);
        if (request.onDisable != null) {
            builder.setNeutralButton(getActivity().getResources().getString(R.string.disable), request.onDisable);
        }
        builder.build().show();
    }

    /**
     * Not Called Directly
     * Creates a simple prompt dialog
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onPromptDialogRequest(@NonNull final PromptDialogRequest request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), UtilsTheme.getAlertDialogStyle())
                .setTitle(request.title)
                .setMessage(request.content);
        if (request.onPositive != null) {
            if (request.positiveText != null) {
                builder.setPositiveButton(request.positiveText, request.onPositive);
            } else {
                builder.setPositiveButton(R.string.okay, request.onPositive);
            }
        }
        if (request.onNegative != null) {
            if (request.negativeText != null) {
                builder.setNegativeButton(request.negativeText, request.onNegative);
            } else {
                builder.setNegativeButton(R.string.cancel, request.onNegative);
            }
        }
        if (request.dismissListener != null) {
            builder.setOnDismissListener(request.dismissListener);
        }
        builder.show();
    }

    /**
     * Not Called Directly
     * Creates a time picker dialog
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onTimePickerDialogRequest(@NonNull final TimePickerRequest request) {
        TimePickerDialog time = new TimePickerDialog();
        time.initialize(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(@NonNull final RadialPickerLayout view, final int hourOfDay, final int minute, final int second) {
                request.listener.onTimeSet(hourOfDay, minute);
            }
        }, request.timeItem.hour, request.timeItem.minute, 0, request.timeItem.is24Hour());
        if (UtilsTheme.getAppTheme() == UtilsTheme.AppTheme.DARK) {
            time.setThemeDark(true);
        }
        time.enableSeconds(false);
        time.vibrate(false);
        time.dismissOnPause(true);
        time.setAccentColor(UtilsTheme.getAccentColor());
        time.show(getActivity().getFragmentManager(), "TimePickerDialog");
    }

    /**
     * Not Called Directly
     * Creates a single date picker dialog
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onSingleDatePickerDialogRequest(@NonNull final SingleDatePickerRequest request) {
        DatePickerMultiDialogBuilder.with(getActivity(), UtilsTheme.getAlertDialogStyle())
                .setDateRange(request.startDate, request.endDate)
                .setSelectedDate(request.selectedDate, request.singleListener)
                .build()
                .show();
    }

    /**
     * Not Called Directly
     * Creates a multi date picker dialog
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onMultiDatePickerDialogRequest(@NonNull final MultiDatePickerRequest request) {
        DatePickerMultiDialogBuilder.with(getActivity(), UtilsTheme.getAlertDialogStyle())
                .setDateRange(request.startDate, request.endDate)
                .setSelectedDates(request.selectedDates, request.multiListener)
                .build()
                .show();
    }

    /**
     * Not Called Directly
     * Creates a dual spinner dialog
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onDualSpinnerDialogRequest(@NonNull final DualSpinnerRequest request) {
        DualSpinnerDialogBuilder.with(getActivity(), UtilsTheme.getAlertDialogStyle())
                .setTitle(request.title)
                .setFirst(request.first, request.firstSelected, request.firstRestrictingSecond)
                .setSecond(request.second, request.secondSelected, request.secondRestrictingFirst)
                .setListener(request.listener)
                .build()
                .show();
    }

    /**
     * Not Called Directly
     * Creates a edit text list dialog
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onEditTextListRequest(@NonNull final EditTextListRequest request) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        EditTextListDialog editTextListDialog = new EditTextListDialog();
        editTextListDialog.setData(request.title, request.messages, request.onPositive, request.onNegative);
        editTextListDialog.show(fm, "EditTextListDialog");
    }

    /**
     * Not Called Directly
     * Creates a edit sound list dialog
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onEditSoundListRequest(@NonNull final EditSoundListRequest request) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        EditSoundListDialog dialog = new EditSoundListDialog();
        dialog.setData(request.title, request.freeLimit, request.items, request.onPositive, request.onNegative);
        dialog.show(fm, "EditSoundListDialog");
    }

    /**
     * Not Called Directly
     * Creates an edit times list dialog
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onEditTimesListRequest(@NonNull final EditTimesListRequest request) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        EditTimesListDialog editTimesListDialog = new EditTimesListDialog();
        editTimesListDialog.setData(request.title, request.times, request.onPositive, request.onNegative);
        editTimesListDialog.show(fm, "EditTimesDialog");
    }

    /**
     * Not Called Directly
     * Creates a single choice request.
     * Selecting an item instantly picks it (no confirm button).
     * Can execute text only or text plus a left aligned icon.
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onSingleChoiceRequest(@NonNull final SingleChoiceRequest request) {
        if (request.iconItems != null) {
            ListAdapter adapter = new ArrayAdapter<IconItem>(getActivity(), R.layout.dialog_helper_item_material, android.R.id.text1, request.iconItems) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    TextView tv = (TextView) v.findViewById(android.R.id.text1);
                    tv.setCompoundDrawablesWithIntrinsicBounds(request.iconItems[position].icon, 0, 0, 0);
                    tv.setCompoundDrawablePadding(UtilsDisplay.getDipInt(5));
                    return v;
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), UtilsTheme.getAlertDialogStyle())
                    .setCancelable(request.allowCancel)
                    .setTitle(request.title)
                    .setAdapter(adapter, request.clickListener);
            if (request.onNegative != null) {
                builder.setNegativeButton(R.string.cancel, request.onNegative);
            }
            builder.show();
        } else if (request.items != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), UtilsTheme.getAlertDialogStyle())
                    .setCancelable(request.allowCancel)
                    .setTitle(request.title)
                    .setItems(request.items, request.clickListener);
            if (request.onNegative != null) {
                builder.setNegativeButton(R.string.cancel, request.onNegative);
            }
            builder.show();
        }
    }

    /**
     * Not Called Directly
     * Creates a single choice dialog.
     * Options require selection and then confirmation. A callback provides interim selection status for preview usage.
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onSingleChoiceRadioRequest(@NonNull final SingleChoiceRadioRequest request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), UtilsTheme.getAlertDialogStyle())
                .setTitle(request.title)
                .setPositiveButton(R.string.okay, request.onPositive)
                .setSingleChoiceItems(request.items, request.defaultValue, request.clickListener);
        if (request.onNegative != null) {
            builder.setNegativeButton(R.string.cancel, request.onNegative);
        }
        builder.show();
    }

    /**
     * Not Called Directly
     * Creates a multi choice dialog with checkboxes.
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onMultiChoiceRadioRequest(@NonNull final MultiChoiceRequest request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), UtilsTheme.getAlertDialogStyle())
                .setTitle(request.title)
                .setPositiveButton(R.string.okay, request.onPositive)
                .setMultiChoiceItems(request.items, request.checkedItems, request.clickListener);
        if (request.onNegative != null) {
            builder.setNegativeButton(R.string.cancel, request.onNegative);
        }
        builder.show();
    }

    /**
     * Not Called Directly
     * Creates a single choice icon only dialog
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onSingleChoiceIconRequest(@NonNull final SingleChoiceIconRequest request) {
        SingleChoiceIconDialogBuilder builder = SingleChoiceIconDialogBuilder.with(getActivity(), UtilsTheme.getAlertDialogStyle())
                .setTitle(request.title)
                .setBackgroundColor(request.backgroundColor)
                .setOnOptionPicked(request.onOptionPickedListener);
        if (request.itemsIds != null) {
            builder.setItemsIds(request.itemsIds);
        }
        if (request.itemsUri != null) {
            builder.setItemsUri(request.itemsUri);
        }
        if (request.textNeutral != null && request.onNeutral != null) {
            builder.onNeutral(request.textNeutral, request.onNeutral);
        }
        builder.build().show();
    }

    /**
     * TODO incomplete
     * Not Called Directly
     * Creates a single choice radio button dialog.
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onSingleChoiceRadioIconRequest(@NonNull final SingleChoiceRadioIconRequest request) {
        SingleChoiceRadioIconDialogBuilder builder = SingleChoiceRadioIconDialogBuilder.with(getActivity(), UtilsTheme.getAlertDialogStyle())
                .setBackgroundColor(request.backgroundColor)
                .setTitle(request.title);
        if (request.itemsIds != null) {
            builder.setItemsIds(request.itemsIds);
        }
        if (request.itemsUri != null) {
            builder.setItemsUri(request.itemsUri);
        }
        builder.build().show();
    }

    /**
     * Not Called Directly
     * Creates a release notes dialog
     *
     * @param request Dialog request
     */
    @Subscribe
    public final void onReleastNotesDialogRequest(@NonNull final ReleaseNotesDialogRequest request) {
        String content = "";
        TypedArray array = getActivity().getResources().obtainTypedArray(R.array.current_release_notes);
        CharSequence[] data = array.getTextArray(array.getIndex(0));
        array.recycle();
        for (CharSequence item : data) {
            content += item.toString() + "\n\n";
        }
        ReleaseNotesDialogBuilder.with(getActivity(), UtilsTheme.getAlertDialogStyle())
                .setTitle(getActivity().getString(R.string.release_notes) + ": " + UtilsVersion.getVersionName() + " " + UtilsVersion.getVersionType())
                .setContent(content)
                .build()
                .show();
    }

    /**
     * Not Called Directly
     * Creates a vibrate pattern dialog
     *
     * @param request Dialog Request
     */
    @Subscribe
    public final void onVibratePatternRequest(@NonNull final VibratePatternRequest request) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        VibratePatternDialog dialog = new VibratePatternDialog();
        dialog.setData(request.title, request.pattern, request.listener);
        dialog.show(fm, "VibratePatternDialog");
    }

    /**
     * Not Called Directly
     * Creates an edit text prompt dialog
     *
     * @param request Dialog Request
     */
    @Subscribe
    public final void onEditTextRequest(@NonNull final EditTextRequest request) {
        EditTextDialogBuilder builder = EditTextDialogBuilder.with(getActivity(), UtilsTheme.getAlertDialogStyle())
                .setTitle(request.title)
                .setText(request.text, request.hint)
                .setListener(request.listener, AppBase.getContext().getString(R.string.okay), AppBase.getContext().getString(R.string.cancel));
        if (request.message != null) {
            builder.setMessage(request.message);
        }
        builder.build().show();
    }

    /**
     * Not Called Directly
     * Creates an image view dialog
     *
     * @param request Dialog Request
     */
    @Subscribe
    public final void onImageRequest(@NonNull final ImageRequest request) {
        ImageDialogBuilder builder = ImageDialogBuilder.with(getActivity(), UtilsTheme.getAlertDialogStyle())
                .setImage(request.imagePath);
        AlertDialog dialog = builder.build();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}
