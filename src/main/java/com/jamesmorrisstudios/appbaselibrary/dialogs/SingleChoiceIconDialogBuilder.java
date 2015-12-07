package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 7/22/2015.
 */
public class SingleChoiceIconDialogBuilder {
    private AlertDialog.Builder builder;
    private ListView list;
    private OptionPickerListener onOptionPickedListener;
    @DrawableRes private int[] itemsIds;
    private Uri[] itemsUri;
    private AlertDialog dialog;

    private SingleChoiceIconDialogBuilder(@NonNull Context context, int style) {
        builder = new AlertDialog.Builder(context, style);
        LayoutInflater vi= LayoutInflater.from(context);
        RelativeLayout topView = (RelativeLayout) vi.inflate(R.layout.dialog_list, null);
        list = (ListView) topView.findViewById(R.id.list);
        list.setEmptyView(list.findViewById(android.R.id.empty));
        builder.setView(topView);
    }

    public static SingleChoiceIconDialogBuilder with(@NonNull Context context, int style) {
        return new SingleChoiceIconDialogBuilder(context, style);
    }

    public SingleChoiceIconDialogBuilder setTitle(@NonNull String title) {
        builder.setTitle(title);
        return this;
    }

    /*
    public SingleChoiceIconDialogBuilder onPositive(@NonNull String text, @NonNull DialogInterface.OnClickListener listener) {
        builder.setPositiveButton(text, listener);
        return this;
    }

    public SingleChoiceIconDialogBuilder onNegative(@NonNull String text, @NonNull DialogInterface.OnClickListener listener) {
        builder.setNegativeButton(text, listener);
        return this;
    }
    */

    public SingleChoiceIconDialogBuilder onNeutral(@NonNull String text, @NonNull DialogInterface.OnClickListener listener) {
        builder.setNeutralButton(text, listener);
        return this;
    }

    public SingleChoiceIconDialogBuilder setItemsIds(@NonNull @DrawableRes int[] itemsIds) {
        this.itemsIds = itemsIds;
        return this;
    }

    public SingleChoiceIconDialogBuilder setItemsUri(@NonNull Uri[] itemsUri) {
        this.itemsUri = itemsUri;
        return this;
    }

    public SingleChoiceIconDialogBuilder setOnOptionPicked(@NonNull OptionPickerListener onOptionPickedListener) {
        this.onOptionPickedListener = onOptionPickedListener;
        return this;
    }

    public AlertDialog build() {
        ArrayList<ItemWrapper> items = wrapItems();
        ListAdapter adapter = new ListAdapter(builder.getContext(), R.layout.icon_picker_dialog_row_item, items);
        list.setAdapter(adapter);
        dialog = builder.create();
        return dialog;
    }

    private ArrayList<ItemWrapper> wrapItems() {
        ArrayList<ItemWrapper> items = new ArrayList<>();
        if(itemsUri != null) {
            for(Uri uri : itemsUri) {
                items.add(new ItemWrapper(uri));
            }
        } else if(itemsIds != null) {
            for(int item : itemsIds) {
                items.add(new ItemWrapper(item));
            }
        }
        return items;
    }

    class ItemWrapper {
        @DrawableRes public final int itemId;
        public final Uri itemUri;

        public ItemWrapper(int itemId) {
            this.itemId = itemId;
            this.itemUri = null;
        }

        public ItemWrapper(@NonNull Uri itemUri) {
            this.itemId = -1;
            this.itemUri = itemUri;
        }
    }

    class ListAdapter extends ArrayAdapter<ItemWrapper> {
        private int textViewResourceId = -1;

        public ListAdapter(Context context, int textViewResourceId, List<ItemWrapper> items) {
            super(context, textViewResourceId, items);
            this.textViewResourceId = textViewResourceId;
        }

        public ArrayList<ItemWrapper> getItems() {
            ArrayList<ItemWrapper> list = new ArrayList<>();
            for(int i=0; i<getCount(); i++) {
                list.add(getItem(i));
            }
            return list;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ItemWrapper item = getItem(position);

            ImageView image;
            View view = convertView;

            if (view == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                view = vi.inflate(textViewResourceId, null);
            }
            image = (ImageView) view.findViewById(R.id.image);

            if(item.itemUri != null) {
                Picasso.with(getContext()).load(item.itemUri).into(image);
            } else {
                image.setImageResource(item.itemId);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionPickedListener.onClick(position);
                    dialog.dismiss();
                }
            });
            return view;
        }
    }

    public interface OptionPickerListener {
        void onClick(int which);
    }


}
