package com.hammad.managerya.bottomNavFragments.addRecord;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.hammad.managerya.R;
import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.addRecord.expense.expenseDB.ExpenseDetailEntity;
import com.hammad.managerya.bottomNavFragments.addRecord.income.incomeDB.IncomeDetailEntity;
import com.hammad.managerya.mainActivity.HomeScreenActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityAddTransactionDetail extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;
    private static final int LOCATION_REQUEST_CODE = 102;

    private TextView textViewCategoryName, textViewAmount, textViewDate, textViewLocation, textViewReceipt;
    private TextView textViewPreview, textViewDelete;
    private EditText editTextDescription, editTextTag;
    private AppCompatButton buttonFinish;

    //string to save the current datetime
    private String currentDateTime;

    //for saving category id from intent
    private static int categoryId;

    //for saving amount from intent
    private static int transactionAmount=0;

    //for saving category name and date from intent
    private static String categoryName,currentDate;

    //strings for saving the income details
    private String currentPicturePath="";
    private String description, tag, locationAddress;

    //Database instance
    private RoomDBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction_detail);

        //initialize views
        initializeViews();

        //initialize database helper class
        database = RoomDBHelper.getInstance(this);

        //getting the intent which will differentiate from which fragment the activity is called
        Intent fragmentIntent = getIntent();
        String fragmentCalled = fragmentIntent.getStringExtra("fragment");

        //the intent function
        getIntentData(fragmentIntent, fragmentCalled);

        //getting the current location address
        getLocationIntentData();

        //getting the current date and time
        getCurrentDateTime();

        //textview location click listener
        textViewLocation.setOnClickListener(view -> checkLocationPermission());

        //long click on textViewLocation for deleting the set address
        textViewLocation.setOnLongClickListener(view -> {

            textViewLocation.setText("");
            locationAddress = "";
            return true;
        });

        //textview receipt click listener
        textViewReceipt.setOnClickListener(view -> selectImage());

        //textview delete click listener
        textViewDelete.setOnClickListener(view -> {

            currentPicturePath = "";

            //setting the Visibility of Textviews preview and Delete to gone
            textViewPreview.setVisibility(View.GONE);
            textViewDelete.setVisibility(View.GONE);

        });

        //textview preview click listener
        textViewPreview.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(currentPicturePath), "image/*");
            this.startActivity(intent);
        });

        //button finish click listener
        buttonFinish.setOnClickListener(view -> buttonFinishClickListener());
    }

    private void initializeViews() {

        textViewCategoryName = findViewById(R.id.txt_cat_add);
        textViewAmount = findViewById(R.id.txt_amount_add);
        textViewDate = findViewById(R.id.txt_date_add);

        textViewLocation = findViewById(R.id.txt_location_add);
        textViewReceipt = findViewById(R.id.txt_receipt_add);

        textViewPreview = findViewById(R.id.txt_preview_add);
        textViewDelete = findViewById(R.id.txt_delete_image);

        editTextDescription = findViewById(R.id.edit_txt_desc);
        editTextTag = findViewById(R.id.edit_txt_tag);

        buttonFinish = findViewById(R.id.button_finish);
    }

    private void getIntentData(Intent intent, String fragmentCalled) {
        if (fragmentCalled != null) {
            if (fragmentCalled.equals("expense")) {

                //getting the amount and date from Add Expense Fragment
                transactionAmount = Integer.parseInt(intent.getStringExtra("expenseAmount"));
                categoryName = intent.getStringExtra("expenseCat");
                currentDate = intent.getStringExtra("expenseDate");

                textViewAmount.setText(String.valueOf(transactionAmount));
                textViewAmount.append(" -");
                textViewCategoryName.setText(categoryName);
                textViewDate.setText(currentDate);
                textViewAmount.setTextColor(getResources().getColor(R.color.colorRed));

                //getting the expense category id
                categoryId = intent.getIntExtra("expenseCatId", 0);

            } else if (fragmentCalled.equals("income")) {

                //getting the amount and date from Add Income Fragment
                transactionAmount = Integer.parseInt(intent.getStringExtra("incomeAmount"));
                categoryName = intent.getStringExtra("incomeCat");
                currentDate = intent.getStringExtra("incomeDate");

                textViewAmount.setText(String.valueOf(transactionAmount));
                textViewAmount.append(" +");
                textViewCategoryName.setText(categoryName);
                textViewDate.setText(currentDate);
                textViewAmount.setTextColor(getResources().getColor(R.color.colorGreen));

                //getting the income category id
                categoryId = intent.getIntExtra("incomeCatId", 0);

            }
        }
    }

    private void getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTime = dateTimeFormat.format(calendar.getTime());
    }

    private void getLocationIntentData() {

        Intent intent = getIntent();

        if (intent.getStringExtra("location") != null) {
            textViewLocation.setText(intent.getStringExtra("location"));
        }
    }

    private void selectImage() {
        final CharSequence[] itemsCharSequence = {"Capture Image", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Add Image");

        builder.setItems(itemsCharSequence, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (itemsCharSequence[i].equals("Capture Image")) {
                    checkCameraPermission();
                } else if (itemsCharSequence[i].equals("Choose from Gallery")) {
                    checkGalleryPermission();
                } else if (itemsCharSequence[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });

        builder.show();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:

                if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(getApplicationContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
                }

                break;

            case 2:

                if (requestCode == 2 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(getApplicationContext(), "Gallery permission denied", Toast.LENGTH_SHORT).show();
                }

                break;

            case 3:

                if (requestCode == 3 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(new Intent(this, LocationActivity.class), LOCATION_REQUEST_CODE);
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                }

        }

    }

    private void openCamera() {
        Intent takeImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takeImageIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.hammad.android.manageryafileprovider",
                        photoFile);
                takeImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takeImageIntent, CAMERA_REQUEST_CODE);

            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPicturePath = image.getAbsolutePath();
        return image;
    }

    private void checkGalleryPermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 2);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                //Textviews preview & delete visibility set to VISIBLE
                textViewPreview.setVisibility(View.VISIBLE);
                textViewDelete.setVisibility(View.VISIBLE);
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();

                String imageName = getFileExtension(contentUri);

                currentPicturePath = getPathFromUri(contentUri);

                if (currentPicturePath != null || !(currentPicturePath.isEmpty())) {
                    //Textviews preview & delete visibility set to VISIBLE
                    textViewPreview.setVisibility(View.VISIBLE);
                    textViewDelete.setVisibility(View.VISIBLE);
                }
            }
        }
        else if(requestCode == LOCATION_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){

                locationAddress = data.getStringExtra("location");

                //setting the location address to textview
                textViewLocation.setText(locationAddress);
            }
        }
    }

    private String getFileExtension(Uri contentUri) {
        ContentResolver contentResolver = this.getContentResolver();

        /*
        MIME stands for  Multipurpose Internet Mail Extensions
         and refers to a media or content type on the internet.
         With MIME, the data contained in an internet message can be clearly classified as it would in an email or in a HTTP message
        */
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        //contentResolver.getType(contentUri) here will return the file type of image
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(contentUri));
    }

    private String getPathFromUri(Uri selectionImageUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(selectionImageUri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    /*
        onBackPressed we will save transaction to database.
        The if & else if conditions are used to specify the data to be saved to income/expense database table
        'if' is for Income Details, and 'else if' is for Expense Details
    */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //getting the values
        description = editTextDescription.getText().toString();
        tag = editTextTag.getText().toString();
        locationAddress = textViewLocation.getText().toString();

        if (textViewAmount.getText().toString().contains("+")) {
            //saving data to income detail
            database.incomeDetailDao().addIncomeDetail(new IncomeDetailEntity(categoryId, transactionAmount, currentDateTime, description, tag, locationAddress, currentPicturePath));
        }
        else if (textViewAmount.getText().toString().contains("-")) {
            //saving data to expense detail
            database.expenseDetailDao().addExpenseDetail(new ExpenseDetailEntity(categoryId, transactionAmount, currentDateTime, description, tag, locationAddress, currentPicturePath));
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 3);
        } else {
            startActivityForResult(new Intent(this, LocationActivity.class), LOCATION_REQUEST_CODE);
        }
    }

    private void buttonFinishClickListener() {

        //getting the values
        description = editTextDescription.getText().toString();
        tag = editTextTag.getText().toString();
        locationAddress = textViewLocation.getText().toString();

        //validating the string variables values

        if (description == null || description.length() == 0) {
            description = "";
        }

        if (tag == null || tag.length() == 0) {
            tag = "";
        }

        if (locationAddress == null || locationAddress.length() == 0) {
            locationAddress = "";
        }

        if (currentPicturePath == null || currentPicturePath.length() == 0) {
            currentPicturePath = "";
        }

        //saving the data into database
        if (textViewAmount.getText().toString().contains("+"))
        {
            //saving income details
            database.incomeDetailDao().addIncomeDetail(new IncomeDetailEntity(categoryId, transactionAmount, currentDateTime, description, tag, locationAddress, currentPicturePath));

            Toast.makeText(this, "Income Added Successfully", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(ActivityAddTransactionDetail.this, HomeScreenActivity.class));
                    finish();
                }
            }, 1200);
        }
        else if (textViewAmount.getText().toString().contains("-"))
        {
            //saving expense details
            database.expenseDetailDao().addExpenseDetail(new ExpenseDetailEntity(categoryId, transactionAmount, currentDateTime, description, tag, locationAddress, currentPicturePath));

            Toast.makeText(this, "Expense Added Successfully", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(ActivityAddTransactionDetail.this, HomeScreenActivity.class));
                    finish();
                }
            }, 1200);
        }
    }
}