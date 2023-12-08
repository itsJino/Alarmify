package com.example.alarmify;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {AlarmModal.class}, version = 1)
public abstract class AlarmDatabase extends RoomDatabase{
    // Create instance for database class
    public static AlarmDatabase instance;

    // Create abstract variable for dao
    public abstract AlarmDao AlarmDao();

    // Getting instance from database
    public static synchronized AlarmDatabase getInstance(Context context) {
        // Check if instance is null or not
        if (instance == null) {
            instance =
                    // For Creating instance for database - creating a database builder + passing our database class with database name
                    Room.databaseBuilder(context.getApplicationContext(),
                        AlarmDatabase.class, "alarm_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Method called when database is created
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDbAsyncTask(AlarmDatabase instance) {
            AlarmDao alarmDao = instance.AlarmDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
