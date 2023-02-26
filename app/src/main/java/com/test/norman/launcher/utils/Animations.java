 

package com.test.norman.launcher.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.test.norman.launcher.R;

public class Animations {

    public static void makeBiggerAndSmaller(final Context context, final View view, final Runnable runnable) {
        final Animation enlarge =
                AnimationUtils.loadAnimation(context, R.anim.enlarge);
        final Animation ensmall =
                AnimationUtils.loadAnimation(context, R.anim.ensmall);
        enlarge.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(ensmall);
                if (runnable != null)
                    runnable.run();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ensmall.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(enlarge);
                if (runnable != null)
                    runnable.run();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(enlarge);
    }
}
