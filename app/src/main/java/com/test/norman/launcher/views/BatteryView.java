 

package com.test.norman.launcher.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.test.norman.launcher.R;
import com.test.norman.launcher.utils.D;

public class BatteryView extends blankImageButton {
    public int percentage;

    public BatteryView(Context context) {
        super(context);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLevel(int level, boolean charged) {
        percentage = level;
        @DrawableRes int drawableRes = R.drawable.battery_unknown_on_background;
        if (charged) {
            if (level < 20) {
                drawableRes = R.drawable.battery_20_c_on_background;
            } else if (level < 30) {
                drawableRes = R.drawable.battery_30_c_on_background;
            } else if (level < 50) {
                drawableRes = R.drawable.battery_50_c_on_background;
            } else if (level < 60) {
                drawableRes = R.drawable.battery_60_c_on_background;
            } else if (level < 80) {
                drawableRes = R.drawable.battery_80_c_on_background;
            } else if (level < 90) {
                drawableRes = R.drawable.battery_90_c_on_background;
            } else if (level < 100) {
                drawableRes = R.drawable.battery_100_charging;
            } else
                drawableRes = R.drawable.battery_full_on_background;
        } else {
            if (level < D.LOW_BATTERY_LEVEL) {
                drawableRes = R.drawable.battery_20_on_background;
            } else if (level < 30) {
                drawableRes = R.drawable.battery_30_on_background;
            } else if (level < 50) {
                drawableRes = R.drawable.battery_50_on_background;
            } else if (level < 60) {
                drawableRes = R.drawable.battery_60_on_background;
            } else if (level < 80) {
                drawableRes = R.drawable.battery_80_on_background;
            } else if (level < 90) {
                drawableRes = R.drawable.battery_90_on_background;
            } else if (level <= 100) {
                drawableRes = R.drawable.battery_full_on_background;
            }

        }
        setImageResource(drawableRes);
    }
}
