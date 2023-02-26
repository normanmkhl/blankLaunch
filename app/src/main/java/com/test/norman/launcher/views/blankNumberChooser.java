 

package com.test.norman.launcher.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.test.norman.launcher.R;
import com.test.norman.launcher.utils.BPrefs;
import com.test.norman.launcher.utils.D;

import java.util.Objects;

public class blankNumberChooser extends FrameLayout {
    private static final String TAG = blankNumberChooser.class.getSimpleName();
    private ImageView up, down;
    private TextView tv_number, tv_description;
    private int max, min, jumps, number;
    private Vibrator vibrator;
    private OnClickListener onClickListener;

    public blankNumberChooser(@NonNull Context context) {
        super(context);
        throw new NullPointerException("attrs must not be null!");
    }

    public blankNumberChooser(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, Objects.requireNonNull(attrs));

    }

    public blankNumberChooser(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, Objects.requireNonNull(attrs));
    }

    public blankNumberChooser(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, Objects.requireNonNull(attrs));
    }

    private void init(Context context, @NonNull AttributeSet attributeSet) {
        final TypedArray styleAttributesArray = context.obtainStyledAttributes(attributeSet, R.styleable.blankNumberChooser);
        max = styleAttributesArray.getInt(R.styleable.blankNumberChooser_biggest, -1);
        min = styleAttributesArray.getInt(R.styleable.blankNumberChooser_smallest, -1);
        jumps = styleAttributesArray.getInt(R.styleable.blankNumberChooser_jumps, 1);

        if (max == -1 || min == -1)
            throw new IllegalArgumentException("must specify largest and smallest numbers!");

        LayoutInflater.from(context).inflate(R.layout.number_chooser, this, true);

        vibrator = context.getSharedPreferences(BPrefs.KEY, Context.MODE_PRIVATE).getBoolean(BPrefs.VIBRATION_FEEDBACK_KEY, BPrefs.VIBRATION_FEEDBACK_DEFAULT_VALUE) ? (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE) : null;
        up = findViewById(R.id.up);
        tv_number = findViewById(R.id.number);
        down = findViewById(R.id.down);
        tv_description = findViewById(R.id.tv_description);

        up.setOnClickListener(v -> {
            if (vibrator != null) vibrator.vibrate(D.vibetime);
            if ((number += jumps) > max) number = min;
            tv_number.setText(String.valueOf(number));
            if (onClickListener != null)
                onClickListener.onClick(v);

        });

        down.setOnClickListener(v -> {
            if (vibrator != null) vibrator.vibrate(D.vibetime);
            if ((number -= jumps) < min) number = max;
            tv_number.setText(String.valueOf(number));
            if (onClickListener != null)
                onClickListener.onClick(v);

        });

        tv_description.setText(styleAttributesArray.getString(R.styleable.blankNumberChooser_description));

        styleAttributesArray.recycle();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        if (number > max || number < min)
            throw new IllegalArgumentException(String.format("number must be smaller than %d and bigger than %d", max, min));
        this.number = number;
        tv_number.setText(String.valueOf(number));
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
