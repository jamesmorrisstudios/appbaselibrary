package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.R;

/**
 * Created by James on 4/29/2015.
 */
public class TutorialFragment extends BaseFragment {
    public static final String TAG = "TutorialFragment";

    /**
     * Required empty public constructor
     */
    public TutorialFragment() {}

    /**
     * @param savedInstanceState Saved instance state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Create view
     * @param inflater Inflater object
     * @param container Container view
     * @param savedInstanceState Saved instance state
     * @return The top view for this fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScrollView view = (ScrollView) inflater.inflate(R.layout.fragment_tutorial, container, false);
        LinearLayout contView = (LinearLayout)view.findViewById(R.id.tutorial_container);
        TypedArray tutorial = getResources().obtainTypedArray(R.array.help_tutorial);
        for(int i=0; i<tutorial.length(); i++) {
            int id = tutorial.getResourceId(i, 0);
            if(id > 0) {
                TypedArray item = getResources().obtainTypedArray(id);
                if(item.length() >= 2) {
                    int idHeader = item.getResourceId(0, 0);
                    int idText = item.getResourceId(1, 0);
                    String header = getResources().getString(idHeader);
                    String text = getResources().getString(idText);
                    Drawable drawable = null;
                    if(item.length() >= 3) {
                        int idImage = item.getResourceId(2, 0);
                        drawable = getResources().getDrawable(idImage);
                    }
                    addTutorialItem(contView, header, text, drawable);
                }
                item.recycle();
            }
        }
        tutorial.recycle();
        return view;
    }

    /**
     * Adds an individual tutorial item
     * @param container Container to place the item
     * @param header Header text
     * @param text Main description text
     * @param drawable Image to place below. Null if none
     */
    private void addTutorialItem(@NonNull LinearLayout container, @NonNull String header, @NonNull String text, @Nullable Drawable drawable) {
        LinearLayout item = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.tutorial_item, null);
        TextView textHeader = (TextView) item.findViewById(R.id.header);
        TextView textDetail = (TextView) item.findViewById(R.id.textDetail);
        ImageView image = (ImageView) item.findViewById(R.id.image);
        textHeader.setText(header);
        textDetail.setText(text);
        if(drawable == null) {
            image.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
            image.setImageDrawable(drawable);
        }
        container.addView(item);
    }

    @Override
    public void onBack() {

    }

    @Override
    protected void afterViewCreated() {

    }

}
