package com.example.myapplication_finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;

import java.io.File;
import java.text.SimpleDateFormat;

public class CreateInvoice extends AbstractViewRenderer {
    private TextView invoice_name;
    private TextView invoice_number;
    private TextView invoice_phone;
    private TextView invoice_email;
    private TextView invoice_address;
    private TextView invoice_invoicenumbercontext;
    private TextView invoice_invoicenumber;
    private TextView invoice_total;
    private TextView invoice_priceperunit;
    private TextView invoice_count;
    private TextView invoice_description;
    private TextView invoice_total1;
    private TextView invoice_priceperunit1;
    private TextView invoice_count1;
    private TextView invoice_description1;
    private TextView invoice_total2;
    private TextView invoice_priceperunit2;
    private TextView invoice_count2;
    private TextView invoice_description2;
    private TextView invoice_total3;
    private TextView invoice_mailcontext;
    private TextView invoice_priceperunit3;
    private TextView invoice_count3;
    private TextView invoice_description3;
    private TextView invoice_total4;
    private TextView invoice_priceperunit4;
    private TextView invoice_count4;
    private TextView invoice_description4;
    private TextView invoice_total5;
    private TextView invoice_priceperunit5;
    private TextView invoice_count5;
    private TextView invoice_description5;
    private TextView invoice_total6;
    private TextView invoice_priceperunit6;
    private TextView invoice_count6;
    private TextView invoice_description6;
    private TextView invoice_total7;
    private TextView invoice_priceperunit7;
    private TextView invoice_count7;
    private TextView invoice_description7;
    private TextView invoice_total8;
    private TextView invoice_priceperunit8;
    private TextView invoice_count8;
    private TextView invoice_description8;
    private TextView invoice_total9;
    private TextView invoice_priceperunit9;
    private TextView invoice_count9;
    private TextView invoice_description9;
    private TextView invoice_total11;
    private TextView invoice_priceperunit11;
    private TextView invoice_count11;
    private TextView invoice_description11;
    private TextView invoice_total12;
    private TextView invoice_priceperunit12;
    private TextView invoice_count12;
    private TextView invoice_description12;
    private TextView invoice_total13;
    private TextView invoice_priceperunit13;
    private TextView invoice_count13;
    private TextView invoice_description13;
    private TextView invoice_total14;
    private TextView invoice_priceperunit14;
    private TextView invoice_count14;
    private TextView invoice_description14;
    private TextView invoice_total15;
    private TextView invoice_priceperunit15;
    private TextView invoice_count15;
    private TextView invoice_description15;
    private TextView invoice_total16;
    private TextView invoice_priceperunit16;
    private TextView invoice_count16;
    private TextView invoice_description16;
    private TextView invoice_paiedcashcontext;
    private TextView invoice_paypalconfirmcontext;
    private TextView invoice_paiedpaypalcontext;
    private TextView invoice_paypalconfirm;
    private TextView invoice_customercontext;
    private TextView invoice_customerphonecontext;
    private TextView invoice_totaltopaycontext;
    private TextView invoice_paiedcash;
    private TextView invoice_paiedpaypal;
    private TextView invoice_LBL_date;
    private TextView invoice_LBL_original;
    private TextView invoice_eventdatecontext;
    private TextView invoice_timecontext;
    private TextView invoice_addcontext;
    private ImageView invoice_IMG_sig;

    private MyEvent event;
    private User user;
    private final String TAG = CreateInvoice.class.getSimpleName() + "LOG";

    public CreateInvoice(final Context ctx, MyEvent event, final boolean type) {
        super(ctx, R.layout.invoice);
        this.event = event;
        this.user = FilesManager.getInstance().getUser();
        initView(_view);
        initInvoice(type);


        File img = new File(FilesManager.getInstance().getSignaturePath(ctx));
        if (img.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(img.getAbsolutePath());


            invoice_IMG_sig.setImageBitmap(myBitmap);

        }
    }

    public void setOriginalOrCopy(boolean original) {
        if (original)
            invoice_LBL_original.setText("מקור");
        else
            invoice_LBL_original.setText("העתק");

    }

    private void initInvoice(boolean type) {
        Log.d(TAG, "enter init");

        invoice_name.setText(user.getName());
        invoice_number.setText("מספר עוסק פטור: " + user.getId());
        invoice_phone.setText("מספר טלפון: " + user.getPhone());
        invoice_email.setText(user.getMail());
        //invoice_email.setText("דוא״ל: " + user.getMail());
        invoice_address.setText(user.getStreet() + " " + user.getHouseNumber()
                + " " + user.getCity());
//        invoice_address.setText("כתובת: " + user.getStreet() + " " + user.getHouseNumber()
//                + " " + user.getCity());
        invoice_mailcontext.setText(event.geteMail());
        invoice_addcontext.setText(event.getAddress());
        invoice_eventdatecontext.setText(event.getDate());
        invoice_timecontext.setText(event.getStartingTime());
        invoice_customercontext.setText(event.getCustomerName());
        invoice_customerphonecontext.setText(event.getCustomerPhone());
        invoice_totaltopaycontext.setText(event.getTotalPrice() + "");
        invoice_LBL_date.setText(new SimpleDateFormat("d/M/yyyy").format(System.currentTimeMillis()));

        if (type == Constants.INVOCIE) {
            Log.d(TAG, "invoice");

            invoice_invoicenumbercontext.setText(String.valueOf(user.getLastReceiptNumber()));
        } else {
            Log.d(TAG, "bid");
            String s = "הצעת המחיר תקפה למשך " +
                    event.getValidFor_Days() + " ימים. \nלאחר התקופה ההצעה תתבטל אוטומטית.";
            invoice_invoicenumber.setText("הצעת מחיר מספר: ");
            invoice_invoicenumbercontext.setText(String.valueOf(user.getLastBidNumber()));
            invoice_paiedpaypal.setText(s);
            invoice_paiedcashcontext.setVisibility(View.GONE);
            invoice_paypalconfirmcontext.setVisibility(View.GONE);
            invoice_paypalconfirmcontext.setVisibility(View.GONE);
            invoice_paiedpaypalcontext.setVisibility(View.GONE);
            invoice_paiedcash.setVisibility(View.GONE);
            invoice_paiedpaypal.setVisibility(View.VISIBLE);
            invoice_paiedpaypal.setGravity(Gravity.RIGHT);
            invoice_paiedpaypal.setTextSize(12);


        }

        for (int i = 0; i < event.getServices().size(); i++) {
            Log.d(TAG, "line " + i);

            switch (i + 1) {
                case 1:
                    setLine1();
                    break;
                case 2:
                    setLine2();
                    break;

                case 3:
                    setLine3();
                    break;

                case 4:
                    setLine4();
                    break;

                case 5:
                    setLine5();
                    break;

                case 6:
                    setLine6();
                    break;

                case 7:
                    setLine7();
                    break;

                case 8:
                    setLine8();
                    break;

                case 9:
                    setLine9();
                    break;

                case 10:
                    setLine11();
                    break;

                case 11:
                    setLine12();
                    break;

                case 12:
                    setLine13();
                    break;

                case 13:
                    setLine14();
                    break;

                case 14:
                    setLine15();
                    break;

                case 15:
                    setLine16();
                    break;

            }
            Log.d(TAG, "callback");


        }
        if (event.getPayment().getPaidByPayPal() > 0)
            invoice_paypalconfirmcontext.setText(event.getPayment().getPaypalProof().getTransactionId());
        else {
            invoice_paypalconfirmcontext.setVisibility(View.INVISIBLE);
            invoice_paypalconfirm.setVisibility(View.INVISIBLE);
        }

        invoice_paiedpaypalcontext.setText(String.valueOf(event.getPayment().getPaidByPayPal()));
        invoice_paiedcashcontext.setText(String.valueOf(event.getPayment().getPaidByCash()));


        //   callBack_invoice.ready();
    }

    private void setLine1() {
        invoice_total1.setText(String.valueOf(event.getServices().get(0).getTotalPrice()));
        invoice_priceperunit1.setText(String.valueOf(event.getServices().get(0).getServicePrice()));
        invoice_count1.setText(String.valueOf(event.getServices().get(0).getCount()));
        invoice_description1.setText(String.valueOf(event.getServices().get(0).getServiceName()));
        invoice_total1.setVisibility(View.VISIBLE);
        invoice_priceperunit1.setVisibility(View.VISIBLE);
        invoice_count1.setVisibility(View.VISIBLE);
        invoice_description1.setVisibility(View.VISIBLE);
    }

    private void setLine2() {
        invoice_total2.setText(String.valueOf(event.getServices().get(1).getTotalPrice()));
        invoice_priceperunit2.setText(String.valueOf(event.getServices().get(1).getServicePrice()));
        invoice_count2.setText(String.valueOf(event.getServices().get(1).getCount()));
        invoice_description2.setText(String.valueOf(event.getServices().get(1).getServiceName()));
        invoice_total2.setVisibility(View.VISIBLE);
        invoice_priceperunit2.setVisibility(View.VISIBLE);
        invoice_count2.setVisibility(View.VISIBLE);
        invoice_description2.setVisibility(View.VISIBLE);
    }

    private void setLine3() {
        invoice_total3.setText(event.getServices().get(2).getTotalPrice() + "");
        invoice_priceperunit3.setText(event.getServices().get(2).getServicePrice() + "");
        invoice_count3.setText(event.getServices().get(2).getCount() + "");
        invoice_description3.setText(event.getServices().get(2).getServiceName() + "");
        invoice_total3.setVisibility(View.VISIBLE);
        invoice_priceperunit3.setVisibility(View.VISIBLE);
        invoice_count3.setVisibility(View.VISIBLE);
        invoice_description3.setVisibility(View.VISIBLE);
    }

    private void setLine4() {
        invoice_total4.setText(event.getServices().get(3).getTotalPrice() + "");
        invoice_priceperunit4.setText(event.getServices().get(3).getServicePrice() + "");
        invoice_count4.setText(event.getServices().get(3).getCount() + "");
        invoice_description4.setText(event.getServices().get(3).getServiceName() + "");
        invoice_total4.setVisibility(View.VISIBLE);
        invoice_priceperunit4.setVisibility(View.VISIBLE);
        invoice_count4.setVisibility(View.VISIBLE);
        invoice_description4.setVisibility(View.VISIBLE);
    }

    private void setLine5() {
        invoice_total5.setText(event.getServices().get(4).getTotalPrice() + "");
        invoice_priceperunit5.setText(event.getServices().get(4).getServicePrice() + "");
        invoice_count5.setText(event.getServices().get(4).getCount() + "");
        invoice_description5.setText(event.getServices().get(4).getServiceName() + "");
        invoice_total5.setText(event.getServices().get(4).getTotalPrice() + "");
        invoice_total5.setVisibility(View.VISIBLE);
        invoice_priceperunit5.setVisibility(View.VISIBLE);
        invoice_count5.setVisibility(View.VISIBLE);
        invoice_description5.setVisibility(View.VISIBLE);
    }

    private void setLine6() {
        invoice_total6.setText(event.getServices().get(5).getTotalPrice() + "");
        invoice_priceperunit6.setText(event.getServices().get(5).getServicePrice() + "");
        invoice_count6.setText(event.getServices().get(5).getCount() + "");
        invoice_description6.setText(event.getServices().get(5).getServiceName() + "");
        invoice_total6.setText(event.getServices().get(5).getTotalPrice() + "");
        invoice_total6.setVisibility(View.VISIBLE);
        invoice_priceperunit6.setVisibility(View.VISIBLE);
        invoice_count6.setVisibility(View.VISIBLE);
        invoice_description6.setVisibility(View.VISIBLE);
    }

    private void setLine7() {
        invoice_total7.setText(event.getServices().get(6).getTotalPrice() + "");
        invoice_priceperunit7.setText(event.getServices().get(6).getServicePrice() + "");
        invoice_count7.setText(event.getServices().get(6).getCount() + "");
        invoice_description7.setText(event.getServices().get(6).getServiceName() + "");
        invoice_total7.setText(event.getServices().get(6).getTotalPrice() + "");
        invoice_total7.setVisibility(View.VISIBLE);
        invoice_priceperunit7.setVisibility(View.VISIBLE);
        invoice_count7.setVisibility(View.VISIBLE);
        invoice_description7.setVisibility(View.VISIBLE);
    }

    private void setLine8() {
        invoice_total8.setText(event.getServices().get(7).getTotalPrice() + "");
        invoice_priceperunit8.setText(event.getServices().get(7).getServicePrice() + "");
        invoice_count8.setText(event.getServices().get(7).getCount() + "");
        invoice_description8.setText(event.getServices().get(7).getServiceName() + "");
        invoice_total8.setText(event.getServices().get(7).getTotalPrice() + "");
        invoice_total8.setVisibility(View.VISIBLE);
        invoice_priceperunit8.setVisibility(View.VISIBLE);
        invoice_count8.setVisibility(View.VISIBLE);
        invoice_description8.setVisibility(View.VISIBLE);
    }

    private void setLine9() {
        invoice_total9.setText(event.getServices().get(8).getTotalPrice() + "");
        invoice_priceperunit9.setText(event.getServices().get(8).getServicePrice() + "");
        invoice_count9.setText(event.getServices().get(8).getCount() + "");
        invoice_description9.setText(event.getServices().get(8).getServiceName() + "");
        invoice_total9.setText(event.getServices().get(8).getTotalPrice() + "");
        invoice_total9.setVisibility(View.VISIBLE);
        invoice_priceperunit9.setVisibility(View.VISIBLE);
        invoice_count9.setVisibility(View.VISIBLE);
        invoice_description9.setVisibility(View.VISIBLE);
    }

    private void setLine11() {
        invoice_total11.setText(event.getServices().get(9).getTotalPrice() + "");
        invoice_priceperunit11.setText(event.getServices().get(9).getServicePrice() + "");
        invoice_count11.setText(event.getServices().get(9).getCount());
        invoice_description11.setText(event.getServices().get(9).getServiceName() + "");
        invoice_total11.setText(event.getServices().get(9).getTotalPrice() + "");
        invoice_total11.setVisibility(View.VISIBLE);
        invoice_priceperunit11.setVisibility(View.VISIBLE);
        invoice_count11.setVisibility(View.VISIBLE);
        invoice_description11.setVisibility(View.VISIBLE);
    }

    private void setLine12() {
        invoice_total12.setText(event.getServices().get(10).getTotalPrice() + "");
        invoice_priceperunit12.setText(event.getServices().get(10).getServicePrice() + "");
        invoice_count12.setText(event.getServices().get(10).getCount());
        invoice_description12.setText(event.getServices().get(10).getServiceName() + "");
        invoice_total12.setText(event.getServices().get(10).getTotalPrice() + "");
        invoice_total12.setVisibility(View.VISIBLE);
        invoice_priceperunit12.setVisibility(View.VISIBLE);
        invoice_count12.setVisibility(View.VISIBLE);
        invoice_description12.setVisibility(View.VISIBLE);
    }

    private void setLine13() {
        invoice_total13.setText(event.getServices().get(11).getTotalPrice() + "");
        invoice_priceperunit13.setText(event.getServices().get(11).getServicePrice() + "");
        invoice_count13.setText(event.getServices().get(11).getCount() + "");
        invoice_description13.setText(event.getServices().get(11).getServiceName() + "");
        invoice_total13.setText(event.getServices().get(11).getTotalPrice() + "");
        invoice_total13.setVisibility(View.VISIBLE);
        invoice_priceperunit13.setVisibility(View.VISIBLE);
        invoice_count13.setVisibility(View.VISIBLE);
        invoice_description13.setVisibility(View.VISIBLE);
    }

    private void setLine14() {
        invoice_total14.setText(event.getServices().get(12).getTotalPrice() + "");
        invoice_priceperunit14.setText(event.getServices().get(12).getServicePrice() + "");
        invoice_count14.setText(event.getServices().get(12).getCount() + "");
        invoice_description14.setText(event.getServices().get(12).getServiceName() + "");
        invoice_total14.setText(event.getServices().get(12).getTotalPrice() + "");
        invoice_total14.setVisibility(View.VISIBLE);
        invoice_priceperunit14.setVisibility(View.VISIBLE);
        invoice_count14.setVisibility(View.VISIBLE);
        invoice_description14.setVisibility(View.VISIBLE);
    }

    private void setLine15() {
        invoice_total15.setText(event.getServices().get(13).getTotalPrice() + "");
        invoice_priceperunit15.setText(event.getServices().get(13).getServicePrice() + "");
        invoice_count15.setText(event.getServices().get(13).getCount() + "");
        invoice_description15.setText(event.getServices().get(13).getServiceName() + "");
        invoice_total15.setText(event.getServices().get(13).getTotalPrice() + "");
        invoice_total15.setVisibility(View.VISIBLE);
        invoice_priceperunit15.setVisibility(View.VISIBLE);
        invoice_count15.setVisibility(View.VISIBLE);
        invoice_description15.setVisibility(View.VISIBLE);
    }

    private void setLine16() {
        invoice_total16.setText(event.getServices().get(14).getTotalPrice() + "");
        invoice_priceperunit16.setText(event.getServices().get(14).getServicePrice() + "");
        invoice_count16.setText(event.getServices().get(14).getCount() + "");
        invoice_description16.setText(event.getServices().get(14).getServiceName() + "");
        invoice_total16.setText(event.getServices().get(14).getTotalPrice() + "");
        invoice_total16.setVisibility(View.VISIBLE);
        invoice_priceperunit16.setVisibility(View.VISIBLE);
        invoice_count16.setVisibility(View.VISIBLE);
        invoice_description16.setVisibility(View.VISIBLE);
    }


    @Override
    protected void initView(View view) {
        invoice_name = view.findViewById(R.id.invoice_name);
        invoice_number = view.findViewById(R.id.invoice_number);
        invoice_phone = view.findViewById(R.id.invoice_phone);
        invoice_email = view.findViewById(R.id.invoice_email);
        invoice_address = view.findViewById(R.id.invoice_address);
        invoice_invoicenumbercontext = view.findViewById(R.id.invoice_invoicenumbercontext);
        invoice_invoicenumber = view.findViewById(R.id.invoice_invoicenumber);
        invoice_total = view.findViewById(R.id.invoice_total);
        invoice_total1 = view.findViewById(R.id.invoice_total1);
        invoice_total2 = view.findViewById(R.id.invoice_total2);
        invoice_total3 = view.findViewById(R.id.invoice_total3);
        invoice_total4 = view.findViewById(R.id.invoice_total4);
        invoice_total5 = view.findViewById(R.id.invoice_total5);
        invoice_total6 = view.findViewById(R.id.invoice_total6);
        invoice_total7 = view.findViewById(R.id.invoice_total7);
        invoice_total8 = view.findViewById(R.id.invoice_total8);
        invoice_total9 = view.findViewById(R.id.invoice_total9);
        invoice_total11 = view.findViewById(R.id.invoice_total11);
        invoice_total12 = view.findViewById(R.id.invoice_total12);
        invoice_total13 = view.findViewById(R.id.invoice_total13);
        invoice_total14 = view.findViewById(R.id.invoice_total14);
        invoice_total15 = view.findViewById(R.id.invoice_total15);
        invoice_total16 = view.findViewById(R.id.invoice_total16);
        invoice_priceperunit = view.findViewById(R.id.invoice_priceperunit);
        invoice_priceperunit1 = view.findViewById(R.id.invoice_priceperunit1);
        invoice_priceperunit2 = view.findViewById(R.id.invoice_priceperunit2);
        invoice_priceperunit3 = view.findViewById(R.id.invoice_priceperunit3);
        invoice_priceperunit4 = view.findViewById(R.id.invoice_priceperunit4);
        invoice_priceperunit5 = view.findViewById(R.id.invoice_priceperunit5);
        invoice_priceperunit6 = view.findViewById(R.id.invoice_priceperunit6);
        invoice_priceperunit7 = view.findViewById(R.id.invoice_priceperunit7);
        invoice_priceperunit8 = view.findViewById(R.id.invoice_priceperunit8);
        invoice_priceperunit9 = view.findViewById(R.id.invoice_priceperunit9);
        invoice_priceperunit11 = view.findViewById(R.id.invoice_priceperunit11);
        invoice_priceperunit12 = view.findViewById(R.id.invoice_priceperunit12);
        invoice_priceperunit13 = view.findViewById(R.id.invoice_priceperunit13);
        invoice_priceperunit14 = view.findViewById(R.id.invoice_priceperunit14);
        invoice_priceperunit15 = view.findViewById(R.id.invoice_priceperunit15);
        invoice_priceperunit16 = view.findViewById(R.id.invoice_priceperunit16);
        invoice_count = view.findViewById(R.id.invoice_count);
        invoice_count1 = view.findViewById(R.id.invoice_count1);
        invoice_count2 = view.findViewById(R.id.invoice_count2);
        invoice_count3 = view.findViewById(R.id.invoice_count3);
        invoice_count4 = view.findViewById(R.id.invoice_count4);
        invoice_count5 = view.findViewById(R.id.invoice_count5);
        invoice_count6 = view.findViewById(R.id.invoice_count6);
        invoice_count7 = view.findViewById(R.id.invoice_count7);
        invoice_count8 = view.findViewById(R.id.invoice_count8);
        invoice_count9 = view.findViewById(R.id.invoice_count9);
        invoice_count11 = view.findViewById(R.id.invoice_count11);
        invoice_count12 = view.findViewById(R.id.invoice_count12);
        invoice_count13 = view.findViewById(R.id.invoice_count13);
        invoice_count14 = view.findViewById(R.id.invoice_count14);
        invoice_count15 = view.findViewById(R.id.invoice_count15);
        invoice_count16 = view.findViewById(R.id.invoice_count16);
        invoice_description = view.findViewById(R.id.invoice_description);
        invoice_description1 = view.findViewById(R.id.invoice_description1);
        invoice_description2 = view.findViewById(R.id.invoice_description2);
        invoice_description3 = view.findViewById(R.id.invoice_description3);
        invoice_description4 = view.findViewById(R.id.invoice_description4);
        invoice_description5 = view.findViewById(R.id.invoice_description5);
        invoice_description6 = view.findViewById(R.id.invoice_description6);
        invoice_description7 = view.findViewById(R.id.invoice_description7);
        invoice_description8 = view.findViewById(R.id.invoice_description8);
        invoice_description9 = view.findViewById(R.id.invoice_description9);
        invoice_description11 = view.findViewById(R.id.invoice_description11);
        invoice_description12 = view.findViewById(R.id.invoice_description12);
        invoice_description13 = view.findViewById(R.id.invoice_description13);
        invoice_description14 = view.findViewById(R.id.invoice_description14);
        invoice_description15 = view.findViewById(R.id.invoice_description15);
        invoice_description16 = view.findViewById(R.id.invoice_description16);
        invoice_description = view.findViewById(R.id.invoice_description);
        invoice_paiedcashcontext = view.findViewById(R.id.invoice_paiedcashcontext);
        invoice_paypalconfirmcontext = view.findViewById(R.id.invoice_paypalconfirmcontext);
        invoice_paiedpaypalcontext = view.findViewById(R.id.invoice_paiedpaypalcontext);
        invoice_paypalconfirm = view.findViewById(R.id.invoice_paypalconfirm);
        invoice_customercontext = view.findViewById(R.id.invoice_customercontext);
        invoice_customerphonecontext = view.findViewById(R.id.invoice_customerphonecontext);
        invoice_totaltopaycontext = view.findViewById(R.id.invoice_totaltopaycontext);
        invoice_paiedcash = view.findViewById(R.id.invoice_paiedcash);
        invoice_paiedpaypal = view.findViewById(R.id.invoice_paiedpaypal);
        invoice_IMG_sig = view.findViewById(R.id.invoice_IMG_sig);
        invoice_LBL_date = view.findViewById(R.id.invoice_LBL_date);
        invoice_LBL_original = view.findViewById(R.id.invoice_LBL_original);
        invoice_mailcontext = view.findViewById(R.id.invoice_mailcontext);
        invoice_eventdatecontext = view.findViewById(R.id.invoice_eventdatecontext);
        invoice_timecontext = view.findViewById(R.id.invoice_timecontext);
        invoice_addcontext = view.findViewById(R.id.invoice_addcontext);


    }
}
