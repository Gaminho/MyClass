package com.example.la.myclass.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.la.myclass.C;
import com.example.la.myclass.beans.Database;
import com.example.la.myclass.notifications.AbstractNotification;
import com.example.la.myclass.utils.MyJSONParser;

import java.util.Date;

/**
 * Created by ariche on 13/10/2016.
 */

public class DatabaseService extends Service {

    // TODO : Make maps service
    // TODO : Replace physical adresse with lat;long
    // TODO : save best journey
    // TODO : make themes for Layout
    // TODO : Notifications colors
    // TODO : Day view from calendar
    // TODO : Notification course reminder  => plan a journey / use habits journey
    // TODO : Notification for new weeks
    // TODO : Change notification text for devoir multi-pupils
    // TODO : Notification course depart
    // TODO : Correct bugs of current week number of courses and devoirs


    public static final String UPDATE_DB = "updateDb";
    public static final String NO_ACTION = "noAction";

    protected Context mContext;
    protected Database mDatabase;
    protected SharedPreferences mSharedPreferences;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent.getAction() != null){
            if(intent.getAction().equals(UPDATE_DB) && C.updateDB(getApplicationContext())){
                NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                nMgr.cancel(AbstractNotification.DATABASE_UPDATE);
            }
            else if(intent.getAction().equals(NO_ACTION)){
                NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                nMgr.cancel(AbstractNotification.DATABASE_UPDATE);
                stopSelf();
                return START_NOT_STICKY;
            }
        }


        mSharedPreferences =  mContext.getSharedPreferences(C.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        mDatabase = new MyJSONParser().getDatabaseFromJsonFile(mSharedPreferences.getString(C.CURRENT_DB, C.NO_DB));

        if(mDatabase != null) {

            long alarmDate = 0;
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intentAlarm = new Intent(mContext, AlarmReceiver.class);
            int requestCode = AbstractNotification.DATABASE_UPDATE;

            /**
             * For the test
             */
            //long nextUpdate = mDatabase.getLastUpdate() + ( 5 * C.SECOND * mSharedPreferences.getLong(C.SP_UPDATE_DB_DELAY,0) / C.DAY);
            long nextUpdate = mDatabase.getLastUpdate() + mSharedPreferences.getLong(C.SP_UPDATE_DB_DELAY,0);
            Log.e("DatabaseService", "Next Update : " + new Date(nextUpdate));
            if (nextUpdate > System.currentTimeMillis())
                alarmDate = nextUpdate;

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
