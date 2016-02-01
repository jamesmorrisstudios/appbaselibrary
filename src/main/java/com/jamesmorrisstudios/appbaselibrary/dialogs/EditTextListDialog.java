package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.listeners.AfterTextChangedWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Edit text list dialog
 * <p/>
 * Created by James on 7/9/2015.
 */
public final class EditTextListDialog extends BaseDialogFragment {
    private ListView list;
    private ArrayList<String> messages = null;
    private EditTextListAdapter adapter = null;
    private String titleText;
    private EditTextListListener onPositive;
    private View.OnClickListener onNegative;

    /**
     * @param inflater           Inflater
     * @param container          Container view
     * @param savedInstanceState Saved instance state
     * @return Dialog view
     */
    @Override
    @NonNull
    public final View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_text_list, container);
        list = (ListView) view.findViewById(R.id.list);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnAdd = (Button) view.findViewById(R.id.btn_neutral);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(titleText);
        if (messages != null) {
            adapter = new EditTextListAdapter(getActivity(), R.layout.dialog_edit_text_list_item, wrapString(messages));
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
                adapter.add(new StringWrapper(""));
            }
        });
        return view;
    }

    /**
     * @param titleText  Dialog title
     * @param messages   List of text items
     * @param onPositive onPositive
     * @param onNegative onNegative
     */
    public final void setData(@NonNull final String titleText, @NonNull final ArrayList<String> messages, @NonNull final EditTextListListener onPositive, @Nullable final View.OnClickListener onNegative) {
        this.titleText = titleText;
        this.messages = new ArrayList<>(messages);
        this.onPositive = onPositive;
        this.onNegative = onNegative;
    }

    /**
     * @param list List of strings
     * @return Wrapped string items
     */
    @NonNull
    private List<StringWrapper> wrapString(@NonNull final List<String> list) {
        List<StringWrapper> wrapList = new ArrayList<>();
        for (String text : list) {
            wrapList.add(new StringWrapper(text));
        }
        return wrapList;
    }

    /**
     * Edit text list listener
     */
    public interface EditTextListListener {

        /**
         * @param messages list of Strings
         */
        void onPositive(@NonNull final ArrayList<String> messages);
    }

    /**
     * String wrapper object
     */
    private class StringWrapper {
        public String text;
        public TextWatcher textWatcher = new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(@NonNull final Editable s) {
                text = s.toString();
            }
        };

        /**
         * Constructor
         *
         * @param text String
         */
        public StringWrapper(@NonNull final String text) {
            this.text = text;
        }
    }

    /**
     * List adapter
     */
    private class EditTextListAdapter extends ArrayAdapter<StringWrapper> {

        /**
         * @param context  Context
         * @param resource Row layout id
         * @param items    List of items
         */
        public EditTextListAdapter(@NonNull final Context context, final int resource, @NonNull final List<StringWrapper> items) {
            super(context, resource, items);
        }

        /**
         * @return List of String items
         */
        @NonNull
        public ArrayList<String> getItems() {
            ArrayList<String> wrapList = new ArrayList<>();
            for (int i = 0; i < getCount(); i++) {
                wrapList.add(getItem(i).text);
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
            final StringWrapper item = getItem(position);
            EditText editText;
            ImageView delete;
            View view = convertView;
            if (view == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                view = vi.inflate(R.layout.dialog_edit_text_list_item, null);
                editText = (EditText) view.findViewById(R.id.text1);
                delete = (ImageView) view.findViewById(R.id.delete1);
            } else {
                editText = (EditText) view.findViewById(R.id.text1);
                delete = (ImageView) view.findViewById(R.id.delete1);
                for (int i = 0; i < getCount(); i++) {
                    editText.removeTextChangedListener(getItem(i).textWatcher);
                    delete.setOnClickListener(null);
                }
            }
            if (item != null) {
                editText.setText(item.text);
                editText.addTextChangedListener(item.textWatcher);
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
