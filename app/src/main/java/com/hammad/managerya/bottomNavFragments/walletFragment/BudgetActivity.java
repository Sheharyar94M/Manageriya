package com.hammad.managerya.bottomNavFragments.walletFragment;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.hammad.managerya.R;
import com.hammad.managerya.bottomNavFragments.homeFragment.MonthAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BudgetActivity extends AppCompatActivity implements MonthAdapter.OnMonthClickListener, BudgetAdapter.OnBudgetClickListener {

    private Toolbar toolbar;
    private LinearProgressIndicator progressBar;
    private TextView textViewCurrency1,textViewCurrency2,textViewBudgetSpend,textViewTotalBudget,textViewRemainingBudget;
    private TextView textViewCreateBudget;

    private RecyclerView recyclerViewMonth,recyclerViewBudget;

    private List<String> monthsList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        //initialize toolbar
        setToolbar();

        //initialize views
        initializeViews();

        //get the month list
        getMonthsList();

        //month recyclerview
        setMonthRecyclerView();

        //budget recyclerview
        setBudgetRecyclerview();

        //create budget click listener
        textViewCreateBudget.setOnClickListener(view -> Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show());
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

        textViewBudgetSpend=findViewById(R.id.txt_total_budget_spend);
        textViewTotalBudget=findViewById(R.id.txt_total_budget);
        textViewRemainingBudget=findViewById(R.id.txt_remaining_budget_amount);

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

        BudgetAdapter budgetAdapter=new BudgetAdapter(this,this);
        recyclerViewBudget.setAdapter(budgetAdapter);
    }

    //Budget Adapter click listener
    @Override
    public void onBudgetItemClicked(int position) {

        Toast.makeText(this, "Position: "+position, Toast.LENGTH_SHORT).show();
    }
}