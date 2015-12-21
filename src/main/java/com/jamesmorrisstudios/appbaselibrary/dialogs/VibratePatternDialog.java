package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.UtilsRingtone;

import java.util.ArrayList;
import java.util.List;

/**
 * Vibrate pattern dialog builder
 * <p/>
 * Created by James on 11/25/2015.
 */
public class VibratePatternDialog extends DialogFragment {
    private ListView list;
    private TextView title;
    private ListAdapter adapter = null;
    private String titleText;
    private long[] pattern;
    private VibratePatternListener listener;

    /**
     * Empty constructor required for DialogFragment
     */
    public VibratePatternDialog() {
    }

    /**
     * On Pause
     */
    public void onPause() {
        UtilsRingtone.vibrateCancel();
        dismiss();
        super.onPause();
    }

    /**
     * On Create View
     *
     * @param inflater           Inflater
     * @param container          Container view
     * @param savedInstanceState Saved instance state
     * @return Item view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_vibrate_pattern, container);
        list = (ListView) view.findViewById(R.id.list);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnPreview = (Button) view.findViewById(R.id.btn_neutral);
        title = (TextView) view.findViewById(R.id.title);
        title.setText(titleText);
        btnPreview.setText(R.string.preview);
        adapter = new ListAdapter(getActivity(), R.layout.dialog_vibrate_pattern_list_item, wrapLong(pattern));
        list.setAdapter(adapter);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
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
                if (listener != null && adapter != null) {
                    listener.onConfirm(title.getText().toString(), adapter.getItems());
                }
                list.post(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                });
            }
        });
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilsRingtone.vibrateCancel();
                if (adapter != null) {
                    long[] items = adapter.getItems();
                    UtilsRingtone.vibrate(items);
                }
            }
        });
        return view;
    }

    /**
     * Set the dialog data
     *
     * @param titleText Title text
     * @param pattern   Current pattern. Must be even length
     * @param listener  Listener
     * @return Dialog
     */
    public VibratePatternDialog setData(@NonNull String titleText, @NonNull long[] pattern, @NonNull VibratePatternListener listener) {
        this.titleText = titleText;
        this.pattern = pattern;
        this.listener = listener;
        return this;
    }

    /**
     * Wrap the array into a wrapper that holds each 2 elements
     *
     * @param list List
     * @return List of wrapped objects
     */
    @NonNull
    private List<LongWrapper> wrapLong(@NonNull long[] list) {
        List<LongWrapper> wrapList = new ArrayList<>();
        for (int i = 1; i < list.length; i += 2) {
            wrapList.add(new LongWrapper(list[i - 1], list[i]));
        }
        while (wrapList.size() < 20) {
            wrapList.add(new LongWrapper(0, 0));
        }
        return wrapList;
    }

    /**
     * Convert value to index of item
     *
     * @param value Value
     * @return Index
     */
    private int valueToIndex(long value) {
        return (int) value / 50;
    }

    /**
     * Convert index back to value
     *
     * @param index Index
     * @return Value
     */
    private long indexToValue(int index) {
        return 50 * index;
    }

    /**
     * @param spinner Spinner
     * @param value   Value
     */
    private void configureSpinner(@NonNull AppCompatSpinner spinner, long value) {
        ArrayList<String> firstList = new ArrayList<>();
        for (int i = 0; i <= 50; i++) {
            firstList.add(50 * i + "");
        }
        ArrayAdapter<String> firstAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, firstList);
        firstAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(firstAdapter);
        spinner.setSelection(valueToIndex(value), false);
    }

    /**
     * Vibrate Pattern listener
     */
    public interface VibratePatternListener {

        /**
         * Confirmed
         *
         * @param title   Title Text
         * @param pattern Pattern
         */
        void onConfirm(@NonNull String title, @NonNull long[] pattern);

        /**
         * Canceled
         */
        void onCancel();
    }

    /**
     * Wrapper object
     */
    class LongWrapper {
        public long offTime, onTime;

        public LongWrapper(long offTime, long onTime) {
            this.offTime = offTime;
            this.onTime = onTime;
        }
    }

    /**
     * List Adapter
     */
    class ListAdapter extends ArrayAdapter<LongWrapper> {
        private int resourceId;

        /**
         * Constructor
         *
         * @param context  Context
         * @param resource Item resource id
         * @param items    Item List
         */
        public ListAdapter(@NonNull Context context, int resource, @NonNull List<LongWrapper> items) {
            super(context, resource, items);
            this.resourceId = resource;
        }

        /**
         * @return List of items. Auto trimmed off zeros
         */
        @NonNull
        public long[] getItems() {
            int size = getCount();
            for (int i = getCount() - 1; i >= 0; i--) {
                if (getItem(i).offTime != 0 || getItem(i).onTime != 0) {
                    size = i + 1;
                    break;
                }
            }
            long[] items = new long[size * 2];
            int index = 0;
            for (int i = 0; i < size; i++) {
                items[index] = getItem(i).offTime;
                items[index + 1] = getItem(i).onTime;
                index += 2;
            }
            return items;
        }

        /**
         * @param position    Position of item
         * @param convertView Recycle view
         * @param parent      Parent object
         * @return Item view
         */
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final LongWrapper item = getItem(position);
            AppCompatSpinner offSpinner, onSpinner;
            View view = convertView;
            if (view == null) {
                LayoutInflater vi = LayoutInflater.from(getContext());
                view = vi.inflate(resourceId, null);
            }
            offSpinner = (AppCompatSpinner) view.findViewById(R.id.offSpinner);
            onSpinner = (AppCompatSpinner) view.findViewById(R.id.onSpinner);
            configureSpinner(offSpinner, item.offTime);
            configureSpinner(onSpinner, item.onTime);
            offSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    item.offTime = indexToValue(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            onSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    item.onTime = indexToValue(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return view;
        }
    }

}
