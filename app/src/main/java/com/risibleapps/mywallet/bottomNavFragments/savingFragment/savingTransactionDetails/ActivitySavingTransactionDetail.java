package com.risibleapps.mywallet.bottomNavFragments.savingFragment.savingTransactionDetails;

import static com.risibleapps.mywallet.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.savingFragment.DB.SavingModel;
import com.risibleapps.mywallet.bottomNavFragments.savingFragment.savingGoalTransaction.ActivityAddSavingTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivitySavingTransactionDetail extends AppCompatActivity implements SavingRecentTransAdapter.OnSavingRecentTransListener {

    private TextView textViewSavingGoalTitle,textViewDate,textViewCurrency,textViewAmountSaved,textViewSavingGoal,textViewPercentage;
    private ImageView imageViewCategory;
    private LinearProgressIndicator progressBar;
    private Toolbar toolbar;

    private TextView textViewSavingTransaction;
    private RecyclerView recyclerViewSavingTransaction;

    private SavingRecentTransAdapter recentTransAdapter;

    //variables for saving the intent data (static so that the values can't be deleted when activity recreates)
    private static int id,icon;
    private static String title,date;
    private static float savingGoalAmount;

    //database instance
    private RoomDBHelper database;

    //integer for sum of saving goal transactions
    private float amountSaved = 0;

    //list of recent transaction
    List<SavingModel> savingTransactionList = new ArrayList<>();

    private TextView textViewNoSavingTrans;

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

        //getting the list of recent transaction
        savingTransactionList = database.savingDao().getSavingTransDetailsListById(id);

        //set the recent saving transaction recyclerview
        setRecyclerView();

        //create saving transaction
        textViewSavingTransaction.setOnClickListener(view -> {
            Intent intent= new Intent(this, ActivityAddSavingTransaction.class);
            intent.putExtra("id",id);
            intent.putExtra("title",title);
            intent.putExtra("icon",icon);
            startActivity(intent);
            finish();
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

        //no saving transaction
        textViewNoSavingTrans =findViewById(R.id.no_recent_trans_saving_1);

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

        if(savingTransactionList.size() == 0)
        {
            textViewNoSavingTrans.setVisibility(View.VISIBLE);
            recyclerViewSavingTransaction.setVisibility(View.INVISIBLE);
        }
        else if(savingTransactionList.size() > 0)
        {
            textViewNoSavingTrans.setVisibility(View.GONE);
            recyclerViewSavingTransaction.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewSavingTransaction.setLayoutManager(layoutManager);

        recentTransAdapter = new SavingRecentTransAdapter(this,savingTransactionList,this);
        recyclerViewSavingTransaction.setAdapter(recentTransAdapter);

    }

    private void getIntentData(){

        Intent intent=getIntent();

        /*
            This condition is used to check the intent data. In case this activity is called from SavingFragment (fragment), intent will have data.
            If this activity is called from ActivityAddSavingTransaction (activity), then intent will be null
        */
        if(intent.getStringExtra("date") != null)
        {
            id=intent.getIntExtra("id",-1);
            savingGoalAmount =intent.getIntExtra("amount",0);
            icon=intent.getIntExtra("icon",0);

            title=intent.getStringExtra("title");
            date = intent.getStringExtra("date");
        }

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
        //in case of progress bars, we need to write the max value first followed by set progress
        progressBar.setMax((int) savingGoalAmount);
        progressBar.setProgress((int) amountSaved);

        Log.i("HELLO_123", "amount saved: "+amountSaved);
        Log.i("HELLO_123", "progress: "+progressBar.getProgress());
        Log.i("HELLO_123", "max: "+progressBar.getMax());

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

    //SavingRecentTransAdapter long click listener
    @Override
    public void onSavingRecentTransLongClick() {

        //getting the latest changes

        //get the intent data
        getIntentData();

        //getting the list of recent transaction
        savingTransactionList = database.savingDao().getSavingTransDetailsListById(id);

        //set the recent saving transaction recyclerview
        setRecyclerView();
    }
}