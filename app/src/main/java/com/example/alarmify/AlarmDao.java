package com.example.alarmify;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlarmDao {
    @Query("SELECT * FROM alarm_table ORDER BY alarmTime DESC")
    LiveData<List<AlarmModal>>  getAllAlarms();

    @Query("DELETE FROM alarm_table")
    void deleteAllAlarms();

    @Insert
    void insert(AlarmModal model);

    @Update
    void update(AlarmModal model);

    @Delete
    void delete(AlarmModal model);


}
