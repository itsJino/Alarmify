package com.example.alarmify;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * BroadcastReceiver triggered when the alarm is received.
 * Generates a notification to alert the user.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        // Create an intent to open the NotificationActivity when the notification is clicked
        Intent nextActivity = new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Create a PendingIntent to launch the NotificationActivity
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, nextActivity, PendingIntent.FLAG_MUTABLE);

        // Build a notification with title, content, icon, and actions
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarmify")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Alarmify")
                .setContentText("WAKE YO ASS UP!!")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        // Display the notification using NotificationManagerCompat
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());
    }
}