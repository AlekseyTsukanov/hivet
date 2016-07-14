package com.acukanov.hivet.utils;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import com.acukanov.hivet.R;
import com.acukanov.hivet.ui.main.MainActivity;
import com.acukanov.hivet.ui.start.StartActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationsUtils {

    public static void createNotificationWithStartActivity(Context context, int notificationId, String userName, String messageText) {
        Intent intent = new Intent(context, MainActivity.class);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
        Notification notification  = new Notification.Builder(context)
                .setContentTitle("New message " + userName)
                .setContentText(messageText)
                .setSmallIcon(R.drawable.ic_send_black_24dp)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(notificationId, notification);
    }

    public static void createSimpleNotification(Context context, int notificationId, String userName, String messageText, boolean sound) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, StartActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("New message " + userName)
                .setContentText(messageText)
                .setContentIntent(pIntent)
                .setSmallIcon(R.drawable.ic_send_black_24dp)
                .setAutoCancel(true);
        if (sound) {
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        notificationManager.notify(notificationId, builder.build());
    }


}
