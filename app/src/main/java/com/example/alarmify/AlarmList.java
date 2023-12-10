package com.example.alarmify;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class AlarmList extends ArrayAdapter<AlarmModal> {
    private Context context;
    private List<AlarmModal> alarms;
    private AlarmDao alarmDao;

    public AlarmList(Context context, List<AlarmModal> alarms) {
        super(context, R.layout.item_alarm_row, alarms);
        this.context = context;
        this.alarms = alarms;
        this.alarmDao = AlarmDatabase.getInstance(context).AlarmDao();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_alarm_row, parent, false);
        }

        TextView textViewName = row.findViewById(R.id.textViewName);
        TextView textViewTime = row.findViewById(R.id.textViewTime);

        AlarmModal currentAlarm = alarms.get(position);

        textViewName.setText(currentAlarm.getAlarmName());
        textViewTime.setText(currentAlarm.getAlarmTime());
        // If you have more fields to display, update them accordingly

        return row;
    }
}
