package com.example.myapplication_finalproject;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UpdateEventThread implements Runnable {

    private Gson gson;
    private Context context;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference myRef = database.getReference(Constants.USER_PATH).child(firebaseUser.getUid());

    private ArrayList<MyEvent> events;
    private MyEvent event;

    public UpdateEventThread(MyEvent event,
                             ArrayList<MyEvent> events, Context context) {
        this.event = event;
        this.events = events;
        this.context = context;
        gson = new Gson();

    }

    public UpdateEventThread(
            MyEvent event) {
        this.event = event;
        gson = new Gson();

    }

    @Override
    public void run() {

        myRef.child(Constants.EVENTS_CHILD).child(event.getYear()).child(event.getMonth()).child(event.getDay())
                .child(event.getCustomerPhone() + event.getStartingTime()).setValue(gson.toJson(event));

    }


}
