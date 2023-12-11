package com.example.alarmify;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewModal viewmodal;

    private RecyclerView alarmRV;
    private static final int ADD_ALARM_REQUEST = 1;
    private static final int EDIT_ALARM_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmRV = findViewById(R.id.listAlarms);
        Button addAlarm = findViewById(R.id.addAlarm);
        ImageButton openReminderButton = findViewById(R.id.openReminderActivity);

        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addAlarmActivity.class);
                startActivityForResult(intent, ADD_ALARM_REQUEST);
            }
        });

        openReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReminderActivity.class);
                startActivity(intent);
            }
        });

        alarmRV.setLayoutManager(new GridLayoutManager(this, 1));
        alarmRV.setHasFixedSize(true);

        final AlarmRVAdapter adapter = new AlarmRVAdapter();

        alarmRV.setAdapter(adapter);
        viewmodal = new ViewModelProvider(this).get(ViewModal.class);

        viewmodal.getAllAlarms().observe(this, new Observer<List<AlarmModal>>() {
            @Override
            public void onChanged(List<AlarmModal> models) {
                adapter.submitList(models);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewmodal.delete(adapter.getAlarmAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Alarm deleted", Toast.LENGTH_SHORT).show();
            }
        }).
                attachToRecyclerView(alarmRV);

        adapter.setOnItemClickListener(new AlarmRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AlarmModal model) {
                Intent intent = new Intent(MainActivity.this, addAlarmActivity.class);
                intent.putExtra(addAlarmActivity.EXTRA_ID, model.getId());
                intent.putExtra(addAlarmActivity.EXTRA_ALARM_NAME, model.getAlarmName());
                intent.putExtra(addAlarmActivity.EXTRA_ALARM_TIME, model.getAlarmTime());

                startActivityForResult(intent, EDIT_ALARM_REQUEST);
            }
        });
    }

    protected void onActivityResult(int requestCode,int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ALARM_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra(addAlarmActivity.EXTRA_ALARM_NAME)
                    && data.hasExtra(addAlarmActivity.EXTRA_ALARM_TIME)) {
                String alarmName = data.getStringExtra(addAlarmActivity.EXTRA_ALARM_NAME);
                String alarmTime = data.getStringExtra(addAlarmActivity.EXTRA_ALARM_TIME);
                AlarmModal model = new AlarmModal(alarmName, alarmTime);
                viewmodal.insert(model);
                Toast.makeText(this, "Alarm Saved!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Alarm not saved", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == EDIT_ALARM_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(addAlarmActivity.EXTRA_ID, -1);
            if(id == -1) {
                Toast.makeText(this, "Alarm can't be updated", Toast.LENGTH_SHORT).show();
            }
            String alarmName = data.getStringExtra(addAlarmActivity.EXTRA_ALARM_NAME);
            String alarmTime = data.getStringExtra(addAlarmActivity.EXTRA_ALARM_TIME);
            AlarmModal model = new AlarmModal(alarmName, alarmTime);
            model.setId(id);
            viewmodal.update(model);
            Toast.makeText(this, "Alarm updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Alarm not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
