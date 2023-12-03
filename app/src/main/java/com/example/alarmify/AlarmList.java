package com.example.alarmify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AlarmList extends ArrayAdapter<Alarm> {
    private ArrayList<Alarm> alarms;
    private Activity context;

    public AlarmList(Activity context, ArrayList<Alarm> alarms) {
        super(context, R.layout.item_alarm_row, alarms);
        this.context = context;
        this.alarms = alarms;
    }

    public View getView(int position, View convertview, ViewGroup parent) {
        View row = convertview;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertview == null) {
            row = inflater.inflate(R.layout.item_alarm_row, null, true);
            TextView textViewName = (TextView) row.findViewById(R.id.textViewName);
            TextView textViewTime = (TextView) row.findViewById(R.id.textViewTime);
            TextView textViewMission = (TextView) row.findViewById(R.id.textViewMission);

            Alarm currentAlarm = alarms.get(position);

            textViewName.setText(currentAlarm.getAlarmName());
            textViewTime.setText(currentAlarm.getAlarmTime());
            textViewMission.setText(currentAlarm.getAlarmMission());

            textViewTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        return row;
    }
}
