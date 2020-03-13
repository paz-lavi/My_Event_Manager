package com.example.myapplication_finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FragmentEventsList extends Fragment {
    private CallBack_ShowEvent callBack_showEvent;


    private View view;
    private Context context;
    private OvershootInterpolator interpolator = new OvershootInterpolator();
    private float translationY = 100f;
    private RecyclerView eventviewer_LST_services;
    private ArrayList<MyEvent> events;
    private Adapter_EventModel adapter_eventModel;
    private Toolbar eventviewer_TOOL_toolbar;
    private Spinner eventviewer_SPN_filter;
    private FloatingActionButton eventviewer_FAB_search, eventviewer_FAB_phone, eventviewer_FAB_date;
    private boolean isMenuOpen = false;
    private String phone = "";
    private long date1, date2;

    private final String[] filters = {"אירועים עתידיים",
            "אירועי עבר",
            "כל האירועים"
            , "אירועי עבר לא משולמים"
            , "אירועים משולמים"
            , "אירועים לא משולמים"
            , "אירועים עתידיים מאושרים"
            , "אירועים עתידיים לא מאושרים"
    };

    public FragmentEventsList setEvents(ArrayList<MyEvent> events) {
        this.events = events;
        initList(SortAndFilter.filterByFutureEvents(events));
        return this;
    }

    public void setCallBack(CallBack_ShowEvent callBack_showEvent) {
        this.callBack_showEvent = callBack_showEvent;
    }

    public FragmentEventsList(ArrayList<MyEvent> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("pttt", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("pttt", "onCreateView");
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_events_list, container, false);
        }

        findViews(view);
        initFabMenu();


        ArrayAdapter<String> aa = new ArrayAdapter(context, android.R.layout.simple_spinner_item, filters);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        eventviewer_SPN_filter.setAdapter(aa);
        eventviewer_SPN_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        initList(SortAndFilter.filterByFutureEvents(events));
                        break;
                    case 1:
                        initList(SortAndFilter.filterByPreviousEvents(events));
                        break;
                    case 2:
                        SortAndFilter.sortByDate(events);
                        initList(events);
                        break;
                    case 3:
                        initList(SortAndFilter.filterByNotPaidPreviousEvents(events));
                        break;
                    case 4:
                        initList(SortAndFilter.filterByPaid(events));
                        break;
                    case 5:
                        initList(SortAndFilter.filterByNotPaid(events));
                        break;
                    case 6:
                        initList(SortAndFilter.filterByFutureConfirmedEvents(events));
                        break;
                    case 7:
                        initList(SortAndFilter.filterByFutureNotConfirmedEvents(events));
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        eventviewer_TOOL_toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        eventviewer_TOOL_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack_showEvent.back();
            }
        });
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    callBack_showEvent.back();
                }
                return false;
            }
        });

        return view;
    }


    private void initList(ArrayList<MyEvent> e) {
        adapter_eventModel = new Adapter_EventModel(e);
        eventviewer_LST_services.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        eventviewer_LST_services.setLayoutManager(layoutManager);
        eventviewer_LST_services.setAdapter(adapter_eventModel);

        adapter_eventModel.SetOnItemClickListener(new Adapter_EventModel.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, MyEvent event) {
                callBack_showEvent.showEvent(event);
            }
        });
    }


    private void initFabMenu() {
        eventviewer_FAB_phone.setAlpha(0f);
        eventviewer_FAB_date.setAlpha(0f);

        eventviewer_FAB_phone.setTranslationY(translationY);
        eventviewer_FAB_date.setTranslationY(translationY);

        eventviewer_FAB_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }
            }
        });

        eventviewer_FAB_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnPhone();
                closeMenu();

            }
        });

        eventviewer_FAB_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnDate();
                closeMenu();

            }
        });


    }

    private void clickOnPhone() {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("חיפוש לפי מספר טלפון");

// Set up the input
        final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("חפש", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                phone = input.getText().toString();
                initList(SortAndFilter.filterByPhoneNumber(events, phone));
            }
        });
        builder.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void clickOnDate() {

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        Pair<Long, Long> sel = new Pair<>(today, today);
        MaterialDatePicker.Builder<Pair<Long, Long>> builder =
                MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("בחר תאריכים לסינון");
        builder.setSelection(sel);
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        builder.setCalendarConstraints(constraintsBuilder.build());
        MaterialDatePicker<Pair<Long, Long>> picker = builder.build();

        picker.show(getFragmentManager(), picker.toString());

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {


            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                date1 = selection.first;
                date2 = selection.second;
                filterByDates();

            }
        });

    }

    private void filterByDates() {
        SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
        String start_date = formatter.format(date1);
        String end_date = formatter.format(date2);
        initList(SortAndFilter.filterByDateRange(FilesManager.getInstance().getEvents(context)
                , start_date, end_date));
    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;

        eventviewer_FAB_search.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();

        eventviewer_FAB_date.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        eventviewer_FAB_phone.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();


    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;

        eventviewer_FAB_search.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        eventviewer_FAB_date.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        eventviewer_FAB_phone.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();

    }

    private void findViews(View view) {
        eventviewer_LST_services = view.findViewById(R.id.eventviewer_LST_services);
        eventviewer_TOOL_toolbar = view.findViewById(R.id.eventviewer_TOOL_toolbar);
        eventviewer_SPN_filter = view.findViewById(R.id.eventviewer_SPN_filter);
        eventviewer_FAB_search = view.findViewById(R.id.eventviewer_FAB_search);
        eventviewer_FAB_phone = view.findViewById(R.id.eventviewer_FAB_phone);
        eventviewer_FAB_date = view.findViewById(R.id.eventviewer_FAB_date);
    }
}
