 

package com.test.norman.launcher.utils;

import android.content.Context;
import android.os.Handler;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.test.norman.launcher.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class blankToast {
    public static final int LENGTH_SEC = -1;
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_ERROR = 1;
    public static final int TYPE_INFORMATIVE = 2;
    @LayoutRes
    private final static int layout = R.layout.toast_layout;
    @DrawableRes
    private static final int TYPE_DEFAULT_BACKGROUND_COLOR_RES_ID =
            R.drawable.toast_default_background;
    @ColorRes
    private static final int TYPE_DEFAULT_FOREGROUND_COLOR_RES_ID =
            R.color.toast_foreground_default;
    @DrawableRes
    private static final int TYPE_ERROR_BACKGROUND_COLOR_RES_ID =
            R.drawable.toast_error_background;
    @ColorRes
    private static final int TYPE_ERROR_FOREGROUND_COLOR_RES_ID =
            R.color.toast_foreground_error;
    @DrawableRes
    private static final int TYPE_INFORMATIVE_BACKGROUND_COLOR_RES_ID =
            R.drawable.toast_informative_background;
    @ColorRes
    private static final int TYPE_INFORMATIVE_FOREGROUND_COLOR_RES_ID =
            R.color.toast_foreground_informative;
    private final Context context;
    @ToastType
    private int type = TYPE_DEFAULT;
    private CharSequence text;
    private boolean big = false;
    private int duration = Toast.LENGTH_LONG;
    private Toast toast;
    private boolean built;

    private blankToast(@NonNull Context context) {
        this.context = new ContextThemeWrapper(context.getApplicationContext(), R.style.blank_light);
    }

    public static blankToast from(@NonNull Context context) {
        return new blankToast(context);
    }

    public static void error(Context context) {
        blankToast.from(context).setText(R.string.an_error_has_occurred).setType(TYPE_ERROR).show();
    }

    public static void simple(Context context, CharSequence text) {
        blankToast.from(context).setText(text).setType(TYPE_DEFAULT).show();
    }

    public static void simple(Context context, @StringRes int resId) {
        blankToast.from(context).setText(context.getText(resId)).setType(TYPE_DEFAULT).show();
    }

    public static void longer(Context context) {
        blankToast.from(context).setText(R.string.press_longer).setType(TYPE_DEFAULT).setLength(-1).show();
    }

    public blankToast setType(@ToastType int type) {
        this.type = type;
        return this;
    }

    public blankToast setText(CharSequence text) {
        this.text = text;
        return this;
    }

    public blankToast setText(@StringRes int resString) {
        this.text = context.getString(resString);
        return this;
    }

    public blankToast setLength(@IntRange(from = -1, to = 1) int duration) {
        this.duration = duration;
        return this;
    }

    public blankToast setBig(boolean big) {
        this.big = big;
        return this;
    }

    public void show() {
        if (!built)
            build();
        toast.show();
        if (duration == -1) {
            new Handler()
                    .postDelayed(() -> toast.cancel(), D.SECOND);
        }
    }

    public blankToast build() {
        //not sure why but removing this line crashes app! so don't
        final View toastView = LayoutInflater.from(context).inflate(layout, null);
        final TextView textView = (TextView) toastView;
        if (big)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize() * 2);

        final @DrawableRes int toastViewBackground;
        final @ColorInt int textViewColor;
        switch (type) {
            case TYPE_DEFAULT:
                toastViewBackground = TYPE_DEFAULT_BACKGROUND_COLOR_RES_ID;
                textViewColor = context.getResources().getColor(TYPE_DEFAULT_FOREGROUND_COLOR_RES_ID);
                break;
            case TYPE_ERROR:
                toastViewBackground = TYPE_ERROR_BACKGROUND_COLOR_RES_ID;
                textViewColor = context.getResources().getColor(TYPE_ERROR_FOREGROUND_COLOR_RES_ID);
                break;
            case TYPE_INFORMATIVE:
                toastViewBackground = TYPE_INFORMATIVE_BACKGROUND_COLOR_RES_ID;
                textViewColor = context.getResources().getColor(TYPE_INFORMATIVE_FOREGROUND_COLOR_RES_ID);
                break;
            default:
                throw new IllegalArgumentException("type not supported!");
        }

        textView.setTextColor(textViewColor);
        toastView.setBackground(ContextCompat.getDrawable(context, toastViewBackground));

        textView.setText(text);
        toast = new Toast(context);
        toast.setDuration(duration == LENGTH_SEC ? Toast.LENGTH_SHORT : duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(toastView);
        built = true;
        return this;
    }

    @IntDef({TYPE_DEFAULT, TYPE_ERROR, TYPE_INFORMATIVE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ToastType {
    }
}
