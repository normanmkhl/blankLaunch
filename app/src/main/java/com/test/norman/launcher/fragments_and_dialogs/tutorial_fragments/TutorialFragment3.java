 

package com.test.norman.launcher.fragments_and_dialogs.tutorial_fragments;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.test.norman.launcher.R;
import com.test.norman.launcher.utils.blankToast;
import com.test.norman.launcher.views.blankButton;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * View only fragment, contains almost nothing
 */
public class TutorialFragment3 extends TutorialFragment {
    private static final String TAG = TutorialFragment3.class.getSimpleName();
    blankButton bt_set_keyboard;
    private TextView blank;

    @Override
    public void onStart() {
        super.onStart();
        firstAnimation(6000);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bt_set_keyboard != null)
            setupBtn();
    }

    @Override
    protected void attachXml() {
        bt_set_keyboard = root.findViewById(R.id.bt_set_keyboard);
        blank = root.findViewById(R.id.blank);
    }

    @Override
    protected void actualSetup() {

    }

    @Override
    protected int layoutRes() {
        return R.layout.tutorial_fragment_3;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (blank != null)
            if (isVisibleToUser) {
                firstAnimation(6000);
            }
        if (bt_set_keyboard != null)
            if (isVisibleToUser)
                setupBtn();

    }

    public void setupBtn() {

        final InputMethodManager im = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        final List<InputMethodInfo> inputMethodInfoList = im.getEnabledInputMethodList();
        boolean turnedOn = false;
        for (InputMethodInfo inputMethodInfo : inputMethodInfoList) {
            if (inputMethodInfo.getId().equals("com.blank.norman.blankLaunch/.keyboard.blankInputMethodService")) {
                turnedOn = true;
                break;
            }
        }
        if (!turnedOn)
            bt_set_keyboard.setOnClickListener((v) -> {
                v.getContext().startActivity(new Intent("android.settings.INPUT_METHOD_SETTINGS"));
            });
        else {
            bt_set_keyboard.setText(R.string.already_setted);
            bt_set_keyboard.setOnClickListener(v -> {
                try {
                    ((InputMethodManager) getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                    blankToast.error(getContext());
                }
            });

        }
    }

    public void firstAnimation(final long msDuration) {
        final String theString = getString(R.string.have_hard_time_using_the_phone_s_keyboard_use_blankLaunch_s_big_keyboard_instead);
        final int theStringLength = theString.length();
        final Animation animation = new Animation() {
            int a = 0;

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime * theStringLength > a) {
                    a++;
                    blank.setText(theString.substring(0, a));
                }
            }
        };

        animation.setDuration(msDuration);
        blank.startAnimation(animation);
    }

}
