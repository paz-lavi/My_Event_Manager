package com.example.myapplication_finalproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.hendrix.pdfmyxml.PdfDocument;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.io.File;
import java.math.BigDecimal;

public class PayPalActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = FilesManager.getInstance().getUser().getPaypalClientID();

    private static final int REQUEST_CODE_PAYMENT = 1;

    private MyEvent event;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("My Event Manager");


    private Button payment_BTN_cash, payment_BTN_resend, payment_BTN_paypal;
    private TextView payment_LBL_totalamount;
    private TextView payment_LBL_paidamount;
    private TextView payment_LBL_leftamount;
    private TextView payment_LBL_paidpaypalamount;
    private TextView payment_LBL_totalgot_context;
    private TextView payment_LBL_cash;
    private TextView payment_LBL_resend;
    private TextView payment_LBL_res;
    private EditText payment_EDT_cash;
    private Toolbar payment_TOOL_toolbar;
    private LinearLayout payment_LAY_resend;
    private LinearLayout payment_LAY_cash;

    private StorageReference mStorageRef;
    private CreateInvoice invoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_v2);
        findViews();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        String str = getIntent().getExtras().getString(Constants.EVENT_TO_PAY);
        if (str != null) {
            Gson gson = new Gson();
            event = gson.fromJson(str, MyEvent.class);
        } else eventNotFoundError();


        if (event == null)
            eventNotFoundError();

        Intent intent = new Intent(PayPalActivity.this, PayPalFuturePaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        setTextViews();
        setButtons();


    }


    private void setTextViews() {
        payment_LBL_totalamount.setText(String.valueOf(event.getPayment().getTotalAmount()));
        payment_LBL_paidamount.setText(String.valueOf(event.getPayment().getPaidByCash()));
        payment_LBL_leftamount.setText(String.valueOf(event.getPayment().getAmountleft()));
        payment_LBL_totalgot_context.setText(String.valueOf(event.getPayment().getAmountPaid()));
        payment_LBL_paidpaypalamount.setText(String.valueOf(event.getPayment().getPaidByPayPal()));
    }

    private void eventNotFoundError() {
        Toast.makeText(this, "לא נמצא אירוע לתשלום", Toast.LENGTH_SHORT).show();
        finish();
    }


    protected void displayResultText(PaymentConfirmation confirm) {

        event.getPayment().setpaidByPayPal(confirm.getProofOfPayment());
        setTextViews();


        payment_EDT_cash.setText("");
        FilesManager.getInstance().updateEventPayment(event, PayPalActivity.this);

        payment_LBL_res.setText("Result : " + confirm.getProofOfPayment().getTransactionId());
        new AlertDialog.Builder(PayPalActivity.this)
                .setTitle("שולם")
                .setMessage("התקבל תשלום בPayPal. " + "\nמספר אישור:\n" + confirm.getProofOfPayment().getTransactionId())
                .setPositiveButton("סגור", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
        createInvoice();
    }


    private PayPalPayment payment() {

        return new PayPalPayment(new BigDecimal(event.getPayment().getAmountleft()), "ILS", "תשלום עבור אירוע",
                PayPalPayment.PAYMENT_INTENT_SALE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));


                        displayResultText(confirm);


                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    private void createInvoice() {
        FilesManager manager = FilesManager.getInstance();
        manager.newInvoice();
        event.setInvocieNumber(manager.getUser().getLastReceiptNumber());
        manager.updateEvent(this, event);


        invoice = new CreateInvoice(this, event, Constants.INVOCIE, new CallBack_Invoice() {
            @Override
            public void ready() {
                readyToCreate();
            }
        });

        readyToCreate();
    }

    private void readyToCreate() {
        File sdcard = this.getFilesDir();
        File dir = new File(sdcard.getAbsolutePath() + File.separator + FirebaseAuth.getInstance().getCurrentUser() +
                File.separator + Constants.DIR_NAME + File.separator + Constants.INVOICES);

        dir.mkdirs();

        PdfDocument doc = new PdfDocument(this);
        String title = "קבלה מספר " + FilesManager.getInstance().getUser().getLastReceiptNumber();
// add as many pages as you have
        doc.addPage(invoice);

        doc.setRenderWidth(1500);
        doc.setRenderHeight(2115);
        doc.setOrientation(PdfDocument.A4_MODE.PORTRAIT);
        doc.setProgressTitle(R.string.creat_pdf);
        doc.setProgressMessage(R.string.creating_file);
        doc.setFileName(title);
        doc.setSaveDirectory(dir);
        doc.setInflateOnMainThread(false);
        doc.setListener(new PdfDocument.Callback() {
            @Override
            public void onComplete(File file) {
                Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete");
                send(file);
            }

            @Override
            public void onError(Exception e) {
                Log.i(PdfDocument.TAG_PDF_MY_XML, "Error");
            }
        });

        doc.createPdf(this);


    }

    private void send(File pdf) {


        Uri file = Uri.fromFile(pdf);
        final StorageReference riversRef = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).child(Constants.INVOICES).child(pdf.getName());

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                event.setInvocieURL(uri.toString());
                                FilesManager.getInstance().updateEvent(PayPalActivity.this, event);
                                SendSms.sendInvoice(event);
                                Log.d(TAG, "onSuccess: uri= " + uri.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("aappapal", exception.getMessage());
                    }
                });
    }

    private void resendClick() {
        SendSms.sendInvoice(event);
        new AlertDialog.Builder(PayPalActivity.this)
                .setTitle("נשלח")
                .setMessage("קישור לקבלה נשלח ללקוח בהודעת sms")
                .setPositiveButton("אישור", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void paypalClick() {
        if (event.getPayment().getAmountleft() == 0)
            return;

        PayPalPayment payment = payment();

        Intent intent = new Intent(PayPalActivity.this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);

    }

    private void cashClick() {
        if (event.getPayment().getAmountleft() == 0)
            return;
        try {
            final int cash = Integer.parseInt(payment_EDT_cash.getText().toString().trim());
            if (cash <= event.getPayment().getAmountleft())
                new AlertDialog.Builder(PayPalActivity.this)
                        .setTitle("קבלת תשלום")
                        .setMessage("לאשר קבלת תשלום במזומן ע״ס " + cash + "₪?")
                        .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                event.getPayment().payWithCash(cash);
                                setTextViews();
                                payment_EDT_cash.setText("");
                                FilesManager.getInstance().updateEventPayment(event, PayPalActivity.this);
                                if (event.getPayment().getAmountleft() == 0)
                                    createInvoice();
                            }
                        }).setNegativeButton("ביטול", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            else {
                new AlertDialog.Builder(PayPalActivity.this)
                        .setTitle("שגיאה")
                        .setMessage("הסכום שהוזן גבוה מהסכום שנותר לתשלום")
                        .setPositiveButton("אישור", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        } catch (NumberFormatException e) {

        }

    }

    private void setButtons() {
        payment_TOOL_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (event.getPayment().getAmountleft() == 0) {
            payment_LAY_resend.setVisibility(View.VISIBLE);
            payment_BTN_paypal.setVisibility(View.GONE);
            payment_LBL_res.setVisibility(View.GONE);
            payment_LAY_cash.setVisibility(View.GONE);
            payment_EDT_cash.setVisibility(View.GONE);
            payment_BTN_resend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resendClick();
                }
            });
            payment_LBL_resend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    payment_LBL_resend.performClick();
                }
            });

        } else {
            payment_BTN_paypal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paypalClick();

                }
            });

            payment_BTN_cash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cashClick();

                }
            });
            payment_LBL_cash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    payment_BTN_cash.performClick();
                }
            });
        }
    }


    private void findViews() {
        payment_BTN_paypal = findViewById(R.id.payment_BTN_paypal);
        payment_BTN_cash = findViewById(R.id.payment_BTN_cash);
        payment_LBL_totalamount = findViewById(R.id.payment_LBL_totalamount);
        payment_LBL_paidamount = findViewById(R.id.payment_LBL_paidamount);
        payment_LBL_leftamount = findViewById(R.id.payment_LBL_leftamount);
        payment_LBL_paidpaypalamount = findViewById(R.id.payment_LBL_paidpaypalamount);
        payment_LBL_totalgot_context = findViewById(R.id.payment_LBL_totalgot_context);
        payment_EDT_cash = findViewById(R.id.payment_EDT_cash);
        payment_LBL_res = findViewById(R.id.payment_LBL_res);
        payment_LBL_cash = findViewById(R.id.payment_LBL_cash);
        payment_BTN_resend = findViewById(R.id.payment_BTN_resend);
        payment_TOOL_toolbar = findViewById(R.id.payment_TOOL_toolbar);
        payment_LBL_resend = findViewById(R.id.payment_LBL_resend);
        payment_LAY_resend = findViewById(R.id.payment_LAY_resend);
        payment_LAY_cash = findViewById(R.id.payment_LAY_cash);
    }
}


