package com.example.la.myclass.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.example.la.myclass.activities.course.ActivityCourse;
import com.example.la.myclass.beans.Course;



/**
 * Created by ariche on 07/10/2016.
 */

public class NotificationCourseBegin extends AbstractNotification {


    public NotificationCourseBegin(Context context, Course course) {
        this.requestCode = AbstractNotification.COURSE_BEGIN;
        this.mContext = context;
        this.mTitle = "Début de cours";
        this.mContent = String.format("Le cours avec %s a commencé", course.getPupil().getFullName());
        this.mLaunchIntent = new Intent(context, ActivityCourse.class);
        this.mLaunchIntent.putExtra(ActivityCourse.COURSE_ID, course.getId());
        this.mPendingIntent = PendingIntent.getActivity(context,
                AbstractNotification.COURSE_BEGIN, this.mLaunchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        this.mLightColor = 0x81d4fa;
    }

}
