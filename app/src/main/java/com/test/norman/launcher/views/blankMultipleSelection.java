 

package com.test.norman.launcher.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.test.norman.launcher.R;
import com.test.norman.launcher.utils.D;

import java.util.ArrayList;

public class blankMultipleSelection extends LinearLayout {
    private @ColorInt
    int textColorOnSelected;
    private @ColorInt
    int textColorOnButton;
    private OnItemClickListener onItemClickListener = (v) -> {
    };
    private Drawable defaultDrawableSelected, defaultDrawable;
    private Context context;
    private LayoutInflater layoutInflater;
    private int selection = -1;
    private int size = 0;
    private float pxDimen;
    private ArrayList<blankButton> buttons = new ArrayList<>(5);

    public blankMultipleSelection(Context context) {
        super(context);
        init(context, null);
    }

    public blankMultipleSelection(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public blankMultipleSelection(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(value = 21)
    public blankMultipleSelection(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @SuppressLint("WrongConstant")
    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);

        final TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.blank_text_on_selected, typedValue, true);
        textColorOnSelected = typedValue.data;
        theme.resolveAttribute(R.attr.blank_text_on_button, typedValue, true);
        textColorOnButton = typedValue.data;

        if (attrs == null) {
            defaultDrawable = ContextCompat.getDrawable(context, R.drawable.style_for_buttons);
            defaultDrawableSelected = ContextCompat.getDrawable(context, R.drawable.btn_selected);
            setOrientation(HORIZONTAL);
        } else {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.blankMultipleSelection);
            setOrientation((typedArray.getBoolean(R.styleable.blankMultipleSelection_is_vertical, false)) ? VERTICAL : HORIZONTAL);
            Drawable drawable = typedArray.getDrawable(R.styleable.blankMultipleSelection_background_default);
            defaultDrawable = drawable != null ? drawable : ContextCompat.getDrawable(context, R.drawable.style_for_buttons);
            Drawable drawableSelected = typedArray.getDrawable(R.styleable.blankMultipleSelection_background_selected);
            if (drawableSelected != null)
                defaultDrawableSelected = drawableSelected;
            else
                defaultDrawableSelected = ContextCompat.getDrawable(context, R.drawable.btn_selected);
            pxDimen = typedArray.getDimension(R.styleable.blankMultipleSelection_text_size, -1);
            textColorOnButton = typedArray.getColor(R.styleable.blankMultipleSelection_text_color, textColorOnButton);
            textColorOnSelected = typedArray.getColor(R.styleable.blankMultipleSelection_selected_text_color, textColorOnSelected);
            typedArray.recycle();
        }

    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
        for (blankButton button : buttons) {
            final LayoutParams layoutParams = new LayoutParams(orientation == VERTICAL ? ViewGroup.LayoutParams.MATCH_PARENT : 0, orientation != VERTICAL ? ViewGroup.LayoutParams.MATCH_PARENT : 0);
            int inPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics());
            layoutParams.setMargins(orientation == VERTICAL ? 0 : inPx, orientation != VERTICAL ? 0 : inPx, orientation == VERTICAL ? 0 : inPx, orientation != VERTICAL ? 0 : inPx);
            layoutParams.weight = 1f;
            button.setLayoutParams(layoutParams);
        }

    }

    public void addSelection(@StringRes int resId) {
        addSelection(context.getText(resId));
    }

    public void setButtonsDrawable(@DrawableRes int drawable) {
        setButtonsDrawable(ContextCompat.getDrawable(context, drawable));

    }

    public void setButtonsDrawable(Drawable drawable) {
        for (final blankButton button : buttons)
            button.setBackground(drawable);
        defaultDrawable = drawable;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void addSelection(CharSequence... charSequences) {
        for (CharSequence charSequence : charSequences)
            addSelection(charSequence);
    }

    public void addSelection(@StringRes int... resIds) {
        for (@StringRes int resId : resIds)
            addSelection(resId);
    }

    public void addSelection(CharSequence name) {

        final int index = size++;
        final blankButton button = (blankButton) layoutInflater.inflate(R.layout.multiple_vertical_selection_view_item, this, false);
        button.setBackground(defaultDrawable.getConstantState().newDrawable());

        button.setText(name);
        if (pxDimen != -1)
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX, pxDimen);
        else
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.medium));
        this.addView(button);
        buttons.add(button);
        final LayoutParams layoutParams = new LayoutParams(getOrientation() == VERTICAL ? ViewGroup.LayoutParams.MATCH_PARENT : 0, getOrientation() != VERTICAL ? ViewGroup.LayoutParams.MATCH_PARENT : 0);
        int inPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());
        layoutParams.setMargins(getOrientation() == VERTICAL ? 0 : inPx, getOrientation() != VERTICAL ? 0 : inPx, getOrientation() == VERTICAL ? 0 : inPx, getOrientation() != VERTICAL ? 0 : inPx);
        layoutParams.weight = 1f;
        button.setLayoutParams(layoutParams);
        button.setOnClickListener(D.longer);
        button.setTextColor(textColorOnButton);
        button.setOnClickListener(v -> {
            if (selection == index)
                return;
            if (selection != -1)
                setClicked(buttons.get(selection), false);
            selection = index;
            setClicked(buttons.get(selection), true);
            onItemClickListener.onItemClick(getSelection());
        });
        if (selection == -1) {
            selection = index;
            setClicked(button, true);
        }

        //TODO DELETE
    }

    public int getSelection() {
        if (selection == -1)
            throw new RuntimeException("nothing to select from!");

        return selection;
    }

    public void setSelection(int selectionIndex) {
        setClicked(buttons.get(selection), false);
        this.selection = selectionIndex;
        setClicked(buttons.get(selection), true);

    }

    private void setClicked(blankButton button, boolean state) {
        if (state) {
            button.setBackground(defaultDrawableSelected.getConstantState().newDrawable());
            button.setTextColor(textColorOnSelected);
        } else {
            button.setBackground(defaultDrawable.getConstantState().newDrawable());
            button.setTextColor(textColorOnButton);

        }

    }

    @FunctionalInterface
    public interface OnItemClickListener {
        void onItemClick(int whichItem);
    }
}
