package com.example.la.myclass.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.la.myclass.R;
import com.example.la.myclass.activities.course.ActivityCourse;
import com.example.la.myclass.activities.devoir.ActivityDevoir;
import com.example.la.myclass.activities.pupil.ActivityPupil;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Devoir;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by ariche on 07/10/2016.
 */

public class NotificationDevoirBegin extends AbstractNotification {

    public NotificationDevoirBegin(Context context, Devoir devoir) {
        this.requestCode = AbstractNotification.COURSE_BEGIN;
        this.mContext = context;
        this.mTitle = "Devoir";
        this.mContent = String.format("%s a un devoir aujourdh'hui.", devoir.getPupil().getFullName());
        this.mLaunchIntent = new Intent(context, ActivityDevoir.class);
        this.mLaunchIntent.putExtra(ActivityDevoir.DEVOIR_ID, devoir.getId());
        this.mPendingIntent = PendingIntent.getActivity(context,
                AbstractNotification.DEVOIR_BEGIN, this.mLaunchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        this.mLightColor = 0x81d4fa;
    }
}
