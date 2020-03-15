package com.example.myapplication_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentService extends Fragment {

    private CallBack_ActivityList callBack_activityList;

    private View view;
    private Button allservices_BTN_add, allservices_BTN_save, allservices_BTN_remove, allservices_BTN_cancel;
    private EditText allservices_EDT_name;
    private EditText allservices_EDT_price;
    private EditText allservices_EDT_max;
    private TextView allservices_LBL_add, allservices_LBL_save, allservices_LBL_remove, allservices_LBL_cancel;
    private LinearLayout allservices_LAY_btn, allservices_LAY_add;

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
        setEnabled(false);
        setButtons();

        return view;
    }


    private void onCancleClick() {
        setEnabled(false);
        allservices_LAY_btn.setVisibility(View.GONE);
        allservices_LAY_add.setVisibility(View.VISIBLE);
        callBack_activityList.visible();
    }

    private void onRemoveClick() {
        allservices_EDT_name.setText("");
        allservices_EDT_price.setText("");
        allservices_EDT_max.setText("");
    }

    private void onAddClick() {
        callBack_activityList.gone();
        setEnabled(true);
        allservices_LAY_btn.setVisibility(View.VISIBLE);
        allservices_LAY_add.setVisibility(View.GONE);
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

    private void setEnabled(boolean b) {
        allservices_EDT_name.setEnabled(b);
        allservices_EDT_price.setEnabled(b);
        allservices_EDT_max.setEnabled(b);
    }

    private void setButtons() {

        allservices_BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClick();
            }
        });
        allservices_LBL_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allservices_BTN_add.performClick();
            }
        });
        allservices_LBL_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allservices_BTN_add.performClick();
            }
        });
        allservices_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBTclick();
            }
        });
        allservices_LBL_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allservices_BTN_remove.performClick();
            }
        });
        allservices_BTN_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRemoveClick();
            }
        });
        allservices_LBL_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allservices_BTN_cancel.performClick();
            }
        });
        allservices_BTN_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancleClick();
            }
        });

    }

    private void findViews(View view) {
        allservices_BTN_add = view.findViewById(R.id.allservices_BTN_add);
        allservices_EDT_name = view.findViewById(R.id.allservices_EDT_name);
        allservices_EDT_price = view.findViewById(R.id.allservices_EDT_price);
        allservices_EDT_max = view.findViewById(R.id.allservices_EDT_max);
        allservices_LBL_add = view.findViewById(R.id.allservices_LBL_add);
        allservices_LAY_btn = view.findViewById(R.id.allservices_LAY_btn);
        allservices_LAY_add = view.findViewById(R.id.allservices_LAY_add);
        allservices_BTN_save = view.findViewById(R.id.allservices_BTN_save);
        allservices_LBL_save = view.findViewById(R.id.allservices_LBL_save);
        allservices_BTN_remove = view.findViewById(R.id.allservices_BTN_remove);
        allservices_LBL_remove = view.findViewById(R.id.allservices_LBL_remove);
        allservices_BTN_cancel = view.findViewById(R.id.allservices_BTN_cancel);
        allservices_LBL_cancel = view.findViewById(R.id.allservices_LBL_cancel);
    }


}
