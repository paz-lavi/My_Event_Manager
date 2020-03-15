package com.example.myapplication_finalproject;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FilesManager {
    // static variable single_instance of type Singleton
    private static FilesManager single_instance = null;
    private final String TAG = this.getClass().getSimpleName() + "_LOG";
    private String signaturePath;
    private Gson gson;
    private MySharedPreferences msp;
    private HashMap<String, HashMap<String, HashMap<String, HashMap<String, MyEvent>>>> events;
    private ArrayList<MyService> myservices;
    private HashMap<String, HashMap<String, ArrayList<Expenses>>> expenses;
    private HashMap<String, MyService> services;
    private ArrayList<MyEvent> myEvents;
    private User user;
    private boolean databaseRead = true;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference myRef = database.getReference(Constants.USER_PATH).child(firebaseUser.getUid());


    private FilesManager() {
        events = new HashMap<>();
        gson = new Gson();
        myEvents = new ArrayList<>();
        myservices = new ArrayList<>();
        services = new HashMap<>();
        user = new User();
        events = new HashMap<>();

        Log.d("ptpt", "constructor");

    }


    public HashMap<String, HashMap<String, ArrayList<Expenses>>> getExpenses() {
        String s = msp.getString(firebaseUser.getUid() + Constants.EXPENSES, "");
        expenses = gson.fromJson(s, new TypeToken<HashMap<String, HashMap<String, ArrayList<Expenses>>>>() {
        }.getType());
        return expenses;
    }

    public FilesManager setExpenses(HashMap<String, HashMap<String, ArrayList<Expenses>>> expenses) {
        this.expenses = expenses;
        msp.putString(firebaseUser.getUid() + Constants.EXPENSES, gson.toJson(this.expenses));
        return this;
    }

    public void addExpenses(Expenses _expenses) {
        String[] keys = _expenses.getDate().split("/");
        if (this.expenses == null)
            this.expenses = new HashMap<>();

        if (this.expenses.get(keys[2]) != null) {
            if (this.expenses.get(keys[2]).get(keys[1]) != null) {
                this.expenses.get(keys[2]).get(keys[1]).add(_expenses);
            } else {
                this.expenses.get(keys[2]).put(keys[1], new ArrayList<Expenses>());
                this.expenses.get(keys[2]).get(keys[1]).add(_expenses);
            }


        } else {
            this.expenses.put(keys[2], new HashMap<String, ArrayList<Expenses>>());
            this.expenses.get(keys[2]).put(keys[1], new ArrayList<Expenses>());
            this.expenses.get(keys[2]).get(keys[1]).add(_expenses);

        }
        myRef.child(Constants.EXPENSES).child(keys[2]).child(keys[1]).push().setValue(gson.toJson(_expenses));
        msp.putString(firebaseUser.getUid() + Constants.EXPENSES, gson.toJson(this.expenses));
    }

    public String getSignaturePath(Context context) {
        if (signaturePath == null || signaturePath.equals("")) {
            msp = new MySharedPreferences(context);
            signaturePath = msp.getString(firebaseUser.getUid() + Constants.SIGNATURE, "");

        }

        return signaturePath;
    }

    public FilesManager setSignaturePath(String signaturePath, Context context) {
        msp = new MySharedPreferences(context);
        this.signaturePath = signaturePath;
        msp.putString(firebaseUser.getUid() + Constants.SIGNATURE, signaturePath);
        return this;
    }


    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }


    // static method to create instance of FilesManager class
    public static FilesManager getInstance() {
        if (single_instance == null)
            single_instance = new FilesManager();

        return single_instance;
    }


    public void setUser(User user) {
        this.user = user;
    }


    private void read(int type, Context context) {

        String s = "";
        msp = new MySharedPreferences(context);

        switch (type) {
            case Constants.EVENTS:
                s = msp.getString(firebaseUser.getUid() + Constants.EVENTS_FILE_NAME, "");
                Log.d("dfddfv", s);
                events = gson.fromJson(s, new TypeToken<HashMap<String, HashMap<String, HashMap<String,
                        HashMap<String, MyEvent>>>>>() {
                }.getType());
                break;
            case Constants.SERVICES:
                s = msp.getString(firebaseUser.getUid() + Constants.SERVICE_FILE_NAME, "");

                services = gson.fromJson(s, new TypeToken<HashMap<String, MyService>>() {
                }.getType());
                break;
        }


    }


    private boolean write(final String fileName, int type, Context context) {
        File sdcard = context.getFilesDir();
        File dir = new File(sdcard.getAbsolutePath() + File.separator + firebaseUser.getUid() +
                File.separator + Constants.DIR_NAME);
        dir.mkdirs();
        File file = new File(dir, fileName);
        FileOutputStream os = null;
        try {
            msp = new MySharedPreferences(context);
            String data = "";
            os = new FileOutputStream(file);
            switch (type) {
                case Constants.EVENTS:
                    data = gson.toJson(events);
                    msp.putString(firebaseUser.getUid() + Constants.EVENTS_FILE_NAME, data);
                    break;
                case Constants.SERVICES:
                    data = gson.toJson(services);
                    msp.putString(firebaseUser.getUid() + Constants.SERVICE_FILE_NAME, data);

                    break;
            }
            Log.d("My File Class", "data = " + data);
            os.write(data.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }


    public boolean addService(MyService service, Context context) {
        if (services == null)
            services = new HashMap<>();
        if (service != null)
            services.put(service.getServiceName(), service);
        myRef.child(Constants.SERVICES_CHILD).setValue(gson.toJson(services));
        return write(Constants.SERVICE_FILE_NAME, Constants.SERVICES, context);

    }

    public boolean updateService(MyService service, Context context) {
        if (service != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                services.replace(service.getServiceName(), service);
            } else {
                services.remove(service.getServiceName());
                services.put(service.getServiceName(), service);
            }
        myRef.child(Constants.SERVICES_CHILD).setValue(gson.toJson(services));
        return write(Constants.SERVICE_FILE_NAME, Constants.SERVICES, context);

    }

    public boolean removeService(MyService service, Context context) {
        if (service != null)
            services.remove(service.getServiceName());

        myRef.child(Constants.SERVICES_CHILD).setValue(gson.toJson(services));
        return write(Constants.SERVICE_FILE_NAME, Constants.SERVICES, context);

    }


    public boolean addEvent(MyEvent event, Context context) {
        Log.d("pttt", "size = " + myEvents.size());
        if (event == null)
            return false;
        myEvents.add(event);

        Log.d("pttt", "size = " + myEvents.size());
        myRef.child(Constants.EVENTS_CHILD).child(event.getYear()).child(event.getMonth())
                .child(event.getDay()).child(event.getCustomerPhone() + event.getStartingTime()).setValue(gson.toJson(event));
        addEventStep1(event);

        return write(Constants.EVENTS_FILE_NAME, Constants.EVENTS, context);

    }

    private void addEventStep1(@NonNull MyEvent event) {
        if (!events.containsKey(event.getYear()))
            events.put(event.getYear(), new HashMap<String, HashMap<String, HashMap<String, MyEvent>>>());
        addEventStep2(events.get(event.getYear()), event);
    }

    private void addEventStep2(HashMap<String, HashMap<String, HashMap<String, MyEvent>>> map, @NonNull MyEvent event) {
        if (!map.containsKey(event.getMonth()))
            map.put(event.getMonth(), new HashMap<String, HashMap<String, MyEvent>>());
        addEventStep3(map.get(event.getMonth()), event);

    }

    private void addEventStep3(HashMap<String, HashMap<String, MyEvent>> map, @NonNull MyEvent event) {
        if (!map.containsKey(event.getDay()))
            map.put(event.getDay(), new HashMap<String, MyEvent>());
        addEventStep4(map.get(event.getDay()), event);
    }

    private void addEventStep4(HashMap<String, MyEvent> events, MyEvent event) {
        if (events == null)
            events = new HashMap<>();
        events.put(event.getCustomerPhone() + event.getStartingTime(), event);
    }

    public HashMap<String, HashMap<String, HashMap<String, HashMap<String, MyEvent>>>> getEvents(Context context) {
        read(Constants.EVENTS, context);
        return events;
    }

    public HashMap<String, MyService> getServicesMap(Context context) {
        read(Constants.SERVICES, context);
        return services;
    }

    public ArrayList<MyService> getServices(Context context) {
        read(Constants.SERVICES, context);
        ArrayList<MyService> temp = new ArrayList<>();
        if (services == null)
            return null;
        for (String s : services.keySet()
        ) {
            temp.add(services.get(s));

        }
        return temp;
    }

    public ArrayList<MyEvent> getMyEvents(Context context) {
        read(Constants.EVENTS, context);
        ArrayList<MyEvent> temp = new ArrayList<>();
        if (events == null)
            return temp;
        for (String key : events.keySet()) {
            for (String key2 : events.get(key).keySet()) {
                for (String key3 : events.get(key).get(key2).keySet()
                ) {
                    for (String key4 : events.get(key).get(key2).get(key3).keySet()) {
                        temp.add(events.get(key).get(key2).get(key3).get(key4));

                    }
                }
            }

        }
        return temp;
    }

    public ArrayList<MyEvent> getMyEventsForYear(Context context, String year) {
        read(Constants.EVENTS, context);
        ArrayList<MyEvent> temp = new ArrayList<>();
        if (events == null || events.get(year) == null)
            return temp;
        for (String key2 : events.get(year).keySet()) {
            for (String key3 : events.get(year).get(key2).keySet()
            ) {
                for (String key4 : events.get(year).get(key2).get(key3).keySet()) {
                    temp.add(events.get(year).get(key2).get(key3).get(key4));

                }
            }
        }


        return temp;
    }

    public void readEventsFromDatabase(final Context context, CallBack_FireBase_Finish finish) {

        if (msp.getString(firebaseUser.getUid() + Constants.EVENTS_FILE_NAME, "").equals("") || databaseRead) {
            ReadEventsThread thread = new ReadEventsThread(context, finish);
            Thread t = new Thread(thread);
            t.setPriority(Thread.MAX_PRIORITY);
            t.run();
        } else {
            String s = "";
            try {
                s = msp.getString(firebaseUser.getUid() + Constants.EVENTS_FILE_NAME, "");
                Log.d("dfddfv", s);
                events = gson.fromJson(s, new TypeToken<HashMap<String, HashMap<String, HashMap<String,
                        HashMap<String, MyEvent>>>>>() {
                }.getType());
            } catch (com.google.gson.JsonSyntaxException e) {
                msp.putString(firebaseUser.getUid() + Constants.EVENTS_FILE_NAME, "");
                readEventsFromDatabase(context, finish);
            }
            //read(Constants.EVENTS, context);
            finish.onFinish();
        }

    }


    public void readFromDatabase(final Context context, CallBack_FireBase_Finish finish) {
        if (!databaseRead) {
            boolean services = msp.getString(firebaseUser.getUid() + Constants.SERVICE_FILE_NAME, "").equals("");
            boolean user = msp.getString(firebaseUser.getUid() + Constants.USER_PATH, "").equals("");
            boolean _expenses = msp.getString(firebaseUser.getUid() + Constants.EXPENSES, "").equals("");
            boolean sig = msp.getString(firebaseUser.getUid() + Constants.SIGNATURE, "").equals("");
            if (!services)
                read(Constants.SERVICES, context);
            if (!user)
                setUser(gson.fromJson(msp.getString(firebaseUser.getUid() + Constants.USER_PATH, ""), User.class));
            if (!_expenses)
                this.expenses = gson.fromJson(msp.getString(firebaseUser.getUid() + Constants.EXPENSES, "")
                        , new TypeToken<HashMap<String, HashMap<String, ArrayList<Expenses>>>>() {
                        }.getType());
            if (services || user || _expenses || sig) {
                Log.d(TAG, "read from firebase");
                ReadServicedAndUserThread thread = new ReadServicedAndUserThread(context, services, user, _expenses, sig, finish);
                Thread t = new Thread(thread);
                t.setPriority(Thread.MAX_PRIORITY);
                t.run();
            } else {
                Log.d(TAG, "don't nee to read from firebase");

                finish.onFinish();
            }
        } else {
            Log.d(TAG, "new device");

            ReadServicedAndUserThread thread = new ReadServicedAndUserThread(context, true, true, true, true, finish);
            Thread t = new Thread(thread);
            t.setPriority(Thread.MAX_PRIORITY);
            t.run();
        }
    }

    public User getUser() {
        return user;
    }

    public void refreshUser(Context context) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = database.getReference(Constants.USER_PATH).child(firebaseUser.getUid());
        events = new HashMap<>();
        services = new HashMap<>();


    }

    public void updateUser(User user1) {
        user = user1;
        msp.putString(firebaseUser.getUid() + Constants.EVENTS_FILE_NAME, gson.toJson(user));
        myRef.child(Constants.USER_CHILD).setValue(user);

    }

    public void setEvents(HashMap<String, HashMap<String, HashMap<String, HashMap<String, MyEvent>>>> events, Context context) {
        this.events = events;
        write(Constants.EVENTS_FILE_NAME, Constants.EVENTS, context);


    }

    public void setServices(HashMap<String, MyService> services, Context context) {
        this.services = services;
        write(Constants.SERVICE_FILE_NAME, Constants.SERVICES, context);

    }

    public void updateEventStatus(MyEvent e, @NonNull String status, Context context) {


        if (events.get(e.getYear()).get(e.getMonth()).get(e.getDay()).
                get(e.getCustomerPhone() + e.getStartingTime()) != null) {
            events.get(e.getYear()).get(e.getMonth()).get(e.getDay()).
                    get(e.getCustomerPhone() + e.getStartingTime()).setStatus(status);

            UpdateEventThread thread = new UpdateEventThread(events.get(e.getYear()).get(e.getMonth()).get(e.getDay()).
                    get(e.getCustomerPhone() + e.getStartingTime()));
            Thread t = new Thread(thread);
            t.setPriority(Thread.MAX_PRIORITY);
            t.run();
            write(Constants.EVENTS_FILE_NAME, Constants.EVENTS, context);

        }
    }


    public void updateEventPayment(MyEvent e, Context context) {

        if (events.get(e.getYear()).get(e.getMonth()).get(e.getDay()).
                get(e.getCustomerPhone() + e.getStartingTime()) != null) {
            events.get(e.getYear()).get(e.getMonth()).get(e.getDay()).
                    get(e.getCustomerPhone() + e.getStartingTime()).setPayment(e.getPayment());


            UpdateEventThread thread = new UpdateEventThread(events.get(e.getYear()).get(e.getMonth()).get(e.getDay()).
                    get(e.getCustomerPhone() + e.getStartingTime()));
            Thread t = new Thread(thread);
            t.setPriority(Thread.MAX_PRIORITY);
            t.run();
            write(Constants.EVENTS_FILE_NAME, Constants.EVENTS, context);

        }
    }

    public void updateEvent(Context context, MyEvent event) {

        UpdateEventThread thread = new UpdateEventThread(event);
        events.get(event.getYear()).get(event.getMonth()).get(event.getDay()).get(event.getCustomerPhone()
                + event.getStartingTime()).setOriginalInvoiceURL(event.getOriginalInvoiceURL()).setInvoiceNumber(event.getInvoiceNumber())
                .setCopyInvoiceURL(event.getCopyInvoiceURL());
        Thread t = new Thread(thread);
        t.setPriority(Thread.MAX_PRIORITY);
        t.run();
        write(Constants.EVENTS_FILE_NAME, Constants.EVENTS, context);

    }

    public void newInvoice() {
        user.newInvoice();
        updateUser(user);
    }

    public void newBid() {
        user.newBid();
        updateUser(user);
    }

    public void updateData(final CallBack_FireBase_Finish finish) {
        myRef.child(Constants.LAST).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    long l1 = dataSnapshot.getValue(Long.class).longValue();
                    long l2 = msp.getLong(Constants.LAST, 1);
                    Log.d(TAG, "last = " + l1);
                    Log.d(TAG, "msp  = " + l2);

                    databaseRead = !(l1 == l2);
                } catch (NullPointerException e) {
                    databaseRead = true;
                } finally {
                    Log.d(TAG, "read = " + databaseRead);
                    long now = System.currentTimeMillis();
                    myRef.child(Constants.LAST).setValue(now);
                    msp.putLong(Constants.LAST, now);
                    finish.onFinish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}


