package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.utilitieslibrary.controls.ButtonFlat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 7/9/2015.
 */
public class EditTextListDialog extends DialogFragment {

    private ListView list;
    private ButtonFlat btnCancel, btnOk;
    private ArrayList<String> messages = null;
    private ListAdapter adapter = null;
    private EditMessageListener onPositive;
    private View.OnClickListener onNegative;

    public EditTextListDialog() {
        // Empty constructor required for DialogFragment
    }

    public void onPause() {
        dismiss();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_text_list_dialog, container);
        list = (ListView) view.findViewById(R.id.list);
        btnCancel = (ButtonFlat) view.findViewById(R.id.btn_cancel);
        btnOk = (ButtonFlat) view.findViewById(R.id.btn_ok);

        if(messages != null) {
            adapter = new ListAdapter(getActivity(), R.layout.edit_text_list_line_item, wrapString(messages));

            // Assign adapter to ListView
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.v("EditMessageDialog", "Item Clicked: " + position);
                }
            });
            list.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    Log.v("EditMessageDialog", "Key Press");
                    return false;
                }
            });
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
                if(onPositive != null && adapter != null) {
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
        return view;
    }

    public void setData(@NonNull ArrayList<String> messages, @NonNull EditMessageListener onPositive, @Nullable View.OnClickListener onNegative) {
        this.messages = new ArrayList<>(messages);
        this.onPositive = onPositive;
        this.onNegative = onNegative;
}

    public interface EditMessageListener {
        void onPositive(ArrayList<String> messages);
    }

    private List<StringWrapper> wrapString(List<String> list) {
        List<StringWrapper> wrapList = new ArrayList<>();
        for(String text : list) {
            wrapList.add(new StringWrapper(text));
        }
        return wrapList;
    }

    class StringWrapper {
        public String text;
        public TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(@NonNull Editable s) {
                text = s.toString();
            }
        };
        public StringWrapper(String text) {
            this.text = text;
        }
    }

    class ListAdapter extends ArrayAdapter<StringWrapper> {

        public ListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListAdapter(Context context, int resource, List<StringWrapper> items) {
            super(context, resource, items);
            Log.v("EditTextListDialog", "Item Count: "+getCount());
        }

        public ArrayList<String> getItems() {
            ArrayList<String> wrapList = new ArrayList<>();
            for(int i=0; i<getCount(); i++) {
                wrapList.add(getItem(i).text);
            }
            return wrapList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StringWrapper item = getItem(position);

            EditText editText;
            View view = convertView;

            if (view == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                view = vi.inflate(R.layout.edit_text_list_line_item, null);
                editText = (EditText) view.findViewById(R.id.text1);
            } else {
                editText = (EditText) view.findViewById(R.id.text1);
                for(int i=0; i<getCount(); i++) {
                    editText.removeTextChangedListener(getItem(i).textWatcher);
                }
            }

            if (item != null) {
                editText.setText(item.text);
                editText.addTextChangedListener(item.textWatcher);
            }
            return view;
        }

    }

}
