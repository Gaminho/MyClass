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
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.database.DevoirBDD;
import com.example.la.myclass.notifications.AbstractNotification;

import java.util.Date;
import java.util.List;

/**
 * Created by ariche on 14/10/2016.
 */

public class DevoirService extends Service{

    public static final String NO_ACTION = "noAction";
    public static final long MAX_DELAY_VALIDATION = 10 * C.HOUR + 5 * C.MINUTE;

    protected Context mContext;
    protected Devoir mNextDevoir;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (intent.getAction() != null) {
            if (intent.getAction().equals(NO_ACTION)) {
                NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                nMgr.cancel(AbstractNotification.DEVOIR_VALIDATION);
                stopSelf();
                return START_NOT_STICKY;
            }
        }

        Intent intentAlarm = new Intent(mContext, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int requestCode = 0;
        long alarmDate = 0;

        for(Devoir devoir : DevoirBDD.getForeseenDevoir(mContext)){
            if(devoir.getDate() + MAX_DELAY_VALIDATION < System.currentTimeMillis()) {
                DevoirBDD.changeDevoirState(mContext, devoir, Devoir.STATE_WAITING_FOR_VALIDATION);
                Log.e("DevoirService", "Changement d'etat : " + devoir.toString());
            }
        }

        mNextDevoir = DevoirBDD.getNextDevoir(mContext);
        if(mNextDevoir != null) {
            Log.e("DevoirService", "Next Devoir : " + new Date(mNextDevoir.getDate()) + "\n" + mNextDevoir.toString());
            intentAlarm.putExtra("devoirID", mNextDevoir.getId());

            if(mNextDevoir.getDate() > System.currentTimeMillis()){
                alarmDate = mNextDevoir.getDate();
                requestCode = AbstractNotification.DEVOIR_BEGIN;
            }

            else if(mNextDevoir.getDate() + MAX_DELAY_VALIDATION > System.currentTimeMillis()){
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

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
