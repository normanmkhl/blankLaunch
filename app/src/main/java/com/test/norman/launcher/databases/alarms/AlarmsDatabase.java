 

package com.test.norman.launcher.databases.alarms;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Alarm.class}, version = 1, exportSchema = false)

public abstract class AlarmsDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static AlarmsDatabase alarmsDatabase = null;

    public static AlarmsDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (alarmsDatabase == null)
                alarmsDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        AlarmsDatabase.class, "alarmsbeta").allowMainThreadQueries().build();
            return alarmsDatabase;
        }
    }

    public abstract AlarmsDatabaseDao alarmsDatabaseDao();
}
