package com.example.myapplication_finalproject;

import android.telephony.SmsManager;

import java.util.ArrayList;

public class SendSms {


    private static void sendSmsMSG(String msg, String phone) {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> msgArray = smsManager.divideMessage(msg);
        smsManager.sendMultipartTextMessage(phone, null, msgArray, null, null);

    }


    public static void sendInvoice(MyEvent event) {
        String sb = "לצפיה בקבלה עבור האירוע בתאריך " +
                event.getDate() +
                " לחץ על הקישור הבא:\n " +
                event.getOriginalInvoiceURL();
        sendSmsMSG(sb, event.getCustomerPhone());
    }

    public static void sendCopyInvoice(MyEvent event) {
        String sb = "לצפיה בקבלה עבור האירוע בתאריך " +
                event.getDate() +
                " לחץ על הקישור הבא:\n " +
                event.getCopyInvoiceURL();
        sendSmsMSG(sb, event.getCustomerPhone());
    }

    public static void sendBid(MyEvent event) {

        String sb = "האירוע נקלט במערכת ומחכה לאישור.\n " +
                "לצפיה בהצעת המחיר לחץ על הקישור הבא:\n" +
                event.getBidURL() +
                "\nלאישור השב: " +
                "1" + event.getBidNumber() +
                " \n" +
                "לביטול השב: " +
                "2" + event.getBidNumber() +
                " \n";
        sendSmsMSG(sb, event.getCustomerPhone());
    }

    public static void remainderMSG(MyEvent event) {
        String s = "עדיין לא התקבלה תשובתך עבור הצעת המחיר לאירוע בתאריך " +
                event.getDate() + "." +
                "\nלאישור השב: " +
                "1" + event.getBidNumber() +
                " \n" +
                "לביטול השב: " +
                "2" + event.getBidNumber() +
                "\nלצפיה בהצעת המחיר לחץ על הקישור הבא:\n" +
                event.getBidURL();
        ;
        sendSmsMSG(s, event.getCustomerPhone());
    }

    public static void cancelAutoMSG(MyEvent event) {
        String s = "פג תוקפה של הצעת המחיר עבור האירוע בתאריך " + event.getDate()
                + " . \nהאירוע בוטל באופן אוטומטי \nאשמח לעמוד לרשותך בעתיד";
        ;
        sendSmsMSG(s, event.getCustomerPhone());
    }

    public static void confirmMSG(String phoneNumber) {

        sendSmsMSG("תשובתך נקלטה, נתראה באירוע.", phoneNumber);
    }

    public static void manualConfirmMSG(String phoneNumber, String date) {

        sendSmsMSG("לבקשתך האירוע בתאריך " + date + " אושר , נתראה באירוע.", phoneNumber);
    }

    public static void manualCCancelMSG(String phoneNumber, String date) {

        sendSmsMSG("לבקשתך האירוע בתאריך " + date + " בוטל , אשמח לעמוד לרשותך בעתיד.", phoneNumber);
    }

    public static void cancelMSG(String phoneNumber) {

        sendSmsMSG("תשובתך נקלטה, אשמח לעמוד לרשותך בעתיד.", phoneNumber);
    }

    public static void errorMSG(String phoneNumber) {

        sendSmsMSG("התשובה לא נקלטה במערכת, נא לשלוח תשובתך בשנית או צור קשר.", phoneNumber);
    }
}