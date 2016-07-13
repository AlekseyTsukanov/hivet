package com.acukanov.hivet.utils;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.acukanov.hivet.R;
import com.acukanov.hivet.ui.main.MainActivity;

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

    public static void createSimpleNotification(Context context, int notificationId, String userName, String messageText) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(context)
                .setContentTitle("New message " + userName)
                .setContentText(messageText)
                .setSmallIcon(R.drawable.ic_send_black_24dp)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(notificationId, notification);
    }
}
