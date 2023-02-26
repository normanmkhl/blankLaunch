 

package com.test.norman.launcher.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.test.norman.launcher.activities.CrashActivity;

import org.acra.ACRA;

public class blankUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Context context;
    private final Thread.UncaughtExceptionHandler defaultUEH;

    public blankUncaughtExceptionHandler(Context context, Thread.UncaughtExceptionHandler defaultUEH) {
        this.context = context;
        this.defaultUEH = defaultUEH;
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        final SharedPreferences blankPrefs = BPrefs.get(context);
        final long currentTime = System.currentTimeMillis();
        if (currentTime - blankPrefs.getLong(BPrefs.LAST_CRASH_KEY, -1) < BPrefs.LAST_CRASH_TIME_OK) {
            defaultUEH.uncaughtException(t, e);
            return;
        }
        blankPrefs.edit().putLong(BPrefs.LAST_CRASH_KEY, currentTime).commit(); // commit and not apply because of System.exit(2)
        S.logImportant("blankLaunch CRASHED!");
        if (blankPrefs.getBoolean(BPrefs.CRASH_REPORTS_KEY, BPrefs.CRASH_REPORTS_DEFAULT_VALUE))
            ACRA.getErrorReporter().handleException(e);

        final PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context,
                        19337,
                        new Intent(context, CrashActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        | Intent.FLAG_ACTIVITY_NEW_TASK),
                        PendingIntent.FLAG_ONE_SHOT
                );

        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 300 * D.MILLISECOND, pendingIntent);

        Runtime.getRuntime().exit(2);
    }
}