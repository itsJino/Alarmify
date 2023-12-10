package com.example.alarmify;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModal extends AndroidViewModel {
    private AlarmRepository repository;
    private LiveData<List<AlarmModal>> allAlarms;

    public ViewModal(@NonNull Application application) {
        super(application);
        repository = new AlarmRepository(application);
        allAlarms = repository.getAllAlarms();
    }

    public void insert(AlarmModal model) {
        repository.insert(model);
    }

    public void update(AlarmModal model) {
        repository.update(model);
    }

    public void delete(AlarmModal model) {
        repository.delete(model);
    }

    public void deleteAllAlarms(AlarmModal model) {
        repository.deleteAllAlarms();
    }

    public LiveData<List<AlarmModal>> getAllAlarms() {
        return allAlarms;
    }
}
