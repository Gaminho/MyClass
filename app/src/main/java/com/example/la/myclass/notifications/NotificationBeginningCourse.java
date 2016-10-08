package com.example.la.myclass.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.example.la.myclass.activities.course.ActivityCourse;
import com.example.la.myclass.beans.Course;



/**
 * Created by ariche on 07/10/2016.
 */

public class NotificationBeginningCourse extends AbstractNotification {

    static final int REQUEST_CODE = -50;

    public NotificationBeginningCourse(Context context, Course course) {
        this.requestCode = REQUEST_CODE;
        this.mTitle = "Début de cours";
        this.mContent = String.format("Le cours avec %s a commencé", course.getPupil().getFullName());
        this.mLaunchIntent = new Intent(context, ActivityCourse.class);
        this.mLaunchIntent.putExtra(ActivityCourse.COURSE_ID, course.getId());
        this.mPendingIntent = PendingIntent.getActivity(context,
                REQUEST_CODE, this.mLaunchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
