 

package com.test.norman.launcher.activities.pills;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.blankActivity;
import com.test.norman.launcher.databases.reminders.ReminderScheduler;
import com.test.norman.launcher.utils.BPrefs;
import com.test.norman.launcher.views.blankMultipleSelection;
import com.test.norman.launcher.views.blankNumberChooser;

public class PillTimeSetterActivity extends blankActivity {
    private blankNumberChooser hour, minute;
    private blankMultipleSelection multipleSelection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_time_setter);
        hour = findViewById(R.id.chooser_hours);
        minute = findViewById(R.id.chooser_minutes);
        multipleSelection = findViewById(R.id.multiple_selection);
        multipleSelection.addSelection(R.string.morning, R.string.afternoon, R.string.evening);
        applyChoosers();

        multipleSelection.setOnItemClickListener(i -> applyChoosers());

        final View.OnClickListener onClickListener =
                v -> BPrefs.setHourAndMinute(
                        this,
                        multipleSelection.getSelection(),
                        hour.getNumber(),
                        minute.getNumber());
        hour.setOnClickListener(onClickListener);
        minute.setOnClickListener(onClickListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        ReminderScheduler.reStartReminders(this);
    }

    private void applyChoosers() {
        hour.setNumber(BPrefs.getHour(multipleSelection.getSelection(), this));
        minute.setNumber(BPrefs.getMinute(multipleSelection.getSelection(), this));
    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_SYSTEM_ALERT_WINDOW;
    }
}
