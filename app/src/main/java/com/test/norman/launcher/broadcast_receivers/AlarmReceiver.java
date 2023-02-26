 

package com.test.norman.launcher.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.test.norman.launcher.activities.alarms.AlarmScreenActivity;
import com.test.norman.launcher.databases.alarms.Alarm;
import com.test.norman.launcher.databases.alarms.AlarmScheduler;
import com.test.norman.launcher.databases.alarms.AlarmsDatabase;

/**
 * the middle man between the {@link AlarmScheduler} and {@link AlarmScreenActivity}.
 * the reason for having this is the wake lock the system creates for broadcast receivers.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        final int key = intent.getIntExtra(Alarm.ALARM_KEY_VIA_INTENTS, -1);
        if (key == -1) throw new IllegalArgumentException("set alarm key!");
        final Alarm alarm = AlarmsDatabase.getInstance(context).alarmsDatabaseDao().getByKey(key);
        if (alarm == null) {
            Log.e(TAG, "onReceive: AlarmsDatabase.getInstance(context).alarmsDatabaseDao().getByKey(key) == null");
            return;
        } else if (!alarm.isEnabled()) {
            Log.e(TAG, "!alarm.isEnabled(), yet, most probably because of snooze...");
        }

        final Context appContext = context.getApplicationContext();
        appContext.startActivity(new Intent(appContext, AlarmScreenActivity.class)
                .putExtra(Alarm.ALARM_KEY_VIA_INTENTS, key)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }
}
