 

package com.test.norman.launcher.keyboard;

import android.annotation.SuppressLint;
import android.content.Context;

import com.test.norman.launcher.R;

@SuppressLint("ViewConstructor")
public class NumberKeyboard extends blankKeyboard {
    public static final int LANGUAGE_ID = 0;
    public static final char[] numbersKeyboardsCodes = new char[]{
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
            , '(', ')', '+', '-', '/', '?', '!', ':', ';', ',',
            '\'', '@', '*', '#', '^', '$', '%', '=', BACKSPACE,
            NUMBERS, SPEECH_TO_TEXT, ' ', HIDE, '.', ENTER,
    };

    public NumberKeyboard(Context context, OnClickListener onClickListener, Runnable backspace, int imeOptions) {
        super(context, onClickListener, backspace, imeOptions);
    }

    @Override
    protected int layout() {
        return R.layout.numbers_keyboard_layout;
    }

    @Override
    int nextLanguage() {
        return EnglishKeyboard.LANGUAGE_ID;
    }

    @Override
    protected char[] codes() {
        return numbersKeyboardsCodes;
    }

}
