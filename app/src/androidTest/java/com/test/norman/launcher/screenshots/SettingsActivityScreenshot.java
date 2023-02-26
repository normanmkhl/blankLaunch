 

package com.test.norman.launcher.screenshots;

import android.content.Intent;

import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.test.norman.launcher.activities.SettingsActivity;

import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsActivityScreenshot extends BaseScreenshotTakerTest<SettingsActivity> {

    public void test() {
        getInstrumentation().waitForIdleSync();
        mActivityTestRule.launchActivity(new Intent());
        getInstrumentation().waitForIdleSync();
    }

    @Override
    protected Class<SettingsActivity> activity() {
        return SettingsActivity.class;
    }
}
