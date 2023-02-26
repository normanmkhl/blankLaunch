 

package com.test.norman.launcher.databases.alarms;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;

/**
 * Alarm class.
 * using this old java getters and setters because Room requires that.
 * see {@link Entity}
 */
@Entity
public class Alarm {
    //NOTE: Snoozes should NEVER be in this database.

    @Ignore
    public static final String ALARM_KEY_VIA_INTENTS = "alarm";

    @PrimaryKey(autoGenerate = true)
    private int key;

    @ColumnInfo(name = "days")
    private int days;

    @ColumnInfo(name = "enabled")
    private boolean enabled;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "hour")
    private int hour;

    @ColumnInfo(name = "minute")
    private int minute;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Ignore
    public void addMinutes(int minutes) {
        this.minute += minutes;
        while (minute >= 60) {
            minute -= 60;
            hour++;
            if (hour >= 24)
                hour -= 24;
        }
    }

    @Ignore
    @Nullable
    public Calendar getNext() {
        if (isEnabled()) {
            Calendar a = Calendar.getInstance();
            a.setTimeInMillis(AlarmScheduler.nextTimeAlarmWillWorkInMs(this));
            return a;
        }
        return null;
    }
}
