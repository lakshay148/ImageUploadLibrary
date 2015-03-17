package com.imageuploadlib.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * Created by Lakshay on 17-03-2015.
 */
public class CommonUtils {

    public static void createNotification(Context context , int smallIcon , String title, String content, Intent resultIntent, int imageUploadNotifId)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(content);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(imageUploadNotifId, mBuilder.build());
    }
}
