 

package com.test.norman.launcher.activities.alarms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.blankActivity;
import com.test.norman.launcher.databases.alarms.Alarm;
import com.test.norman.launcher.databases.alarms.AlarmScheduler;
import com.test.norman.launcher.databases.alarms.AlarmsDatabase;
import com.test.norman.launcher.utils.D;

import org.joda.time.DateTime;

/**
 * Activity for creating {@link Alarm}, as Timers.
 */
public class AddTimerActivity extends blankActivity {
    private static final String TAG = AddTimerActivity.class.getSimpleName();
    static final String ALARM_KEY_AS_EXTRA_KEY = "alarm";
    private int timeIn5Minutes = 1;
    private View bt_alarm_submit, bt_add, bt_dec;
    private TextView time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alarm_quick);
        attachXml();
        genOnClickListeners();
        setupYoutube(6);
    }

    private void attachXml() {
        bt_alarm_submit = findViewById(R.id.bt_alarm_submit);
        bt_add = findViewById(R.id.bt_add);
        bt_dec = findViewById(R.id.bt_dec);
        time = findViewById(R.id.tv_number);
    }

    private void submit() {
        final String name = getString(R.string.timer);
        final DateTime now = DateTime.now();

        final Alarm alarm = new Alarm();
        alarm.setDays(-1);
        alarm.setHour(now.getHourOfDay());
        alarm.setMinute(now.getMinuteOfHour());
        alarm.addMinutes(5 * timeIn5Minutes);
        alarm.setEnabled(true);
        alarm.setName(name);

        final int key = (int) AlarmsDatabase.getInstance(this)
                .alarmsDatabaseDao().insert(alarm);
        alarm.setKey(key);
        AlarmScheduler.scheduleAlarm(alarm, this);

        setResult(
                Activity.RESULT_OK,
                new Intent()
                        .putExtra(Alarm.ALARM_KEY_VIA_INTENTS, alarm.getKey())
                        .putExtra(AddTimerActivity.ALARM_KEY_AS_EXTRA_KEY, alarm.getKey())
        );
        finish();

    }

    private void genOnClickListeners() {
        bt_alarm_submit.setOnClickListener((v) -> submit());
        bt_add.setOnClickListener(v -> {
            timeIn5Minutes++;
            updateAccordingToTime();
        });
        bt_dec.setOnClickListener(v -> {
            timeIn5Minutes--;
            updateAccordingToTime();
        });
        updateAccordingToTime();
    }

    @SuppressLint("DefaultLocale")
    private void updateAccordingToTime() {
        if (timeIn5Minutes < 1 || timeIn5Minutes > (D.DAY / (5 * D.MINUTE)))//its 288
            timeIn5Minutes = 1;
        time.setText(String.format("%d %s", timeIn5Minutes * 5, getString(R.string.minutes)));
    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_SYSTEM_ALERT_WINDOW;
    }
}
