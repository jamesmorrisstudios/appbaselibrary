package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
 * Single choice with icon dialog
 * <p/>
 * Created by James on 7/22/2015.
 */
public final class SingleChoiceIconDialogBuilder {
    private AlertDialog.Builder builder;
    private ListView list;
    private OptionPickerListener onOptionPickedListener;
    @DrawableRes
    private int[] itemsIds;
    private Uri[] itemsUri;
    private AlertDialog dialog;
    private int backgroundColor = -1;

    /**
     * @param context Context
     * @param style   Style
     */
    private SingleChoiceIconDialogBuilder(@NonNull Context context, int style) {
        builder = new AlertDialog.Builder(context, style);
        LayoutInflater vi = LayoutInflater.from(context);
        RelativeLayout topView = (RelativeLayout) vi.inflate(R.layout.dialog_helper_list, null);
        list = (ListView) topView.findViewById(R.id.list);
        list.setEmptyView(list.findViewById(android.R.id.empty));
        builder.setView(topView);
    }

    /**
     * @param context Context
     * @param style   Style
     * @return Dialog Builder
     */
    public static SingleChoiceIconDialogBuilder with(@NonNull Context context, int style) {
        return new SingleChoiceIconDialogBuilder(context, style);
    }

    /**
     * @param title String title
     * @return Dialog Builder
     */
    public final SingleChoiceIconDialogBuilder setTitle(@NonNull String title) {
        builder.setTitle(title);
        return this;
    }

    /**
     * @param text     neutral text
     * @param listener Neutral listener
     * @return Dialog Builder
     */
    public final SingleChoiceIconDialogBuilder onNeutral(@NonNull String text, @NonNull DialogInterface.OnClickListener listener) {
        builder.setNeutralButton(text, listener);
        return this;
    }

    /**
     * @param itemsIds Item Id list
     * @return Dialog Builder
     */
    public final SingleChoiceIconDialogBuilder setItemsIds(@NonNull @DrawableRes int[] itemsIds) {
        this.itemsIds = itemsIds;
        return this;
    }

    /**
     * @param itemsUri Item Uri list
     * @return Dialog Builder
     */
    public final SingleChoiceIconDialogBuilder setItemsUri(@NonNull Uri[] itemsUri) {
        this.itemsUri = itemsUri;
        return this;
    }

    /**
     * @param onOptionPickedListener Option picker listener
     * @return Dialog Builder
     */
    public final SingleChoiceIconDialogBuilder setOnOptionPicked(@NonNull OptionPickerListener onOptionPickedListener) {
        this.onOptionPickedListener = onOptionPickedListener;
        return this;
    }

    /**
     * @param backgroundColor background color. -1 to disable
     * @return Dialog Builder
     */
    public final SingleChoiceIconDialogBuilder setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Builds the dialog
     *
     * @return Alert dialog. Must call execute
     */
    public final AlertDialog build() {
        ArrayList<ItemWrapper> items = wrapItems();
        ListAdapter adapter = new ListAdapter(builder.getContext(), R.layout.dialog_icon_picker_list_item, items);
        list.setAdapter(adapter);
        dialog = builder.create();
        return dialog;
    }

    /**
     * Wrap the string items
     *
     * @return List of item wrapper objects
     */
    @NonNull
    private ArrayList<ItemWrapper> wrapItems() {
        ArrayList<ItemWrapper> items = new ArrayList<>();
        if (itemsUri != null) {
            for (Uri uri : itemsUri) {
                items.add(new ItemWrapper(uri));
            }
        } else if (itemsIds != null) {
            for (int item : itemsIds) {
                items.add(new ItemWrapper(item));
            }
        }
        return items;
    }

    /**
     * Option picked listener
     */
    public interface OptionPickerListener {

        /**
         * @param which Selected item index
         */
        void onClick(int which);
    }

    /**
     * Item wrapper class
     */
    private class ItemWrapper {
        @DrawableRes
        public final int itemId;
        public final Uri itemUri;

        /**
         * Constructor from item id
         *
         * @param itemId Int id
         */
        public ItemWrapper(int itemId) {
            this.itemId = itemId;
            this.itemUri = null;
        }

        /**
         * Constructor from Uri
         *
         * @param itemUri Uri
         */
        public ItemWrapper(@NonNull Uri itemUri) {
            this.itemId = -1;
            this.itemUri = itemUri;
        }
    }

    /**
     * List Adapter class
     */
    private class ListAdapter extends ArrayAdapter<ItemWrapper> {
        private int textViewResourceId = -1;

        /**
         * @param context            Context
         * @param textViewResourceId Row layout id
         * @param items              Item list
         */
        public ListAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<ItemWrapper> items) {
            super(context, textViewResourceId, items);
            this.textViewResourceId = textViewResourceId;
        }

        /**
         * @return Get the items in the list
         */
        @NonNull
        public ArrayList<ItemWrapper> getItems() {
            ArrayList<ItemWrapper> list = new ArrayList<>();
            for (int i = 0; i < getCount(); i++) {
                list.add(getItem(i));
            }
            return list;
        }

        /**
         * @param position    Position of object
         * @param convertView Recycled view
         * @param parent      Parent view
         * @return Returned view
         */
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
            final ItemWrapper item = getItem(position);
            ImageView image;
            View view = convertView;
            if (view == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                view = vi.inflate(textViewResourceId, null);
            }
            image = (ImageView) view.findViewById(R.id.image);
            if (item.itemUri != null) {
                Picasso.with(getContext()).load(item.itemUri).into(image);
            } else {
                image.setImageResource(item.itemId);
            }
            if (backgroundColor != -1) {
                image.setBackgroundResource(R.drawable.circle);
                ((GradientDrawable) image.getBackground()).setColor(backgroundColor);
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

}
