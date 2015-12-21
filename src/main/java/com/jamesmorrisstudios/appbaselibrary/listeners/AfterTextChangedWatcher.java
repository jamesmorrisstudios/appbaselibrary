package com.jamesmorrisstudios.appbaselibrary.listeners;

import android.text.TextWatcher;

/**
 * Text Watcher that fake implements all but the often used AfterTextChanged listener
 *
 * Created by James on 12/18/2015.
 */
public abstract class AfterTextChangedWatcher implements TextWatcher {

    /**
     * Before Text changed. Override if you need to provide an implementation
     * @param s Text
     * @param start start char
     * @param count number of chars
     * @param after After char
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * On Text changed. Override if you need to provide an implementation
     * @param s Text
     * @param start start char
     * @param before Before char
     * @param count number of chars
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

}
