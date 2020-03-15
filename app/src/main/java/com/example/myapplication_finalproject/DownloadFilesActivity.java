package com.example.myapplication_finalproject;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DownloadFilesActivity extends AppCompatActivity {
    private Button down_BTN_expenses, down_BTN_bid, down_BTN_invoice;
    private Toolbar down_TOOL_toolbar;
    private File bid, invoice, expenses;
    private TextView down_LBL_invoice, down_LBL_expenses, down_LBL_bid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_files);
        findViews();


        File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File dir = new File(sdcard.getAbsolutePath() + File.separator + Constants.DIR_NAME +
                File.separator + FilesManager.getInstance().getUser().getName(), "הוצאות");
        if (!dir.exists())
            dir.mkdirs();

        expenses = dir;

        dir = new File(sdcard.getAbsolutePath() + File.separator + Constants.DIR_NAME +
                File.separator + FilesManager.getInstance().getUser().getName(), "קבלות");
        if (!dir.exists())
            dir.mkdirs();

        invoice = dir;

        dir = new File(sdcard.getAbsolutePath() + File.separator + Constants.DIR_NAME +
                File.separator + FilesManager.getInstance().getUser().getName(), "הצעות מחיר");
        if (!dir.exists())
            dir.mkdirs();

        bid = dir;

        setButtons();
    }

    private void setButtons() {

        down_TOOL_toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        down_TOOL_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        down_BTN_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allExpensesInvoiceClick();
            }
        });
        down_BTN_bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allBidsClick();
            }
        });
        down_BTN_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allInvoice();
            }
        });
        down_LBL_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down_BTN_invoice.performClick();
            }
        });
        down_LBL_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down_BTN_expenses.performClick();
            }
        });
        down_LBL_bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down_BTN_bid.performClick();
            }
        });
    }

    private void allInvoice() {
        alterRUsure(invoice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadAllInvoice();
            }
        });
    }

    private void allExpensesInvoiceClick() {
        alterRUsure(expenses, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadAllExpensesInvoice();
            }
        });
    }

    private void allBidsClick() {
        alterRUsure(bid, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadAllBids();
            }
        });
    }

    private void downloadAllInvoice() {
        ArrayList<MyEvent> events = SortAndFilter.filterByPaid(FilesManager.getInstance().getMyEvents(this));
        String s;
        alterStart(invoice);
        for (MyEvent e : events
        ) {
            if (e.getInvocieURL() != null) {
                s = "קבלה מספר -  " + e.getInvocieNumber() + " .pdf";
                if (!e.getBidURL().equals(""))
                    downloadFile(s, invoice, e.getInvocieURL());
            }
        }
    }

    private void downloadAllBids() {
        ArrayList<MyEvent> events = FilesManager.getInstance().getMyEvents(this);
        String s;
        alterStart(bid);
        for (MyEvent e : events
        ) {
            if (e.getBidURL() != null) {

                s = "הצעת מחיר מספר - " + e.getBidNumber() + " .pdf";
                if (!e.getBidURL().equals(""))
                    downloadFile(s, bid, e.getBidURL());
            }
        }
    }

    private void downloadAllExpensesInvoice() {
        HashMap<String, HashMap<String, ArrayList<Expenses>>> ex = FilesManager.getInstance().getExpenses();

        if (ex == null)
            return;

        String s = "";
        alterStart(expenses);
        ArrayList<Expenses> temp = new ArrayList<>();
        for (String k : ex.keySet()) {
            for (String key : ex.get(k).keySet()) {
                temp.addAll(ex.get(k).get(key));
            }
        }
        ;
        for (Expenses e : temp
        ) {
            s = "הוצאה בתאריך - " + e.getDate().replaceAll("/", ".") + " עבור - " + e.getPurpose() + ".jpg";
            downloadFile(s, expenses, e.getImage_url());
        }

    }

    private void downloadFile(String fileName, File dir, String url) {

        DownloadManager downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.allowScanningByMediaScanner();
        request.setDestinationUri(Uri.fromFile(new File(dir.getAbsolutePath() + File.separator + fileName)));

        downloadManager.enqueue(request);

    }

    private void alterStart(File file) {
        new AlertDialog.Builder(DownloadFilesActivity.this)
                .setTitle("ההורדה התחילה")
                .setMessage("הקבצים ישמרו בנתיב:\n" + file.getAbsolutePath())
                .setPositiveButton("אישור", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void alterRUsure(File file, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(DownloadFilesActivity.this)
                .setTitle("להוריד קבצים?")
                .setMessage("הקבצים ישמרו בנתיב:\n" + file.getAbsolutePath())
                .setPositiveButton("התחל הורדה", listener)
                .setNegativeButton("ביטול", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }


    private void findViews() {
        down_BTN_expenses = findViewById(R.id.down_BTN_expenses);
        down_BTN_bid = findViewById(R.id.down_BTN_bid);
        down_BTN_invoice = findViewById(R.id.down_BTN_invoice);
        down_TOOL_toolbar = findViewById(R.id.down_TOOL_toolbar);
        down_LBL_invoice = findViewById(R.id.down_LBL_invoice);
        down_LBL_bid = findViewById(R.id.down_LBL_bid);
        down_LBL_expenses = findViewById(R.id.down_LBL_expenses);

    }
}
