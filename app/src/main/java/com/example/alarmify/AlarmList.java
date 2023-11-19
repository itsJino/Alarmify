package com.example.alarmify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AlarmList extends ArrayAdapter {
    private String[] name;
    private String[] alarmTime;
    private String[] mission;
    private Activity context;

    public AlarmList(Activity context, String[] alarmTime, String[] name, String[] mission) {
        super(context, R.layout.item_alarm_row, alarmTime);
        this.name = name;
        this.context = context;
        this.alarmTime = alarmTime;
        this.mission = mission;
    }

    public View getView(int position, View convertview, ViewGroup parent) {
        View row = convertview;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertview == null) {
            row = inflater.inflate(R.layout.item_alarm_row, null, true);
            TextView textViewRepeat = (TextView) row.findViewById(R.id.textViewName);
            TextView textViewTime = (TextView) row.findViewById(R.id.textViewTime);
            TextView textViewMission = (TextView) row.findViewById(R.id.textViewMission);

            textViewRepeat.setText(name[position]);
            textViewTime.setText(alarmTime[position]);
            textViewMission.setText(mission[position]);

            textViewTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        return row;
    }
}
