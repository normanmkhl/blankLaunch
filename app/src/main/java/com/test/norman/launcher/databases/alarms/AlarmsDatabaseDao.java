 

package com.test.norman.launcher.databases.alarms;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AlarmsDatabaseDao {
    @Query("SELECT * FROM Alarm")
    List<Alarm> getAll();

    @Query("SELECT * FROM Alarm ORDER BY hour ASC, minute ASC")
    List<Alarm> getAllSortedByTime();

    @Query("SELECT * FROM Alarm WHERE enabled = 1")
    List<Alarm> getAllEnabled();

    @Query("UPDATE Alarm SET enabled=:enabled WHERE `key` = :key")
    void update(int key, boolean enabled);

    @Query("SELECT * FROM Alarm WHERE `key` = :key LIMIT 1")
    Alarm getByKey(int key);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Alarm... alarms);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Alarm alarm);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long replace(Alarm alarm);

    @Delete
    void delete(Alarm alarm);

    @Query("DELETE FROM Alarm WHERE `key` IN (:keys)")
    void deleteByIds(int... keys);

    @Query("DELETE FROM Alarm")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM Alarm")
    int getNumberOfRows();
}
