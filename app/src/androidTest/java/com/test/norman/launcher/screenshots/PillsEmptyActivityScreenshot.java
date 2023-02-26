 

package com.test.norman.launcher.screenshots;

import android.content.Intent;
import android.os.Handler;

import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.test.norman.launcher.activities.pills.PillsActivity;
import com.test.norman.launcher.databases.reminders.RemindersDatabase;

import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PillsEmptyActivityScreenshot extends BaseScreenshotTakerTest<PillsActivity> {

    public void test() {
        mActivityTestRule.launchActivity(new Intent());
        getInstrumentation().waitForIdleSync();
        new Handler(mActivityTestRule.getActivity().getMainLooper())
                .post(() -> {
                    final PillsActivity dis = mActivityTestRule.getActivity();
                    RemindersDatabase.getInstance(dis).remindersDatabaseDao().deleteAll();
                    dis.refreshViews();
                });
        getInstrumentation().waitForIdleSync();

    }

    @Override
    protected Class<PillsActivity> activity() {
        return PillsActivity.class;
    }
}
