 

package com.test.norman.launcher.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;

import com.test.norman.launcher.R;
import com.test.norman.launcher.utils.BPrefs;
import com.test.norman.launcher.utils.blankToast;
import com.test.norman.launcher.utils.D;

public class blankTitleBar extends LinearLayout {
    private CharSequence title;
    private Context context;
    private ImageView bt_back, bt_help;
    private TextView tv_title;
    private boolean added;
    private int textColorBeforeGold = -1;

    public blankTitleBar(Context context) {
        super(context);
        init(context, null);
    }

    public blankTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public blankTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public blankTitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);

    }

    private void init(Context context, @Nullable AttributeSet attributeSet) {
        this.context = context;
        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutDirection(LAYOUT_DIRECTION_LTR);

        setBackgroundResource(R.drawable.btn_enabled_rectangle);

        final TypedArray styleAttributesArray = context.obtainStyledAttributes(attributeSet, R.styleable.blankTitleBar);
        title = styleAttributesArray.getString(R.styleable.blankTitleBar_title);
        if (title == null)
            title = "";
        styleAttributesArray.recycle();

        final LayoutInflater inflater = LayoutInflater.from(context);
        tv_title = (TextView) inflater.inflate(R.layout.bald_title_title, this, false);
        bt_back = (ImageView) inflater.inflate(R.layout.bald_title_back_button, this, false);
        bt_help = (ImageView) inflater.inflate(R.layout.bald_title_help, this, false);
        tv_title.setText(title);
        updateView();

    }

    public void setGold(boolean gold) {
        if (textColorBeforeGold == -1) {
            textColorBeforeGold = tv_title.getCurrentTextColor();
        }
        setBackgroundTintList(gold ? ColorStateList.valueOf(getContext().getResources().getColor(R.color.gold)) : null);
        final int onGoldColorInt = getContext().getResources().getColor(R.color.on_gold);
        final ColorStateList onGold = gold ? ColorStateList.valueOf(onGoldColorInt) : null;
        bt_back.setImageTintList(onGold);
        bt_help.setImageTintList(onGold);
        tv_title.setTextColor(gold ? onGoldColorInt : textColorBeforeGold);
    }

    //public on purpose, because of settings app... trust me
    private void updateView() {
        if (added) {
            removeAllViews();
        }
        bt_back.setOnClickListener(v -> {
            if (context instanceof Activity)
                ((Activity) context).finish();

        });

        bt_help.setOnClickListener(v -> blankToast.from(context).setText(R.string.coming_soon).show());
        final boolean rightHanded = context.getSharedPreferences(D.blank_PREFS, Context.MODE_PRIVATE).getBoolean(BPrefs.RIGHT_HANDED_KEY, BPrefs.RIGHT_HANDED_DEFAULT_VALUE);
        addView(rightHanded ? bt_help : bt_back);
        addView(tv_title);
        addView(rightHanded ? bt_back : bt_help);
        added = true;

    }

    public void setTitle(@StringRes int resId) {
        this.title = getResources().getString(resId);
        tv_title.setText(title);
    }

    public void setTitle(CharSequence title) {
        this.title = title;
        tv_title.setText(title);
    }

    /**
     * use with cation
     *
     * @return btBack
     */
    public ImageView getBt_back() {
        return bt_back;
    }

    /**
     * use with cation
     *
     * @return btHelp
     */
    public ImageView getBt_help() {
        return bt_help;
    }

}
