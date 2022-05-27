package com.hammad.managerya.bottomNavFragments.loanFragment.contact;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.hammad.managerya.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddLoanTransactionActivity extends AppCompatActivity {

    private EditText editTextAmount, editTextDetails;
    private TextView textViewDate;
    private Toolbar toolbar;
    private AppCompatButton saveButton;
    private String currentDateTime, selectedDateTime;

    //string to save loan details
    private String loanDetails;

    //string to save the button pressed type from getIntent
    private String buttonPressedType;

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

        //setting the 'type' to string
        buttonPressedType=intent.getStringExtra("type");

        if (intent.getStringExtra("type").equals("Lend")) {
            toolbar.setTitle("Lended Amount");
            saveButton.setBackgroundResource(R.drawable.drawable_round_green_button);

        }
        else if (intent.getStringExtra("type").equals("Borrow")) {
            toolbar.setTitle("Borrowed Amount");
            saveButton.setBackgroundResource(R.drawable.drawable_round_red_button);
        }

    }

    private void saveLoanTrans(){

        //setting the details to string variable (if any)
        if(editTextDetails.getText().toString().length() > 0)
        {
            loanDetails=editTextDetails.getText().toString();
        }
        else if(editTextDetails.getText() == null || editTextDetails.getText().toString().length() == 0)
        {
            loanDetails="";
        }

        Intent intent=new Intent(this, ConsumerDetailActivity.class);
        intent.putExtra("btnType", buttonPressedType);
        intent.putExtra("loanDetails",loanDetails);

        //checking whether user entered loan amount or not
        if(editTextAmount.getText().toString().length() > 0)
        {
            if(selectedDateTime == null || selectedDateTime.length() == 0)
            {
                intent.putExtra("amount",editTextAmount.getText().toString());
                intent.putExtra("transDate",currentDateTime);
                startActivity(intent);
                finish();

            }
            else if(selectedDateTime != null || selectedDateTime.length() > 0)
            {
                intent.putExtra("amount",editTextAmount.getText().toString());
                intent.putExtra("transDate",selectedDateTime);
                startActivity(intent);
                finish();
            }
        }
        else if(editTextAmount.getText().toString().length() <= 0)
        {
            Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
        }

    }

    private void getCurrentDate() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String currentDate = dateFormat.format(calendar.getTime());

        textViewDate.setText("\t\t");
        textViewDate.append(currentDate);

        //this is used to save the current date along with time (in case user does not change current date)
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTime=dateFormat1.format(calendar.getTime());
    }

    private void dateTimeDialog(){

        final Calendar calendar=Calendar.getInstance();

        DatePickerDialog.OnDateSetListener onDateSetListener= (datePicker, year, month, day) -> {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,day);

            //time picker dialog
            TimePickerDialog.OnTimeSetListener onTimeSetListener= (timePicker, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);

                //date format for setting selected date to textViewDate
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMM dd, yyyy");
                String selectedDate=simpleDateFormat.format(calendar.getTime());

                textViewDate.setText("\t\t");
                textViewDate.append(selectedDate);

                //selected date and time
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                selectedDateTime =dateFormat.format(calendar.getTime());
            };

            new TimePickerDialog(AddLoanTransactionActivity.this,onTimeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
        };

        new DatePickerDialog(AddLoanTransactionActivity.this,onDateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}