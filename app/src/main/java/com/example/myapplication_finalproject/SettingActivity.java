package com.example.myapplication_finalproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.signature.ObjectKey;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SettingActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private EditText setting_EDT_name;
    private EditText setting_EDT_businessnumber;
    private EditText setting_EDT_phone;
    private EditText setting_EDT_lasteceipt;
    private EditText setting_EDT_paypal;
    private EditText setting_EDT_email;
    private EditText setting_EDT_street;
    private EditText setting_EDT_house;
    private EditText setting_EDT_city;
    private EditText setting_EDT_lastbid;


    private TextView setting_LBL_name;
    private TextView setting_LBL_businessnumber;
    private TextView setting_LBL_phone;
    private TextView setting_LBL_address;
    private TextView setting_LBL_lasteceipt;
    private TextView setting_LBL_email;
    private Button setting_BTN_save;
    private Button setting_BTN_cancel;
    private SignaturePad setting_SIG_pad;
    private Button setting_BTN_savesig;
    private Button setting_BTN_clear;
    private Toolbar setting_TOOL_toolbar;
    private boolean edit = false;
    private CheckBox setting_CB_paypal;

    private ImageView setting_IMG_pad;

    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findViews();
        User user = FilesManager.getInstance().getUser();
        setting_TOOL_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        glide();
        setting_EDT_name.setHint(user.getName());
        setting_EDT_businessnumber.setHint(user.getId());
        setting_EDT_phone.setHint(user.getPhone());
        setting_EDT_street.setHint(user.getStreet());
        setting_EDT_house.setHint(user.getHouseNumber());
        setting_EDT_city.setHint(user.getCity());
        setting_EDT_lasteceipt.setHint(user.getLastReceiptNumber() + "");
        setting_EDT_lastbid.setHint(user.getLastBidNumber() + "");
        setting_EDT_email.setHint(user.getMail());
        setting_BTN_save.setText("ערוך");
        setting_SIG_pad.setVisibility(View.GONE);

        enabled(false);


        //disable both buttons at start
        setting_BTN_savesig.setEnabled(false);
        setting_BTN_clear.setEnabled(false);


        setting_SIG_pad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                setting_BTN_savesig.setEnabled(true);
                setting_BTN_clear.setEnabled(true);
            }

            @Override
            public void onClear() {
                setting_BTN_savesig.setEnabled(false);
                setting_BTN_clear.setEnabled(false);
            }
        });

        setting_BTN_savesig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = setting_SIG_pad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap)) {
                    Toast.makeText(SettingActivity.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }

            }
        });
        setting_BTN_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting_SIG_pad.clear();
            }
        });


        setting_CB_paypal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    setting_EDT_paypal.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    setting_EDT_paypal.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        });
        setting_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        setting_BTN_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting_BTN_cancel.setVisibility(View.GONE);
                setting_BTN_save.setText("ערוך");
                setting_SIG_pad.setVisibility(View.GONE);
                setting_IMG_pad.setVisibility(View.VISIBLE);
                setting_BTN_clear.setVisibility(View.GONE);
                setting_BTN_savesig.setVisibility(View.GONE);

                edit = false;
                enabled(false);
                glide();
                setting_EDT_name.setHint(FilesManager.getInstance().getUser().getName());
                setting_EDT_businessnumber.setHint(FilesManager.getInstance().getUser().getId());
                setting_EDT_phone.setHint(FilesManager.getInstance().getUser().getPhone());
                setting_EDT_street.setHint(FilesManager.getInstance().getUser().getStreet());
                setting_EDT_house.setHint(FilesManager.getInstance().getUser().getHouseNumber());
                setting_EDT_city.setHint(FilesManager.getInstance().getUser().getCity());
                setting_EDT_lasteceipt.setHint(FilesManager.getInstance().getUser().getLastReceiptNumber() + "");
                setting_EDT_lastbid.setHint(FilesManager.getInstance().getUser().getLastBidNumber() + "");
                setting_EDT_email.setHint(FilesManager.getInstance().getUser().getMail());
                setting_EDT_paypal.setHint(FilesManager.getInstance().getUser().getPaypalClientID());
            }
        });
    }

    private void glide() {
        GlideApp.with(SettingActivity.this).load(FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).child(Constants.SIGNATURE).child("signature"))
                .signature(new ObjectKey(FilesManager.getInstance().getUser().getSignatureVersion())).into(setting_IMG_pad);
    }

    private void enabled(boolean b) {
        setting_EDT_name.setEnabled(b);
        setting_EDT_businessnumber.setEnabled(b);
        setting_EDT_phone.setEnabled(b);
        setting_EDT_street.setEnabled(b);
        setting_EDT_house.setEnabled(b);
        setting_EDT_city.setEnabled(b);
        setting_EDT_lasteceipt.setEnabled(b);
        setting_EDT_lastbid.setEnabled(b);
        setting_EDT_email.setEnabled(b);
        setting_EDT_paypal.setEnabled(b);
    }

    private void saveData() {
        User user = FilesManager.getInstance().getUser();

        if (edit) {
            edit = false;
            enabled(false);
            setting_BTN_cancel.setVisibility(View.GONE);
            setting_BTN_save.setText("ערוך");
            setting_SIG_pad.setVisibility(View.GONE);
            setting_IMG_pad.setVisibility(View.VISIBLE);
            setting_BTN_clear.setVisibility(View.GONE);
            setting_BTN_savesig.setVisibility(View.GONE);
            glide();
            user.setName(setting_EDT_name.getText().toString())
                    .setId(setting_EDT_businessnumber.getText().toString())
                    .setPhone(setting_EDT_phone.getText().toString())
                    .setHouseNumber(setting_EDT_house.getText().toString())
                    .setCity(setting_EDT_city.getText().toString())
                    .setStreet(setting_EDT_street.getText().toString())
                    .setLastReceiptNumber(Integer.parseInt(setting_EDT_lasteceipt.getText().toString()))
                    .setLastBidNumber(Integer.parseInt(setting_EDT_lastbid.getText().toString()))
                    .setPaypalClientID(setting_EDT_paypal.getText().toString())
                    .setMail(setting_EDT_email.getText().toString());
            FilesManager.getInstance().updateUser(user);
            setting_EDT_name.setHint(user.getName());
            setting_EDT_businessnumber.setHint(user.getId());
            setting_EDT_phone.setHint(user.getPhone());
            setting_EDT_street.setHint(user.getStreet());
            setting_EDT_house.setHint(user.getHouseNumber());
            setting_EDT_city.setHint(user.getCity());
            setting_EDT_lasteceipt.setHint(user.getLastReceiptNumber() + "");
            setting_EDT_lastbid.setHint(user.getLastBidNumber() + "");
            setting_EDT_email.setHint(user.getMail());
            setting_EDT_paypal.setHint(user.getPaypalClientID());
        } else {
            edit = true;
            enabled(true);
            setting_SIG_pad.setVisibility(View.VISIBLE);
            setting_IMG_pad.setVisibility(View.GONE);
            setting_BTN_clear.setVisibility(View.VISIBLE);
            setting_BTN_savesig.setVisibility(View.VISIBLE);
            setting_BTN_cancel.setVisibility(View.VISIBLE);
            setting_BTN_save.setText("שמור");
            setting_EDT_name.setText(user.getName());
            setting_EDT_businessnumber.setText(user.getId());
            setting_EDT_phone.setText(user.getPhone());
            setting_EDT_street.setText(user.getStreet());
            setting_EDT_house.setText(user.getHouseNumber());
            setting_EDT_city.setText(user.getCity());
            setting_EDT_lasteceipt.setText(String.valueOf(user.getLastReceiptNumber()));
            setting_EDT_lastbid.setText(String.valueOf(user.getLastBidNumber()));
            setting_EDT_email.setText(user.getMail());
            setting_EDT_paypal.setText(user.getPaypalClientID());

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(SettingActivity.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.

        File sdcard = this.getFilesDir();
        File dir = new File(sdcard.getAbsolutePath() + File.separator + FilesManager.getInstance().getFirebaseUser() +
                File.separator + Constants.SIGNATURE);

        dir.mkdirs();

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + "sig");
        Log.d("SignaturePad", file.getPath());

        if (!file.mkdirs()) {
            Log.d("SignaturePad", "Directory not created");
        }
        return dir;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Log.d("SignaturePad", photo.getPath());

        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        stream.close();
        Uri file = Uri.fromFile(photo);
        FilesManager.getInstance().setSignaturePath(photo.getAbsolutePath(), this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference riversRef = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).child(Constants.SIGNATURE).child("signature");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                User user = FilesManager.getInstance().getUser();
                                user.setSignatureURL(uri.toString());
                                user.setSignatureVersion(user.getSignatureVersion() + 1);

                                FilesManager.getInstance().updateUser(user);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {


                    }
                });
    }


    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), "SignatureT");
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        SettingActivity.this.sendBroadcast(mediaScanIntent);
    }


    private void findViews() {
        setting_EDT_name = findViewById(R.id.setting_EDT_name);
        setting_EDT_businessnumber = findViewById(R.id.setting_EDT_businessnumber);
        setting_EDT_phone = findViewById(R.id.setting_EDT_phone);
        setting_EDT_lasteceipt = findViewById(R.id.setting_EDT_lasteceipt);
        setting_EDT_email = findViewById(R.id.setting_EDT_email);
        setting_LBL_name = findViewById(R.id.setting_LBL_name);
        setting_LBL_businessnumber = findViewById(R.id.setting_LBL_businessnumber);
        setting_LBL_phone = findViewById(R.id.setting_LBL_phone);
        setting_LBL_lasteceipt = findViewById(R.id.setting_LBL_lasteceipt);
        setting_LBL_email = findViewById(R.id.setting_LBL_email);
        setting_LBL_email.setMovementMethod(new ScrollingMovementMethod());
        setting_BTN_save = findViewById(R.id.setting_BTN_save);
        setting_CB_paypal = findViewById(R.id.setting_CB_paypal);
        setting_EDT_paypal = findViewById(R.id.setting_EDT_paypal);
        setting_EDT_paypal.setMovementMethod(new ScrollingMovementMethod());
        setting_EDT_city = findViewById(R.id.setting_EDT_city);
        setting_EDT_street = findViewById(R.id.setting_EDT_street);
        setting_EDT_house = findViewById(R.id.setting_EDT_house);
        setting_TOOL_toolbar = findViewById(R.id.setting_TOOL_toolbar);
        setting_BTN_cancel = findViewById(R.id.setting_BTN_cancel);
        setting_EDT_lastbid = findViewById(R.id.setting_EDT_lastbid);
        setting_SIG_pad = findViewById(R.id.setting_SIG_pad);
        setting_BTN_savesig = findViewById(R.id.setting_BTN_savesig);
        setting_BTN_clear = findViewById(R.id.setting_BTN_clear);
        setting_IMG_pad = findViewById(R.id.setting_IMG_pad);

    }


}
