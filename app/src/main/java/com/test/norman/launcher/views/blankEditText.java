 

package com.test.norman.launcher.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

import com.test.norman.launcher.utils.BPrefs;
import com.test.norman.launcher.utils.D;

/**
 * This class isn't the same as {@link blankButton} so be careful
 */
public class blankEditText extends AppCompatEditText implements View.OnLongClickListener {
    private final SharedPreferences sharedPreferences;
    private final boolean vibrationFeedback;
    private final Vibrator vibrator;

    public blankEditText(Context context) {
        super(context);
        this.sharedPreferences = context.getSharedPreferences(D.blank_PREFS, Context.MODE_PRIVATE);
        this.vibrationFeedback = sharedPreferences.getBoolean(BPrefs.VIBRATION_FEEDBACK_KEY, BPrefs.VIBRATION_FEEDBACK_DEFAULT_VALUE);
        this.vibrator = this.vibrationFeedback ? (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE) : null;
        super.setOnLongClickListener(this);
    }

    public blankEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.sharedPreferences = context.getSharedPreferences(D.blank_PREFS, Context.MODE_PRIVATE);
        this.vibrationFeedback = sharedPreferences.getBoolean(BPrefs.VIBRATION_FEEDBACK_KEY, BPrefs.VIBRATION_FEEDBACK_DEFAULT_VALUE);
        this.vibrator = this.vibrationFeedback ? (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE) : null;
        super.setOnLongClickListener(this);
    }

    public blankEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.sharedPreferences = context.getSharedPreferences(D.blank_PREFS, Context.MODE_PRIVATE);
        this.vibrationFeedback = sharedPreferences.getBoolean(BPrefs.VIBRATION_FEEDBACK_KEY, BPrefs.VIBRATION_FEEDBACK_DEFAULT_VALUE);
        this.vibrator = this.vibrationFeedback ? (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE) : null;
        super.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        if (vibrationFeedback)
            vibrator.vibrate(D.vibetime);

        if (requestFocus()) {
            final CharSequence charSequence = getText();
            if (charSequence != null) {
                setSelection(charSequence.length());
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
            }
        }
        return true;
    }
}