package com.jamesmorrisstudios.appbaselibrary.dialogs;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import com.jamesmorrisstudios.appbaselibrary.dialogRequests.TimePickerRequest;
import com.jamesmorrisstudios.appbaselibrary.time.TimeItem;
import com.jamesmorrisstudios.appbaselibrary.time.UtilsTime;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Edit time list dialog
 * <p/>
 * Created by James on 7/9/2015.
 */
public final class EditTimesListDialog extends BaseDialogFragment {
    private ListView list;
    private ArrayList<TimeItem> times = null;
    private String titleText;
    private EditTimesListAdapter adapter = null;
    private EditTimesListListener onPositive;
    private View.OnClickListener onNegative;

    /**
     * @param inflater           Inflater
     * @param container          Container view
     * @param savedInstanceState Saved instance state
     * @return Top view
     */
    @Override
    @NonNull
    public final View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_times_list, container);
        list = (ListView) view.findViewById(R.id.list);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnAdd = (Button) view.findViewById(R.id.btn_neutral);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(titleText);
        if (times != null) {
            adapter = new EditTimesListAdapter(getActivity(), R.layout.dialog_edit_times_list_item, times);
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
                adapter.add(new TimeItem(9, 0));
            }
        });
        return view;
    }

    /**
     * @param titleText  Title
     * @param times      List of times
     * @param onPositive onPositive
     * @param onNegative onNegative
     */
    public final void setData(@NonNull final String titleText, @NonNull final ArrayList<TimeItem> times, @NonNull final EditTimesListListener onPositive, @Nullable final View.OnClickListener onNegative) {
        this.titleText = titleText;
        this.times = new ArrayList<>(times);
        this.onPositive = onPositive;
        this.onNegative = onNegative;
    }

    /**
     * List adapter
     */
    class EditTimesListAdapter extends ArrayAdapter<TimeItem> {
        private int resourceId;

        /**
         * @param context  Context
         * @param resource Row resource id
         * @param items    List of items
         */
        public EditTimesListAdapter(@NonNull final Context context, final int resource, @NonNull final List<TimeItem> items) {
            super(context, resource, items);
            this.resourceId = resource;
        }

        /**
         * @return List of items
         */
        @NonNull
        public ArrayList<TimeItem> getItems() {
            ArrayList<TimeItem> list = new ArrayList<>();
            for (int i = 0; i < getCount(); i++) {
                list.add(getItem(i));
            }
            return list;
        }

        /**
         * @param position    Position
         * @param convertView Recycle view
         * @param parent      parent view
         * @return row view
         */
        @Override
        @NonNull
        public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
            final TimeItem item = getItem(position);
            TextView time;
            ImageView delete;
            View view = convertView;
            if (view == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                view = vi.inflate(resourceId, null);
            }
            time = (TextView) view.findViewById(R.id.time);
            delete = (ImageView) view.findViewById(com.jamesmorrisstudios.appbaselibrary.R.id.delete1);
            if (item != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bus.postObject(new TimePickerRequest(item, new TimePickerRequest.OnTimePickerListener() {
                            @Override
                            public void onTimeSet(final int hourOfDay, final int minute) {
                                item.hour = hourOfDay;
                                item.minute = minute;
                                notifyDataSetChanged();
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
            if (item != null) {
                time.setText(UtilsTime.getTimeFormatted(item));
            }
            return view;
        }
    }

    /**
     * Edit times listener
     */
    public interface EditTimesListListener {

        /**
         * @param times List of times
         */
        void onPositive(@NonNull final ArrayList<TimeItem> times);
    }

}
