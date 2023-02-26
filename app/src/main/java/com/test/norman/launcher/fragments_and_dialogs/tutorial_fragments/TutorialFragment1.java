 

package com.test.norman.launcher.fragments_and_dialogs.tutorial_fragments;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.norman.launcher.R;

public class TutorialFragment1 extends TutorialFragment {
    private TextView hello, and_welcome_to_blankLaunch;

    protected void attachXml() {
        hello = root.findViewById(R.id.hello);
        and_welcome_to_blankLaunch = root.findViewById(R.id.and_welcome_to_blankLaunch);
    }

    private void firstAnimation(final View view, final Runnable afterAnimation, final long msDuration) {
        final LinearLayout.LayoutParams layoutParamsBefore = (LinearLayout.LayoutParams) view.getLayoutParams();
        final int topMarginBefore = layoutParamsBefore.topMargin;
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                layoutParamsBefore.topMargin = (int) ((1 - interpolatedTime) * topMarginBefore);
                view.setLayoutParams(layoutParamsBefore);
            }
        };
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                afterAnimation.run();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        animation.setDuration(msDuration);
        view.startAnimation(animation);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (hello != null)
            if (isVisibleToUser) {
                firstAnimation(hello, () -> and_welcome_to_blankLaunch.setVisibility(View.VISIBLE), 2000);
            }
    }

    @Override
    protected void actualSetup() {
        firstAnimation(hello, () -> and_welcome_to_blankLaunch.setVisibility(View.VISIBLE), 2000);
    }

    @Override
    protected int layoutRes() {
        return R.layout.tutorial_fragment_1;
    }
}