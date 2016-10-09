package com.example.la.myclass.services;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.la.myclass.C;
import com.example.la.myclass.notifications.AbstractNotification;
import com.example.la.myclass.notifications.NotificationBeginningCourse;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.notifications.NotificationEndCourse;

import java.util.Date;


/**
 * Created by ariche on 07/10/2016.
 */

public class MyService extends Service {

    protected Context mContext;
    protected Course mNextCourse;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mNextCourse = getNextCourses();
        Log.e("MYSERVICE", "Next Course : " + new Date(mNextCourse.getDate()));

        if(mNextCourse == null)
            return START_NOT_STICKY;

        Intent intentAlarm = new Intent(mContext, AlarmReceiver.class);
        intentAlarm.putExtra("courseID", mNextCourse.getId());
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int requestCode = 0;
        long alarmDate = 0;
        String legend = "";

        if(mNextCourse.getDate() > System.currentTimeMillis()){
            alarmDate = mNextCourse.getDate();
            requestCode = AbstractNotification.COURSE_BEGIN;
            legend = "Debut : ";
        }

        else if(mNextCourse.getDate() + mNextCourse.getDuration() * C.MINUTE > System.currentTimeMillis()){
            //alarmDate = mNextCourse.getDate() + mNextCourse.getDuration() * C.MINUTE;
            alarmDate = mNextCourse.getDate() + 15 * C.SECOND;
            requestCode = AbstractNotification.COURSE_END;
            legend = "Fin : ";
        }

        intentAlarm.putExtra("requestCode", requestCode);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmDate,
                PendingIntent.getBroadcast(mContext, requestCode,
                        intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT)
        );

        Log.e("MYSERVICE", legend + new Date(alarmDate));

        stopSelf();

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected Course getNextCourses(){
        CoursesBDD coursesBDD = new CoursesBDD(mContext);
        coursesBDD.open();
        Course course = coursesBDD.getNextCourse();
        coursesBDD.close();

        return course;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
