package com.example.la.myclass.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.example.la.myclass.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by ariche on 07/10/2016.
 */

public abstract class AbstractNotification {

    /**
     * NOTIFICATION TYPE
     */
    public static final int COURSE_BEGIN = 1;
    public static final int COURSE_END = 2;
    public static final int DEVOIR_BEGIN = 11;
    public static final int DEVOIR_VALIDATION = 12;
    public static final int DATABASE_UPDATE = 21;

    protected int requestCode;
    protected Context mContext;
    protected String mTitle;
    protected String mContent;
    protected Intent mLaunchIntent;
    protected PendingIntent mPendingIntent;
    protected int mLightColor;
    protected Notification.Builder mBuilder;

    public AbstractNotification(Context context){
        this.mBuilder = new Notification.Builder(context);
    }

    public void create(){
        final NotificationManager mNotification = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        mBuilder
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo_app)
                .setContentTitle(mTitle)
                .setContentText(mContent)
                .setStyle(new Notification.BigTextStyle().bigText(mContent))
                .setContentIntent(mPendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(mLightColor, 3000, 1500)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotification.notify(requestCode,mBuilder.build());
        }
    }
}
