 

package com.test.norman.launcher.databases.reminders;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Reminder.class}, version = 1, exportSchema = false)
public abstract class RemindersDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static RemindersDatabase remindersDatabase = null;

    public static RemindersDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (remindersDatabase == null)
                remindersDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        RemindersDatabase.class, "reminders")
                        .allowMainThreadQueries()
                        .build();
            return remindersDatabase;
        }
    }

    public abstract RemindersDatabaseDao remindersDatabaseDao();
}
