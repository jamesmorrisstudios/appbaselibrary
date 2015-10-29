package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.Utils;

/**
 * Created by James on 7/22/2015.
 */
public class SingleChoiceIconDialogBuilder {
    private AlertDialog.Builder builder;
    private ScrollView mainView;
    private LinearLayout pickerContainer;
    private OptionPickerListener onOptionPickedListener;
    @DrawableRes private int[] items;
    private AlertDialog dialog;

    private SingleChoiceIconDialogBuilder(@NonNull Context context, int style) {
        builder = new AlertDialog.Builder(context, style);
        mainView = new ScrollView(context);
        pickerContainer = new LinearLayout(context);
        pickerContainer.setOrientation(LinearLayout.VERTICAL);
        pickerContainer.setGravity(Gravity.CENTER_HORIZONTAL);
        mainView.addView(pickerContainer);
        builder.setView(mainView);
    }

    public static SingleChoiceIconDialogBuilder with(@NonNull Context context, int style) {
        return new SingleChoiceIconDialogBuilder(context, style);
    }

    public SingleChoiceIconDialogBuilder setTitle(@NonNull String title) {
        builder.setTitle(title);
        return this;
    }

    public SingleChoiceIconDialogBuilder setItems(@NonNull @DrawableRes int[] items) {
        this.items = items;
        return this;
    }

    public SingleChoiceIconDialogBuilder setOnOptionPicked(@NonNull OptionPickerListener onOptionPickedListener) {
        this.onOptionPickedListener = onOptionPickedListener;
        return this;
    }

    public AlertDialog build() {
        Context context = builder.getContext();
        buildOptionList(context);
        dialog = builder.create();
        return dialog;
    }

    private void buildOptionList(@NonNull Context context) {
        for (int i = 0; i < items.length; i++) {
            final int index = i;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(Utils.getDipInt(6), Utils.getDipInt(6), Utils.getDipInt(6), Utils.getDipInt(6));
            ImageView image = new ImageView(context);
            image.setLayoutParams(params);
            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            image.setImageResource(items[i]);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionPickedListener.onClick(index);
                    dialog.dismiss();
                }
            });
            pickerContainer.addView(image);
        }
    }

    public interface OptionPickerListener {
        void onClick(int which);
    }


}
