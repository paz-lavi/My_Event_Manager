package com.example.myapplication_finalproject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class MyAlarmService extends IntentService {
    private final String TAG = getClass().getCanonicalName() + "_Log";
    private long day = 1000 * 60 * 60 * 24;// 1sec * 60sec * 60min * 24H
    private static final String CHANNEL_ID = "CHANNEL_NO_1jghg11";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public MyAlarmService() {
        super("sss");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "bind");
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ArrayList<MyEvent> events = SortAndFilter.filterByFutureNotConfirmedEvents(
                FilesManager.getInstance().getMyEvents(this));
        long now = System.currentTimeMillis();
        for (MyEvent e : events) {
            if ((e.getTimeStamp() + e.getValidFor_Days() * day) > now && e.isRemainder())
                SendSms.remainderMSG(e);
            else {
                if ((e.getTimeStamp() + e.getValidFor_Days() * day) < now)
                    FilesManager.getInstance().updateEventStatus(e, Constants.STATUS_CANCEL,
                            MyAlarmService.this);
                SendSms.cancelAutoMSG(e);
            }
        }
        stopSelf();
        return Service.START_NOT_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground2();
        else
            startForeground(1, new Notification());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground2() {
        String NOTIFICATION_CHANNEL_ID = "com.example.myapplication_finalproject";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_notification_icon1)
                .setContentTitle("שולח תזכורות")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }
}
