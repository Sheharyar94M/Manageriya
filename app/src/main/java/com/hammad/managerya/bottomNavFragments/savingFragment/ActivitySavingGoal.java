package com.hammad.managerya.bottomNavFragments.savingFragment;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hammad.managerya.R;
import com.hammad.managerya.bottomNavFragments.addRecord.expense.AddExpenseAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivitySavingGoal extends AppCompatActivity implements AddExpenseAdapter.ExpenseInterfaceListener {

    private Toolbar toolbar;
    private EditText editTextEnterAmount, editTextGoalTitle, editTextAddTag;
    private RecyclerView recyclerView;
    private TextView textViewTargetDate;

    private String dateToSet;

    private AppCompatButton buttonCreateGoal;

    private int twentyFourImagesList[] = {R.drawable.bills, R.drawable.charity, R.drawable.committee, R.drawable.entertainment,
            R.drawable.electronics, R.drawable.education, R.drawable.family, R.drawable.food,
            R.drawable.fuel, R.drawable.grocery, R.drawable.health, R.drawable.home_e,
            R.drawable.installment, R.drawable.insurance, R.drawable.loan_paid, R.drawable.medical,
            R.drawable.mobile, R.drawable.office, R.drawable.other_expenses, R.drawable.rent_paid,
            R.drawable.shopping, R.drawable.transport, R.drawable.travel, R.drawable.wedding};


    private String[] twentyFourCatNameList = {"Bills &\nUtilities", "Charity", "Committee", "Entertain\nment",
            "Electronics", "Education", "Family", "Food"
            , "Fuel", "Grocery", "Health", "Home"
            , "Installment", "Insurance", "Loan\nPaid", "Medical"
            , "Mobile", "Office", "Other\nExpenses", "Rent\nPaid"
            , "Shopping", "Transport", "Travel", "Wedding"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_goal);

        //setting the toolbar
        setToolbar();

        //initialize views
        initialViews();

        //get current date and display it in edit text date
        getCurrentDate();

        //textview click listener which will display calendar alert dialog
        textViewTargetDate.setOnClickListener(view -> dateAlertDialog());

        //setting the recyclerview
        setRecyclerView();

        //button click listener
        buttonCreateGoal.setOnClickListener(view -> savingGoal());

    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar_saving_goal);
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

    private void initialViews() {
        editTextEnterAmount = findViewById(R.id.edit_text_amount_saving);
        //setting the hint
        editTextEnterAmount.setHint(CURRENCY_ + " 0");

        editTextGoalTitle = findViewById(R.id.edit_text_goal_saving);
        editTextAddTag = findViewById(R.id.edit_text_tag_saving);

        textViewTargetDate = findViewById(R.id.text_view_target_date_saving);

        recyclerView = findViewById(R.id.recycler_add_saving_goal);

        buttonCreateGoal=findViewById(R.id.button_saving_goal);

    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        dateToSet = dateFormat.format(calendar.getTime());

        textViewTargetDate.setText("\t\t" + dateToSet);
    }

    private void dateAlertDialog() {
        final String[] date = {""};

        CalendarView calendarView;

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        LayoutInflater layoutInflater=getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.alert_dialog_calendar,null);

        builder.setView(view)
                .setPositiveButton("Select Date", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //setting the selected date to textview
                        textViewTargetDate.setText("\t\t" +date[0]);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        //initializing the calendar view
        calendarView=view.findViewById(R.id.calendar_view);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day)
            {
                Calendar calendar=Calendar.getInstance();

                calendar.set(Calendar.MONTH,month);
                String monthName=new SimpleDateFormat("LLLL").format(calendar.getTime());

                String strDay="";

                if(day < 10)
                {
                    strDay="0"+day;
                }
                else if(day >= 10)
                {
                    strDay=String.valueOf(day);
                }

                date[0] =monthName+" "+strDay+", "+year;

                Log.i("DATE_1", "date: "+ date[0]);

            }
        });

        builder.create();
        builder.show();
    }

    private void setRecyclerView()
    {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        AddExpenseAdapter adapter=new AddExpenseAdapter(this,twentyFourImagesList,twentyFourCatNameList,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onExpenseItemClicked(int position, String catName) {
        Toast.makeText(this, catName, Toast.LENGTH_SHORT).show();
    }

    private void savingGoal()
    {
        if(editTextEnterAmount.getText().toString().trim().isEmpty())
        {
            Snackbar snackbar = Snackbar.make(recyclerView, "Enter amount", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        else if(editTextGoalTitle.getText().toString().trim().isEmpty())
        {
            Snackbar snackbar = Snackbar.make(recyclerView, "Enter Saving Goal Title", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        else if(editTextAddTag.getText().toString().trim().isEmpty())
        {
            Snackbar snackbar = Snackbar.make(recyclerView, "Add tag", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        else
        {
            Toast.makeText(this, "Saving Goal Added", Toast.LENGTH_SHORT).show();
        }
    }
}