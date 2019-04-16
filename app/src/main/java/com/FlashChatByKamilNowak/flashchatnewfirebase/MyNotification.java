package com.FlashChatByKamilNowak.flashchatnewfirebase;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyNotification {

    private String mContentTitle;
    private String mContentText;
    private String mTicket;
    private int mSmallIcon;
    private PendingIntent mContentIntent;
    private Context mContext;
    private NotificationCompat.Builder mNotification;
    private static int mNumberOfChannel;
    public static final String NOTIFICATION_CHANNEL_ID = "4565";

    public  MyNotification(String contentTitle, String contentText, String ticket, int smallIcon, PendingIntent contentIntent, Context context) {
        mContentTitle = contentTitle;
        mContentText = contentText;
        mTicket = ticket;
        mSmallIcon = smallIcon;
        mContentIntent =contentIntent;
        mContext=context;
        mNumberOfChannel++;
        createNotificationChannel();
    }

    public void createNotification()
    {

        mNotification= new NotificationCompat.Builder(mContext,"10")
                .setContentTitle(mContentTitle)
                .setContentText(mContentText)
                .setSmallIcon(mSmallIcon)
                .setAutoCancel(true)
                 .setContentIntent(mContentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


    }
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = NOTIFICATION_CHANNEL_ID;
            String description = mContentText;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(mNumberOfChannel+"", name, importance);
            channel.setDescription(description);
        }
    }
    public void sendNotification()
    {
        try {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
            notificationManager.notify(0, mNotification.build());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}