 

package com.test.norman.launcher.views;

import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.test.norman.launcher.utils.D;

public class MediumblankButtonForTutorial extends androidx.appcompat.widget.AppCompatTextView implements blankButtonInterface {
    private Vibrator vibrator;
    private OnClickListener onClickListener;

    public MediumblankButtonForTutorial(Context context) {
        super(context);
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public MediumblankButtonForTutorial(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public MediumblankButtonForTutorial(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void blankPerformClick() {
        if (onClickListener != null)
            onClickListener.onClick(this);
    }

    @Override
    public void vibrate() {
        vibrator.vibrate(D.vibetime);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.onClickListener = l;
        super.setOnClickListener(D.EMPTY_CLICK_LISTENER);
        super.setOnTouchListener(new blankButtonTouchListener(this));

    }
}