 

package com.test.norman.launcher.screenshots;

import android.content.Intent;

import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.alarms.AlarmsActivity;
import com.test.norman.launcher.databases.alarms.Alarm;
import com.test.norman.launcher.databases.alarms.AlarmsDatabase;
import com.test.norman.launcher.utils.D;

import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AlarmsActivityFullScreenshot extends BaseScreenshotTakerTest<AlarmsActivity> {

    public void test() {
        final Alarm alarm = new Alarm();
        alarm.setDays(D.Days.ALL);
        alarm.setHour(8);
        alarm.setMinute(30);
        alarm.setEnabled(true);
        alarm.setName(getInstrumentation().getTargetContext().getString(R.string.morning));
        AlarmsDatabase.getInstance(getInstrumentation().getTargetContext()).alarmsDatabaseDao().insert(alarm);
        mActivityTestRule.launchActivity(new Intent());
        AlarmsDatabase.getInstance(getInstrumentation().getTargetContext()).alarmsDatabaseDao().deleteAll();
    }

    @Override
    protected Class<AlarmsActivity> activity() {
        return AlarmsActivity.class;
    }
}
