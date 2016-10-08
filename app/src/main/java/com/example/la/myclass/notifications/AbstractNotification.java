package com.example.la.myclass.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.example.la.myclass.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by ariche on 07/10/2016.
 */

public abstract class AbstractNotification {

    protected int requestCode;
    protected String mTitle;
    protected String mContent;
    protected Intent mLaunchIntent;
    protected PendingIntent mPendingIntent;

    public void create(Context context){
        final NotificationManager mNotification = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo_app)
                .setContentTitle(mTitle)
                .setContentText(mContent)
                .setContentIntent(mPendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotification.notify(requestCode,builder.build());
        }
    }
}
