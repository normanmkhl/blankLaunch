 

package com.test.norman.launcher.keyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.test.norman.launcher.R;

@SuppressLint("ViewConstructor")
public class EnglishKeyboard extends blankKeyboard implements blankKeyboard.Capitalised {
    public static final int LANGUAGE_ID = 2;
    public static final char[] usKeyboardCodes = new char[]{
            'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'
            , 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', ',',
            SHIFT, 'z', 'x', 'c', 'v', 'b', 'n', 'm', BACKSPACE,
            NUMBERS, LANGUAGE, SPEECH_TO_TEXT, ' ', HIDE, '.', ENTER,
    };
    public static final char[] usKeyboardCodesCAPS = new char[]{
            'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'
            , 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', ',',
            SHIFT, 'Z', 'X', 'C', 'V', 'B', 'N', 'M', BACKSPACE,
            NUMBERS, LANGUAGE, SPEECH_TO_TEXT, ' ', HIDE, '.', ENTER,
    };

    private boolean caps;

    public EnglishKeyboard(Context context, OnClickListener onClickListener, Runnable backspace, int imeOptions) {
        super(context, onClickListener, backspace, imeOptions);
    }

    public void setCaps() {
        caps = !caps;
        final char[] codes = codes();
        for (int i = 0; i < children.length; i++) {
            children[i].setTag(codes[i]);
            if (children[i] instanceof TextView)
                ((TextView) children[i]).setText(new char[]{codes[i]}, 0, 1);
        }
    }

    @Override
    protected int layout() {
        return R.layout.us_keyboard_layout;
    }

    @Override
    int nextLanguage() {
        return HebrewKeyboard.LANGUAGE_ID;
    }

    @Override
    protected char[] codes() {
        return caps ? usKeyboardCodesCAPS : usKeyboardCodes;
    }
}