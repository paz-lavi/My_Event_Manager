package com.example.myapplication_finalproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentServicesList extends Fragment {
    private CallBack_ChangeService callBack_changeService;
    private RecyclerView show_LST_services;
    private Adapter_ServiceModel adapter_serviceModel;
    private View view;
    private ArrayList<MyService> services;
    private Context context;

    public FragmentServicesList setServices(ArrayList<MyService> services) {
        this.services = services;
        initList();
        return this;
    }

    public void setCallBack(CallBack_ChangeService callBack_changeService) {
        this.callBack_changeService = callBack_changeService;
    }

    public FragmentServicesList(ArrayList<MyService> services, Context context) {
        this.services = services;
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_show_services, container, false);
        }

        findViews(view);
        initList();


        return view;
    }


    private void initList() {
        adapter_serviceModel = new Adapter_ServiceModel(services);
        show_LST_services.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        show_LST_services.setLayoutManager(layoutManager);
        show_LST_services.setAdapter(adapter_serviceModel);

        adapter_serviceModel.SetOnItemClickListener(new Adapter_ServiceModel.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, MyService services) {
                callBack_changeService.change(FragmentServicesList.this.services.get(position));
            }
        });

    }

    private void findViews(View view) {
        show_LST_services = view.findViewById(R.id.show_LST_services);
    }
}
