package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.AppBaseUtils;
import com.jamesmorrisstudios.appbaselibrary.AutoLockOrientation;
import com.jamesmorrisstudios.appbaselibrary.Bus;
import com.jamesmorrisstudios.appbaselibrary.IconItem;
import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.RingtoneItem;
import com.jamesmorrisstudios.appbaselibrary.ThemeManager;
import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.activities.CustomFilePickerActivity;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.colorpicker.ColorPickerView;
import com.jamesmorrisstudios.appbaselibrary.colorpicker.OnColorSelectedListener;
import com.jamesmorrisstudios.appbaselibrary.colorpicker.builder.ColorPickerDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.ColorPickerRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.DualSpinnerRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.EditTextListRequest;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.FileBrowserRequest;
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
import com.jamesmorrisstudios.appbaselibrary.dialogs.DatePickerMultiDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.dialogs.DualSpinnerDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.dialogs.EditTextListDialog;
import com.jamesmorrisstudios.appbaselibrary.dialogs.ReleaseNotesDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.dialogs.SingleChoiceIconDialogBuilder;
import com.jamesmorrisstudios.appbaselibrary.dialogs.SingleChoiceRadioIconDialogBuilder;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.squareup.otto.Subscribe;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;

/**
 * Created by James on 12/7/2015.
 */
public class DialogBuildManager extends BaseBuildManager {

    @Subscribe
    public void onFileBrowserRequest(@NonNull final FileBrowserRequest request) {
        Intent i = new Intent(AppBase.getContext(), CustomFilePickerActivity.class);
        i.putExtra(CustomFilePickerActivity.EXTRA_EXTENSION, request.extension);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false); //Not supported on the return yet
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, request.allowCreateDir);
        if (request.dirType == FileBrowserRequest.DirType.DIRECTORY) {
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
        } else {
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        }
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

        if(ThemeManager.getAppTheme() == ThemeManager.AppTheme.LIGHT) {
            i.putExtra(CustomFilePickerActivity.EXTRA_THEME, R.style.FilePickerTheme);
        } else {
            i.putExtra(CustomFilePickerActivity.EXTRA_THEME, R.style.FilePickerThemeDark);
        }

        i.putExtra(CustomFilePickerActivity.EXTRA_THEME_PRIMARY, ThemeManager.getPrimaryColorStyle());
        i.putExtra(CustomFilePickerActivity.EXTRA_THEME_ACCENT, ThemeManager.getAccentColorStyle());

        Bus.postObject(new StartActivityForResultRequest(i, new StartActivityForResultRequest.OnStartActivityForResultListener() {
            @Override
            public void resultOk(Intent intent) {
                request.fileBrowserRequestListener.path(intent.getData());
            }

            @Override
            public void resultFailed() {

            }
        }));
    }

    @Subscribe
    public void onRingtoneRequest(@NonNull final RingtoneRequest request) {
        Bus.postObject(new PermissionRequest(PermissionRequest.AppPermission.READ_EXTERNAL_STORAGE, new PermissionRequest.OnRequestPermissionListener() {
            @Override
            public void permissionGranted() {
                Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                if (request.currentTone != null) {
                    defaultUri = request.currentTone;
                }

                if (!AppBaseUtils.useCustomRingtonePicker()) {
                    //We are using the system ringtone picker
                    Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, request.title);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, defaultUri);

                    Bus.postObject(new StartActivityForResultRequest(intent, new StartActivityForResultRequest.OnStartActivityForResultListener() {
                        @Override
                        public void resultOk(Intent intent) {
                            String name = null;
                            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                            if (uri != null) {
                                Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), uri);
                                if (ringtone != null) {
                                    name = ringtone.getTitle(getActivity());
                                }
                            }
                            request.listener.ringtoneResponse(uri, name);
                        }

                        @Override
                        public void resultFailed() {

                        }
                    }));
                } else {
                    //We are using the custom ringtone picker
                    final ArrayList<RingtoneItem> ringtones = Utils.getRingtones(Utils.RingtoneType.NOTIFICATION);
                    ringtones.add(0, new RingtoneItem(Utils.RingtoneType.NOTIFICATION, getActivity().getString(R.string.none)));
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    ringtones.add(0, new RingtoneItem(Utils.RingtoneType.NOTIFICATION, getActivity().getString(R.string.default_), uri));
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
                    Bus.postObject(new SingleChoiceRadioRequest(dialogTitle, items, selectedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Selection changed
                            Utils.ringtoneCancel();
                            if (ringtones.get(which).uri != null) {
                                Utils.ringtonePlay(ringtones.get(which).uri);
                            }
                            for (int i = 0; i < ringtones.size(); i++) {
                                ringtones.get(i).selected = i == which;
                            }
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Confirmed
                            Utils.ringtoneCancel();
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
                            Utils.ringtoneCancel();
                        }
                    }));
                }
            }

            @Override
            public void permissionDenied() {

            }
        }));
    }

    @Subscribe
    public void onColorPickerRequest(@NonNull ColorPickerRequest request) {
        ColorPickerDialogBuilder builder = ColorPickerDialogBuilder.with(getActivity(), ThemeManager.getAlertDialogStyle())
                .setTitle(getActivity().getResources().getString(R.string.choose_color))
                .initialColor(request.initialColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .noSliders()
                .density(6)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //Unused as the onPositive call gives the same info
                    }
                })
                .setPositiveButton(getActivity().getResources().getString(R.string.okay), request.onColorPickerClickListener)
                .setNegativeButton(getActivity().getResources().getString(R.string.cancel), request.onNegative);
        if(request.onDisable != null) {
            builder.setNeutralButton(getActivity().getResources().getString(R.string.disable), request.onDisable);
        }
        builder.build().show();
    }

    @Subscribe
    public void onPromptDialogRequest(@NonNull PromptDialogRequest request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), ThemeManager.getAlertDialogStyle())
                .setTitle(request.title)
                .setMessage(request.content);
        if(request.positiveText != null) {
            builder.setPositiveButton(request.positiveText, request.onPositive);
        } else {
            builder.setPositiveButton(R.string.okay, request.onPositive);
        }
        if(request.negativeText != null) {
            builder.setNegativeButton(request.negativeText, request.onNegative);
        } else {
            builder.setNegativeButton(R.string.cancel, request.onNegative);
        }
        builder.show();
    }

    @Subscribe
    public void onTimePickerDialogRequest(@NonNull TimePickerRequest request) {
        TimePickerDialog time = new TimePickerDialog();
        time.initialize(request.onTimeSetListener, request.hour, request.minute, 0, request.is24Hour);
        if(ThemeManager.getAppTheme() == ThemeManager.AppTheme.DARK) {
            time.setThemeDark(true);
        }
        time.enableSeconds(false);
        time.vibrate(false);
        time.dismissOnPause(true);
        time.setAccentColor(ThemeManager.getAccentColor());
        time.show(getActivity().getFragmentManager(), "TimePickerDialog");
    }

    @Subscribe
    public void onSingleDatePickerDialogRequest(@NonNull SingleDatePickerRequest request) {
        DatePickerMultiDialogBuilder.with(getActivity(), ThemeManager.getAlertDialogStyle())
                .setDateRange(request.startDate,request. endDate)
                .setSelectedDate(request.selectedDate, request.singleListener)
                .build()
                .show();
    }

    @Subscribe
    public void onMultiDatePickerDialogRequest(@NonNull MultiDatePickerRequest request) {
        DatePickerMultiDialogBuilder.with(getActivity(), ThemeManager.getAlertDialogStyle())
                .setDateRange(request.startDate, request.endDate)
                .setSelectedDates(request.selectedDates, request.multiListener)
                .build()
                .show();
    }

    @Subscribe
    public void onDualSpinnerDialogRequest(@NonNull DualSpinnerRequest request) {
        DualSpinnerDialogBuilder.with(getActivity(), ThemeManager.getAlertDialogStyle())
                .setTitle(request.title)
                .setFirst(request.first, request.firstSelected, request.firstRestrictions)
                .setSecond(request.second, request.secondSelected, request.secondRestrictions)
                .setListener(request.listener)
                .build()
                .show();
    }

    @Subscribe
    public void onEditTextListRequest(@NonNull EditTextListRequest request) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        EditTextListDialog editTextListDialog = new EditTextListDialog();
        editTextListDialog.setData(request.title, request.messages, request.onPositive, request.onNegative);
        editTextListDialog.show(fm, "EditTextListDIalog");
    }

    @Subscribe
    public void onSingleChoiceRequest(@NonNull final SingleChoiceRequest request) {
        if(request.iconItems != null) {
            ListAdapter adapter = new ArrayAdapter<IconItem>(getActivity(), R.layout.dialog_item_material, android.R.id.text1, request.iconItems){
                public View getView(int position, View convertView, ViewGroup parent) {
                    //Use super class to create the View
                    View v = super.getView(position, convertView, parent);
                    TextView tv = (TextView)v.findViewById(android.R.id.text1);

                    //Put the image on the TextView
                    tv.setCompoundDrawablesWithIntrinsicBounds(request.iconItems[position].icon, 0, 0, 0);

                    //Add margin between image and text (support various screen densities)
                    int dp5 = (int) (5 * getActivity().getResources().getDisplayMetrics().density + 0.5f);
                    tv.setCompoundDrawablePadding(dp5);

                    return v;
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), ThemeManager.getAlertDialogStyle())
                    .setCancelable(request.allowCancel)
                    .setTitle(request.title)
                    .setAdapter(adapter, request.clickListener);
            if(request.onNegative != null) {
                builder.setNegativeButton(R.string.cancel, request.onNegative);
            }
            builder.show();
        } else if(request.items != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), ThemeManager.getAlertDialogStyle())
                    .setCancelable(request.allowCancel)
                    .setTitle(request.title)
                    .setItems(request.items, request.clickListener);
            if(request.onNegative != null) {
                builder.setNegativeButton(R.string.cancel, request.onNegative);
            }
            builder.show();
        }
    }

    @Subscribe
    public void onSingleChoiceRadioRequest(@NonNull SingleChoiceRadioRequest request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), ThemeManager.getAlertDialogStyle())
                .setTitle(request.title)
                .setPositiveButton(R.string.okay, request.onPositive)
                .setSingleChoiceItems(request.items, request.defaultValue, request.clickListener);
        if(request.onNegative != null) {
            builder.setNegativeButton(R.string.cancel, request.onNegative);
        }
        builder.show();
    }

    @Subscribe
    public void onMultiChoiceRadioRequest(@NonNull MultiChoiceRequest request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), ThemeManager.getAlertDialogStyle())
                .setTitle(request.title)
                .setPositiveButton(R.string.okay, request.onPositive)
                .setMultiChoiceItems(request.items, request.checkedItems, request.clickListener);
        if(request.onNegative != null) {
            builder.setNegativeButton(R.string.cancel, request.onNegative);
        }
        builder.show();
    }

    @Subscribe
    public void onSingleChoiceIconRequest(@NonNull SingleChoiceIconRequest request) {
        SingleChoiceIconDialogBuilder builder = SingleChoiceIconDialogBuilder.with(getActivity(), ThemeManager.getAlertDialogStyle())
                .setTitle(request.title)
                .setOnOptionPicked(request.onOptionPickedListener);
        if(request.itemsIds != null) {
            builder.setItemsIds(request.itemsIds);
        }
        if(request.itemsUri != null) {
            builder.setItemsUri(request.itemsUri);
        }
        if(request.textNeutral != null && request.onNeutral != null) {
            builder.onNeutral(request.textNeutral, request.onNeutral);
        }
        builder.build().show();
    }

    @Subscribe
    public void onSingleChoiceRadioIconRequest(@NonNull SingleChoiceRadioIconRequest request) {
        SingleChoiceRadioIconDialogBuilder builder = SingleChoiceRadioIconDialogBuilder.with(getActivity(), ThemeManager.getAlertDialogStyle())
                .setTitle(request.title);
        if(request.itemsIds != null) {
            builder.setItemsIds(request.itemsIds);
        }
        if(request.itemsUri != null) {
            builder.setItemsUri(request.itemsUri);
        }
        builder.build().show();
    }

    @Subscribe
    public void onReleastNotesDialogRequest(@NonNull ReleaseNotesDialogRequest request) {
        String content = "";
        TypedArray array = getActivity().getResources().obtainTypedArray(R.array.current_release_notes);
        CharSequence[] data = array.getTextArray(array.getIndex(0));
        array.recycle();
        for(CharSequence item : data) {
            content += item.toString() +"\n\n";
        }
        ReleaseNotesDialogBuilder.with(getActivity(), ThemeManager.getAlertDialogStyle())
                .setTitle(getActivity().getString(R.string.release_notes) + ": " + Utils.getVersionName()+" "+Utils.getVersionType())
                .setContent(content)
                .build()
                .show();
    }

}
