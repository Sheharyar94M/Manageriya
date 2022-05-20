package com.hammad.managerya;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddLoanTransactionActivity extends AppCompatActivity {

    private EditText editTextAmount, editTextDetails;
    private TextView textViewDate;
    private Toolbar toolbar;
    private AppCompatButton saveButton;
    private String currentDateTime,currentDateTime1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan_transaction);

        //initialize views
        initializeViews();

        //setting the toolbar
        setToolbar();

        //getting the intent data
        getData();

        //getting the current date and displaying it in textviewDate
        getCurrentDate();

        //button save clicker
        saveButton.setOnClickListener(view -> saveLoanTrans());

        //textview date click listener
        textViewDate.setOnClickListener(view -> dateTimeDialog());
    }

    private void initializeViews() {

        toolbar = findViewById(R.id.toolbar_add_loan_trans);
        editTextAmount = findViewById(R.id.edit_text_amount);
        editTextDetails = findViewById(R.id.edit_text_detail);
        textViewDate = findViewById(R.id.txt_date);

        //setting the amount
        editTextAmount.setHint(CURRENCY_ + "0");

        saveButton = findViewById(R.id.btn_save);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);

        //back button on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    // handles the toolbar back pressed
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData() {

        Intent intent = getIntent();

        if (intent.getStringExtra("type").equals("lend")) {
            toolbar.setTitle("Lended Amount");
            saveButton.setBackgroundResource(R.drawable.drawable_round_green_button);
        }
        else if (intent.getStringExtra("type").equals("borrow")) {
            toolbar.setTitle("Borrowed Amount");
            saveButton.setBackgroundResource(R.drawable.drawable_round_red_button);
        }

    }

    private void saveLoanTrans(){}

    private void getCurrentDate() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yy");
        String currentDate = dateFormat.format(calendar.getTime());

        textViewDate.setText("\t\t");
        textViewDate.append(currentDate);

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MMMM dd, yy hh:mm aaa");
        currentDateTime=dateFormat1.format(calendar.getTime());

        //setting the am/pm to AM/PM (in capital)
        if(currentDateTime.contains("pm"))
        {
            Log.i("DATETIME_1", "if called: ");
            currentDateTime.replace("pm","PM");
        }
        else if(currentDateTime.contains("am"))
        {
            currentDateTime.replace("am","AM");
        }

        Log.i("DATETIME_1", "current formatted date: "+currentDateTime);
    }

    private void dateTimeDialog(){
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();

        final Calendar calendar=Calendar.getInstance();

        DatePickerDialog.OnDateSetListener onDateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Log.d("HELLO_123", "i: "+i+"\ni1: "+i1+"\ni2: "+i2);
            }
        };
    }
}