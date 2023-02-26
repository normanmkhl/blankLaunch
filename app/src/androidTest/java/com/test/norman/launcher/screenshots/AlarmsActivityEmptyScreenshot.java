 

package com.test.norman.launcher.screenshots;

import android.content.Intent;

import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.test.norman.launcher.activities.alarms.AlarmsActivity;

import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AlarmsActivityEmptyScreenshot extends BaseScreenshotTakerTest<AlarmsActivity> {

    public void test() {
        mActivityTestRule.launchActivity(new Intent());
    }

    @Override
    protected Class<AlarmsActivity> activity() {
        return AlarmsActivity.class;
    }
}
