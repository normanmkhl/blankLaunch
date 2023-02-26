 

package com.test.norman.launcher.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.test.norman.launcher.R;

public class blankPictureTextButton extends blankLinearLayoutButton {

    protected final Context context;
    protected final LayoutInflater layoutInflater;
    protected ImageView imageView;
    protected TextView textView;

    public blankPictureTextButton(Context context) {
        super(context);
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public blankPictureTextButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init(attrs);
    }

    public blankPictureTextButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init(attrs);
    }

    public blankPictureTextButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        final TypedArray styleAttributesArray = context.obtainStyledAttributes(attributeSet, R.styleable.blankPictureTextButton);
        final float pxSize = styleAttributesArray.getDimension(R.styleable.blankPictureTextButton__size, 0);
        final Drawable pic = styleAttributesArray.getDrawable(R.styleable.blankPictureTextButton__src);
        final String text = styleAttributesArray.getString(R.styleable.blankPictureTextButton__text);
        styleAttributesArray.recycle();

        imageView = (ImageView) layoutInflater.inflate(R.layout.bald_picture_text_button_picture, this, false);
        imageView.setImageDrawable(pic);
        addView(imageView);

        textView = (TextView) layoutInflater.inflate(R.layout.bald_picture_text_button_text, this, false);
        textView.setText(text);
        addView(textView);
    }

    public void setText(CharSequence text) {
        textView.setText(text);
    }

    public void setText(@StringRes int resId) {
        textView.setText(resId);
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

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTextView() {
        return textView;
    }

}
