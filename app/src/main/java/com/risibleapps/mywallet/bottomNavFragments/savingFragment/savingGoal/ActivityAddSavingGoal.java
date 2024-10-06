package com.risibleapps.mywallet.bottomNavFragments.savingFragment.savingGoal;

import static com.risibleapps.mywallet.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.app.DatePickerDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.savingFragment.DB.SavingEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityAddSavingGoal extends AppCompatActivity implements SavingCategoryAdapter.OnSavingCategoryListener {

    private Toolbar toolbar;
    private EditText editTextEnterAmount, editTextGoalTitle, editTextAddTag;
    private RecyclerView recyclerView;
    private TextView textViewTargetDate;

    //two different date formats are used. One for displaying date and second for saving data in database based on date format
    private String dateToSet;
    private String dateToSave;

    private AppCompatButton buttonCreateGoal;

    //Database instance
    private RoomDBHelper database;

    //integer for saving the category icon
    private int categoryIcon = 0;

    //saving categories list
    List<SavingCategoryModel> savingCategoryList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_saving_goal);

        //setting the toolbar
        setToolbar();

        //initialize views
        initialViews();

        //initialize database helper class
        database = RoomDBHelper.getInstance(this);

        //getting the income categories list
        savingCategoriesList();

        //get current date and display it in edit text date
        getCurrentDate();

        //textview click listener which will display calendar alert dialog
        textViewTargetDate.setOnClickListener(view -> dateAlertDialog());

        //setting the recyclerview
        setRecyclerView();

        //button click listener
        buttonCreateGoal.setOnClickListener(view -> saveGoal());

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

        buttonCreateGoal = findViewById(R.id.button_saving_goal);

    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        dateToSet = dateFormat.format(calendar.getTime());

        textViewTargetDate.setText("\t\t" + dateToSet);

        //date to be saved in database in format (yyyy-MM-dd)
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        dateToSave = dateFormat1.format(calendar.getTime());

        Log.i("DATE_1", "current display date: "+dateToSet);
        Log.i("DATE_1", "current DB date: "+dateToSave);
    }

    private void savingCategoriesList() {

        savingCategoryList.add(new SavingCategoryModel(R.drawable.appliances,"Appliances"));

        savingCategoryList.add(new SavingCategoryModel(R.drawable.education,"Education"));
        savingCategoryList.add(new SavingCategoryModel(R.drawable.electronics,"Electronics"));
        savingCategoryList.add(new SavingCategoryModel(R.drawable.emergency,"Emergency"));

        savingCategoryList.add(new SavingCategoryModel(R.drawable.family,"Family"));

        savingCategoryList.add(new SavingCategoryModel(R.drawable.gifts_received,"Gifts"));

        savingCategoryList.add(new SavingCategoryModel(R.drawable.hajj,"Hajj"));
        savingCategoryList.add(new SavingCategoryModel(R.drawable.home_e,"Home"));

        savingCategoryList.add(new SavingCategoryModel(R.drawable.other,"Other"));

        savingCategoryList.add(new SavingCategoryModel(R.drawable.party,"Party"));
        savingCategoryList.add(new SavingCategoryModel(R.drawable.personal_12,"Personal"));
        savingCategoryList.add(new SavingCategoryModel(R.drawable.investment,"Profit"));

        savingCategoryList.add(new SavingCategoryModel(R.drawable.shopping,"Shopping"));

        savingCategoryList.add(new SavingCategoryModel(R.drawable.travel,"Travel"));

        savingCategoryList.add(new SavingCategoryModel(R.drawable.vehicle,"Vehicle"));

        savingCategoryList.add(new SavingCategoryModel(R.drawable.wedding,"Wedding "));

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

                textViewTargetDate.setText("\t\t" + dateToSet);

                //date to save in database
                SimpleDateFormat sdp2 = new SimpleDateFormat("yyyy-MM-dd");
                dateToSave = sdp2.format(calendar.getTime());

                Log.i("DATE_1", "date picker display date: "+dateToSet);
                Log.i("DATE_1", "date picker DB date: "+dateToSave);
            }
        };

        new DatePickerDialog(ActivityAddSavingGoal.this, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //GridLayoutManager layoutManager=new GridLayoutManager(this,2,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        SavingCategoryAdapter adapter=new SavingCategoryAdapter(this,savingCategoryList,this);
        recyclerView.setAdapter(adapter);
    }

    //SavingCategoryAdapter interface click listener
    @Override
    public void onSavingCategoryClick(int position) {

        //saving the category icon to integer variable
        categoryIcon = savingCategoryList.get(position).getCategoryImage();
    }

    private void saveGoal() {
        if (editTextEnterAmount.getText().toString().trim().isEmpty()) {
            Snackbar snackbar = Snackbar.make(recyclerView, "Enter amount", Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else if (editTextGoalTitle.getText().toString().trim().isEmpty()) {
            Snackbar snackbar = Snackbar.make(recyclerView, "Enter Saving Goal Title", Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else if (editTextAddTag.getText().toString().trim().isEmpty()) {
            Snackbar snackbar = Snackbar.make(recyclerView, "Add tag", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        else if(categoryIcon == 0)
        {
            Snackbar snackbar = Snackbar.make(recyclerView, "Select Category Icon", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        else {

            //saving the goal to Database
            database.savingDao().addSavingGoal(new SavingEntity(Integer.valueOf(editTextEnterAmount.getText().toString().trim()),editTextGoalTitle.getText().toString(),editTextAddTag.getText().toString(),categoryIcon,dateToSave));
            Toast.makeText(this, "Saving Goal Added Successfully", Toast.LENGTH_SHORT).show();

            //1 second delay. After that activity will be finished
            new Handler().postDelayed(() -> finish(),1000);
        }
    }

}