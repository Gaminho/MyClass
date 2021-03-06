package com.example.la.myclass.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.la.myclass.C;
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.database.DevoirBDD;
import com.example.la.myclass.notifications.AbstractNotification;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.database.CoursesBDD;

import java.util.Date;


/**
 * Created by ariche on 07/10/2016.
 */

public class CourseService extends Service {


    public static final String NO_ACTION = "noAction";

    protected Context mContext;
    protected Course mNextCourse;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if(intent.getAction() != null){
            if(intent.getAction().equals(NO_ACTION)){
                NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                nMgr.cancel(AbstractNotification.COURSE_END);
                stopSelf();
                return START_NOT_STICKY;
            }
        }

        Intent intentAlarm = new Intent(mContext, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int requestCode = 0;
        long alarmDate = 0;

        for(Course course : CoursesBDD.getForeseenCourse(mContext)){
            if(course.getDate() + course.getDuration() * C.MINUTE + C.MINUTE < System.currentTimeMillis()) {
                CoursesBDD.changeCourseState(mContext, course, Course.WAITING_FOT_VALIDATION);
                Log.e("CourseService", "Changement d'etat : " + course.toString());
                Log.e("CourseService", "Changement d'etat : " + new Date(course.getDate()
                        + course.getDuration() * C.MINUTE + C.HOUR)
                        +"\n"+course.toString());
            }
        }

        mNextCourse = CoursesBDD.getNextCourse(mContext);
        if(mNextCourse != null) {
            Log.e("CourseService", "Next Course : " + new Date(mNextCourse.getDate()));
            intentAlarm.putExtra("courseID", mNextCourse.getId());

            if (mNextCourse.getDate() > System.currentTimeMillis()) {
                alarmDate = mNextCourse.getDate();
                requestCode = AbstractNotification.COURSE_BEGIN;
            }
            else if (mNextCourse.getDate() + mNextCourse.getDuration() * C.MINUTE > System.currentTimeMillis()) {
                alarmDate = mNextCourse.getDate() + mNextCourse.getDuration() * C.MINUTE;
                requestCode = AbstractNotification.COURSE_END;
            }

            intentAlarm.putExtra("requestCode", requestCode);
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmDate,
                    PendingIntent.getBroadcast(mContext, requestCode,
                            intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT)
            );
        }

        stopSelf();

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
