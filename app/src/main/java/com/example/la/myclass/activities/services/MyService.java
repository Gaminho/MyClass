package com.example.la.myclass.activities.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.la.myclass.C;
import com.example.la.myclass.notifications.NotificationBeginningCourse;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.notifications.NotificationEndCourse;


/**
 * Created by ariche on 07/10/2016.
 */

public class MyService extends Service {


    protected Context mContext;
    protected Course mNextCourse;

    protected Handler mHandler;
    private Runnable mRunnableStartingCours = new Runnable() {
        public void run() {
            new NotificationBeginningCourse(mContext, mNextCourse).create(mContext);
            changeCourseState(mNextCourse, Course.WAITING_FOT_VALIDATION);
            mNextCourse = getNextCourses();

            if(mNextCourse != null) {
                long nextCourseBeginning = ( mNextCourse.getDate() - System.currentTimeMillis() );
                mHandler.postDelayed(mRunnableStartingCours, nextCourseBeginning);
                mHandler.postDelayed(mRunnableEndingCourse, nextCourseBeginning + C.MINUTE * mNextCourse.getDuration());
                Log.e("SERVICE", "Prochaine exécution : " + C.formatDate(nextCourseBeginning,C.dd_HH_mm_ss) );
            }
        }
    };
    private Runnable mRunnableEndingCourse = new Runnable() {
        public void run() {
            new NotificationEndCourse(mContext, mNextCourse).create(mContext);
        }
    };
    private Runnable mRunnableMonitoring = new Runnable() {
        public void run() {
            long nextCourseBeginning = ( mNextCourse.getDate() - System.currentTimeMillis() );
            Log.e("MONITORING", "Prochain début : " + C.formatDate(nextCourseBeginning,C.dd_HH_mm_ss));
            Log.e("MONITORING", "Prochaine fin : " + C.formatDate((nextCourseBeginning + mNextCourse.getDuration() * C.MINUTE),C.dd_HH_mm_ss));
            mHandler.postDelayed(mRunnableMonitoring, 3000);
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("SERVICE","onStartCommand");
        mContext = getApplicationContext();
        mNextCourse = getNextCourses();
        mHandler = new Handler();
        mHandler.removeCallbacks(mRunnableStartingCours);
        mHandler.removeCallbacks(mRunnableEndingCourse);
        mHandler.removeCallbacks(mRunnableMonitoring);

        mHandler.post(mRunnableMonitoring);

        if(mNextCourse != null){
            Log.e("SERVICE", "Prochaine exécution : " + C.formatDate((mNextCourse.getDate() - System.currentTimeMillis()),C.dd_HH_mm_ss) );
            long nextCourseBeginning = ( mNextCourse.getDate() - System.currentTimeMillis() );
            mHandler.postDelayed(mRunnableStartingCours, nextCourseBeginning);
            mHandler.postDelayed(mRunnableEndingCourse, nextCourseBeginning + C.MINUTE * mNextCourse.getDuration());
        }

        return START_STICKY;
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

    protected void changeCourseState(Course course, int newState){
        CoursesBDD coursesBDD = new CoursesBDD(mContext);
        coursesBDD.open();
        course.setState(newState);
        coursesBDD.updateCourse(course.getId(), course);
        coursesBDD.close();
    }

}
