 

package com.test.norman.launcher.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class SquaredCircleImageView extends CircleImageView {
    public SquaredCircleImageView(Context context) {
        super(context);
    }

    public SquaredCircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquaredCircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = getMeasuredWidth();
        setMeasuredDimension(width / 2, width / 2);
    }

}
