package com.example.alarmify;

public class Alarm {
    private String alarmName;
    private String alarmTime;
    private String alarmMission;

    public String getAlarmName() {
        return alarmName;
    }

    public String getAlarmMission() {
        return alarmMission;
    }

    public void setAlarmMission(String alarmMission) {
        this.alarmMission = alarmMission;
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

    public Alarm(String alarmName, String alarmTime, String alarmMission) {
        this.alarmName = alarmName;
        this.alarmTime = alarmTime;
        this.alarmMission = alarmMission;
    }
}
