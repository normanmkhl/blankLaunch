 

package com.test.norman.launcher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.annotation.Nullable;

import com.test.norman.launcher.utils.BPrefs;
import com.test.norman.launcher.utils.D;

public class ModularScrollView extends ScrollView implements Modular {
    public boolean touchEnabled;

    public ModularScrollView(Context context) {
        super(context);
        touchEnabled = context.getSharedPreferences(D.blank_PREFS, Context.MODE_PRIVATE).getBoolean(BPrefs.TOUCH_NOT_HARD_KEY, false);
    }

    public ModularScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        touchEnabled = context.getSharedPreferences(D.blank_PREFS, Context.MODE_PRIVATE).getBoolean(BPrefs.TOUCH_NOT_HARD_KEY, false);
    }

    public ModularScrollView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        touchEnabled = context.getSharedPreferences(D.blank_PREFS, Context.MODE_PRIVATE).getBoolean(BPrefs.TOUCH_NOT_HARD_KEY, false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return touchEnabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return touchEnabled && super.onTouchEvent(ev);
    }

}
