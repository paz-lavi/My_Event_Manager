package com.example.myapplication_finalproject;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
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
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadServicedAndUserThread implements Runnable {
    private final String TAG = this.getClass().getSimpleName() + "_LOG";
    private Gson gson;
    private Context context;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference myRef = database.getReference(Constants.USER_PATH).child(firebaseUser.getUid());

    private HashMap<String, MyService> services;
    private User user;
    private boolean services_finish, user_finish, expenses_finish;
    private CallBack_FireBase_Finish finish;
    private boolean read_services, read_user, read_expenses, sig;
    private HashMap<String, HashMap<String, ArrayList<Expenses>>> expenses;

    public ReadServicedAndUserThread(Context context, boolean services, boolean user, boolean expenses, boolean sig,
                                     CallBack_FireBase_Finish finish) {
        this.services = new HashMap<>();
        this.context = context;
        this.user = new User();
        this.expenses = new HashMap<>();
        gson = new Gson();
        services_finish = false;
        user_finish = false;
        expenses_finish = false;
        this.finish = finish;
        this.read_services = services;
        this.read_user = user;
        this.sig = sig;
        this.read_expenses = expenses;
        Log.d(TAG, "constructor");
    }

    @Override
    public void run() {
        Log.d(TAG, "satrt run");

        if (read_services) {
            Log.d(TAG, "read services");

            try {
                myRef.child(Constants.SERVICES_CHILD).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("ptpt", "snap" + dataSnapshot.toString());

                        services = gson.fromJson(dataSnapshot.getValue(String.class), new TypeToken<HashMap<String, MyService>>() {
                        }.getType());
                        FilesManager.getInstance().setServices(services, context);
                        services_finish = true;
                        done();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (DatabaseException e) {
                Log.d("ptpt", "services fail");

            }
        } else {
            Log.d(TAG, "don't read services");

            services_finish = true;
            done();
        }

        if (read_user) {
            Log.d(TAG, "read user");

            try {
                myRef.child(Constants.USER_CHILD).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);
                        if (user == null)
                            user = new User();
                        Log.d("ptpt", user.toString());
                        FilesManager.getInstance().setUser(user);
                        user_finish = true;
                        done();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (DatabaseException e) {
                Log.d("ptpt", "user fail");


            }
        } else {
            Log.d(TAG, "don't read user");

            user_finish = true;
            done();
        }

        if (read_expenses) {
            Log.d(TAG, "read expenses");

            try {
                myRef.child(Constants.EXPENSES).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) { // each child is a year
                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) { // each child is a mount
                                for (DataSnapshot ds : dataSnapshot2.getChildren()) { // each child is an expenses
                                    String s = ds.getValue(String.class);
                                    Log.d(TAG, "s =" + s);

                                    Expenses exp = gson.fromJson(s, Expenses.class);

                                    if (exp != null) {
                                        Log.d(TAG, exp.toString());
                                        addExpensesStep1(exp);
                                    } else Log.d(TAG, "expenses restore fail");
                                }
                            }

                        }
                        Log.d(TAG, "don't read expenses");

                        FilesManager.getInstance().setExpenses(expenses);
                        expenses_finish = true;
                        done();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (DatabaseException e) {
                Log.d(TAG, "expenses fail");


            }
        } else {
            Log.d(TAG, "don't read expenses");

            expenses_finish = true;
            done();
        }


    }

    private void addExpensesStep1(Expenses exp) {
        if (expenses != null) {
            addExpensesStep2(exp);
        } else {
            expenses = new HashMap<>();
            addExpensesStep2(exp);
        }
    }

    private void addExpensesStep2(Expenses exp) {
        String[] date = exp.getDate().split("/");
        if (expenses.get(date[2]) != null) {
            addExpensesStep3(exp);
        } else {
            expenses.put(date[2], new HashMap<String, ArrayList<Expenses>>());
            addExpensesStep3(exp);
        }
    }

    private void addExpensesStep3(Expenses exp) {
        String[] date = exp.getDate().split("/");
        if (expenses.get(date[2]).get(date[1]) != null) {
            expenses.get(date[2]).get(date[1]).add(exp);
        } else {
            expenses.get(date[2]).put(date[1], new ArrayList<Expenses>());
            expenses.get(date[2]).get(date[1]).add(exp);

        }
    }

    private void done() {
        Log.d(TAG, "done");

        if (user_finish && services_finish && expenses_finish) {
            Log.d(TAG, "callback");
            if (sig && FilesManager.getInstance().getUser() != null) {
                File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File dir = new File(sdcard.getAbsolutePath() + File.separator + Constants.DIR_NAME +
                        File.separator + FilesManager.getInstance().getUser().getName(), "חתימה");
                if (!dir.exists())
                    dir.mkdirs();

                String fileName = "/Signature.jpg";

                File f = new File(dir.getAbsolutePath() + fileName);
                if (f.exists())
                    f.delete();

                try {

                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(FilesManager.getInstance().getUser().getSignatureURL());
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                    request.allowScanningByMediaScanner();
                    request.setDestinationUri(Uri.fromFile(new File(dir.getAbsolutePath() + File.separator + fileName)));
                    downloadManager.enqueue(request);

                    File file = new File(dir.getAbsolutePath(), fileName);
                    if (file.exists()) {//File Exists};
                        FilesManager.getInstance().setSignaturePath(file.getAbsolutePath(), context);
                    }
                } catch (NullPointerException e) {
                    Log.d(TAG, e.getMessage());

                } finally {
                    finish.onFinish();

                }
            } else
                finish.onFinish();

        }


    }
}
