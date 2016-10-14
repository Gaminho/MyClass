package com.example.la.myclass.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.activities.MainActivity;
import com.example.la.myclass.beans.MyDb;
import com.example.la.myclass.services.DatabaseService;

/**
 * Created by ariche on 13/10/2016.
 */

public class NotificationDatabaseUpdate extends AbstractNotification {


    public NotificationDatabaseUpdate(Context context, MyDb myDb) {
        super(context);
        this.requestCode = AbstractNotification.DATABASE_UPDATE;
        this.mTitle = "Mise à jour de la base";
        this.mContext = context;
        this.mContent = String.format("Pensez à mettre à jour la base de données.\nDernière mise à jour : %s", C.formatDate(myDb.getLastUpdate(), C.DD_MM_YY));
        this.mLaunchIntent = new Intent(context, MainActivity.class);
        this.mPendingIntent = PendingIntent.getActivity(context,
                AbstractNotification.DATABASE_UPDATE, this.mLaunchIntent,
                PendingIntent.FLAG_ONE_SHOT);
        this.mLightColor = 0xffcd07;
        Intent intent = new Intent(context, DatabaseService.class);

        intent.setAction(DatabaseService.UPDATE_DB);
        this.mBuilder.addAction(R.drawable.ic_update_white_24dp, "Update", PendingIntent.getService(context,-99,intent,PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(DatabaseService.NO_ACTION);
        this.mBuilder.addAction(R.drawable.ic_clear_white_24dp, "Plus tard", PendingIntent.getService(context,-98,intent,PendingIntent.FLAG_UPDATE_CURRENT));
        this.mBuilder.setAutoCancel(false);
    }
}
