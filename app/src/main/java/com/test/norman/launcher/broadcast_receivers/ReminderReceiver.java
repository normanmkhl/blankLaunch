 

package com.test.norman.launcher.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.test.norman.launcher.activities.pills.PillScreenActivity;
import com.test.norman.launcher.databases.reminders.Reminder;
import com.test.norman.launcher.databases.reminders.ReminderScheduler;
import com.test.norman.launcher.databases.reminders.RemindersDatabase;

/**
 * the middle man between the {@link ReminderScheduler} and {@link PillScreenActivity}.
 * the reason for having this is the wake lock the system creates for broadcast receivers.
 */
public class ReminderReceiver extends BroadcastReceiver {
    private static final String TAG = ReminderReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        final int id = intent.getIntExtra(Reminder.REMINDER_KEY_VIA_INTENTS, -1);
        if (id == -1) throw new IllegalArgumentException("set reminder id!");
        final Reminder reminder = RemindersDatabase.getInstance(context).remindersDatabaseDao().getById(id);
        if (reminder == null) {
            Log.e(TAG, "onReceive: RemindersDatabase.getInstance(context).remindersDatabaseDao().getByKey(id) == null");
            return;
        }

        final Context appContext = context.getApplicationContext();
        appContext.startActivity(new Intent(appContext, PillScreenActivity.class)
                .putExtra(Reminder.REMINDER_KEY_VIA_INTENTS, id)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }
}
