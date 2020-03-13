package com.example.myapplication_finalproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.stetho.Stetho;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Menu extends AppCompatActivity {
    private Button menu_BTN_createnewevent;
    private Button menu_BTN_myservices;
    private Button menu_BTN_myevents;
    private Button menu_BTN_setting;
    private Button menu_BTN_logout;
    private Button menu_BTN_reports;
    private Button menu_BTN_expenses;
    private Button menu_BTN_down;
    private TextView menu_LBL_createnewevent;
    private TextView menu_LBL_myservices;
    private TextView menu_LBL_myevents;
    private TextView menu_LBL_setting;
    private TextView menu_LBL_logout;
    private TextView menu_LBL_reports;
    private TextView menu_LBL_expenses;
    private TextView menu_LBL_down;
    private TextView menu_LBL_name;
    private TextView menu_LBL_events;
    private RecyclerView menu_LST_events;
    private FilesManager filesManager = FilesManager.getInstance();
    private Adapter_EventsThisWeek adapter;
    private RelativeLayout menu_LAY_no_events;
    public static final int REQUEST_CODE = 987;

    private long numOfDays = 1000 * 60 * 60 * 24 * 7;// 1sec * 60sec * 60min * 24H * 7days


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_vs2);
        Stetho.initializeWithDefaults(this);
        findViews();
        filesManager.refreshUser(this);
        filesManager.getServices(this);
        filesManager.updateData(new CallBack_FireBase_Finish() {
            @Override
            public void onFinish() {
                startRun();
            }
        });


    }

    private void startRun() {
        filesManager.readFromDatabase(this, new CallBack_FireBase_Finish() {
            @Override
            public void onFinish() {

                menu_BTN_expenses.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Menu.this, ExpensesActivity.class);
                        Menu.this.startActivity(intent);
                    }
                });

                menu_BTN_reports.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Menu.this, ReportActivity.class);
                        Menu.this.startActivity(intent);
                    }
                });
                menu_BTN_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Menu.this, SettingActivity.class);
                        Menu.this.startActivity(intent);
                    }
                });
                menu_BTN_myservices.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Menu.this, ServicesManager.class);
                        Menu.this.startActivity(intent);
                    }
                });
                menu_BTN_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Menu.this, DownloadFilesActivity.class);
                        Menu.this.startActivity(intent);
                    }
                });
                String s = FilesManager.getInstance().getUser().getName();
                menu_LBL_name.setText("שלום " + (s == null ? "" : s));

            }
        });

        filesManager.readEventsFromDatabase(this, new CallBack_FireBase_Finish() {
            @Override
            public void onFinish() {
                menu_BTN_createnewevent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Menu.this, CreateEvent.class);
                        Menu.this.startActivity(intent);
                    }
                });


                menu_BTN_myevents.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Menu.this, EventViewer.class);
                        Menu.this.startActivity(intent);
                    }
                });

                initList();
                startAlarmManager();
            }
        });


        menu_BTN_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutClick();
            }
        });

        menu_LBL_createnewevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_BTN_createnewevent.performClick();
            }
        });
        menu_LBL_myservices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_BTN_myservices.performClick();
            }
        });
        menu_LBL_myevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_BTN_myevents.performClick();
            }
        });
        menu_LBL_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_BTN_setting.performClick();
            }
        });
        menu_LBL_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_BTN_logout.performClick();
            }
        });
        menu_LBL_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_BTN_reports.performClick();
            }
        });
        menu_LBL_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_BTN_expenses.performClick();
            }
        });
        menu_LBL_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_BTN_down.performClick();
            }
        });

    }

    private void logoutClick() {
        new AlertDialog.Builder(Menu.this)
                .setTitle("להתנתק?")
                .setMessage("האם אתה בטוח שברצונך להתנתק?")
                .setPositiveButton("התנתק", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Menu.this, LogInActivity.class);
                        Menu.this.startActivity(intent);
                        finish();
                    }
                })
                .setNeutralButton("מעדיף להישאר", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void startAlarmManager() {

        Intent i = new Intent(this, MyReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // Intent intent_ = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        long now = System.currentTimeMillis();
        calendar.setTimeInMillis(now);//+ 1000 * 60 * 60 * 24
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 10)  /* if now after 10am set triggered for tomorrow else for today */
            calendar.setTimeInMillis(now + 1000 * 60 * 60 * 24);//+ 1000 * 60 * 60 * 24

        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
/*
Alarm will be triggered exactly at 10:00 every day
*/
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);


    }


    private void initList() {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        Long now = System.currentTimeMillis();
        String start = format.format(now);
        String end = format.format(now + numOfDays);

        ArrayList<MyEvent> show = SortAndFilter.filterByDateRange(FilesManager.getInstance().getEvents(this)
                , start, end);
        if (show.size() == 0) {
            menu_LAY_no_events.setVisibility(View.VISIBLE);
            menu_LST_events.setVisibility(View.GONE);

        } else {
            menu_LST_events.setVisibility(View.VISIBLE);
            menu_LAY_no_events.setVisibility(View.GONE);
            adapter = new Adapter_EventsThisWeek(show);
            menu_LST_events.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            menu_LST_events.setLayoutManager(layoutManager);
            menu_LST_events.setAdapter(adapter);

            adapter.SetOnItemClickListener(new Adapter_EventsThisWeek.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, final MyEvent note) {
                    new AlertDialog.Builder(Menu.this)
                            .setTitle("פרטי האירוע:")
                            .setMessage(note.toString())
                            .setPositiveButton("אישור", null)
                            .setNegativeButton("התקשר ללקוח", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                    callIntent.setData(Uri.parse("tel:" + note.getCustomerPhone()));
                                    startActivity(callIntent);
                                }
                            })
                            .setNeutralButton("נווט לאירוע", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        String address = note.getAddress().trim().replaceAll(" ", ",");
                                        String url = "https://waze.com/ul?q=" + address + "&navigate=yes";
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException ex) {
                                        // If Waze is not installed, open it in Google Play:
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                                        startActivity(intent);
                                    }
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
            });
        }
    }

    private void findViews() {
        menu_BTN_createnewevent = findViewById(R.id.menu_BTN_createnewevent);
        menu_BTN_myservices = findViewById(R.id.menu_BTN_myservices);
        menu_BTN_myevents = findViewById(R.id.menu_BTN_myevents);
        menu_BTN_setting = findViewById(R.id.menu_BTN_setting);
        menu_BTN_logout = findViewById(R.id.menu_BTN_logout);
        menu_BTN_reports = findViewById(R.id.menu_BTN_reports);
        menu_BTN_down = findViewById(R.id.menu_BTN_down);
        menu_BTN_expenses = findViewById(R.id.menu_BTN_expenses);
        menu_LBL_name = findViewById(R.id.menu_LBL_name);
        menu_LST_events = findViewById(R.id.menu_LST_events);
        menu_LAY_no_events = findViewById(R.id.menu_LAY_no_events);
        menu_LBL_events = findViewById(R.id.menu_LBL_events);
        menu_LBL_events.setMovementMethod(new ScrollingMovementMethod());
        menu_LBL_createnewevent = findViewById(R.id.menu_LBL_createnewevent);
        menu_LBL_myservices = findViewById(R.id.menu_LBL_myservices);
        menu_LBL_myevents = findViewById(R.id.menu_LBL_myevents);
        menu_LBL_setting = findViewById(R.id.menu_LBL_setting);
        menu_LBL_logout = findViewById(R.id.menu_LBL_logout);
        menu_LBL_reports = findViewById(R.id.menu_LBL_reports);
        menu_LBL_down = findViewById(R.id.menu_LBL_down);
        menu_LBL_expenses = findViewById(R.id.menu_LBL_expenses);

    }


}
