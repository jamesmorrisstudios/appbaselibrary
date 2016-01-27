package com.jamesmorrisstudios.appbaselibrary.dialogs;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by James on 12/23/2015.
 */
public class ImageDialogBuilder {
    private AlertDialog.Builder builder;
    private String imagePath;
    private ImageView image;
    private ImageButton close;
    private PhotoViewAttacher mAttacher;

    /**
     * @param context Context
     * @param style   Style
     */
    private ImageDialogBuilder(@NonNull final Context context, final int style) {
        builder = new AlertDialog.Builder(context, style);
        LayoutInflater vi = LayoutInflater.from(context);
        RelativeLayout topView = (RelativeLayout) vi.inflate(R.layout.dialog_image, null);
        image = (ImageView) topView.findViewById(R.id.image);
        close = (ImageButton) topView.findViewById(R.id.close);
        builder.setView(topView);
        mAttacher = new PhotoViewAttacher(image);
    }

    /**
     * @param context Context
     * @param style   Style
     * @return Dialog Builder
     */
    @NonNull
    public static ImageDialogBuilder with(@NonNull final Context context, final int style) {
        return new ImageDialogBuilder(context, style);
    }

    /**
     * @param imagePath String imagePath
     * @return Dialog Builder
     */
    @NonNull
    public final ImageDialogBuilder setImage(@NonNull final String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    /**
     * Builds the dialog
     *
     * @return Alert dialog. Must call execute
     */
    @NonNull
    public final AlertDialog build() {
        Picasso.with(AppBase.getContext()).load(Uri.parse(imagePath)).into(image, new Callback() {
            @Override
            public void onSuccess() {
                mAttacher.update();
            }

            @Override
            public void onError() {

            }
        });
        final AlertDialog dialog = builder.create();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

}
