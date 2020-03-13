package com.example.myapplication_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class FragmentService extends Fragment {

    private CallBack_ActivityList callBack_activityList;

    private View view;
    Button allservices_BTN_add;
    EditText allservices_EDT_name;
    EditText allservices_EDT_price;
    EditText allservices_EDT_max;

    public void setCallBack(CallBack_ActivityList _callBack_activityList) {
        this.callBack_activityList = _callBack_activityList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_service, container, false);
        }

        findViews(view);

        allservices_BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBTclick();
            }
        });


        return view;
    }

    private void onBTclick() {
        int price, max;
        String name = allservices_EDT_name.getText().toString();
        try {
            price = Integer.parseInt(allservices_EDT_price.getText().toString());
        } catch (NumberFormatException e) {
            price = -1;
        }

        try {
            max = Integer.parseInt(allservices_EDT_max.getText().toString());
        } catch (NumberFormatException e) {
            max = -1;
        }
        if (price > 0 && max > 0 && !name.equals("")) {
            if (callBack_activityList.addService(name, price, max)) {
                callBack_activityList.toast(Constants.SERVICE_ADD_SUC);
                allservices_EDT_name.setText("");
                allservices_EDT_price.setText("");
                allservices_EDT_max.setText("");
            } else
                callBack_activityList.toast(Constants.SERVICE_ADD_FAIL);


        } else {
            if (price <= 0)
                callBack_activityList.toast(Constants.PRICE_MSG);

            if (max <= 0)
                callBack_activityList.toast(Constants.MAX_MSG);

            if (name.equals(""))
                callBack_activityList.toast(Constants.NAME_MSG);

        }

    }


    private void findViews(View view) {
        allservices_BTN_add = view.findViewById(R.id.allservices_BTN_add);
        allservices_EDT_name = view.findViewById(R.id.allservices_EDT_name);
        allservices_EDT_price = view.findViewById(R.id.allservices_EDT_price);
        allservices_EDT_max = view.findViewById(R.id.allservices_EDT_max);
    }


}
