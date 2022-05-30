package com.hammad.managerya.bottomNavFragments.walletFragment.budget;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.hammad.managerya.R;
import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.homeFragment.MonthAdapter;
import com.hammad.managerya.bottomNavFragments.walletFragment.budget.RoomDB.BudgetDetailsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        //initialize toolbar
        setToolbar();

        //initialize views
        initializeViews();

        //initializing database instance
        database = RoomDBHelper.getInstance(this);

        //getting the list of addedBudgetList
        addedBudgetList = database.budgetDao().getBudgetList();

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
            totalBudgetSpend += getBudgetSpend(addedBudgetList.get(i).getBudgetCatId());
        }

        //create budget click listener
        textViewCreateBudget.setOnClickListener(view -> {
            startActivity(new Intent(this, ActivityCreateBudget.class));
            finish();
        });

        //setting the total budget info
        setTotalBudgetInfo(totalBudgetAllocated,totalBudgetSpend);

    }

    private void setToolbar()
    {
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

    private void initializeViews()
    {
        textViewCurrency1=findViewById(R.id.txt_currency_budget);
        textViewCurrency2=findViewById(R.id.txt_currency_2_budget);

        //setting the currency to textviews
        textViewCurrency1.setText(CURRENCY_);
        textViewCurrency2.setText(CURRENCY_);

        textViewTotalBudgetSpend =findViewById(R.id.txt_total_budget_spend);
        textViewTotalBudgetAllocated =findViewById(R.id.txt_total_budget);
        textViewTotalRemainingBudget =findViewById(R.id.txt_remaining_budget_amount);

        textViewCreateBudget=findViewById(R.id.txt_create_budget);

        progressBar=findViewById(R.id.progress_budget);

        //recycler views
        recyclerViewMonth=findViewById(R.id.recycler_view_months_budget);
        recyclerViewBudget=findViewById(R.id.recycler_budget);
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

    private void setMonthRecyclerView()
    {
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
    public void onMonthSelected(String monthName) {}

    private void setBudgetRecyclerview()
    {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewBudget.setLayoutManager(layoutManager);

        BudgetAdapter budgetAdapter=new BudgetAdapter(this,addedBudgetList,this);
        recyclerViewBudget.setAdapter(budgetAdapter);
    }

    //Budget Adapter click listener
    @Override
    public void onBudgetItemClicked(int position) {

        Intent intent=new Intent(this, ActivityBudgetHistory.class);
        intent.putExtra("BudgetCatId",addedBudgetList.get(position).getBudgetCatId());
        startActivity(intent);
    }

    //function for getting the details of particular category (expense transactions)
    private int getBudgetSpend(int budgetCatId) {

        return database.expenseDetailDao().getExpenseCategorySum(budgetCatId);
    }

    private void setTotalBudgetInfo(int totalAllocatedBudget,int totalSpendBudget) {

        //setting the values to textviews
        textViewTotalBudgetAllocated.setText(String.valueOf(totalAllocatedBudget));

        textViewTotalBudgetSpend.setText(String.valueOf(totalBudgetSpend));

        textViewTotalRemainingBudget.setText(String.valueOf(totalBudgetAllocated - totalBudgetSpend));

        //setting progressbar values
        progressBar.setMax(totalAllocatedBudget);
        progressBar.setProgress(totalSpendBudget);
    }
}