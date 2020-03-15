package com.example.myapplication_finalproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hendrix.pdfmyxml.PdfDocument;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CreateEvent extends AppCompatActivity {

    private RecyclerView create_LST_notes;
    private Adapter_OrderModel adapter_orderModel;
    private Button create_BTN_createevent;
    private Button create_BTN_date;
    private Button create_BTN_time;
    private EditText create_EDT_name;
    private EditText create_EDT_phone;
    private EditText create_EDT_email;
    private EditText create_EDT_address;
    private EditText create_EDT_valid;
    private Toolbar create_TOOL_toolbar;
    private String date;
    private String time;
    private Switch create_SW_rem;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePicker;
    private String TAG = CreateEvent.class.getSimpleName();
    private MyEvent event;
    private StorageReference mStorageRef;
    private CreateInvoice invoice;
    private TextView create_LBL_date, create_LBL_time, create_LBL_createevent;
    private boolean check = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        mStorageRef = FirebaseStorage.getInstance().getReference();


        findViews();
        initList();
        setButtons();
    }


    private void alter(String msg) {
        new AlertDialog.Builder(this)
                .setTitle("שגיאה")
                .setMessage(msg)
                .setPositiveButton("אישור", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void onBTNclick() {
        String name, phone, email, address, valid;
        name = create_EDT_name.getText().toString();
        phone = create_EDT_phone.getText().toString();
        email = create_EDT_email.getText().toString();
        valid = create_EDT_valid.getText().toString();

        address = create_EDT_address.getText().toString();


        String[] strings = {name, phone, email, address, valid};
        if (checkText(strings)) {

            event = new MyEvent(name, phone, email, date, this.time, address,
                    Integer.parseInt(valid), check, adapter_orderModel.getFilteredServices());

            if (FilesManager.getInstance().addEvent(event, CreateEvent.this)) {
                Toast.makeText(CreateEvent.this, Constants.EVENT_ADD_SUC, Toast.LENGTH_SHORT).show();

                create_EDT_name.setText("");
                create_EDT_phone.setText("");
                create_EDT_email.setText("");
                create_EDT_address.setText("");
                create_EDT_valid.setText("");
                Log.d(TAG, " before create");
                createBid(event);
            } else alter(Constants.EVENT_ADD_FAIL);

        } else alter(Constants.EVENT_ADD_MSG);

    }

    private boolean checkText(String[] strings) {
        for (String s : strings) {
            if (s.equals(""))
                return false;

        }
        return true;
    }


    private void initList() {
        ArrayList<OrderService> services = getServices();
        adapter_orderModel = new Adapter_OrderModel(this, services);
        create_LST_notes.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        create_LST_notes.setLayoutManager(layoutManager);
        create_LST_notes.setAdapter(adapter_orderModel);

        adapter_orderModel.SetOnItemClickListener(new Adapter_OrderModel.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, OrderService service) {
                showService(service);
            }
        });
    }


    private void showService(OrderService service) {
        String s = "מחיר ליחידה: " + service.getServicePrice() + "₪\n" + "כמות מקסימלית לאירוע: " + service.getMaxPerEvent();
        new AlertDialog.Builder(CreateEvent.this)
                .setTitle(service.getServiceName())
                .setMessage(s)
                .setPositiveButton("סגור", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private ArrayList<OrderService> getServices() {
        ArrayList<MyService> services;
        ArrayList<OrderService> orderServices = new ArrayList<>();
        FilesManager filesManager = FilesManager.getInstance();
        services = filesManager.getServices(CreateEvent.this);
        if (services == null)
            return new ArrayList<>();
        for (MyService s : services) {
            orderServices.add(new OrderService(s, 0));
        }
        return orderServices;
    }


    private void createBid(final MyEvent event) {
        Log.d(TAG, " enter create bit");


        invoice = new CreateInvoice(this, event, Constants.BID, new CallBack_Invoice() {
            @Override
            public void ready() {
                Log.d(TAG, " ready");
                readyToCreate(event);
            }
        });
        readyToCreate(event);


    }

    /**
     * create bid pdf file
     */
    private void readyToCreate(final MyEvent event) {
        Log.d(TAG, " enter readyToCreate");

        FilesManager.getInstance().newBid();
        File sdcard = this.getFilesDir();
        File dir = new File(sdcard.getAbsolutePath() + File.separator + FirebaseAuth.getInstance().getCurrentUser() +
                File.separator + Constants.DIR_NAME + File.separator + Constants.BIDS);

        dir.mkdirs();
        Log.d(TAG, " builder");

        new PdfDocument.Builder(this).addPage(invoice).orientation(PdfDocument.A4_MODE.PORTRAIT)
                .progressMessage(R.string.creat_pdf).progressTitle(R.string.creating_file)
                .renderWidth(1500).renderHeight(2115)
                .saveDirectory(dir)
                .filename("bid " + (FilesManager.getInstance().getUser().getLastBidNumber() - 1))
                .listener(new PdfDocument.Callback() {
                    @Override
                    public void onComplete(File file) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete");
                        Log.d(TAG, "Complete");
                        send(file, event);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Error");
                        Log.d(TAG, "Error");
                    }
                }).create().createPdf(this);
    }

    /**
     * uploading the bid pdf file to firebase and send the link to the costumer
     */
    private void send(File pdf, final MyEvent event) {
        Log.d(TAG, " enter dens");

        Uri file = Uri.fromFile(pdf);
        final StorageReference riversRef = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).child(Constants.BIDS).child(pdf.getName());
        Log.d(TAG, " upload");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                event.setBidURL(uri.toString());
                                event.setBidNumber(FilesManager.getInstance().getUser().getLastBidNumber());
                                FilesManager.getInstance().updateEvent(CreateEvent.this, event);
                                SendSms.sendBid(event);
                                Log.d(TAG, "onSuccess: uri= " + uri.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("aappapal", exception.getMessage());
                        Log.d(TAG, " upload fail");

                    }
                });
    }


    private void setButtons() {
        create_TOOL_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        create_BTN_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                long now = System.currentTimeMillis();
                date = new SimpleDateFormat("dd/MM/yyyy").format(now);

                String[] datestr = date.split("/");

                datePickerDialog = new DatePickerDialog(CreateEvent.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        create_LBL_date.setText(date);
                    }
                }, Integer.parseInt(datestr[2]), Integer.parseInt(datestr[1]) - 1, Integer.parseInt(datestr[0]));

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        create_LBL_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_BTN_date.performLongClick();
            }
        });
        create_BTN_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(CreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        time = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute);
                        create_LBL_time.setText(time);
                    }
                }, 0, 0, true);
                timePicker.show();
            }
        });

        create_LBL_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_LBL_time.performLongClick();
            }
        });
        create_BTN_createevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBTNclick();
            }
        });
        create_LBL_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_BTN_time.performClick();
            }
        });
        create_LBL_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_LBL_date.performClick();
            }
        });
        create_LBL_createevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_BTN_createevent.performClick();
            }
        });

        create_SW_rem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check = isChecked;
            }
        });
        create_SW_rem.setChecked(check);

    }

    private void findViews() {
        create_LST_notes = findViewById(R.id.create_LST_notes);
        create_BTN_createevent = findViewById(R.id.create_BTN_createevent);
        create_EDT_name = findViewById(R.id.create_EDT_name);
        create_EDT_phone = findViewById(R.id.create_EDT_phone);
        create_EDT_email = findViewById(R.id.create_EDT_email);
        create_EDT_address = findViewById(R.id.create_EDT_address);
        create_BTN_date = findViewById(R.id.create_BTN_date);
        create_BTN_time = findViewById(R.id.create_BTN_time);
        create_EDT_valid = findViewById(R.id.create_EDT_valid);
        create_SW_rem = findViewById(R.id.create_SW_rem);
        create_TOOL_toolbar = findViewById(R.id.create_TOOL_toolbar);
        create_LBL_date = findViewById(R.id.create_LBL_date);
        create_LBL_time = findViewById(R.id.create_LBL_time);
        create_LBL_createevent = findViewById(R.id.create_LBL_createevent);
    }

}
