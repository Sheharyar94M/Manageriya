package com.risibleapps.mywallet.bottomNavFragments.walletFragment.history;

import static com.risibleapps.mywallet.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;
import static com.risibleapps.mywallet.bottomNavFragments.walletFragment.WalletFragment.CHECK_VALUE;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.homeFragment.HomeFragTransAdapter;
import com.risibleapps.mywallet.bottomNavFragments.homeFragment.MonthAdapter;
import com.risibleapps.mywallet.bottomNavFragments.homeFragment.ViewTransDetailsActivity;
import com.risibleapps.mywallet.bottomNavFragments.homeFragment.homeDB.HomeRecentTransModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements MonthAdapter.OnMonthClickListener, HomeFragTransAdapter.RecentTransInterface, AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerViewMonth,recyclerViewTransaction;
    private TextView textViewCurrency, textViewBalance,textViewNoOfTrans;

    private List<String> monthsList=new ArrayList<>();
    private Spinner spinner;

    private Toolbar toolbar;

    //for Spinner
    String[] dateSortList={"Date (Descending)", "Date (Ascending)"};

    //variables for calculating the current earning,spending and remaining balance
    private float earning=0, expense =0,remainingBalance=0;

    //Room DB
    private RoomDBHelper database;

    //recent transaction list
    private List<HomeRecentTransModel> recentTransList=new ArrayList<>();

    //string for saving current month and year
    private String currentMonth="";

    private TextView textViewNoRecentTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //initialize views
        initializeViews();

        //getting the current date
        getCurrentDate();

        //initializing database instances
        database = RoomDBHelper.getInstance(this);

        //getting the total sum of income
        earning = database.incomeDetailDao().getTotalIncomeSum(monthDateConversion(currentMonth));

        //getting the total sum of expense
        expense = database.expenseDetailDao().getTotalExpenseSum(monthDateConversion(currentMonth));

        //remaining balance
        remainingBalance = earning - expense;

        //setting the remaining balance value to textview balance
        textViewBalance.setText(String.valueOf((int) remainingBalance));

        //get the months list
        getMonthsList();

        //set months recyclerview
        setMonthRecyclerView();

        //setting the spinner
        setSpinner();

        //setting the toolbar as actionbar
        setSupportActionBar(toolbar);

        //setting the back button on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initializeViews() {

        //toolbar
        toolbar=findViewById(R.id.toolbar_history);

        recyclerViewMonth=findViewById(R.id.recycler_view_months_history);

        recyclerViewTransaction=findViewById(R.id.recycler_trans_history);

        textViewCurrency=findViewById(R.id.text_currency_history);

        //no recent trans
        textViewNoRecentTrans=findViewById(R.id.no_recent_trans_history);

        //setting the currency
        textViewCurrency.setText(CURRENCY_);

        textViewBalance =findViewById(R.id.text_amount_history);
        textViewNoOfTrans=findViewById(R.id.text_monthly_trans);

        //spinner
        spinner=findViewById(R.id.spinner_sort);
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

        currentMonth = monthName;

        //getting the latest changes

        //getting the total sum of income
        earning = database.incomeDetailDao().getTotalIncomeSum(monthDateConversion(currentMonth));

        //getting the total sum of expense
        expense = database.expenseDetailDao().getTotalExpenseSum(monthDateConversion(currentMonth));

        //remaining balance
        remainingBalance = earning - expense;

        //setting the remaining balance value to textview balance
        textViewBalance.setText(String.valueOf((int) remainingBalance));

        //setting the spinner
        setSpinner();

    }

    private void setRecentTransRecyclerview(List<HomeRecentTransModel> list) {

        if(list.size() == 0)
        {
            textViewNoRecentTrans.setVisibility(View.VISIBLE);
            recyclerViewTransaction.setVisibility(View.INVISIBLE);
        }
        else if(list.size() > 0)
        {
            textViewNoRecentTrans.setVisibility(View.GONE);
            recyclerViewTransaction.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewTransaction.setLayoutManager(layoutManager);

        HomeFragTransAdapter recentTransAdapter=new HomeFragTransAdapter(this,this,list);
        recyclerViewTransaction.setAdapter(recentTransAdapter);

        //setting the list size to textview no of transactions
        textViewNoOfTrans.setText(String.valueOf(list.size()));
    }

    //recent transaction click listener
    @Override
    public void onRecentTransClick(int position) {

        HomeRecentTransModel item = recentTransList.get(position);

        Intent intent = new Intent(this, ViewTransDetailsActivity.class);

        intent.putExtra("type",item.getTransType());
        intent.putExtra("catName", item.getCatName());
        intent.putExtra("amount", String.valueOf(item.getTransAmount()));
        intent.putExtra("date", getConvertedDate(item.getTransDate()));
        intent.putExtra("desc", item.getTransDesc());
        intent.putExtra("tag", item.getTransTag());
        intent.putExtra("loc", item.getTransLocation());
        intent.putExtra("img", item.getTransImagePath());

        startActivity(intent);
    }

    //overridden long click listener of HomeFragTransAdapter
    @Override
    public void onRecentTransLongClick() {
        //getting the latest changes when a record is deleted

        //getting the total sum of income
        earning = database.incomeDetailDao().getTotalIncomeSum(currentMonth);

        //getting the total sum of expense
        expense = database.expenseDetailDao().getTotalExpenseSum(currentMonth);

        //remaining balance
        remainingBalance = earning - expense;

        //setting the remaining balance value to textview balance
        textViewBalance.setText(String.valueOf((int) remainingBalance));

        //setting the spinner
        setSpinner();

        //incrementing the variable value of Wallet Fragment for fetching latest data in Wallet Fragment in case any record is deleted
        CHECK_VALUE++;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setSpinner() {

        ArrayAdapter spinnerAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item,dateSortList);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setBackgroundColor(getResources().getColor(R.color.white));

        //spinner click listener
        spinner.setOnItemSelectedListener(this);
    }

    //spinner item selected listeners
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        switch (position)
        {
            case 0:
                recentTransList = database.homeFragmentDao().getAllTransactions(monthDateConversion(currentMonth));
                //setting the list to recycler view
                setRecentTransRecyclerview(recentTransList);
                break;

            case 1:
                recentTransList = database.homeFragmentDao().getAllTransactionsByASC(monthDateConversion(currentMonth));
                //setting the list to recycler view
                setRecentTransRecyclerview(recentTransList);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    //for handling the back pressed
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

    /*
       This function is used to convert date from 'yyyy-MM-dd HH:mm:ss' to 'MMM dd, yyyy hh:mm aaa' format
       2022-05-27 11:05:32 to May 27, 2022 11:05 am
   */
    private String getConvertedDate(String databaseDate) {
        String convertedDate = "";

        //database date format
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //converting date format to another
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM dd, yyyy hh:mm aaa");
        try {
            Date date = dateFormat1.parse(databaseDate);
            convertedDate = dateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate;
    }

    //date conversion for month (Jun 2022 to 2022-06)
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

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat1 =new SimpleDateFormat("MMM yyyy");

        currentMonth = dateFormat1.format(calendar.getTime());
    }
}