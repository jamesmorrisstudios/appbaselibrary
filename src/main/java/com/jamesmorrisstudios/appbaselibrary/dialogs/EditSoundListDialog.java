package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.Bus;
import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.UtilsVersion;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.RingtoneRequest;
import com.jamesmorrisstudios.appbaselibrary.items.SoundItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 5/2/2016.
 */
public class EditSoundListDialog extends BaseDialogFragment {
    private ListView list;
    private ArrayList<SoundItem> items = null;
    private EditSoundListAdapter adapter = null;
    private String titleText;
    private EditSoundListListener onPositive;
    private View.OnClickListener onNegative;
    private int freeLimit;

    /**
     * @param inflater           Inflater
     * @param container          Container view
     * @param savedInstanceState Saved instance state
     * @return Dialog view
     */
    @Override
    @NonNull
    public final View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_sound_list, container);
        list = (ListView) view.findViewById(R.id.list);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnAdd = (Button) view.findViewById(R.id.btn_neutral);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(titleText);
        if (items != null) {
            adapter = new EditSoundListAdapter(getActivity(), R.layout.dialog_edit_sound_list_item, items);
            list.setAdapter(adapter);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNegative.onClick(v);
                list.post(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                });
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPositive != null && adapter != null) {
                    onPositive.onPositive(adapter.getItems());
                }
                list.post(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                });
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UtilsVersion.isPro() && freeLimit != -1 && items.size() >= freeLimit) {
                    UtilsVersion.showProPopupLimited();
                } else {
                    Bus.postObject(new RingtoneRequest(null, AppBase.getContext().getResources().getString(R.string.select_notification), new RingtoneRequest.RingtoneRequestListener() {
                        @Override
                        public final void ringtoneResponse(@Nullable final Uri uri, @Nullable final String name) {
                            if(uri != null && name != null) {
                                adapter.add(new SoundItem(uri, name));
                            }
                        }
                    }));
                }
            }
        });
        return view;
    }

    /**
     * @param titleText  Dialog title
     * @param items   List of sound items
     * @param onPositive onPositive
     * @param onNegative onNegative
     */
    public final void setData(@NonNull final String titleText, int freeLimit, @NonNull final ArrayList<SoundItem> items, @NonNull final EditSoundListListener onPositive, @Nullable final View.OnClickListener onNegative) {
        this.titleText = titleText;
        this.freeLimit = freeLimit;
        this.items = new ArrayList<>(items);
        this.onPositive = onPositive;
        this.onNegative = onNegative;
    }


    /**
     * Edit text list listener
     */
    public interface EditSoundListListener {

        /**
         * @param messages list of sound items
         */
        void onPositive(@NonNull final ArrayList<SoundItem> messages);
    }

    /**
     * List adapter
     */
    private class EditSoundListAdapter extends ArrayAdapter<SoundItem> {

        /**
         * @param context  Context
         * @param resource Row layout id
         * @param items    List of items
         */
        public EditSoundListAdapter(@NonNull final Context context, final int resource, @NonNull final List<SoundItem> items) {
            super(context, resource, items);
        }

        /**
         * @return List of String items
         */
        @NonNull
        public ArrayList<SoundItem> getItems() {
            ArrayList<SoundItem> wrapList = new ArrayList<>();
            for (int i = 0; i < getCount(); i++) {
                wrapList.add(getItem(i));
            }
            return wrapList;
        }

        /**
         * @param position    Position of view
         * @param convertView Recycled view
         * @param parent      Parent view
         * @return Dialog view
         */
        @Override
        @NonNull
        public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
            final SoundItem item = getItem(position);
            TextView text;
            ImageView delete;
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_sound_list_item, null);
            }
            text = (TextView) view.findViewById(R.id.text1);
            delete = (ImageView) view.findViewById(R.id.delete1);
            for (int i = 0; i < getCount(); i++) {
                delete.setOnClickListener(null);
            }
            if (item != null) {
                text.setText(item.name);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bus.postObject(new RingtoneRequest(item.getUri(), AppBase.getContext().getResources().getString(R.string.select_notification), new RingtoneRequest.RingtoneRequestListener() {
                            @Override
                            public final void ringtoneResponse(@Nullable final Uri uri, @Nullable final String name) {
                                if(uri != null && name != null) {
                                    item.set(uri, name);
                                    notifyDataSetChanged();
                                }
                            }
                        }));
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove(item);
                    }
                });
            }
            return view;
        }
    }
}
