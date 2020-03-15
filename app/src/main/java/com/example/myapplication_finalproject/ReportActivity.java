package com.example.myapplication_finalproject;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.hendrix.pdfmyxml.PdfDocument;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {
    private final String TAG = getClass().getCanonicalName() + "_Log";
    private Button reports_BTN_export;
    private TextView reports_LBL_year, reports_LBL_export,
            reports_LBL_l0, reports_LBL_l1, reports_LBL_l2, reports_LBL_l3, reports_LBL_l4, reports_LBL_l5, reports_LBL_l6, reports_LBL_l7, reports_LBL_l8, reports_LBL_l9, reports_LBL_l10, reports_LBL_l11, reports_LBL_l12, reports_LBL_l13,
            reports_LBL_lm0, reports_LBL_lm1, reports_LBL_lm2, reports_LBL_lm3, reports_LBL_lm4, reports_LBL_lm5, reports_LBL_lm6, reports_LBL_lm7, reports_LBL_lm8, reports_LBL_lm9, reports_LBL_lm10, reports_LBL_lm11, reports_LBL_lm12, reports_LBL_lm13,
            reports_LBL_rm0, reports_LBL_rm1, reports_LBL_rm2, reports_LBL_rm3, reports_LBL_rm4, reports_LBL_rm5, reports_LBL_rm6, reports_LBL_rm7, reports_LBL_rm8, reports_LBL_rm9, reports_LBL_rm10, reports_LBL_rm11, reports_LBL_rm12, reports_LBL_rm13,
            reports_LBL_r0, reports_LBL_r1, reports_LBL_r2, reports_LBL_r3, reports_LBL_r4, reports_LBL_r5, reports_LBL_r6, reports_LBL_r7, reports_LBL_r8, reports_LBL_r9, reports_LBL_r10, reports_LBL_r11, reports_LBL_r12, reports_LBL_r13;
    private Spinner reports_SPN_year;
    private String[] years;
    private Toolbar reports_TOOL_toolbar;
    private CreateReport report;
    private String y = "2020";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        findViews();
        setButtons();
        fillData();
    }

    private void fillData() {
        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(System.currentTimeMillis()));
        years = new String[currentYear - 2020 + 1];
        for (int i = currentYear; i >= 2020; i--)
            years[i - 2020] = String.valueOf(i);

        setReportPreview(years[years.length - 1]);
        reports_LBL_year.setText(years[years.length - 1]);

        ArrayAdapter<String> aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, years);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        reports_SPN_year.setAdapter(aa);
        reports_SPN_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                y = years[position];
                setReportPreview(y);
                reports_LBL_year.setText(years[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private int[] totalIncomeForYear(String year) {
        int[] res = new int[12];
        ArrayList<MyEvent> events = SortAndFilter.filterByPaid(FilesManager.getInstance().getMyEventsForYear(this, year));
        for (MyEvent e : events) {
            String[] date = e.getDate().split("/");
            res[Integer.parseInt(date[1]) - 1] += e.getTotalPrice();
        }

        return res;
    }

    private double[] totalExpensesForYear(String year) {
        double[] res = new double[12];
        ArrayList<Expenses> expenses = SortAndFilter.expensesFilterByYear(year, FilesManager.getInstance().getExpenses());
        for (Expenses e : expenses) {
            String[] date = e.getDate().split("/");
            res[Integer.parseInt(date[1]) - 1] += e.getAmount();
        }

        return res;
    }

    private double[] totalNetoIncomeForYear(String year) {
        double[] res = new double[12];
        int[] in = totalIncomeForYear(year);
        double[] exp = totalExpensesForYear(year);
        for (int i = 0; i < res.length; i++) {
            res[i] = (double) in[i] - exp[i];
        }
        return res;
    }

    private void setReportPreview(String year) {
        int[] in = totalIncomeForYear(year);
        double[] exp = totalExpensesForYear(year);
        double[] total = totalNetoIncomeForYear(year);
        for (int i = 0; i < total.length; i++) {
            switch (i) {
                case 0:
                    setRow(reports_LBL_rm1, in[0], reports_LBL_lm1, exp[0], reports_LBL_l1, total[0]);
                case 1:
                    setRow(reports_LBL_rm2, in[1], reports_LBL_lm2, exp[1], reports_LBL_l2, total[1]);
                case 2:
                    setRow(reports_LBL_rm3, in[2], reports_LBL_lm3, exp[2], reports_LBL_l3, total[2]);
                case 3:
                    setRow(reports_LBL_rm4, in[3], reports_LBL_lm4, exp[3], reports_LBL_l4, total[3]);
                case 4:
                    setRow(reports_LBL_rm5, in[4], reports_LBL_lm5, exp[4], reports_LBL_l5, total[4]);
                case 5:
                    setRow(reports_LBL_rm6, in[5], reports_LBL_lm6, exp[5], reports_LBL_l6, total[5]);
                case 6:
                    setRow(reports_LBL_rm7, in[6], reports_LBL_lm7, exp[6], reports_LBL_l7, total[6]);
                case 7:
                    setRow(reports_LBL_rm8, in[7], reports_LBL_lm8, exp[7], reports_LBL_l8, total[7]);
                case 8:
                    setRow(reports_LBL_rm9, in[8], reports_LBL_lm9, exp[8], reports_LBL_l9, total[7]);
                case 9:
                    setRow(reports_LBL_rm10, in[9], reports_LBL_lm10, exp[9], reports_LBL_l10, total[9]);
                case 10:
                    setRow(reports_LBL_rm11, in[10], reports_LBL_lm11, exp[10], reports_LBL_l11, total[10]);
                case 11:
                    setRow(reports_LBL_rm12, in[11], reports_LBL_lm12, exp[11], reports_LBL_l12, total[11]);
                case 12:
                    setRow(reports_LBL_rm13, sum(in), reports_LBL_lm13, sumDouble(exp), reports_LBL_l13, sumDouble(total));


            }
        }
        report = new CreateReport(this, in, exp, total, year, new CallBack_Invoice() {

            @Override
            public void ready() {
                savePdf();
            }
        });
    }

    private void savePdf() {
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
            lbl.setTextColor(ContextCompat.getColor(this, R.color.red));
    }

    private <T extends Number> void setColor(TextView lbl, T i) {
        if (i.doubleValue() > 0)
            lbl.setTextColor(ContextCompat.getColor(this, R.color.green));
        if (i.doubleValue() < 0)
            lbl.setTextColor(ContextCompat.getColor(this, R.color.red));
    }


    private void readyToCreate() {

        //File sdcard = this.getFilesDir();
        File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File dir = new File(sdcard.getAbsolutePath() + File.separator + Constants.DIR_NAME +
                File.separator + FilesManager.getInstance().getUser().getName() +
                File.separator + Constants.REPORTS);

        dir.mkdirs();
        // Log.d(TAG, " builder");

        new PdfDocument.Builder(this).addPage(report).orientation(PdfDocument.A4_MODE.PORTRAIT)
                .progressMessage(R.string.creat_pdf).progressTitle(R.string.creating_file)
                .renderWidth(1500).renderHeight(2115)
                .saveDirectory(dir)
                .filename("דו״ח הוצאות לשנת " + y + " נוצר בתאריך " +
                        new SimpleDateFormat("d.M.yyyy").format(System.currentTimeMillis()))
                .listener(new PdfDocument.Callback() {
                    @Override
                    public void onComplete(File file) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete");
                        new AlertDialog.Builder(ReportActivity.this)
                                .setTitle("נשמר")
                                .setMessage("הקובץ נשמר בהצלחה\nהקובץ נשמר בנתיב:\n" + file.getAbsolutePath())
                                .setPositiveButton("אישור", null)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Error");
                        Log.d(TAG, "Error " + e.getMessage());
                    }
                }).create().createPdf(this);
    }

    private void setButtons() {
        reports_TOOL_toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        reports_TOOL_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reports_BTN_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyToCreate();

            }
        });
        reports_LBL_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reports_BTN_export.performClick();

            }
        });

    }


    private void findViews() {
        reports_SPN_year = findViewById(R.id.reports_SPN_year);
        reports_LBL_year = findViewById(R.id.reports_LBL_year);
        reports_BTN_export = findViewById(R.id.reports_BTN_export);
        reports_TOOL_toolbar = findViewById(R.id.reports_TOOL_toolbar);

        reports_LBL_l0 = findViewById(R.id.reports_LBL_l0);
        reports_LBL_l1 = findViewById(R.id.reports_LBL_l1);
        reports_LBL_l2 = findViewById(R.id.reports_LBL_l2);
        reports_LBL_l3 = findViewById(R.id.reports_LBL_l3);
        reports_LBL_l4 = findViewById(R.id.reports_LBL_l4);
        reports_LBL_l5 = findViewById(R.id.reports_LBL_l5);
        reports_LBL_l6 = findViewById(R.id.reports_LBL_l6);
        reports_LBL_l7 = findViewById(R.id.reports_LBL_l7);
        reports_LBL_l8 = findViewById(R.id.reports_LBL_l8);
        reports_LBL_l9 = findViewById(R.id.reports_LBL_l9);
        reports_LBL_l10 = findViewById(R.id.reports_LBL_l10);
        reports_LBL_l11 = findViewById(R.id.reports_LBL_l11);
        reports_LBL_l12 = findViewById(R.id.reports_LBL_l12);
        reports_LBL_l13 = findViewById(R.id.reports_LBL_l13);
        reports_LBL_lm0 = findViewById(R.id.reports_LBL_lm0);
        reports_LBL_lm1 = findViewById(R.id.reports_LBL_lm1);
        reports_LBL_lm2 = findViewById(R.id.reports_LBL_lm2);
        reports_LBL_lm3 = findViewById(R.id.reports_LBL_lm3);
        reports_LBL_lm4 = findViewById(R.id.reports_LBL_lm4);
        reports_LBL_lm5 = findViewById(R.id.reports_LBL_lm5);
        reports_LBL_lm6 = findViewById(R.id.reports_LBL_lm6);
        reports_LBL_lm7 = findViewById(R.id.reports_LBL_lm7);
        reports_LBL_lm8 = findViewById(R.id.reports_LBL_lm8);
        reports_LBL_lm9 = findViewById(R.id.reports_LBL_lm9);
        reports_LBL_lm10 = findViewById(R.id.reports_LBL_lm10);
        reports_LBL_lm11 = findViewById(R.id.reports_LBL_lm11);
        reports_LBL_lm12 = findViewById(R.id.reports_LBL_lm12);
        reports_LBL_lm13 = findViewById(R.id.reports_LBL_lm13);
        reports_LBL_rm0 = findViewById(R.id.reports_LBL_rm0);
        reports_LBL_rm1 = findViewById(R.id.reports_LBL_rm1);
        reports_LBL_rm2 = findViewById(R.id.reports_LBL_rm2);
        reports_LBL_rm3 = findViewById(R.id.reports_LBL_rm3);
        reports_LBL_rm4 = findViewById(R.id.reports_LBL_rm4);
        reports_LBL_rm5 = findViewById(R.id.reports_LBL_rm5);
        reports_LBL_rm6 = findViewById(R.id.reports_LBL_rm6);
        reports_LBL_rm7 = findViewById(R.id.reports_LBL_rm7);
        reports_LBL_rm8 = findViewById(R.id.reports_LBL_rm8);
        reports_LBL_rm9 = findViewById(R.id.reports_LBL_rm9);
        reports_LBL_rm10 = findViewById(R.id.reports_LBL_rm10);
        reports_LBL_rm11 = findViewById(R.id.reports_LBL_rm11);
        reports_LBL_rm12 = findViewById(R.id.reports_LBL_rm12);
        reports_LBL_rm13 = findViewById(R.id.reports_LBL_rm13);
        reports_LBL_r0 = findViewById(R.id.reports_LBL_r0);
        reports_LBL_r1 = findViewById(R.id.reports_LBL_r1);
        reports_LBL_r2 = findViewById(R.id.reports_LBL_r2);
        reports_LBL_r3 = findViewById(R.id.reports_LBL_r3);
        reports_LBL_r4 = findViewById(R.id.reports_LBL_r4);
        reports_LBL_r5 = findViewById(R.id.reports_LBL_r5);
        reports_LBL_r6 = findViewById(R.id.reports_LBL_r6);
        reports_LBL_r7 = findViewById(R.id.reports_LBL_r7);
        reports_LBL_r8 = findViewById(R.id.reports_LBL_r8);
        reports_LBL_r9 = findViewById(R.id.reports_LBL_r9);
        reports_LBL_r10 = findViewById(R.id.reports_LBL_r10);
        reports_LBL_r11 = findViewById(R.id.reports_LBL_r11);
        reports_LBL_r12 = findViewById(R.id.reports_LBL_r12);
        reports_LBL_r13 = findViewById(R.id.reports_LBL_r13);
        reports_LBL_export = findViewById(R.id.reports_LBL_export);


    }
}
