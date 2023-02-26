 

package com.test.norman.launcher.keyboard;

import android.annotation.SuppressLint;
import android.content.Context;

import com.test.norman.launcher.R;

@SuppressLint("ViewConstructor")

public class HebrewKeyboard extends blankKeyboard {
    public static final int LANGUAGE_ID = 1;
    public static final char[] hebrewKeyboardCodes = new char[]{
            'ק', 'ר', 'א', 'ט', 'ו', 'ן', 'ם', 'פ', BACKSPACE,
            'ש', 'ד', 'ג', 'כ', 'ע', 'י', 'ח', 'ל', 'ך', 'ף',
            'ז', 'ס', 'ב', 'ה', 'נ', 'מ', 'צ', 'ת', 'ץ',
            NUMBERS, LANGUAGE, SPEECH_TO_TEXT, ' ', HIDE, '.', ENTER,

    };

    public HebrewKeyboard(Context context, OnClickListener onClickListener, Runnable backspace, int imeOptions) {
        super(context, onClickListener, backspace, imeOptions);
    }

    @Override
    protected int layout() {
        return R.layout.he_keyboard_layout;
    }

    @Override
    int nextLanguage() {
        return EnglishKeyboard.LANGUAGE_ID;
    }

    @Override
    protected char[] codes() {
        return hebrewKeyboardCodes;
    }

}
