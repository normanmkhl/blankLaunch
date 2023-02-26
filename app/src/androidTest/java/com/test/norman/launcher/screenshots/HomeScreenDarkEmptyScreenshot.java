 

package com.test.norman.launcher.screenshots;

import android.content.Intent;

import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.test.norman.launcher.activities.HomeScreenActivity;
import com.test.norman.launcher.utils.BPrefs;

import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeScreenDarkEmptyScreenshot extends BaseScreenshotTakerTest<HomeScreenActivity> {

    public void test() {
        mActivityTestRule.launchActivity(new Intent());
    }

    @Override
    protected Class<HomeScreenActivity> activity() {
        return HomeScreenActivity.class;
    }

    @Override
    protected int theme() {
        return BPrefs.Themes.DARK;
    }
}
