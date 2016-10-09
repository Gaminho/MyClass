package com.example.la.myclass.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.la.myclass.C;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.notifications.AbstractNotification;
import com.example.la.myclass.notifications.NotificationBeginningCourse;
import com.example.la.myclass.notifications.NotificationEndCourse;

import java.util.Date;

/**
 * Created by ariche on 09/10/2016.
 */

public class AlarmReceiver extends BroadcastReceiver {

    protected SharedPreferences mSharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("ALARM", "***** " + new Date().toString() + " ******");
        mSharedPreferences = context.getSharedPreferences(C.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        int requestCode = intent.getExtras().getInt("requestCode",-1);

        if(requestCode == AbstractNotification.COURSE_BEGIN
                || requestCode == AbstractNotification.COURSE_END) {

            Course course = C.getCourseWithId(context, intent.getExtras().getInt("courseID", -1));

            if(requestCode == AbstractNotification.COURSE_BEGIN
                    && mSharedPreferences.getBoolean(C.SP_NOTIF_COURSE_BEGIN, false))
                new NotificationBeginningCourse(context, course).create();

            else if (requestCode == AbstractNotification.COURSE_END){
                C.changeCourseState(context, course, Course.WAITING_FOT_VALIDATION);

                if (mSharedPreferences.getBoolean(C.SP_NOTIF_COURSE_END, false))
                    new NotificationEndCourse(context, course).create();
            }

            intent = new Intent(context, MyService.class);
            context.startService(intent);
        }

    }
}
