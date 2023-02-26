 

package com.test.norman.launcher.views;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;

public class blankKeyboardView extends KeyboardView {

    public invokePressListener invokePressListener;

    public blankKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public blankKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public blankKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean onLongPress(Keyboard.Key popupKey) {
        if (invokePressListener != null) {
            invokePressListener.onKey(popupKey.codes[popupKey.codes.length - 1], popupKey.codes);

            return true;
        }
        return false;

    }

    interface invokePressListener {
        void onKey(int primaryCode, int[] keyCodes);
    }
}