 

package com.test.norman.launcher.utils;

import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.ColorInt;

import com.test.norman.launcher.R;

public class D {
    public static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";
    public static final int
            MILLISECOND = 1,
            SECOND = 1000 * MILLISECOND,
            MINUTE = 60 * SECOND,
            HOUR = 60 * MINUTE,
            DAY = 24 * HOUR;
    public static final int vibetime = 100;
    public static final String blank_PREFS = BPrefs.KEY;//default device settings
    @ColorInt
    public static final int DEFAULT_STATUS_BAR_COLOR = Color.BLACK;
    public final static View.OnClickListener longer = v -> Toast.makeText(v.getContext(), R.string.press_longer, Toast.LENGTH_LONG).show();
    public static final View.OnClickListener EMPTY_CLICK_LISTENER = v -> {
    };
    public static final int LOW_BATTERY_LEVEL = 20;

    //nope.
    private D() {
    }

    public static class Days {
        public static final int SUNDAY = 0b1;
        public static final int MONDAY = 0b10;
        public static final int TUESDAY = 0b100;
        public static final int WEDNESDAY = 0b1000;
        public static final int THURSDAY = 0b10000;
        public static final int FRIDAY = 0b100000;
        public static final int SATURDAY = 0b1000000;
        public static final int ALL = SUNDAY | MONDAY | TUESDAY | WEDNESDAY | THURSDAY | FRIDAY | SATURDAY;
        public static final int[] ARRAY_ALL = new int[]{SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY};
    }
}
