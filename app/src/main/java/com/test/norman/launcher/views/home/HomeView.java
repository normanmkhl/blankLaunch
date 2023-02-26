 

package com.test.norman.launcher.views.home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.test.norman.launcher.activities.HomeScreenActivity;

public abstract class HomeView extends FrameLayout {
    protected final HomeScreenActivity homeScreen;
    protected final Activity activity;

    public HomeView(HomeScreenActivity homeScreen, Activity activity) {
        super(homeScreen == null ? activity : homeScreen);
        this.homeScreen = homeScreen;
        this.activity = activity;
        addView(onCreateView(LayoutInflater.from(activity), this), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public abstract View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup);

}
