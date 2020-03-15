package com.example.myapplication_finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class ServicesManager extends AppCompatActivity {
    private Button main_BTN_new;
    private Button main_BTN_edit;
    private FragmentUpdateService fragmentUpdateService;
    private FragmentService fragmentService;
    private FragmentServicesList fragmentServicesList;
    private Toolbar main_TOOL_toolbar;
    private FrameLayout main_LAY_bottom;
    private UpdateServices updateServices;
    private CallBack_ActivityList myCallBack;
    private CallBack_ChangeService callBack_changeService;
    private TextView main_LBL_new, setting_LBL_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_manager);
        findViews();
        createCallBacks();
        fragmentService = new FragmentService();
        fragmentService.setCallBack(myCallBack);

        fragmentUpdateService = new FragmentUpdateService();
        fragmentUpdateService.setCallBack(updateServices);
        fragmentServicesList = new FragmentServicesList(FilesManager.getInstance().getServices(
                ServicesManager.this), this);
        fragmentServicesList.setCallBack(callBack_changeService);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_LAY_top, fragmentService);
        transaction.replace(R.id.main_LAY_bottom, fragmentServicesList);
        transaction.commit();

        setButtons();
    }


    private void createCallBacks() {

        updateServices = new UpdateServices() {
            @Override
            public boolean update(MyService service) {
                boolean b = FilesManager.getInstance().updateService(service, ServicesManager.this);
                if (b)
                    fragmentServicesList.setServices(FilesManager.getInstance().getServices(ServicesManager.this));

                return b;
            }

            @Override
            public void remove(MyService service) {
                boolean b = FilesManager.getInstance().removeService(service, ServicesManager.this);
                if (b)
                    fragmentServicesList.setServices(FilesManager.getInstance().getServices(ServicesManager.this));

            }

            @Override
            public void toast(String msg) {
                Toast.makeText(ServicesManager.this, msg, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void gone() {
                main_LAY_bottom.setVisibility(View.GONE);
            }

            @Override
            public void visible() {
                main_LAY_bottom.setVisibility(View.VISIBLE);

            }
        };

        myCallBack = new CallBack_ActivityList() {
            @Override
            public boolean addService(String name, int price, int max) {

                boolean res = FilesManager.getInstance().addService(new MyService(name, price, max), ServicesManager.this);
                if (res)
                    fragmentServicesList.setServices(FilesManager.getInstance().getServices(ServicesManager.this));
                return res;
            }

            @Override
            public void toast(String msg) {
                Toast.makeText(ServicesManager.this, msg, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void gone() {
                main_LAY_bottom.setVisibility(View.GONE);
            }

            @Override
            public void visible() {
                main_LAY_bottom.setVisibility(View.VISIBLE);

            }
        };

        callBack_changeService = new CallBack_ChangeService() {
            @Override
            public void change(MyService service) {
                fragmentUpdateService.setService(service);

            }
        };

    }

    private void setButtons() {

        main_BTN_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_LAY_top, fragmentUpdateService);
                transaction.commit();
            }
        });
        main_BTN_new.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                main_LAY_bottom.setVisibility(View.VISIBLE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_LAY_top, fragmentService);
                transaction.commit();
            }
        });
        main_LBL_new.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                main_BTN_new.performClick();
            }
        });
        setting_LBL_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                main_BTN_edit.performClick();
            }
        });


        main_TOOL_toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        main_TOOL_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void findViews() {
        main_BTN_new = findViewById(R.id.main_BTN_new);
        main_BTN_edit = findViewById(R.id.main_BTN_edit);
        main_TOOL_toolbar = findViewById(R.id.main_TOOL_toolbar);
        main_LAY_bottom = findViewById(R.id.main_LAY_bottom);
        main_LBL_new = findViewById(R.id.main_LBL_new);
        setting_LBL_edit = findViewById(R.id.setting_LBL_edit);


    }
}
