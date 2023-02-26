 

package com.test.norman.launcher.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.test.norman.launcher.R;
import com.test.norman.launcher.fragments_and_dialogs.tutorial_fragments.TutorialFragment1;
import com.test.norman.launcher.fragments_and_dialogs.tutorial_fragments.TutorialFragment2;
import com.test.norman.launcher.fragments_and_dialogs.tutorial_fragments.TutorialFragment4;
import com.test.norman.launcher.utils.BPrefs;
import com.test.norman.launcher.views.ViewPagerHolder;

public class TutorialActivity extends blankActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_activity);
        getSharedPreferences(BPrefs.KEY, MODE_PRIVATE).edit().putBoolean(BPrefs.AFTER_TUTORIAL_KEY, true).apply();
        ViewPagerHolder viewPagerHolder = findViewById(R.id.view_pager_holder);
        viewPagerHolder.setViewPagerAdapter(new Adapter(getSupportFragmentManager()));
    }

    private static class Adapter extends FragmentPagerAdapter {
        Adapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(final int position) {
            switch (position) {
                case 0:
                    return new TutorialFragment1();
                case 1:
                    return new TutorialFragment2();
                case 2:
                    return new TutorialFragment4();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_NONE;
    }
}