package com.example.la.myclass.services;

import android.app.AlarmManager;
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

    protected Context mContext;
    protected Course mNextCourse;
    protected Devoir mNextDevoir;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intentAlarm = new Intent(mContext, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int requestCode = 0;
        long alarmDate = 0;

        mNextCourse = getNextCourse();
        if(mNextCourse != null) {
            Log.e("MYSERVICE", "Next Course : " + new Date(mNextCourse.getDate()));
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

        mNextDevoir = getNextDevoir();
        if(mNextDevoir != null) {
            Log.e("MYSERVICE", "Next Devoir : " + new Date(mNextDevoir.getDate()));
            intentAlarm.putExtra("devoirID", mNextDevoir.getId());

            if(mNextDevoir.getDate() > System.currentTimeMillis()){
                alarmDate = mNextDevoir.getDate();
                requestCode = AbstractNotification.DEVOIR_BEGIN;
            }
            else if(mNextDevoir.getDate() + 10 * C.HOUR > System.currentTimeMillis()){
                alarmDate = mNextDevoir.getDate() + 10 * C.HOUR;
                requestCode = AbstractNotification.DEVOIR_VALIDATION;
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

    protected Course getNextCourse(){
        CoursesBDD coursesBDD = new CoursesBDD(mContext);
        coursesBDD.open();
        Course course = coursesBDD.getNextCourse();
        coursesBDD.close();

        return course;
    }

    protected Devoir getNextDevoir(){
        DevoirBDD devoirBDD = new DevoirBDD(mContext);
        devoirBDD.open();
        Devoir devoir = devoirBDD.getNextDevoir();
        devoirBDD.close();

        return devoir;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
