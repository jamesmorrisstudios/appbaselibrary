package com.jamesmorrisstudios.appbaselibrary.activityHandlers;

import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by James on 5/5/2016.
 */
public final class IntentBuilderRequest {
    public String action = null;
    public String category = null;
    public String package_ = null;
    public String class_ = null;
    public String extra1 = null;
    public String extra2 = null;
    public String extra3 = null;

    @NonNull
    public final IntentBuilderRequest setAction(@Nullable String action) {
        this.action = action;
        return this;
    }

    @NonNull
    public final IntentBuilderRequest setCategory(@Nullable String category) {
        this.category = category;
        return this;
    }

    @NonNull
    public final IntentBuilderRequest setPackage(@Nullable String package_) {
        this.package_ = package_;
        return this;
    }

    @NonNull
    public final IntentBuilderRequest setClass(@Nullable String class_) {
        this.class_ = class_;
        return this;
    }

    @NonNull
    public final IntentBuilderRequest setExtra1(@Nullable String extra1) {
        this.extra1 = extra1;
        return this;
    }

    @NonNull
    public final IntentBuilderRequest setExtra2(@Nullable String extra2) {
        this.extra2 = extra2;
        return this;
    }

    @NonNull
    public final IntentBuilderRequest setExtra3(@Nullable String extra3) {
        this.extra3 = extra3;
        return this;
    }

    @NonNull
    public final Intent getIntent() {
        Intent intent = new Intent();
        if(action != null && !action.isEmpty()) {
            intent.setAction(action);
        }
        if(category != null && !category.isEmpty()){
            intent.addCategory(category);
        }
        addExtra(extra1, intent);
        addExtra(extra2, intent);
        addExtra(extra3, intent);
        if(package_ != null && !package_.isEmpty()) {
            intent.setPackage(package_);
            if(class_ != null && !class_.isEmpty()) {
                intent.setClassName(package_, class_);
            }
        }
        if(package_ != null && !package_.isEmpty() && class_ != null && !class_.isEmpty()) {
            intent.setComponent(ComponentName.unflattenFromString(package_+"/"+class_));
        }
        return intent;
    }

    private void addExtra(@Nullable String extra, @NonNull Intent intent) {
        if(extra != null && !extra.isEmpty() && extra.contains(":")) {
            String[] extras = extra.split(":");
            if(extras.length == 2) {
                intent.putExtra(extras[0], extras[1]);
            }
        }
    }

    public void startActivityForResult(@NonNull StartActivityForResultRequest.OnStartActivityForResultListener listener) {
        new StartActivityForResultRequest(getIntent(), listener).execute();
    }

    public void startActivity() {
        new StartActivityRequest(getIntent()).execute();
    }

    public void sendBroadcast() {
        new SendBroadcastRequest(getIntent()).execute();
    }

}
