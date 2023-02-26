 

package com.test.norman.launcher.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.test.norman.launcher.R;
import com.test.norman.launcher.utils.BPrefs;
import com.test.norman.launcher.utils.blankToast;
import com.test.norman.launcher.utils.D;

/**
 * Simple Button, extends {@link LinearLayout}; adapted to App settings.
 * EXACTLY the same code as {@link blankButton}, but extends {@link LinearLayout} instead.
 * for more details, head to {@link blankButton}
 */
public class blankLinearLayoutButton extends LinearLayout implements blankButtonInterface, View.OnLongClickListener, View.OnClickListener {
    private final SharedPreferences sharedPreferences;
    private final boolean longPresses, vibrationFeedback, longPressesShorter;
    private final Vibrator vibrator;
    private final blankToast longer;
    private OnClickListener onClickListener;
    private blankButtonTouchListener blankButtonTouchListener;

    public blankLinearLayoutButton(Context context) {
        super(context);
        this.sharedPreferences = context.getSharedPreferences(D.blank_PREFS, Context.MODE_PRIVATE);
        this.longPresses = sharedPreferences.getBoolean(BPrefs.LONG_PRESSES_KEY, BPrefs.LONG_PRESSES_DEFAULT_VALUE);
        this.longPressesShorter = sharedPreferences.getBoolean(BPrefs.LONG_PRESSES_SHORTER_KEY, BPrefs.LONG_PRESSES_SHORTER_DEFAULT_VALUE);
        this.vibrationFeedback = sharedPreferences.getBoolean(BPrefs.VIBRATION_FEEDBACK_KEY, BPrefs.VIBRATION_FEEDBACK_DEFAULT_VALUE);
        this.vibrator = this.vibrationFeedback ? (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE) : null;
        longer = longPresses ? blankToast.from(context).setText(context.getText(R.string.press_longer)).setType(blankToast.TYPE_DEFAULT).setLength(0).build() : null;
        if (longPresses)
            if (longPressesShorter) {
                blankButtonTouchListener = new blankButtonTouchListener(this);
                super.setOnTouchListener(blankButtonTouchListener);
                super.setOnClickListener(D.EMPTY_CLICK_LISTENER);
            } else {
                super.setOnLongClickListener(this);
                super.setOnClickListener(this);
            }
        else
            super.setOnClickListener(this);

    }

    public blankLinearLayoutButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.sharedPreferences = context.getSharedPreferences(D.blank_PREFS, Context.MODE_PRIVATE);
        this.longPresses = sharedPreferences.getBoolean(BPrefs.LONG_PRESSES_KEY, BPrefs.LONG_PRESSES_DEFAULT_VALUE);
        this.longPressesShorter = sharedPreferences.getBoolean(BPrefs.LONG_PRESSES_SHORTER_KEY, BPrefs.LONG_PRESSES_SHORTER_DEFAULT_VALUE);
        this.vibrationFeedback = sharedPreferences.getBoolean(BPrefs.VIBRATION_FEEDBACK_KEY, BPrefs.VIBRATION_FEEDBACK_DEFAULT_VALUE);
        this.vibrator = this.vibrationFeedback ? (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE) : null;
        longer = longPresses ? blankToast.from(context).setText(context.getText(R.string.press_longer)).setType(blankToast.TYPE_DEFAULT).setLength(0).build() : null;
        if (longPresses)
            if (longPressesShorter) {
                blankButtonTouchListener = new blankButtonTouchListener(this);
                super.setOnTouchListener(blankButtonTouchListener);
                super.setOnClickListener(D.EMPTY_CLICK_LISTENER);
            } else {
                super.setOnLongClickListener(this);
                super.setOnClickListener(this);
            }
        else
            super.setOnClickListener(this);
    }

    public blankLinearLayoutButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.sharedPreferences = context.getSharedPreferences(D.blank_PREFS, Context.MODE_PRIVATE);
        this.longPresses = sharedPreferences.getBoolean(BPrefs.LONG_PRESSES_KEY, BPrefs.LONG_PRESSES_DEFAULT_VALUE);
        this.longPressesShorter = sharedPreferences.getBoolean(BPrefs.LONG_PRESSES_SHORTER_KEY, BPrefs.LONG_PRESSES_SHORTER_DEFAULT_VALUE);
        this.vibrationFeedback = sharedPreferences.getBoolean(BPrefs.VIBRATION_FEEDBACK_KEY, BPrefs.VIBRATION_FEEDBACK_DEFAULT_VALUE);
        this.vibrator = this.vibrationFeedback ? (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE) : null;
        longer = longPresses ? blankToast.from(context).setText(context.getText(R.string.press_longer)).setType(blankToast.TYPE_DEFAULT).setLength(0).build() : null;
        if (longPresses)
            if (longPressesShorter) {
                blankButtonTouchListener = new blankButtonTouchListener(this);
                super.setOnTouchListener(blankButtonTouchListener);
                super.setOnClickListener(D.EMPTY_CLICK_LISTENER);
            } else {
                super.setOnLongClickListener(this);
                super.setOnClickListener(this);
            }
        else
            super.setOnClickListener(this);
    }

    public blankLinearLayoutButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.sharedPreferences = context.getSharedPreferences(D.blank_PREFS, Context.MODE_PRIVATE);
        this.longPresses = sharedPreferences.getBoolean(BPrefs.LONG_PRESSES_KEY, BPrefs.LONG_PRESSES_DEFAULT_VALUE);
        this.longPressesShorter = sharedPreferences.getBoolean(BPrefs.LONG_PRESSES_SHORTER_KEY, BPrefs.LONG_PRESSES_SHORTER_DEFAULT_VALUE);
        this.vibrationFeedback = sharedPreferences.getBoolean(BPrefs.VIBRATION_FEEDBACK_KEY, BPrefs.VIBRATION_FEEDBACK_DEFAULT_VALUE);
        this.vibrator = this.vibrationFeedback ? (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE) : null;
        longer = longPresses ? blankToast.from(context).setText(context.getText(R.string.press_longer)).setType(blankToast.TYPE_DEFAULT).setLength(0).build() : null;
        if (longPresses)
            if (longPressesShorter) {
                blankButtonTouchListener = new blankButtonTouchListener(this);
                super.setOnTouchListener(blankButtonTouchListener);
                super.setOnClickListener(D.EMPTY_CLICK_LISTENER);
            } else {
                super.setOnLongClickListener(this);
                super.setOnClickListener(this);
            }
        else
            super.setOnClickListener(this);
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * use {@link blankButton#setOnLongClickListener(android.view.View.OnLongClickListener)} instead
     */
    @Deprecated
    @Override
    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        throw new RuntimeException("use setOnClickListener(View.OnClickListener onClickListener) instead");
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        if (longPressesShorter)
            blankButtonTouchListener.addListener(l);
        else
            super.setOnTouchListener(l);
    }

    @Override
    public void onClick(View v) {
        if (longPresses) {
            longer.show();
        } else {
            vibrate();
            if (onClickListener != null)
                onClickListener.onClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (longPresses) {
            vibrate();

            if (onClickListener != null)
                onClickListener.onClick(v);
            return true;
        }
        return false;

    }

    @Override
    public void blankPerformClick() {
        if (onClickListener != null)
            onClickListener.onClick(this);
    }

    @Override
    public void vibrate() {
        if (vibrationFeedback)
            vibrator.vibrate(D.vibetime);
    }
}
