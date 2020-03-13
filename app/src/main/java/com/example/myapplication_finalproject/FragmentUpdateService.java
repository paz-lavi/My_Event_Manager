package com.example.myapplication_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentUpdateService extends Fragment {

    private UpdateServices updateServices;

    private View view;
    private Button editservices_BTN_update;
    private Button editservices_BTN_remove;
    private Button editservices_BTN_save;
    private EditText editservices_EDT_name;
    private EditText editservices_EDT_price;
    private EditText editservices_EDT_max;
    private TextView editservices_LBL_update;
    private MyService service;
    private boolean update = false;

    public void setCallBack(UpdateServices updateServices) {
        this.updateServices = updateServices;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_edit_service, container, false);
        }

        findViews(view);
        if (service != null) {
            editservices_EDT_name.setHint(service.getServiceName());
            editservices_EDT_price.setHint("" + service.getServicePrice());
            editservices_EDT_max.setHint("" + service.getMaxPerEvent());
        }

        editservices_BTN_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClick();
            }
        });

        editservices_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBTclick();
            }
        });
        editservices_BTN_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeClick();
            }
        });


        return view;
    }

    private void removeClick() {
        updateServices.remove(service);
        editservices_EDT_name.setText("");
        editservices_EDT_price.setText("");
        editservices_EDT_max.setText("");
        editservices_EDT_name.setHint("");
        editservices_EDT_price.setHint("");
        editservices_EDT_max.setHint("");
        editservices_BTN_update.setText("ערוך");
    }


    private void updateClick() {
        if (!update) {
            editservices_EDT_name.setText(service.getServiceName());
            editservices_EDT_price.setText(String.valueOf(service.getServicePrice()));
            editservices_EDT_max.setText(String.valueOf(service.getMaxPerEvent()));
            update = true;
            editservices_LBL_update.setText("ביטול");
            editservices_BTN_update.setBackgroundResource(R.drawable.ic_cancel_color);

        } else {
            editservices_EDT_name.setText("");
            editservices_EDT_price.setText("");
            editservices_EDT_max.setText("");
            update = false;
            editservices_LBL_update.setText("ערוך");
            editservices_BTN_update.setBackgroundResource(R.drawable.ic_edit_file);


        }
    }

    private void onBTclick() {
        if (!update)
            return;
        int price, max;
        String name = editservices_EDT_name.getText().toString();
        try {
            price = Integer.parseInt(editservices_EDT_price.getText().toString());
        } catch (NumberFormatException e) {
            price = -1;
        }

        try {
            max = Integer.parseInt(editservices_EDT_max.getText().toString());
        } catch (NumberFormatException e) {
            max = -1;
        }
        if (price > 0 && max > 0 && !name.equals("")) {
            service.setServiceName(name);
            service.setServicePrice(price);
            service.setMaxPerEvent(max);
            if (updateServices.update(service)) {


                updateServices.toast(Constants.SERVICE_ADD_SUC);
                editservices_EDT_name.setText("");
                editservices_EDT_price.setText("");
                editservices_EDT_max.setText("");
                editservices_EDT_name.setHint(service.getServiceName());
                editservices_EDT_price.setHint(String.valueOf(service.getServicePrice()));
                editservices_EDT_max.setHint(String.valueOf(service.getMaxPerEvent()));
            } else
                updateServices.toast(Constants.SERVICE_ADD_FAIL);


        } else {
            if (price <= 0)
                updateServices.toast(Constants.PRICE_MSG);

            if (max <= 0)
                updateServices.toast(Constants.MAX_MSG);

            if (name.equals(""))
                updateServices.toast(Constants.NAME_MSG);

        }

    }

    public void setService(MyService service) {
        this.service = service;
        if (view != null) {
            editservices_EDT_name.setHint(service.getServiceName());
            editservices_EDT_price.setHint(String.valueOf(service.getServicePrice()));
            editservices_EDT_max.setHint(String.valueOf(service.getMaxPerEvent()));
        }
    }

    private void findViews(View view) {
        editservices_BTN_update = view.findViewById(R.id.editservices_BTN_update);
        editservices_BTN_save = view.findViewById(R.id.editservices_BTN_save);
        editservices_EDT_name = view.findViewById(R.id.editservices_EDT_name);
        editservices_EDT_price = view.findViewById(R.id.editservices_EDT_price);
        editservices_EDT_max = view.findViewById(R.id.editservices_EDT_max);
        editservices_LBL_update = view.findViewById(R.id.editservices_LBL_update);
        editservices_BTN_remove = view.findViewById(R.id.editservices_BTN_remove);
    }


}
