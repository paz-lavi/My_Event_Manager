package com.example.myapplication_finalproject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FragmentInfo extends Fragment {
    private RecyclerView event_LST_services;
    private Adapter_EventInfo adapter_eventInfo;
    private View view;
    private ArrayList<OrderService> services;
    private Context context;
    private MyEvent event;
    private CallBack_ShowEventList callBack_showEventList;
    private boolean isMenuOpen = false;
    private OvershootInterpolator interpolator = new OvershootInterpolator();

    private Button event_BTN_payment;
    private TextView event_LBL_name;
    private TextView event_LBL_date;
    private TextView event_LBL_status;
    private TextView event_LBL_phone;
    private TextView event_LBL_mail;
    private TextView event_LBL_startingtime;
    private TextView event_LBL_amount;
    private TextView event_LBL_name_context;
    private TextView event_LBL_date_context;
    private TextView event_LBL_status_context;
    private TextView event_LBL_payment;
    private TextView event_LBL_phone_context;
    private TextView event_LBL_mail_context;
    private TextView event_LBL_startingtime_context;
    private TextView event_LBL_amount_context;
    private TextView event_LBL_amountleft_context;
    private TextView event_LBL_address_context;
    private Toolbar event_TOOL_toolbar;
    private FloatingActionButton event_FAB_waze, event_FAB_more, event_FAB_confirm, event_FAB_call, event_FAB_cancel;
    private float translationY = 100f;


    public FragmentInfo(Context context) {
        this.context = context;
    }

    public FragmentInfo(Context context, MyEvent event) {
        this.context = context;
        this.event = event;
        services = event.getServices();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {

            view = inflater.inflate(R.layout.fragment_show_event, container, false);
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //findViews(view)
        if (findViews(view)) {
            initInfo();
            initList();
            initFabMenu();

            event_TOOL_toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
            event_TOOL_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack_showEventList.back();
                }
            });

            event_BTN_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (event.getStatus().equals(Constants.STATUS_CONFIRM))
                        callBack_showEventList.makePayment(event);
                    else {
                        new AlertDialog.Builder(getContext())
                                .setTitle("שגיאה")
                                .setMessage("לא ניתן לבצע תשלום עבור אירוע לא מאושר")
                                .setPositiveButton("אישור", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            });
            event_LBL_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    event_BTN_payment.performClick();
                }
            });
        }


        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    callBack_showEventList.back();
                }
                return false;
            }
        });
    }

    private void clickOnWaze() {
        try {
            String address = event.getAddress().trim().replaceAll(" ", ",");
            String url = "https://waze.com/ul?q=" + address + "&navigate=yes";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // If Waze is not installed, open it in Google Play:
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
            startActivity(intent);
        }
    }

    private void initFabMenu() {
        event_FAB_call.setAlpha(0f);
        event_FAB_cancel.setAlpha(0f);
        event_FAB_confirm.setAlpha(0f);
        event_FAB_waze.setAlpha(0f);

        event_FAB_call.setTranslationY(translationY);
        event_FAB_cancel.setTranslationY(translationY);
        event_FAB_confirm.setTranslationY(translationY);
        event_FAB_waze.setTranslationY(translationY);

        event_FAB_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }
            }
        });


        event_FAB_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnPhone();
                closeMenu();

            }
        });
        event_FAB_waze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnWaze();
                closeMenu();

            }
        });

        event_FAB_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnConfirm();
                closeMenu();

            }
        });

        event_FAB_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnCancel();
                closeMenu();
            }
        });


    }

    private void clickOnPhone() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + event.getCustomerPhone()));
        startActivity(callIntent);
    }

    private void clickOnConfirm() {
        if (!event.getStatus().equals(Constants.STATUS_CONFIRM)) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            FilesManager.getInstance().updateEventStatus(event, Constants.STATUS_CONFIRM
                                    , context);
                            event_LBL_status_context.setText(Constants.STATUS_CONFIRM);

                            SendSms.manualConfirmMSG(event.getCustomerPhone(), event.getDate());
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("לאשר את האירוע?").setPositiveButton("כן", dialogClickListener)
                    .setNegativeButton("לא", dialogClickListener).show();


        }

    }

    private void clickOnCancel() {
        if (!event.getStatus().equals(Constants.STATUS_CANCEL)) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            FilesManager.getInstance().updateEventStatus(event, Constants.STATUS_CANCEL
                                    , context);
                            event_LBL_status_context.setText(Constants.STATUS_CANCEL);
                            SendSms.manualCCancelMSG(event.getCustomerPhone(), event.getDate());
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("לבטל את האירוע?").setPositiveButton("כן", dialogClickListener)
                    .setNegativeButton("לא", dialogClickListener).show();


        }

    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;

        event_FAB_more.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();

        event_FAB_call.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        event_FAB_cancel.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        event_FAB_confirm.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        event_FAB_waze.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();


    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;

        event_FAB_more.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        event_FAB_call.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        event_FAB_cancel.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        event_FAB_confirm.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        event_FAB_waze.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();

    }

    public void setEvent(MyEvent event) {

        this.event = event;
        services = event.getServices();
        initInfo();


    }

    public FragmentInfo setCallBack_showEventList(CallBack_ShowEventList callBack_showEventList) {
        this.callBack_showEventList = callBack_showEventList;
        return this;
    }


    private void initInfo() {

        event_LBL_name_context.setText(event.getCustomerName());
        event_LBL_date_context.setText(event.getDate());
        event_LBL_status_context.setText(event.getStatus());
        event_LBL_phone_context.setText(event.getCustomerPhone());
        event_LBL_address_context.setText(event.getAddress());
        event_LBL_mail_context.setText(event.geteMail());
        event_LBL_startingtime_context.setText(event.getStartingTime());
        event_LBL_amount_context.setText(String.valueOf(event.getTotalPrice()));
        event_LBL_amountleft_context.setText(String.valueOf(event.getPayment().getAmountleft()));

    }


    private void initList() {

        adapter_eventInfo = new Adapter_EventInfo(services);
        event_LST_services.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        event_LST_services.setLayoutManager(layoutManager);
        event_LST_services.setAdapter(adapter_eventInfo);


    }


    private boolean findViews(View view) {

        event_LBL_name_context = view.findViewById(R.id.event_LBL_name_context);
        event_LBL_date_context = view.findViewById(R.id.event_LBL_date_context);
        event_LBL_status_context = view.findViewById(R.id.event_LBL_status_context);
        event_LBL_phone_context = view.findViewById(R.id.event_LBL_phone_context);
        event_LBL_mail_context = view.findViewById(R.id.event_LBL_mail_context);
        event_LBL_startingtime_context = view.findViewById(R.id.event_LBL_startingtime_context);
        event_LBL_amount_context = view.findViewById(R.id.event_LBL_amount_context);
        event_LST_services = view.findViewById(R.id.event_LST_services);
        event_BTN_payment = view.findViewById(R.id.event_BTN_payment);
        event_LBL_name = view.findViewById(R.id.event_LBL_name);
        event_LBL_date = view.findViewById(R.id.event_LBL_date);
        event_LBL_status = view.findViewById(R.id.event_LBL_status);
        event_LBL_phone = view.findViewById(R.id.event_LBL_phone);
        event_LBL_mail = view.findViewById(R.id.event_LBL_mail);
        event_LBL_address_context = view.findViewById(R.id.event_LBL_address_context);
        // event_LBL_address_context.setMovementMethod(new ScrollingMovementMethod());
        event_LBL_mail_context.setMovementMethod(new ScrollingMovementMethod());
        event_LBL_startingtime = view.findViewById(R.id.event_LBL_startingtime);
        event_LBL_amount = view.findViewById(R.id.event_LBL_amount);
        event_LBL_amountleft_context = view.findViewById(R.id.event_LBL_amountleft_context);
        event_TOOL_toolbar = view.findViewById(R.id.event_TOOL_toolbar);
        event_FAB_waze = view.findViewById(R.id.event_FAB_waze);
        event_FAB_more = view.findViewById(R.id.event_FAB_more);
        event_FAB_confirm = view.findViewById(R.id.event_FAB_confirm);
        event_FAB_cancel = view.findViewById(R.id.event_FAB_cancel);
        event_FAB_call = view.findViewById(R.id.event_FAB_call);
        event_LBL_payment = view.findViewById(R.id.event_LBL_payment);

        return true;

    }

}
