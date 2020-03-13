package com.example.myapplication_finalproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentEventInfo extends Fragment {
    private RecyclerView event_LST_services;
    private Adapter_EventInfo adapter_eventInfo;
    private View view;
    private ArrayList<OrderService> services;
    private Context context;
    private MyEvent event;
    private CallBack_ShowEventList callBack_showEventList;

    private Button event_BTN_payment;
    private TextView event_LBL_name, event_LBL_date, event_LBL_status, event_LBL_phone, event_LBL_mail, event_LBL_startingtime,
            event_LBL_amount, event_LBL_name_context, event_LBL_date_context, event_LBL_status_context, event_LBL_phone_context,
            event_LBL_mail_context, event_LBL_startingtime_context, event_LBL_amount_context;


    public FragmentEventInfo setEvent(MyEvent event) {
        Log.d("eventpttp", event.toString());
        this.event = event;
        services = event.getServices();
        initInfo();
        initList();
        return this;
    }

    public FragmentEventInfo setCallBack_showEventList(CallBack_ShowEventList callBack_showEventList) {
        this.callBack_showEventList = callBack_showEventList;
        return this;
    }


    public FragmentEventInfo(Context context) {

        this.context = context;
        Log.d("pttt", "FFFFFFF!!!");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("pttt", "onCreate!!!");
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("pttt", "onCreateView");
        if (view == null) {
            Log.d("pttt", "onCreateView");

            view = inflater.inflate(R.layout.fragment_show_event, container, false);
            Log.d("pttt", "onCreateView");
        }
        Log.d("pttt", "onCreateView");


        findViews(view);

        event_BTN_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack_showEventList.makePayment(event);
            }
        });


        return view;
    }


    private void findViews(View view) {
        Log.d("ptttp", "find views");

        event_BTN_payment = view.findViewById(R.id.event_BTN_payment);
        event_LBL_name = view.findViewById(R.id.event_LBL_name);
        event_LBL_date = view.findViewById(R.id.event_LBL_date);
        event_LBL_status = view.findViewById(R.id.event_LBL_status);
        event_LBL_phone = view.findViewById(R.id.event_LBL_phone);
        event_LBL_mail = view.findViewById(R.id.event_LBL_mail);
        event_LBL_startingtime = view.findViewById(R.id.event_LBL_startingtime);
        event_LBL_amount = view.findViewById(R.id.event_LBL_amount);
        event_LBL_name_context = view.findViewById(R.id.event_LBL_name_context);
        event_LBL_date_context = view.findViewById(R.id.event_LBL_date_context);
        event_LBL_status_context = view.findViewById(R.id.event_LBL_status_context);
        event_LBL_phone_context = view.findViewById(R.id.event_LBL_phone_context);
        event_LBL_mail_context = view.findViewById(R.id.event_LBL_mail_context);
        event_LBL_startingtime_context = view.findViewById(R.id.event_LBL_startingtime_context);
        event_LBL_amount_context = view.findViewById(R.id.event_LBL_amount_context);
        event_LST_services = view.findViewById(R.id.event_LST_services);

    }


    private void initInfo() {
        event_LBL_name_context.setText(event.getCustomerName());
        event_LBL_date_context.setText(event.getDate());
        event_LBL_status_context.setText(event.getStatus());
        event_LBL_phone_context.setText(event.getCustomerPhone());
        event_LBL_mail_context.setText(event.geteMail());
        event_LBL_startingtime_context.setText(event.getStartingTime());
        event_LBL_amount_context.setText(event.getTotalPrice());
    }


    private void initList() {
        adapter_eventInfo = new Adapter_EventInfo(services);
        event_LST_services.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        event_LST_services.setLayoutManager(layoutManager);
        event_LST_services.setAdapter(adapter_eventInfo);


    }

}
