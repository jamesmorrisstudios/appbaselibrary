package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.nononsenseapps.filepicker.FilePickerFragment;
import com.nononsenseapps.filepicker.LogicHandler;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Extension of the file picker fragment to add in file extension filtering
 * <p/>
 * Created by James on 10/2/2015.
 */
public final class CustomFilePickerFragment extends FilePickerFragment {
    private String[] extensions = null;

    /**
     * @param extensions Allowed file extensions. Null for all
     */
    public final void setExtension(@Nullable final String[] extensions) {
        this.extensions = extensions;
    }

    /**
     * For consistency, the top level the back button checks against should be the start itemSelected.
     * But it will fall back on /.
     */
    @NonNull
    public final File getBackTop() {
        if (getArguments().containsKey(KEY_START_PATH)) {
            return getPath(getArguments().getString(KEY_START_PATH));
        } else {
            return new File("/");
        }
    }

    /**
     * @param menuItem Menu Item
     * @return True if consumed action
     */
    @Override
    public final boolean onOptionsItemSelected(@NonNull final MenuItem menuItem) {
        if (R.id.nnf_action_createdir == menuItem.getItemId()) {
            Activity activity = getActivity();
            if (activity instanceof AppCompatActivity) {
                CustomNewFolderFragment.showDialog(((AppCompatActivity) activity).getSupportFragmentManager(), CustomFilePickerFragment.this);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return true if the current itemSelected is the startpath or /
     */
    public final boolean isBackTop() {
        return 0 == compareFiles(mCurrentPath, getBackTop()) || 0 == compareFiles(mCurrentPath, new File("/"));
    }

    /**
     * Go up on level, same as pressing on "..".
     */
    public final void goUp() {
        mCurrentPath = getParent(mCurrentPath);
        mCheckedItems.clear();
        mCheckedVisibleViewHolders.clear();
        refresh();
    }

    /**
     * @param file File to check
     * @return The file extension. If file has no extension, it returns null.
     */
    @Nullable
    private String getExtension(@NonNull final File file) {
        String path = file.getPath();
        int i = path.lastIndexOf(".");
        if (i < 0) {
            return null;
        } else {
            return path.substring(i);
        }
    }

    /**
     * @param file File to check
     * @return True if dir or allowed file extension
     */
    @Override
    protected final boolean isItemVisible(@NonNull final File file) {
        if (!isDir(file) && (mode == MODE_FILE || mode == MODE_FILE_AND_DIR)) {
            if (extensions == null) {
                return true;
            }
            for (String extension : extensions) {
                if (extension.equalsIgnoreCase(getExtension(file))) {
                    return true;
                }
            }
            return false;
        }
        return isDir(file);
    }

    /**
     * @param parent   Containing view
     * @param viewType which the ViewHolder will contain
     * @return a view holder for a file or directory
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case LogicHandler.VIEWTYPE_HEADER:
                v = LayoutInflater.from(getActivity()).inflate(R.layout.nnf_filepicker_listitem_dir,
                        parent, false);
                return new HeaderViewHolder(v);
            case LogicHandler.VIEWTYPE_CHECKABLE:
                v = LayoutInflater.from(getActivity()).inflate(R.layout.filepicker_listitem_checkable,
                        parent, false);
                return new CheckableViewHolder(v);
            case LogicHandler.VIEWTYPE_DIR:
            default:
                v = LayoutInflater.from(getActivity()).inflate(R.layout.nnf_filepicker_listitem_dir,
                        parent, false);
                return new DirViewHolder(v);
        }
    }

    /**
     * @param vh       to bind data from either a file or directory
     * @param position 0 - n, where the header has been subtracted
     * @param data     the file or directory which this item represents
     */
    @Override
    public void onBindViewHolder(DirViewHolder vh, int position, File data) {
        vh.file = data;
        vh.text.setText(getName(data));
        if (isCheckable(data)) {
            if (mCheckedItems.contains(data)) {
                mCheckedVisibleViewHolders.add((CheckableViewHolder) vh);
                ((CheckableViewHolder) vh).checkbox.setChecked(true);
                vh.icon.setVisibility(isDir(data) ? View.VISIBLE : View.GONE);
            } else {
                //noinspection SuspiciousMethodCalls
                mCheckedVisibleViewHolders.remove(vh);
                ((CheckableViewHolder) vh).checkbox.setChecked(false);
                if (isImage(data)) {
                    ImageView icon = (ImageView) vh.icon.findViewById(R.id.item_icon_sub);
                    ImageView image = (ImageView) vh.icon.findViewById(R.id.item_icon_image);
                    icon.setVisibility(View.GONE);
                    image.setVisibility(View.VISIBLE);
                    Picasso.with(AppBase.getContext()).load(Uri.fromFile(data).toString()).resize(50, 50).centerCrop().into(image);
                }
            }
        }
    }

    public boolean isImage(File file) {
        final String[] okFileExtensions = new String[]{"jpg", "png", "jpeg"};
        for (String extension : okFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

}
