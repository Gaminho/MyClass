package com.example.la.myclass.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.la.myclass.activities.devoir.ActivityDevoir;
import com.example.la.myclass.beans.Devoir;

/**
 * Created by ariche on 10/10/2016.
 */

public class NotificationDevoirEnd extends AbstractNotification {

    public NotificationDevoirEnd(Context context, Devoir devoir) {
        super(context);
        this.requestCode = AbstractNotification.DEVOIR_VALIDATION;
        this.mContext = context;
        this.mTitle = "Devoir";
        this.mContent = String.format("Le devoir de %s est en attende de validation.", devoir.getPupil().getFullName());
        this.mLaunchIntent = new Intent(context, ActivityDevoir.class);
        this.mLaunchIntent.putExtra(ActivityDevoir.DEVOIR_ID, devoir.getId());
        this.mPendingIntent = PendingIntent.getActivity(context,
                AbstractNotification.DEVOIR_VALIDATION, this.mLaunchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        this.mLightColor = 0x81d4fa;
    }
}
