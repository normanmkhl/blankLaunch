 

package com.test.norman.launcher.databases.apps;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AppsDatabaseDao {
    @Query("SELECT * FROM App")
    List<App> getAll();

    @Query("SELECT * FROM App WHERE label LIKE :filter")
    List<App> getAllLike(String filter);

    @Query("SELECT * FROM App ORDER BY LOWER(label)")
    List<App> getAllOrderedByABC();

    @Query("SELECT * FROM App WHERE pinned = 1 ORDER BY label ASC")
    List<App> getAllPinned();

    @Query("UPDATE App SET pinned=:pinned WHERE id = :id")
    void update(int id, boolean pinned);

    @Query("SELECT * FROM App WHERE flatten_component_name LIKE :flattenComponentName LIMIT 1")
    App findByFlattenComponentName(String flattenComponentName);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(App... apps);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<App> apps);

    @Delete
    void delete(App app);

    @Query("DELETE FROM App WHERE id IN (:appIds)")
    void deleteByIds(int... appIds);

    @Query("DELETE FROM App")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM App")
    int getNumberOfRows();
}
