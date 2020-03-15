package com.example.myapplication_finalproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewExpenses extends Fragment {
    private EditText newexp_EDT_purpose;
    private EditText newexp_EDT_amount;
    private ImageView newexp_IMG_invoice;
    private Button newexp_BTN_date;
    private Button newexp_BTN_save;
    private TextView newexp_LBL_date;
    private TextView newexp_LBL_save;
    private FloatingActionButton newexp_FAB_addinvoice;
    private DatePickerDialog datePickerDialog;
    private View view;
    private CallBack_SaveExpenses callBack_saveExpenses;
    private Toolbar newexp_TOOL_toolbar;
    private String date;
    private final Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    private final int REQUEST_TAKE_PHOTO = 1;
    private final String TAG = getClass().getSimpleName() + "_Log";
    private String currentPhotoPath;
    private Uri imageUri;
    private StorageReference mStorageRef;
    private boolean has_invoice = false;


    public NewExpenses(CallBack_SaveExpenses callBack_saveExpenses) {
        this.callBack_saveExpenses = callBack_saveExpenses;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.new_expenses_fragment, container, false);
        }


        findViews(view);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        setButtons();

        return view;
    }


    private void dateClick() {
        long now = System.currentTimeMillis();
        date = new SimpleDateFormat("dd/MM/yyyy").format(now);

        String[] datestr = date.split("/");
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date = dayOfMonth + "/" + (month + 1) + "/" + year;
                newexp_LBL_date.setText(date);
            }
        }, Integer.parseInt(datestr[2]), Integer.parseInt(datestr[1]) - 1, Integer.parseInt(datestr[0]));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }


    private void SelectImage() {


        final CharSequence[] items = {"מצלמה", "גלריה", "ביטול"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("מצלמה")) {
                    Log.d(TAG, "0");

                    dispatchTakePictureIntent();
                    Log.d(TAG, "999");


                } else if (items[i].equals("גלריה")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals("ביטול")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                GlideApp.with(getContext()).load(currentPhotoPath).into(newexp_IMG_invoice);
                has_invoice = true;

            } else if (requestCode == SELECT_FILE) {

                Uri selectedImageUri = data.getData();
                imageUri = selectedImageUri;
                GlideApp.with(getContext()).load(selectedImageUri).into(newexp_IMG_invoice);
                has_invoice = true;


            }

        }
    }


    private void dispatchTakePictureIntent() {
        Log.d(TAG, "1");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            Log.d(TAG, "2");

            // Create the File where the photo should go
            File photoFile = null;
            Log.d(TAG, "3");

            try {
                photoFile = createImageFile();
                Log.d(TAG, "4");

            } catch (IOException ex) {
                Log.d(TAG, "exep1");

                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d(TAG, "6");

                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.myapplication_finalproject.fileprovider",
                        photoFile);
                Log.d(TAG, "7");

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Log.d(TAG, "8");

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                Log.d(TAG, "9");

            }
        } else Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        Log.d(TAG, "11");

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Log.d(TAG, "12");

        String imageFileName = "JPEG_" + timeStamp + "_";
        Log.d(TAG, "13");

        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.d(TAG, "14");
        File image = null;
        try {


            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            Log.d(TAG, "15");
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());


        }
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        setImageUri(currentPhotoPath);
        Log.d(TAG, "16");

        return image;
    }


    private void setImageUri(String path) {
        imageUri = Uri.fromFile(new File(path));
    }


    private void upload() {
        if (newexp_BTN_date.getText().toString().equals("תאריך")) {
            alter("חסר תאריך");
            return;
        }
        if (newexp_EDT_amount.getText().toString().equals("")) {
            alter("חסר סכום");
            return;
        }
        if (newexp_EDT_purpose.getText().toString().equals("")) {
            alter("חסר סיבת ההוצאה");
            return;
        }
        if (!has_invoice) {
            alter("חסר צילום קבלה / חשבונית");
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("שומר");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        final long timestamp = System.currentTimeMillis();
        final StorageReference riversRef = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).child(Constants.EXPENSES).child(String.valueOf(timestamp));
        Log.d(TAG, " upload");
        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String purpose = newexp_EDT_purpose.getText().toString();
                                double amount = Double.parseDouble(newexp_EDT_amount.getText().toString().trim());

                                Expenses expenses = new Expenses(date, purpose, amount, uri.toString(), timestamp);
                                Log.d(TAG, "expenses:= " + expenses.toString());

                                Log.d(TAG, "onSuccess: uri= " + uri.toString());
                                FilesManager.getInstance().addExpenses(expenses);
                                progressDialog.dismiss();
                                callBack_saveExpenses.save();

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d(TAG, exception.getMessage());
                        Log.d(TAG, " upload fail");

                    }
                });
    }

    private void alter(String msg) {
        new AlertDialog.Builder(getContext())
                .setTitle("שגיאה")
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void setButtons() {
        newexp_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        newexp_TOOL_toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        newexp_TOOL_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack_saveExpenses.back();
            }
        });

        newexp_BTN_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateClick();
            }
        });
        newexp_LBL_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newexp_BTN_date.performClick();
            }
        });
        newexp_LBL_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newexp_BTN_save.performClick();
            }
        });

        newexp_FAB_addinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectImage();
            }
        });

    }

    private void findViews(View view) {
        newexp_EDT_purpose = view.findViewById(R.id.newexp_EDT_purpose);
        newexp_EDT_amount = view.findViewById(R.id.newexp_EDT_amount);
        newexp_IMG_invoice = view.findViewById(R.id.newexp_IMG_invoice);
        newexp_BTN_date = view.findViewById(R.id.newexp_BTN_date);
        newexp_BTN_save = view.findViewById(R.id.newexp_BTN_save);
        newexp_FAB_addinvoice = view.findViewById(R.id.newexp_FAB_addinvoice);
        newexp_TOOL_toolbar = view.findViewById(R.id.newexp_TOOL_toolbar);
        newexp_LBL_date = view.findViewById(R.id.newexp_LBL_date);
        newexp_LBL_save = view.findViewById(R.id.newexp_LBL_save);
    }

}
