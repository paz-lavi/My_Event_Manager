package com.example.myapplication_finalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import java.util.ArrayList;

public class EventViewer extends AppCompatActivity {
    private ArrayList<MyEvent> events;
    private CallBack_ShowEvent callBack_showEvent;
    private CallBack_ShowEventList callBack_showEventList;
    private Gson gson;
    private boolean showlist;
    private FragmentEventsList eventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_viewer);
        gson = new Gson();


        events = FilesManager.getInstance().getMyEvents(EventViewer.this);

        eventsList = new FragmentEventsList(events, EventViewer.this);


        callBack_showEvent = new CallBack_ShowEvent() {
            @Override
            public void showEvent(MyEvent event) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.eventfrag_LAY_frag, new FragmentInfo(EventViewer.this,
                        event).setCallBack_showEventList(callBack_showEventList));
                transaction.commit();
                showlist = false;


            }

            @Override
            public void back() {
                EventViewer.this.finish();
            }
        };

        callBack_showEventList = new CallBack_ShowEventList() {
            @Override
            public void back() {
                Bundle extras = getIntent().getExtras();
                if (extras == null) {
                    selectListFragment();
                    return;
                }
                boolean launchedFromNotif = false;
                if (extras.containsKey(Constants.INTENT_FLAG)) {
                    launchedFromNotif = extras.getBoolean(Constants.INTENT_FLAG);
                }

                if (launchedFromNotif == Constants.INTENT_FROM_NOTIFICATION) {
                    Intent intent = new Intent(EventViewer.this, Menu.class);
                    startActivity(intent);
                    finish();
                } else {

                    selectListFragment();
                }


            }

            @Override
            public void makePayment(MyEvent event) {
                Intent intent = new Intent(EventViewer.this, PayPalActivity.class);
                intent.putExtra(Constants.EVENT_TO_PAY, gson.toJson(event));
                startActivity(intent);
            }
        };

        eventsList.setCallBack(callBack_showEvent);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean launchedFromNotif = false;
            if (extras.containsKey(Constants.INTENT_FLAG)) {

                launchedFromNotif = extras.getBoolean(Constants.INTENT_FLAG);

            }

            if (launchedFromNotif == Constants.INTENT_FROM_NOTIFICATION) {

                String temp = extras.getString(Constants.EVENT_FROM_INTENT);

                MyEvent event = gson.fromJson(temp, MyEvent.class);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.eventfrag_LAY_frag, new FragmentInfo(EventViewer.this,
                        event).setCallBack_showEventList(callBack_showEventList));

                transaction.commit();
                showlist = false;

            } else {

                selectListFragment();

            }
        } else {

            selectListFragment();

        }


    }


    private void selectListFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.eventfrag_LAY_frag, eventsList);
        transaction.commit();
        showlist = true;
    }

    @Override
    public void onBackPressed() {
        if (showlist)
            super.onBackPressed();
        else {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                selectListFragment();
                return;
            }
            boolean launchedFromNotif = false;
            if (extras.containsKey(Constants.INTENT_FLAG)) {
                launchedFromNotif = extras.getBoolean(Constants.INTENT_FLAG);
            }

            if (launchedFromNotif == Constants.INTENT_FROM_NOTIFICATION) {
                Intent intent = new Intent(EventViewer.this, Menu.class);
                startActivity(intent);
                finish();
            } else {

                selectListFragment();
            }

        }
    }
}
