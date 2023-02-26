 

package com.test.norman.launcher.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.test.norman.launcher.R;

public class FirstPageAppIcon extends blankFrameLayoutButton {
    protected final Context context;
    protected final LayoutInflater layoutInflater;
    public ImageView imageView;
    public TextView textView;
    public View badge;

    public FirstPageAppIcon(Context context) {
        super(context);
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init(null);
    }

    public FirstPageAppIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init(attrs);
    }

    public FirstPageAppIcon(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attributeSet) {
        layoutInflater.inflate(R.layout.first_page_app_icon, this, true);
        imageView = findViewById(R.id.iv);
        textView = findViewById(R.id.tv);
        badge = findViewById(R.id.notifications_counter);

        if (attributeSet != null) {
            final TypedArray styleAttributesArray = context.obtainStyledAttributes(attributeSet, R.styleable.FirstPageAppIcon);
            setImageDrawable(styleAttributesArray.getDrawable(R.styleable.FirstPageAppIcon___src));
            setText(styleAttributesArray.getString(R.styleable.FirstPageAppIcon___text));
            styleAttributesArray.recycle();
        }

    }

    public void setImageDrawable(@Nullable Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    public void setImageBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    public void setImageResource(@DrawableRes int resId) {
        imageView.setImageResource(resId);
    }

    public void setBadgeVisibility(boolean visible) {
        badge.setVisibility(visible ? VISIBLE : INVISIBLE);
    }

    public CharSequence getText() {
        return textView.getText();
    }

    public void setText(CharSequence text) {
        textView.setText(text);
    }

    public void setText(@StringRes int resId) {
        textView.setText(resId);
    }
}
