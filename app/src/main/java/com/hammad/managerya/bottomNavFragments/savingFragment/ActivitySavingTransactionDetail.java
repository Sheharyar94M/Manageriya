package com.hammad.managerya.bottomNavFragments.savingFragment;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;
import com.hammad.managerya.RoomDB.RoomDBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivitySavingTransactionDetail extends AppCompatActivity {

    private TextView textViewSavingGoalTitle,textViewDate,textViewCurrency,textViewAmountSaved,textViewSavingGoal,textViewPercentage;
    private ImageView imageViewCategory;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    private TextView textViewSavingTransaction;
    private RecyclerView recyclerViewSavingTransaction;

    private SavingRecentTransAdapter recentTransAdapter;

    //variables for saving the intent data
    private int id,icon;
    private String title,date;
    private float savingGoalAmount;

    //database instance
    private RoomDBHelper database;

    //integer for sum of saving goal transactions
    private float amountSaved = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_transaction_detail);

        //initializing toolbar
        toolbar = findViewById(R.id.toolbar_saving_trans_detail);

        //initializing database
        database = RoomDBHelper.getInstance(this);

        //setting the toolbar
        setToolbar();

        //initialize views
        initializeViews();

        //get the intent data
        getIntentData();

        //set the recent saving transaction recyclerview
        setRecyclerView();

        //create saving transaction
        textViewSavingTransaction.setOnClickListener(view -> {
            Intent intent= new Intent(this,ActivityAddSavingTransaction.class);
            intent.putExtra("id",id);
            startActivity(intent);
        });
    }

    private void setToolbar() {

        //setting toolbar as action bar
        setSupportActionBar(toolbar);

        //setting the back button on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeViews() {

        imageViewCategory = findViewById(R.id.img_saving_detail);

        textViewSavingGoalTitle = findViewById(R.id.txt_cat_name);
        textViewDate = findViewById(R.id.txt_saving_date_2);

        textViewCurrency = findViewById(R.id.text_currency_saving_trans);
        textViewAmountSaved = findViewById(R.id.total_saving);
        textViewSavingGoal = findViewById(R.id.total_saving_goal);
        textViewPercentage = findViewById(R.id.percentage);

        progressBar = findViewById(R.id.progress_savings_detail);

        textViewSavingTransaction = findViewById(R.id.txt_create_saving_trans);

        recyclerViewSavingTransaction = findViewById(R.id.recycler_recent_saving_trans);

        //setting the currency to textview
        textViewCurrency.setText(CURRENCY_);
    }

    //handling the back pressed button on toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setRecyclerView() {

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewSavingTransaction.setLayoutManager(layoutManager);

        recentTransAdapter = new SavingRecentTransAdapter(this);
        recyclerViewSavingTransaction.setAdapter(recentTransAdapter);

    }

    private void getIntentData(){

        Intent intent=getIntent();

        id=intent.getIntExtra("id",-1);
        savingGoalAmount =intent.getIntExtra("amount",0);
        icon=intent.getIntExtra("icon",0);

        title=intent.getStringExtra("title");
        date = intent.getStringExtra("date");

        //retrieving the sum of saving goal transactions
        amountSaved = database.savingDao().getSavingTransSumById(id);

        //setting the values to views
        imageViewCategory.setImageResource(icon);

        textViewSavingGoalTitle.setText(title);
        textViewDate.setText(getConvertedDate(date));

        textViewSavingGoal.setText(String.valueOf((int) savingGoalAmount));
        textViewAmountSaved.setText(String.valueOf((int) amountSaved));
        textViewPercentage.setText(String.valueOf((int) ((amountSaved / savingGoalAmount) * 100)));
        textViewPercentage.append(" %");

        //progress bar
        progressBar.setProgress((int) amountSaved);
        progressBar.setMax((int) savingGoalAmount);

    }

    /*
        This function is used to convert date from 'yyyy-MM-dd' to 'MMM dd, yyyy' format
        2022-05-27 to May 27, 2022
    */
    private String getConvertedDate(String databaseDate) {
        String convertedDate = "";

        //database date format
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

        //converting date format to another
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date date = dateFormat1.parse(databaseDate);
            convertedDate = dateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate;
    }
}