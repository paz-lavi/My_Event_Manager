package com.example.myapplication_finalproject;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MyNotification extends IntentService {


    private static final String CHANNEL_ID = "CHANNEL_NO_1jghg";

    int notificationId = 999;
    NotificationCompat.Builder builder;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public MyNotification() {
        super("sss");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String msg = bundle.getString(Constants.SMS_TEXT);

            handleSMS(msg);
        }
        return super.onStartCommand(intent, flags, startId);

    }

    private void notification(String msg) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_icon1)
                .setContentTitle("My Events Manager")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        notificationManager.notify(notificationId, builder.build());
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void handleSMS(String sms) {

        String[] msg = sms.split(Constants.SPLITTER);
        String number;
        String text;
        String bid;
        int bidNum;
        char decision;

        try {

            number = 0 + msg[0].trim().substring(Constants.COUNTRY_CODE.length());
            text = msg[1].trim();
        } catch (IndexOutOfBoundsException e) {
            Log.d("tptp", e.toString());
            return;
        }


        try {


            bid = text.substring(1);
            bidNum = Integer.parseInt(bid.trim());

            decision = text.charAt(0);

        } catch (IndexOutOfBoundsException e) {
            Log.d("tptp", e.getMessage());

            error(number);
            return;
        } catch (NumberFormatException e) {
            Log.d("tptp", e.getMessage());
            return;
        }

        ArrayList<MyEvent> events;
        try {
            events = FilesManager.getInstance().getMyEvents(this);
            if (events == null) {
                Log.d("tptp", "null");
                return;
            }
        } catch (NullPointerException e) {
            Log.d("tptp", e.getMessage());


            error(number);
            return;
        }
        Log.d("tptp", "num " + number);


        for (MyEvent event : events) {

            if (event.getBidNumber() == bidNum && event.getCustomerPhone().equals(number)) {

                event.setStatus(askForDecision(decision, number));
                String s = askForDecision(decision, number);
                FilesManager.getInstance().updateEventStatus(event, s, this);
                makeNotification(event.getDate(), s, event);

                return;
            }

        }
        error(number);


    }

    private String askForDecision(char decision, String phone) {
        if (decision == Constants.OK) {
            Log.d("tptp", "אושר");

            SendSms.confirmMSG(phone);
            return Constants.STATUS_CONFIRM;
        } else if (decision == Constants.CANCEL) {
            Log.d("tptp", "בוטל");

            SendSms.cancelMSG(phone);
            return Constants.STATUS_CANCEL;
        } else {
            error(phone);
            return Constants.STATUS_WAIT_FOR_CONFIRM;
        }


    }

    private void error(String phone) {
        Log.d("tptp", "שגיאה");

        SendSms.errorMSG(phone);

    }


    private void makeNotification(String date, String msg, MyEvent event) {
        // Current version
        Context context = this;
        // Create your notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "My Event Manager";
            String description = "My Event Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            // IMPORTANT: CHANNEL_ID
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);


            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, EventViewer.class);
        resultIntent.putExtra(Constants.INTENT_FLAG, Constants.INTENT_FROM_NOTIFICATION);
        Gson gson = new Gson();
        resultIntent.putExtra(Constants.EVENT_FROM_INTENT, gson.toJson(event));

// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get an instance of NotificationManager
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_icon1)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(msg)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("האירוע בתאריך -" + date + " " + msg + ".\nלחץ לצפיה באירוע"))
                .setContentIntent(resultPendingIntent);


        // Gets an instance of the NotificationManager service//
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        mNotificationManager.notify(001, mBuilder.build());
    }

}
