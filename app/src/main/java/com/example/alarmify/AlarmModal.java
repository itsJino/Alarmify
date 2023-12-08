package com.example.alarmify;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarm_table")
public class AlarmModal {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String alarmName;
    private String alarmTime;

    public AlarmModal(String alarmName, String alarmTime) {
        this.alarmName = alarmName;
        this.alarmTime = alarmTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }
}
