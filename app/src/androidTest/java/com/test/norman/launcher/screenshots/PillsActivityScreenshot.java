 

package com.test.norman.launcher.screenshots;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.pills.AddPillActivity;
import com.test.norman.launcher.activities.pills.PillsActivity;
import com.test.norman.launcher.databases.reminders.Reminder;
import com.test.norman.launcher.databases.reminders.RemindersDatabase;
import com.test.norman.launcher.utils.D;

import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PillsActivityScreenshot extends BaseScreenshotTakerTest<PillsActivity> {

    public void test() {
        mActivityTestRule.launchActivity(new Intent());
        getInstrumentation().waitForIdleSync();
        new Handler(mActivityTestRule.getActivity().getMainLooper())
                .post(() -> {
                    final PillsActivity dis = mActivityTestRule.getActivity();

                    final Reminder medication_1 = new Reminder();
                    medication_1.setStartingTime(Reminder.TIME_MORNING);
                    medication_1.setDays(D.Days.ALL ^ D.Days.SUNDAY);
                    medication_1.setTextualContent(dis.getString(R.string.medication_1));
                    medication_1.setBinaryContentType(Reminder.BINARY_RGB);
                    medication_1.setReminderType(Reminder.TYPE_PILL);
                    final int color_1 = ActivityCompat.getColor(dis, AddPillActivity.COLORS[0]);
                    medication_1.setBinaryContent(new byte[]{(byte) Color.red(color_1), (byte) Color.green(color_1), (byte) Color.blue(color_1)});

                    final Reminder medication_2 = new Reminder();
                    medication_2.setStartingTime(Reminder.TIME_MORNING);
                    medication_2.setDays(D.Days.SUNDAY);
                    medication_2.setTextualContent(dis.getString(R.string.medication_2));
                    medication_2.setBinaryContentType(Reminder.BINARY_RGB);
                    medication_2.setReminderType(Reminder.TYPE_PILL);
                    final int color_2 = ActivityCompat.getColor(dis, AddPillActivity.COLORS[4]);
                    medication_2.setBinaryContent(new byte[]{(byte) Color.red(color_2), (byte) Color.green(color_2), (byte) Color.blue(color_2)});
                    RemindersDatabase.getInstance(dis).remindersDatabaseDao().insertAll(medication_1, medication_2);

                    dis.refreshViews();
                });
        getInstrumentation().waitForIdleSync();

    }

    @Override
    protected void cleanupAfterTest() {
        super.cleanupAfterTest();
        RemindersDatabase.getInstance(getInstrumentation().getTargetContext().getApplicationContext()).remindersDatabaseDao().deleteAll();
    }

    @Override
    protected Class<PillsActivity> activity() {
        return PillsActivity.class;
    }
}
