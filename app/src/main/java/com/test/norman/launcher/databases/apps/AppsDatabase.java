 

package com.test.norman.launcher.databases.apps;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {App.class}, version = 1, exportSchema = false)
public abstract class AppsDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static AppsDatabase appsDatabase = null;

    public static AppsDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (appsDatabase == null)
                appsDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        AppsDatabase.class, "applications").allowMainThreadQueries().build();
            return appsDatabase;
        }
    }

    public abstract AppsDatabaseDao appsDatabaseDao();
}
