 

package com.test.norman.launcher.fragments_and_dialogs.tutorial_fragments;

import android.content.Context;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.FakeLauncherActivity;
import com.test.norman.launcher.utils.BPrefs;
import com.test.norman.launcher.views.blankButton;

public class TutorialFragment4 extends TutorialFragment {
    blankButton bt_home;

    @Override
    protected void attachXml() {
        bt_home = root.findViewById(R.id.bt_home);
    }

    @Override
    protected void actualSetup() {
        bt_home.setOnClickListener(v -> {
            FakeLauncherActivity.resetPreferredLauncherAndOpenChooser(v.getContext());
            v.getContext().getSharedPreferences(BPrefs.KEY, Context.MODE_PRIVATE).edit().putBoolean(BPrefs.AFTER_TUTORIAL_KEY, true).apply();
        });
    }

    @Override
    protected int layoutRes() {
        return R.layout.tutorial_fragment_4;
    }
}
