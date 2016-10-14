package com.example.la.myclass.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.la.myclass.R;
import com.example.la.myclass.activities.course.ActivityCourse;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.services.CourseService;
import com.example.la.myclass.services.DatabaseService;

/**
 * Created by ariche on 07/10/2016.
 */

public class NotificationCourseEnd extends AbstractNotification {


    public NotificationCourseEnd(Context context, Course course) {
        super(context);
        this.requestCode = AbstractNotification.COURSE_END;
        this.mTitle = "Fin de cours";
        this.mContext = context;
        this.mContent = String.format("Le cours avec %s est terminé. Complétez le cours maintenant.", course.getPupil().getFullName());
        this.mLaunchIntent = new Intent(context, ActivityCourse.class);
        this.mLaunchIntent.putExtra(ActivityCourse.COURSE_ID, course.getId());
        this.mLaunchIntent.putExtra(ActivityCourse.COURSE_ACTION, ActivityCourse.ADDING);
        this.mPendingIntent = PendingIntent.getActivity(context,
                AbstractNotification.COURSE_END, this.mLaunchIntent,
                PendingIntent.FLAG_ONE_SHOT);
        this.mLightColor = 0xffc107;

        Intent intent = new Intent(context, ActivityCourse.class);
        intent.putExtra(ActivityCourse.COURSE_ID, course.getId());
        intent.putExtra(ActivityCourse.COURSE_ACTION, ActivityCourse.ADDING);
        this.mBuilder.addAction(R.drawable.ic_assignment_white_24dp, "Compléter", PendingIntent.getActivity(context,-90,intent,PendingIntent.FLAG_UPDATE_CURRENT));

        intent = new Intent(context, CourseService.class);
        intent.setAction(CourseService.NO_ACTION);
        this.mBuilder.addAction(R.drawable.ic_clear_white_24dp, "Plus tard", PendingIntent.getService(context,-80,intent,PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
