package com.hammad.managerya.bottomNavFragments.savingFragment;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.hammad.managerya.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivitySavingTransaction extends AppCompatActivity {

    private EditText editTextTrans;
    private TextView textViewDate;
    private AppCompatButton buttonAddTransaction;
    private Toolbar toolbar;

    private String dateToSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_transaction);

        //initialize views
        initializeViews();

        //setting toolbar
        setToolbar();

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

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        dateToSet = dateFormat.format(calendar.getTime());

        textViewDate.setText("\t\t" + dateToSet);
    }

    private void dateAlertDialog() {
        final String[] date = {""};

        CalendarView calendarView;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.alert_dialog_calendar, null);

        builder.setView(view)
                .setPositiveButton("Select Date", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //setting the selected date to textview
                        textViewDate.setText("\t\t" + date[0]);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        //initializing the calendar view
        calendarView = view.findViewById(R.id.calendar_view);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.MONTH, month);
                String monthName = new SimpleDateFormat("LLLL").format(calendar.getTime());

                String strDay = "";

                if (day < 10) {
                    strDay = "0" + day;
                } else if (day >= 10) {
                    strDay = String.valueOf(day);
                }

                date[0] = monthName + " " + strDay + ", " + year;

                Log.i("DATE_1", "date: " + date[0]);

            }
        });

        builder.create();
        builder.show();
    }

    private void addSavingTransaction()
    {
        if (editTextTrans.getText().toString().trim().isEmpty()) {
            Snackbar snackbar = Snackbar.make(textViewDate, "Enter Transaction amount", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        else
        {
            Toast.makeText(this, "Saving Transaction Successful", Toast.LENGTH_SHORT).show();
        }

    }
}