package com.risibleapps.mywallet.bottomNavFragments.walletFragment.budget;

import static com.risibleapps.mywallet.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.risibleapps.mywallet.BuildConfig;
import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.homeFragment.MonthAdapter;
import com.risibleapps.mywallet.bottomNavFragments.walletFragment.budget.RoomDB.BudgetDetailsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BudgetActivity extends AppCompatActivity implements MonthAdapter.OnMonthClickListener, BudgetAdapter.OnBudgetClickListener {

    private Toolbar toolbar;
    private LinearProgressIndicator progressBar;
    private TextView textViewCurrency1,textViewCurrency2, textViewTotalBudgetSpend, textViewTotalBudgetAllocated, textViewTotalRemainingBudget;
    private TextView textViewCreateBudget;

    private RecyclerView recyclerViewMonth,recyclerViewBudget;

    private List<String> monthsList=new ArrayList<>();

    //budget list
    private List<BudgetDetailsModel> addedBudgetList=new ArrayList<>();

    //database instance
    private RoomDBHelper database;

    //variables for saving info related to total budget expenditure
    private int totalBudgetAllocated = 0;
    private int totalBudgetSpend = 0;

    //string for saving current month and year
    private String currentMonth="";

    private InterstitialAd mInterstitialAd;

    private TextView textViewNoActiveBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        //initializing mobile ad
        MobileAds.initialize(this, initializationStatus -> {});

        //initialize toolbar
        setToolbar();

        //initialize views
        initializeViews();

        //getting the current date
        getCurrentDate();

        //loading the ad
        loadAd();

        //initializing database instance
        database = RoomDBHelper.getInstance(this);

        //getting the list of addedBudgetList
        addedBudgetList = database.budgetDao().getBudgetList(monthDateConversion(currentMonth));

        //get the month list
        getMonthsList();

        //month recyclerview
        setMonthRecyclerView();

        //budget recyclerview
        setBudgetRecyclerview();

        //traversing the addedBudgetList for total sum of budget categories and budget spend
        for (int i=0; i < addedBudgetList.size(); i++)
        {
            totalBudgetAllocated += addedBudgetList.get(i).getBudgetLimit();
            totalBudgetSpend += getBudgetSpend(addedBudgetList.get(i).getBudgetCatId(),monthDateConversion(currentMonth));
        }

        //create budget click listener
        textViewCreateBudget.setOnClickListener(view -> {
            showAd();
        });

        //setting the total budget info
        setTotalBudgetInfo(totalBudgetAllocated,totalBudgetSpend);

    }

    private void setToolbar() {

        toolbar=findViewById(R.id.toolbar_budget);

        setSupportActionBar(toolbar);

        //setting the back pressed button on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //handles the back pressed on toolbar
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initializeViews() {

        textViewCurrency1=findViewById(R.id.txt_currency_budget);
        textViewCurrency2=findViewById(R.id.txt_currency_2_budget);

        //setting the currency to textviews
        textViewCurrency1.setText(CURRENCY_);
        textViewCurrency2.setText(CURRENCY_);

        textViewTotalBudgetSpend =findViewById(R.id.txt_total_budget_spend);
        textViewTotalBudgetAllocated =findViewById(R.id.txt_total_budget);
        textViewTotalRemainingBudget =findViewById(R.id.txt_remaining_budget_amount);

        //no recent budget
        textViewNoActiveBudget = findViewById(R.id.no_recent_trans_budget);

        textViewCreateBudget=findViewById(R.id.txt_create_budget);

        progressBar=findViewById(R.id.progress_budget);

        //recycler views
        recyclerViewMonth=findViewById(R.id.recycler_view_months_budget);
        recyclerViewBudget=findViewById(R.id.recycler_budget);
    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat1 =new SimpleDateFormat("MMM yyyy");
        currentMonth = dateFormat1.format(calendar.getTime());
    }

    private void getMonthsList() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy");

        /*
            This returns the last 10 months from current month
            if current month is Apr 2022, then is will return values from Mar 2022 to Jun 2021
        */
        for (int i = 10; i >= 1; i--) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.add(Calendar.MONTH, -i);

            //adding the months in string list
            monthsList.add(dateFormat.format(calendar1.getTime()));

        }

        //returns the current month
        Calendar calendar2 = Calendar.getInstance();
        monthsList.add(dateFormat.format(calendar2.getTime()));

        //returns the next month (if current month is Apr 2022, it will return May 2022)
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.MONTH, +1);

        //adding in the list
        monthsList.add(dateFormat.format(calendar3.getTime()));

    }

    private void setMonthRecyclerView() {

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewMonth.setLayoutManager(layoutManager);

        MonthAdapter monthAdapter=new MonthAdapter(this,monthsList,this);
        recyclerViewMonth.setAdapter(monthAdapter);

        //scroll recyclerview to last month (current month = last month -1)
        int newPosition = monthsList.size() - 1;
        recyclerViewMonth.scrollToPosition(newPosition);
    }

    //Month recyclerview click listener
    @Override
    public void onMonthSelected(String monthName) {

        //setting the selected month value to current month
        currentMonth = monthName;

        //getting the updated data
        getUpdatedData();

    }

    private void setBudgetRecyclerview() {

        if(addedBudgetList.size() == 0)
        {
            textViewNoActiveBudget.setVisibility(View.VISIBLE);
            recyclerViewBudget.setVisibility(View.INVISIBLE);
        }
        else if(addedBudgetList.size() > 0)
        {
            textViewNoActiveBudget.setVisibility(View.GONE);
            recyclerViewBudget.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewBudget.setLayoutManager(layoutManager);

        BudgetAdapter budgetAdapter=new BudgetAdapter(this,monthDateConversion(currentMonth),addedBudgetList,this);
        recyclerViewBudget.setAdapter(budgetAdapter);
    }

    //Budget Adapter click listener
    @Override
    public void onBudgetItemClicked(int position) {

        Intent intent=new Intent(this, ActivityBudgetHistory.class);
        intent.putExtra("BudgetCatId",addedBudgetList.get(position).getBudgetCatId());
        startActivity(intent);
        finish();
    }

    //Budget Interface long click listener
    @Override
    public void onBudgetItemLongClick() {

        //getting the latest changes when a record is deleted
        getUpdatedData();
    }

    private void getUpdatedData(){

        //getting the list of addedBudgetList
        addedBudgetList = database.budgetDao().getBudgetList(monthDateConversion(currentMonth));

        //budget recyclerview
        setBudgetRecyclerview();

        //setting the variables values to zero
        totalBudgetAllocated = 0;
        totalBudgetSpend = 0;

        //traversing the addedBudgetList for total sum of budget categories and budget spend
        for (int i=0; i < addedBudgetList.size(); i++)
        {
            totalBudgetAllocated += addedBudgetList.get(i).getBudgetLimit();
            totalBudgetSpend += getBudgetSpend(addedBudgetList.get(i).getBudgetCatId(),monthDateConversion(currentMonth));
        }

        //setting the total budget info
        setTotalBudgetInfo(totalBudgetAllocated,totalBudgetSpend);
    }

    //function for getting the details of particular category (expense transactions)
    private int getBudgetSpend(int budgetCatId,String date) {

        return database.expenseDetailDao().getExpenseCategorySum(budgetCatId,date);
    }

    private void setTotalBudgetInfo(int totalAllocatedBudget,int totalSpendBudget) {

        //setting the values to textviews
        textViewTotalBudgetAllocated.setText(String.valueOf(totalAllocatedBudget));

        textViewTotalBudgetSpend.setText(String.valueOf(totalSpendBudget));

        textViewTotalRemainingBudget.setText(String.valueOf(totalAllocatedBudget - totalSpendBudget));

        //setting progressbar values
        progressBar.setMax(totalAllocatedBudget);
        progressBar.setProgress(totalSpendBudget);
    }

    private String monthDateConversion(String dateToConvert) {
        String convertedDate="";

        //current format
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MMM yyyy");

        //converting date to another format

        SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy-MM");
        try {
            Date date=dateFormat1.parse(dateToConvert);
            convertedDate = dateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate;
    }

    public void loadAd() {

        //checking whether app is running on release/debug version
        String interstitialAdId="";
        if(BuildConfig.DEBUG)
        {
            interstitialAdId=getString(R.string.interstitial_ad_debug);
            Log.i("BUDGET_AD", "BUDGET debug version: "+interstitialAdId);
        }
        else {
            interstitialAdId=getString(R.string.interstitial_ad_release);
            Log.i("BUDGET_AD", "BUDGET release version: "+interstitialAdId);
        }

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, interstitialAdId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                Log.i("BUDGET_AD", "BUDGET onAdLoaded: ");
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.i("BUDGET_AD", "BUDGET failed ad: "+loadAdError.getCode()+"\n"+loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });
    }

    public void showAd() {
        //checking if ad is loaded or not
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    mInterstitialAd = null;

                    //moving to ActivityCreateBudget
                    startActivity(new Intent(BudgetActivity.this, ActivityCreateBudget.class));
                    finish();
                }

                //this function make sure no ad is loaded for second time
                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();

                    mInterstitialAd = null;
                }
            });
        }
        else
        {
            //if there is no internet connection, then no ad will be loaded and app will move onto next screen
            Intent intent = new Intent(this, ActivityCreateBudget.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mInterstitialAd = null;
    }

    @Override
    protected void onDestroy() {
        mInterstitialAd = null;
        super.onDestroy();
    }
}