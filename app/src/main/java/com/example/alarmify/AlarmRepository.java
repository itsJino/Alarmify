package com.example.alarmify;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import java.util.List;

public class AlarmRepository {
    private AlarmDao alarmDao; // Data Access Object to interact with the database
    private LiveData<List<AlarmModal>> allAlarms; // LiveData containing all alarms

    // Constructor initializing the database and accessing the DAO
    public AlarmRepository(Application application) {
        AlarmDatabase database = AlarmDatabase.getInstance(application);
        alarmDao = database.AlarmDao();
        allAlarms = alarmDao.getAllAlarms();
    }

    // Methods for database operations using AsyncTask classes
    public void insert(AlarmModal model) {
        new InsertAlarmAsyncTask(alarmDao).execute(model);
    }

    public void update(AlarmModal model) {
        new UpdateAlarmAsyncTask(alarmDao).execute(model);
    }

    public void delete(AlarmModal model) {
        new DeleteAlarmAsyncTask(alarmDao).execute(model);
    }

    public void deleteAllAlarms() {
        new DeleteAllAlarmsAsyncTask(alarmDao).execute();
    }

    public LiveData<List<AlarmModal>> getAllAlarms() {
        return allAlarms;
    }

    // AsyncTask classes for performing database operations in the background
    private static class InsertAlarmAsyncTask extends AsyncTask<AlarmModal, Void, Void> {
        private AlarmDao alarmDao;

        private InsertAlarmAsyncTask(AlarmDao alarmDao) {
            this.alarmDao = alarmDao;
        }

        @Override
        protected Void doInBackground(AlarmModal... model) {
            alarmDao.insert(model[0]);
            return null;
        }
    }

    private static class UpdateAlarmAsyncTask extends AsyncTask<AlarmModal, Void, Void> {
        private AlarmDao alarmDao;

        private UpdateAlarmAsyncTask(AlarmDao alarmDao) {
            this.alarmDao = alarmDao;
        }

        @Override
        protected Void doInBackground(AlarmModal... models) {
            alarmDao.update(models[0]);
            return null;
        }
    }

    private static class DeleteAlarmAsyncTask extends AsyncTask<AlarmModal, Void, Void> {
        private AlarmDao alarmDao;

        private DeleteAlarmAsyncTask(AlarmDao alarmDao) {
            this.alarmDao = alarmDao;
        }

        @Override
        protected Void doInBackground(AlarmModal... models) {
            alarmDao.delete(models[0]);
            return null;
        }
    }

    private static class DeleteAllAlarmsAsyncTask extends AsyncTask<AlarmModal, Void, Void> {
        private AlarmDao alarmDao;

        private DeleteAllAlarmsAsyncTask(AlarmDao alarmDao) {
            this.alarmDao = alarmDao;
        }

        @Override
        protected Void doInBackground(AlarmModal... voids) {
            alarmDao.deleteAllAlarms();
            return null;
        }
    }
}
