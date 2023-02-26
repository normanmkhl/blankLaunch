 

package com.test.norman.launcher.databases.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.test.norman.launcher.activities.HomeScreenActivity;
import com.test.norman.launcher.activities.alarms.AlarmsActivity;
import com.test.norman.launcher.broadcast_receivers.AlarmReceiver;
import com.test.norman.launcher.utils.D;
import com.test.norman.launcher.utils.S;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import java.util.List;

/**
 *
 */
public class AlarmScheduler {
    private static final String TAG = AlarmScheduler.class.getSimpleName();
    public static final Object LOCK = new Object();
    public static final int SNOOZE_MILLIS = 5 * D.MINUTE;

    private AlarmScheduler() {
    }

    public static void cancelAlarm(int key, Context context) {
        synchronized (LOCK) {
            _cancelAlarm(key, context);
        }
    }

    // blankDay -
    /*
          sunday = 1
          saturday = 64
          monday =2
          blankDay = 1<<israeliDay...
     */

    // JodaDay -
    /*
        sunday =7
        saturday =6
        monday =1
        jodaDay = day in cristian countries
     */

    private static int getblankDay() {
        int today = DateTime.now().getDayOfWeek();
        if (today == 7)
            today = 0;
        today = 1 << today;
        return today;
    }

    private static int blankDayToJodaDay(int blankDay) {
        int day = 0;
        while (blankDay != 0) {
            day++;
            blankDay >>= 1;
        }
        day -= 1;

        if (day == 0)
            day = 7;
        return day;
    }

    public static long nextTimeAlarmWillWorkInMsFromNow(@NonNull Alarm alarm) {
        return nextTimeAlarmWillWorkInMs(alarm) - DateTime.now().getMillis();
    }

    static long nextTimeAlarmWillWorkInMs(@NonNull Alarm alarm) {
        final MutableDateTime mDateTime = MutableDateTime.now();
        {   //creating a date of today with the hours and minutes of the alarm
            mDateTime.setMillisOfSecond(0);
            mDateTime.setSecondOfMinute(0);
            mDateTime.setHourOfDay(alarm.getHour());
            mDateTime.setMinuteOfHour(alarm.getMinute());
        }

        final int blankDay = getblankDay();

        {   //today or one time
            if ((alarm.getDays() & blankDay) == blankDay) {//today may have an alarm
                if ((alarm.getDays() == blankDay)) {
                    if (mDateTime.isBeforeNow())
                        mDateTime.addWeeks(1);  //next week if today's time already passed
                    return mDateTime.getMillis();
                } else {
                    if (mDateTime.isAfterNow())
                        return mDateTime.getMillis();
                }
            } else if (alarm.getDays() == -1) {
                if (mDateTime.isBeforeNow())
                    mDateTime.addDays(1);
                return mDateTime.getMillis();
            }
        }
        int selectedblankDay = blankDay;

        {   //find next day
            for (int i = blankDay << 1; i != blankDay; i <<= 1) {
                if (i > D.Days.SATURDAY)
                    i = D.Days.SUNDAY;

                if ((alarm.getDays() & i) == i) {
                    selectedblankDay = i;
                    break;
                }

            }
        }

        int day = blankDayToJodaDay(selectedblankDay);
        mDateTime.setDayOfWeek(day);
        if (mDateTime.isBeforeNow()) {
            mDateTime.addWeeks(1);
        }
        return mDateTime.getMillis();
    }

    private static void _cancelAlarm(int key, Context context) {
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).cancel(getIntent(context, key));
    }

    public static void scheduleAlarm(@NonNull Alarm alarm, @NonNull Context context) throws IllegalArgumentException {
        synchronized (LOCK) {
            S.logImportant("Scheduling an alarm!");
            final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            final long nextTimeAlarmWillWorkInMs = nextTimeAlarmWillWorkInMs(alarm);
            alarmManager.setAlarmClock(
                    new AlarmManager.AlarmClockInfo(
                            nextTimeAlarmWillWorkInMs,
                            PendingIntent.getActivity(context, 0, new Intent(context, AlarmsActivity.class), 0)
                    ),
                    getIntent(context, alarm.getKey())
            );
        }
    }

    private static PendingIntent getIntent(Context context, int alarmKey) {
        Log.e(TAG, "getIntent: ");
        Intent intent = new Intent(context, AlarmReceiver.class).putExtra(Alarm.ALARM_KEY_VIA_INTENTS, alarmKey);
        return PendingIntent.getBroadcast(context, alarmKey, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void scheduleSnooze(@NonNull Alarm alarm, Context context) throws IllegalArgumentException {
        synchronized (LOCK) {
            final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setAlarmClock(
                    new AlarmManager.AlarmClockInfo(
                            DateTime.now().getMillis() + SNOOZE_MILLIS,
                            PendingIntent.getActivity(context, alarm.getKey(), new Intent(context, HomeScreenActivity.class), 0)//TODO??
                    ),
                    getIntent(context, alarm.getKey())
            );
        }
    }

    public static void reStartAlarms(final Context context) {
        S.logImportant("reStartAlarms was called!");
        synchronized (LOCK) {
            S.logImportant("reStartAlarms was started!");
            final List<Alarm> alarmList = AlarmsDatabase.getInstance(context).alarmsDatabaseDao().getAllEnabled();
            for (Alarm alarm : alarmList) {
                AlarmScheduler.cancelAlarm(alarm.getKey(), context);
                AlarmScheduler.scheduleAlarm(alarm, context);
            }
            S.logImportant("reStartAlarms has finished!");

        }
    }
}



