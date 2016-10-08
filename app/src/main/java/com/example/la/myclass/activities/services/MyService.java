package com.example.la.myclass.activities.services;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.la.myclass.C;
import com.example.la.myclass.notifications.NotificationBeginningCourse;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.notifications.NotificationEndCourse;

import java.util.Date;


/**
 * Created by ariche on 07/10/2016.
 */

public class MyService extends Service {

    protected Application mApp;
    protected Context mContext;
    protected Course mNextCourse;
    protected long nextBeginning, nextEnding;
    public boolean isActive = false, nextCoursEnd = false;


    protected Handler mHandler, mMonitoring, mEndCourse;
    private Runnable mRunnableStartingCours = new Runnable() {
        public void run() {
            Log.e("RUNNABLE START", "***************** DEBUT : " + new Date().toString() + " *****************");
            changeCourseState(mNextCourse, Course.WAITING_FOT_VALIDATION);

            mHandler.removeCallbacks(mRunnableStartingCours);

            new NotificationBeginningCourse(mContext, mNextCourse).create(mContext);
            Log.e("RUNNABLE START", "***************** Nouveau cours : " + new Date(mNextCourse.getDate()).toString() + " *****************");
            mEndCourse.postAtTime(mRunnableEndingCourse, SystemClock.uptimeMillis() + + 8 * C.SECOND);
        }
    };
    private Runnable mRunnableEndingCourse = new Runnable() {
        public void run() {
            Log.e("RUNNABLE END", "***************** FIN : " + new Date().toString() + " *****************");
            new NotificationEndCourse(mContext, mNextCourse).create(mContext);
            mEndCourse.removeCallbacks(mRunnableEndingCourse);
            mNextCourse = getNextCourses();
            long begin = SystemClock.uptimeMillis() + (mNextCourse.getDate() - System.currentTimeMillis());
            mHandler.postAtTime(mRunnableStartingCours, begin);
            isActive = false;
        }
    };
    private Runnable mRunnableMonitoring = new Runnable() {
        public void run() {
            mMonitoring.removeCallbacks(mRunnableMonitoring);
            if(mNextCourse != null)
                Log.e("MONITORING", "Début  : " + C.formatDate((mNextCourse.getDate()-System.currentTimeMillis()),C.dd_HH_mm_ss));
            mMonitoring.postDelayed(mRunnableMonitoring, 10000);
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("SERVICE","onStartCommand");

        mHandler = new Handler();
        mMonitoring = new Handler();
        mEndCourse = new Handler();

        Log.e("Service", "onStartCommand - 1 - " + mNextCourse);

        if( mNextCourse != null && mNextCourse.getDate() > getNextCourses().getDate() ){
            mHandler.removeCallbacks(mRunnableStartingCours);
            isActive = false;
        }

        Log.e("Service", "onStartCommand - 2 - " + isActive);

        if(!isActive && getNextCourses() != null){
            isActive = true;
            mNextCourse = getNextCourses();

            Log.e("Service", "onStartCommand - 3 - " + mNextCourse);

            if(mNextCourse != null){
                Log.e("Service", "onStartCommand - 4 - " + mNextCourse);
                Log.e("Service", "Prochain cours : " + new Date( mNextCourse.getDate()).toString());
                long begin = SystemClock.uptimeMillis() + (mNextCourse.getDate() - System.currentTimeMillis());
                mHandler.postAtTime(mRunnableStartingCours, begin);
                mEndCourse.postAtTime(mRunnableEndingCourse, begin + 8 * C.SECOND);
                mMonitoring.post(mRunnableMonitoring);

            }
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

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        isActive = false;
        Log.e("My Service", "onstart !");
    }
}
