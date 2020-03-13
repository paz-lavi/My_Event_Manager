package com.example.myapplication_finalproject;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    private String TAG = SmsReceiver.class.getSimpleName();

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) bound to intent
        Bundle bundle = intent.getExtras();

        SmsMessage[] msgs = null;

        String str = "";

        if (bundle != null) {
            String format = bundle.getString("format");
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received
            for (int i = 0; i < msgs.length; i++) {
                // Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format); //createFromPdu((byte[]) pdus[i]);
                // Sender's phone number
                str += msgs[i].getOriginatingAddress() + Constants.SPLITTER;
                // Fetch the text message
                str += msgs[i].getMessageBody().toString();

            }

            // Display the entire SMS Message
            Log.d(TAG, str);


        }


        Intent intent1 = new Intent(context, MyNotification.class);
        intent1.putExtra(Constants.SMS_TEXT, str);
        context.startService(intent1);

    }


}