package com.example.la.myclass.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.la.myclass.R;
import com.example.la.myclass.activities.pupil.ActivityPupil;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by ariche on 07/10/2016.
 */

public class NotificationTest {

    static final int REQUEST_CODE = -50;

    public static void create(Context context){
        final NotificationManager mNotification = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        final Intent launchNotificationIntent = new Intent(context, ActivityPupil.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context,
                REQUEST_CODE, launchNotificationIntent, PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(context)
                .setWhen(System.currentTimeMillis())
                .setTicker("Ma notification")
                .setSmallIcon(R.drawable.logo_app)
                .setContentTitle("Content title")
                .setContentText("Content text")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotification.notify(78,builder.build());
        }
    }
}
