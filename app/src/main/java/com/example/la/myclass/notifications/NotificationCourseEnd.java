package com.example.la.myclass.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.la.myclass.activities.course.ActivityCourse;
import com.example.la.myclass.beans.Course;

/**
 * Created by ariche on 07/10/2016.
 */

public class NotificationCourseEnd extends AbstractNotification {


    public NotificationCourseEnd(Context context, Course course) {
        this.requestCode = AbstractNotification.COURSE_END;
        this.mTitle = "Fin de cours";
        this.mContext = context;
        this.mContent = String.format("Le cours avec %s est terminé. Complétez le cours maintenant.", course.getPupil().getFullName());
        this.mLaunchIntent = new Intent(context, ActivityCourse.class);
        this.mLaunchIntent.putExtra(ActivityCourse.COURSE_ID, course.getId());
        this.mPendingIntent = PendingIntent.getActivity(context,
                AbstractNotification.COURSE_END, this.mLaunchIntent,
                PendingIntent.FLAG_ONE_SHOT);
        this.mLightColor = 0xffc107;
    }
}
