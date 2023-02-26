 

package com.test.norman.launcher.databases.reminders;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RemindersDatabaseDao {

    @Query("SELECT * FROM Reminder")
    List<Reminder> getAllReminders();

    @Query("SELECT * FROM Reminder WHERE `id` = :id LIMIT 1")
    Reminder getById(int id);

    @Query("SELECT * FROM Reminder ORDER BY starting_time ASC")
    List<Reminder> getAllRemindersOrderedByTime();

    @Query("DELETE FROM Reminder WHERE id = :id")
    void removeReminder(int id);

    @Query("DELETE FROM Reminder WHERE id IN (:ids)")
    void removeReminders(int... ids);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Reminder... reminders);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Reminder reminders);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long replace(Reminder reminder);

    @Query("SELECT COUNT(*) FROM Reminder")
    int getNumberOfRows();

    @Query("DELETE FROM Reminder")
    void deleteAll();
}