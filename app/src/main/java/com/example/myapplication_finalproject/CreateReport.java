package com.example.myapplication_finalproject;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class CreateReport extends AbstractViewRenderer {
    private static final String TITLE = "דו״ח הכנסות והוצאות לשנת ";
    private TextView export_name;
    private TextView export_number;
    private TextView export_phone;
    private TextView export_email;
    private TextView export_address;
    private TextView export_invoicenumbercontext;
    private TextView export_invoicenumber;
    private TextView reports_LBL_year, export_LBL_title, export_LBL_date,
            export_LBL_l0, export_LBL_l1, export_LBL_l2, export_LBL_l3, export_LBL_l4, export_LBL_l5,
            export_LBL_l6, export_LBL_l7, export_LBL_l8, export_LBL_l9, export_LBL_l10,
            export_LBL_l11, export_LBL_l12, export_LBL_l13,
            export_LBL_lm0, export_LBL_lm1, export_LBL_lm2, export_LBL_lm3, export_LBL_lm4, export_LBL_lm5,
            export_LBL_lm6, export_LBL_lm7, export_LBL_lm8, export_LBL_lm9, export_LBL_lm10,
            export_LBL_lm11, export_LBL_lm12, export_LBL_lm13,
            export_LBL_rm0, export_LBL_rm1, export_LBL_rm2, export_LBL_rm3, export_LBL_rm4, export_LBL_rm5,
            export_LBL_rm6, export_LBL_rm7, export_LBL_rm8, export_LBL_rm9, export_LBL_rm10, export_LBL_rm11,
            export_LBL_rm12, export_LBL_rm13,
            export_LBL_r0, export_LBL_r1, export_LBL_r2, export_LBL_r3, export_LBL_r4, export_LBL_r5, export_LBL_r6,
            export_LBL_r7, export_LBL_r8, export_LBL_r9, export_LBL_r10, export_LBL_r11,
            export_LBL_r12, export_LBL_r13;


    private CallBack_Invoice callBack_invoice;


    private User user;
    private final String TAG = CreateReport.class.getSimpleName() + "LOG";

    public CreateReport(final Context ctx, int[] in, double[] out, double[] total,
                        String year, CallBack_Invoice callBack_invoice) {
        super(ctx, R.layout.report);
        this.user = FilesManager.getInstance().getUser();
        this.callBack_invoice = callBack_invoice;
        initView(_view);

        initFile(in, out, total, year);

    }

    private void initFile(int[] in, double[] exp, double[] total, String year) {
        Log.d(TAG, "enter init");

        export_name.setText(user.getName());
        export_number.setText("עוסק פטור: " + user.getId());
        export_phone.setText("טל: " + user.getPhone());
        export_email.setText("דוא״ל: " + user.getMail());
        export_address.setText(user.getStreet() + " " + user.getHouseNumber() + " " + user.getCity());
        export_LBL_title.setText(TITLE + year + ":");
        export_LBL_date.setText(new SimpleDateFormat("d/M/yyyy").format(System.currentTimeMillis()));

        for (int i = 0; i < total.length; i++) {
            switch (i) {
                case 0:
                    setRow(export_LBL_rm1, in[0], export_LBL_lm1, exp[0], export_LBL_l1, total[0]);
                case 1:
                    setRow(export_LBL_rm2, in[1], export_LBL_lm2, exp[1], export_LBL_l2, total[1]);
                case 2:
                    setRow(export_LBL_rm3, in[2], export_LBL_lm3, exp[2], export_LBL_l3, total[2]);
                case 3:
                    setRow(export_LBL_rm4, in[3], export_LBL_lm4, exp[3], export_LBL_l4, total[3]);
                case 4:
                    setRow(export_LBL_rm5, in[4], export_LBL_lm5, exp[4], export_LBL_l5, total[4]);
                case 5:
                    setRow(export_LBL_rm6, in[5], export_LBL_lm6, exp[5], export_LBL_l6, total[5]);
                case 6:
                    setRow(export_LBL_rm7, in[6], export_LBL_lm7, exp[6], export_LBL_l7, total[6]);
                case 7:
                    setRow(export_LBL_rm8, in[7], export_LBL_lm8, exp[7], export_LBL_l8, total[7]);
                case 8:
                    setRow(export_LBL_rm9, in[8], export_LBL_lm9, exp[8], export_LBL_l9, total[7]);
                case 9:
                    setRow(export_LBL_rm10, in[9], export_LBL_lm10, exp[9], export_LBL_l10, total[9]);
                case 10:
                    setRow(export_LBL_rm11, in[10], export_LBL_lm11, exp[10], export_LBL_l11, total[10]);
                case 11:
                    setRow(export_LBL_rm12, in[11], export_LBL_lm12, exp[11], export_LBL_l12, total[11]);
                case 12:
                    setRow(export_LBL_rm13, sum(in), export_LBL_lm13, sumDouble(exp), export_LBL_l13, sumDouble(total));


            }
        }
    }

    private double sumDouble(double[] in) {
        double res = 0;
        for (int i = 0; i < in.length; i++)
            res += in[i];
        return res;
    }

    private int sum(int[] in) {
        int res = 0;
        for (int i = 0; i < in.length; i++)
            res += in[i];
        return res;
    }

    private void setRow(TextView in, int i, TextView exp, double j, TextView total, double k) {
        in.setText(String.valueOf(i));
        setColor(in, i);
        exp.setText(new DecimalFormat("#.##").format(j));
        setExpColor(exp, j);
        total.setText(new DecimalFormat("#.##").format(k));
        setColor(total, k);

    }

    private <T extends Number> void setExpColor(TextView lbl, T i) {
        if (i.byteValue() != 0)
            lbl.setTextColor(ContextCompat.getColor(_ctx, R.color.red));
    }

    private <T extends Number> void setColor(TextView lbl, T i) {
        if (i.doubleValue() > 0)
            lbl.setTextColor(ContextCompat.getColor(_ctx, R.color.green));
        if (i.doubleValue() < 0)
            lbl.setTextColor(ContextCompat.getColor(_ctx, R.color.red));
    }


    @Override
    protected void initView(View view) {
        export_name = view.findViewById(R.id.export_name);
        export_number = view.findViewById(R.id.export_number);
        export_phone = view.findViewById(R.id.export_phone);
        export_email = view.findViewById(R.id.export_email);
        export_address = view.findViewById(R.id.export_address);
        export_LBL_title = view.findViewById(R.id.export_LBL_title);
        export_LBL_date = view.findViewById(R.id.export_LBL_date);


        export_LBL_l0 = view.findViewById(R.id.export_LBL_l0);
        export_LBL_l1 = view.findViewById(R.id.export_LBL_l1);
        export_LBL_l2 = view.findViewById(R.id.export_LBL_l2);
        export_LBL_l3 = view.findViewById(R.id.export_LBL_l3);
        export_LBL_l4 = view.findViewById(R.id.export_LBL_l4);
        export_LBL_l5 = view.findViewById(R.id.export_LBL_l5);
        export_LBL_l6 = view.findViewById(R.id.export_LBL_l6);
        export_LBL_l7 = view.findViewById(R.id.export_LBL_l7);
        export_LBL_l8 = view.findViewById(R.id.export_LBL_l8);
        export_LBL_l9 = view.findViewById(R.id.export_LBL_l9);
        export_LBL_l10 = view.findViewById(R.id.export_LBL_l10);
        export_LBL_l11 = view.findViewById(R.id.export_LBL_l11);
        export_LBL_l12 = view.findViewById(R.id.export_LBL_l12);
        export_LBL_l13 = view.findViewById(R.id.export_LBL_l13);
        export_LBL_lm0 = view.findViewById(R.id.export_LBL_lm0);
        export_LBL_lm1 = view.findViewById(R.id.export_LBL_lm1);
        export_LBL_lm2 = view.findViewById(R.id.export_LBL_lm2);
        export_LBL_lm3 = view.findViewById(R.id.export_LBL_lm3);
        export_LBL_lm4 = view.findViewById(R.id.export_LBL_lm4);
        export_LBL_lm5 = view.findViewById(R.id.export_LBL_lm5);
        export_LBL_lm6 = view.findViewById(R.id.export_LBL_lm6);
        export_LBL_lm7 = view.findViewById(R.id.export_LBL_lm7);
        export_LBL_lm8 = view.findViewById(R.id.export_LBL_lm8);
        export_LBL_lm9 = view.findViewById(R.id.export_LBL_lm9);
        export_LBL_lm10 = view.findViewById(R.id.export_LBL_lm10);
        export_LBL_lm11 = view.findViewById(R.id.export_LBL_lm11);
        export_LBL_lm12 = view.findViewById(R.id.export_LBL_lm12);
        export_LBL_lm13 = view.findViewById(R.id.export_LBL_lm13);
        export_LBL_rm0 = view.findViewById(R.id.export_LBL_rm0);
        export_LBL_rm1 = view.findViewById(R.id.export_LBL_rm1);
        export_LBL_rm2 = view.findViewById(R.id.export_LBL_rm2);
        export_LBL_rm3 = view.findViewById(R.id.export_LBL_rm3);
        export_LBL_rm4 = view.findViewById(R.id.export_LBL_rm4);
        export_LBL_rm5 = view.findViewById(R.id.export_LBL_rm5);
        export_LBL_rm6 = view.findViewById(R.id.export_LBL_rm6);
        export_LBL_rm7 = view.findViewById(R.id.export_LBL_rm7);
        export_LBL_rm8 = view.findViewById(R.id.export_LBL_rm8);
        export_LBL_rm9 = view.findViewById(R.id.export_LBL_rm9);
        export_LBL_rm10 = view.findViewById(R.id.export_LBL_rm10);
        export_LBL_rm11 = view.findViewById(R.id.export_LBL_rm11);
        export_LBL_rm12 = view.findViewById(R.id.export_LBL_rm12);
        export_LBL_rm13 = view.findViewById(R.id.export_LBL_rm13);
        export_LBL_r0 = view.findViewById(R.id.export_LBL_r0);
        export_LBL_r1 = view.findViewById(R.id.export_LBL_r1);
        export_LBL_r2 = view.findViewById(R.id.export_LBL_r2);
        export_LBL_r3 = view.findViewById(R.id.export_LBL_r3);
        export_LBL_r4 = view.findViewById(R.id.export_LBL_r4);
        export_LBL_r5 = view.findViewById(R.id.export_LBL_r5);
        export_LBL_r6 = view.findViewById(R.id.export_LBL_r6);
        export_LBL_r7 = view.findViewById(R.id.export_LBL_r7);
        export_LBL_r8 = view.findViewById(R.id.export_LBL_r8);
        export_LBL_r9 = view.findViewById(R.id.export_LBL_r9);
        export_LBL_r10 = view.findViewById(R.id.export_LBL_r10);
        export_LBL_r11 = view.findViewById(R.id.export_LBL_r11);
        export_LBL_r12 = view.findViewById(R.id.export_LBL_r12);
        export_LBL_r13 = view.findViewById(R.id.export_LBL_r13);


    }
}
