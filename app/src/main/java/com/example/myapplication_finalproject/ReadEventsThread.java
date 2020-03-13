package com.example.myapplication_finalproject;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;

public class ReadEventsThread implements Runnable {
    private String TAG = this.getClass().getSimpleName() + "_LOG";


    private Gson gson;
    private Context context;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference myRef = database.getReference(Constants.USER_PATH).child(firebaseUser.getUid());

    private HashMap<String, HashMap<String, HashMap<String, HashMap<String, MyEvent>>>> events;
    private CallBack_FireBase_Finish callBack_fireBase_finish;


    public ReadEventsThread(Context context, CallBack_FireBase_Finish finish) {

        this.events = new HashMap<>();
        this.context = context;
        gson = new Gson();
        this.callBack_fireBase_finish = finish;

    }


    @Override
    public void run() {

        try {


            myRef.child(Constants.EVENTS_CHILD).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // all events
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) { // each child is a year
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) { // each child is a mount
                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) { // each child is a day
                                for (DataSnapshot ds : dataSnapshot3.getChildren()) { // each child is a event
                                    String s = ds.getValue(String.class);
                                    Log.d(TAG, "S = " + s);
                                    MyEvent event = gson.fromJson(s, MyEvent.class);
                                    if (event != null) {
                                        Log.d(TAG, "event restore " + event.toString());
                                        addEventStep1(event);
                                    } else Log.d(TAG, "event restore fail");
                                }
                            }

                        }
                    }
                    callBack_fireBase_finish.onFinish();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (DatabaseException ex) {
            Log.d(TAG, ex.getMessage());
        }
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
        FilesManager.getInstance().setEvents(this.events, context);

    }


}
