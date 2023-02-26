 

package com.test.norman.launcher.keyboard;

import android.annotation.SuppressLint;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.test.norman.launcher.R;
import com.test.norman.launcher.utils.S;

@SuppressLint("ViewConstructor")
public class KeyboardPicker extends FrameLayout {
    public static final int LANGUAGE_ID = -1;

    public KeyboardPicker(blankInputMethodService blankInputMethodService) {
        super(blankInputMethodService);
        final ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(blankInputMethodService, S.getTheme(blankInputMethodService));
        final ConstraintLayout picker = (ConstraintLayout) LayoutInflater.from(contextThemeWrapper).inflate(R.layout.keyboard_language_picker, this, false);
        picker.findViewById(R.id.bt_hebrew).setOnClickListener(v -> blankInputMethodService.changeLanguage(HebrewKeyboard.LANGUAGE_ID));
        picker.findViewById(R.id.bt_english).setOnClickListener(v -> blankInputMethodService.changeLanguage(EnglishKeyboard.LANGUAGE_ID));
        addView(picker);
    }
}
