package com.example.alarmify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewModal viewmodal;

    // Views initialization
    private RecyclerView alarmRV;

    // Request codes for startActivityForResult
    private static final int ADD_ALARM_REQUEST = 1;
    private static final int EDIT_ALARM_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        alarmRV = findViewById(R.id.listAlarms);
        Button addAlarm = findViewById(R.id.addAlarm);
        ImageButton openReminderButton = findViewById(R.id.openReminderActivity);

        // Setting click listeners for buttons
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addAlarmActivity.class);
                startActivityForResult(intent, ADD_ALARM_REQUEST); // Start addAlarmActivity for adding an alarm
            }
        });

        openReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReminderActivity.class);
                startActivity(intent); // Start ReminderActivity
            }
        });

        // Setting up RecyclerView
        alarmRV.setLayoutManager(new GridLayoutManager(this, 1));
        alarmRV.setHasFixedSize(true);

        final AlarmRVAdapter adapter = new AlarmRVAdapter(); // Adapter for the RecyclerView

        alarmRV.setAdapter(adapter);
        viewmodal = new ViewModelProvider(this).get(ViewModal.class); // ViewModel initialization

        // Observing changes in the list of alarms and updating the RecyclerView
        viewmodal.getAllAlarms().observe(this, new Observer<List<AlarmModal>>() {
            @Override
            public void onChanged(List<AlarmModal> models) {
                adapter.submitList(models); // Update the RecyclerView with the new list of alarms
            }
        });

        // Swipe-to-delete functionality using ItemTouchHelper
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewmodal.delete(adapter.getAlarmAt(viewHolder.getAdapterPosition())); // Delete an alarm
                Toast.makeText(MainActivity.this, "Alarm deleted", Toast.LENGTH_SHORT).show(); // Show deletion message
            }
        }).
                attachToRecyclerView(alarmRV);

        // Handling item clicks in the RecyclerView
        adapter.setOnItemClickListener(new AlarmRVAdapter.OnItemClickListener() {
            @Override
            // Start addAlarmActivity for editing an existing alarm
            public void onItemClick(AlarmModal model) {
                // Create an intent to open addAlarmActivity for editing an existing alarm
                Intent intent = new Intent(MainActivity.this, addAlarmActivity.class);

                // Pass alarm details as extras to the intent for editing
                intent.putExtra(addAlarmActivity.EXTRA_ID, model.getId());
                intent.putExtra(addAlarmActivity.EXTRA_ALARM_NAME, model.getAlarmName());
                intent.putExtra(addAlarmActivity.EXTRA_ALARM_TIME, model.getAlarmTime());

                // Start addAlarmActivity with the intent to edit the alarm
                startActivityForResult(intent, EDIT_ALARM_REQUEST);
            }
        });
    }

    // Method called when returning from another activity started for a result
    protected void onActivityResult(int requestCode,int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handling the results returned from activities started for results
        if (requestCode == ADD_ALARM_REQUEST && resultCode == RESULT_OK) {
            // Handling result from addAlarmActivity for adding an alarm
            if (data != null && data.hasExtra(addAlarmActivity.EXTRA_ALARM_NAME)
                    && data.hasExtra(addAlarmActivity.EXTRA_ALARM_TIME)) {

                // Extracting alarm details from the result intent
                String alarmName = data.getStringExtra(addAlarmActivity.EXTRA_ALARM_NAME);
                String alarmTime = data.getStringExtra(addAlarmActivity.EXTRA_ALARM_TIME);

                // Creating a new AlarmModal object with the retrieved details
                AlarmModal model = new AlarmModal(alarmName, alarmTime);

                // Inserting the new alarm into the ViewModel
                viewmodal.insert(model);

                // Displaying a toast message confirming the successful save of the alarm
                Toast.makeText(this, "Alarm Saved!!", Toast.LENGTH_SHORT).show();
            } else {
                // Displaying a toast message indicating that the alarm was not saved due to missing data
                Toast.makeText(this, "Alarm not saved", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == EDIT_ALARM_REQUEST && resultCode == RESULT_OK) {
            // Handling the result from addAlarmActivity for editing an existing alarm
            int id = data.getIntExtra(addAlarmActivity.EXTRA_ID, -1);
            if(id == -1) {
                // Displaying a toast message indicating that the alarm couldn't be updated due to missing ID
                Toast.makeText(this, "Alarm can't be updated", Toast.LENGTH_SHORT).show();
            }

            // Extracting updated alarm details from the result intent
            String alarmName = data.getStringExtra(addAlarmActivity.EXTRA_ALARM_NAME);
            String alarmTime = data.getStringExtra(addAlarmActivity.EXTRA_ALARM_TIME);

            // Creating an AlarmModal object with the updated details
            AlarmModal model = new AlarmModal(alarmName, alarmTime);

            // Setting the ID for the updated alarm
            model.setId(id);

            // Updating the existing alarm in the ViewModel
            viewmodal.update(model);

            // Displaying a toast message confirming the successful update of the alarm
            Toast.makeText(this, "Alarm updated", Toast.LENGTH_SHORT).show();
        } else {
            // Displaying a toast message indicating that the alarm was not saved or updated
            Toast.makeText(this, "Alarm not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
