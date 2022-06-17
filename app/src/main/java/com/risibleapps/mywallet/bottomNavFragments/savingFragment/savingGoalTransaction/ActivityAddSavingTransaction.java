package com.risibleapps.mywallet.bottomNavFragments.savingFragment.savingGoalTransaction;

import static com.risibleapps.mywallet.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.savingFragment.savingTransactionDetails.ActivitySavingTransactionDetail;
import com.risibleapps.mywallet.bottomNavFragments.savingFragment.DB.SavingTransactionEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivityAddSavingTransaction extends AppCompatActivity {

    private EditText editTextTrans;
    private TextView textViewDate;
    private AppCompatButton buttonAddTransaction;
    private Toolbar toolbar;

    private String dateToSet;
    private String dateToSave;

    //for saving the saving goal id
    private int id;

    //for saving goal title and icon
    private String title;
    private int icon;

    private RoomDBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_saving_transaction);

        //initialize views
        initializeViews();

        //initialize database instance
        database = RoomDBHelper.getInstance(this);

        //setting toolbar
        setToolbar();

        //getting the intent data
        getIntentData();

        //get current date and display it in edit text date
        getCurrentDate();

        //textview click listener which will display calendar alert dialog
        textViewDate.setOnClickListener(view -> dateAlertDialog());

        //button click listener
        buttonAddTransaction.setOnClickListener(view -> addSavingTransaction());
    }

    private void initializeViews() {

        editTextTrans = findViewById(R.id.edit_text_saving_trans);

        //setting the hint
        editTextTrans.setHint(CURRENCY_+" 0");
        textViewDate = findViewById(R.id.text_view_saving_date);
        buttonAddTransaction = findViewById(R.id.button_saving_trans);
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar_saving_trans);
        setSupportActionBar(toolbar);
    }

    //onCreateOptionsMenu & onOptionsItemSelected for handling the cancel button on toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_saving_goal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.finish_saving_goal:
                //finish the activity
                finish();
                break;
        }

        return true;
    }

    private void getIntentData(){

        Intent intent=getIntent();
        id = intent.getIntExtra("id",-1);
        title = intent.getStringExtra("title");
        icon = intent.getIntExtra("icon",0);
    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        dateToSet = dateFormat.format(calendar.getTime());

        textViewDate.setText("\t\t" + dateToSet);

        //date to save in database
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        dateToSave = dateFormat1.format(calendar.getTime());

    }

    private void dateAlertDialog() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                //setting the date to textview date
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                dateToSet = sdf.format(calendar.getTime());

                textViewDate.setText("\t\t" + dateToSet);

                //date to save in database
                SimpleDateFormat sdp2 = new SimpleDateFormat("yyyy-MM-dd");
                dateToSave = sdp2.format(calendar.getTime());

                Log.i("DATE_12", "date picker display date: "+dateToSet);
                Log.i("DATE_12", "date picker DB date: "+dateToSave);
            }
        };

        new DatePickerDialog(ActivityAddSavingTransaction.this, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void addSavingTransaction() {
        if (editTextTrans.getText().toString().trim().isEmpty()) {
            Snackbar snackbar = Snackbar.make(textViewDate, "Enter Transaction amount", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        else
        {
            //saving data into database
            database.savingDao().addGoalTransaction(new SavingTransactionEntity(id,title,Integer.valueOf(editTextTrans.getText().toString()),icon,dateToSave));

            Toast.makeText(this, "Saving Transaction Successful", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(ActivityAddSavingTransaction.this, ActivitySavingTransactionDetail.class));
                    finish();
                }
            },1000);
        }

    }
}