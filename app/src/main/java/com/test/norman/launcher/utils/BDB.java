 

package com.test.norman.launcher.utils;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.test.norman.launcher.activities.blankActivity;

/**
 * Builder for {@link BDialog}
 */
public class BDB {
    public Context context;

    @NonNull
    CharSequence title = "";
    CharSequence subText;
    CharSequence[] options;
    int inputType;
    BDialog.DialogBoxListener positiveButtonListener = BDialog.DialogBoxListener.EMPTY;
    BDialog.DialogBoxListener negativeButtonListener = BDialog.DialogBoxListener.EMPTY;
    @NonNull
    BDialog.StartingIndexChooser startingIndexChooser = () -> 0;
    @Nullable
    blankActivity blankActivityToAutoDismiss;
    @Nullable
    View extraView;
    @Nullable
    CharSequence negativeCustomText;
    @Nullable
    CharSequence positiveCustomText;
    int flags;

    private BDB() {
    }

    public static BDB from(@Nullable Context context) {
        BDB bdb = new BDB();
        bdb.context = context;
        if (context instanceof blankActivity) {
            bdb.blankActivityToAutoDismiss = (blankActivity) context;
        }
        return bdb;
    }

    public BDB setTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    public BDB setTitle(@StringRes int titleId) {
        return setTitle(context.getText(titleId));
    }

    public BDB setSubText(CharSequence subText) {
        this.subText = subText;
        return this;
    }

    public BDB setSubText(@StringRes int subTextId) {
        return setSubText(context.getText(subTextId));
    }

    public BDB setOptions(CharSequence... options) {
        flags |= BDialog.FLAG_OPTIONS;
        this.options = options;
        return this;
    }

    public BDB setOptions(@StringRes int... options) {
        final CharSequence[] charSequences = new CharSequence[options.length];
        for (int i = 0; i < options.length; i++)
            charSequences[i] = context.getText(options[i]);

        return setOptions(charSequences);
    }

    public BDB setPositiveButtonListener(@Nullable BDialog.DialogBoxListener dialogBoxListener) {
        this.positiveButtonListener = dialogBoxListener;
        return addFlag(BDialog.FLAG_POSITIVE);
    }

    public BDB setNegativeButtonListener(@Nullable BDialog.DialogBoxListener dialogBoxListener) {
        this.negativeButtonListener = dialogBoxListener;
        return addFlag(BDialog.FLAG_NEGATIVE);
    }

    public BDB setInputType(int inputType) {
        this.inputType = inputType;
        return addFlag(BDialog.FLAG_INPUT);
    }

    public BDB setOptionsStartingIndex(BDialog.StartingIndexChooser startingIndexChooser) {
        this.startingIndexChooser = startingIndexChooser;
        return this;
    }

    public BDB setExtraView(@Nullable View extraView) {
        this.extraView = extraView;
        return this;
    }

    public BDB setblankActivityToAutoDismiss(blankActivity blankActivityToAutoDismiss) {
        this.blankActivityToAutoDismiss = blankActivityToAutoDismiss;
        return this;
    }

    public BDB setNegativeCustomText(@Nullable CharSequence negativeCustomText) {
        this.negativeCustomText = negativeCustomText;
        return addFlag(BDialog.FLAG_CUSTOM_NEGATIVE);
    }

    public BDB setPositiveCustomText(@Nullable CharSequence positiveCustomText) {
        this.positiveCustomText = positiveCustomText;
        return addFlag(BDialog.FLAG_CUSTOM_POSITIVE);
    }

    public BDB addFlag(int flags) {
        this.flags |= flags;
        return this;
    }

    public BDialog show() {
        return BDialog.newInstance(this);
    }
}