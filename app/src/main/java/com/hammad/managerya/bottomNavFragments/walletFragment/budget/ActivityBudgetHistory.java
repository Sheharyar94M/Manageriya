package com.hammad.managerya.bottomNavFragments.walletFragment.budget;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hammad.managerya.R;
import com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragTransAdapter;
import com.hammad.managerya.bottomNavFragments.homeFragment.MonthAdapter;
import com.hammad.managerya.bottomNavFragments.homeFragment.ViewTransDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityBudgetHistory extends AppCompatActivity implements MonthAdapter.OnMonthClickListener, HomeFragTransAdapter.RecentTransInterface {

    private RecyclerView recyclerViewMonth,recyclerViewRecent;
    private TextView textViewBudgetCatName;
    private TextView textViewCurrency1,textViewCurrency2,textViewCurrency3;
    private TextView textViewTotalBudget,textViewBudgetSpend,textViewRemainingBudget;
    private TextView textViewBudgetHistoryAmount, textViewTotalNoOfTrans;

    private ProgressBar progressBar;

    private Toolbar toolbar;

    private List<String> monthsList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_history);

        //toolbar
        setToolbar();

        //initialize views
        initializeViews();

        //getting the months list
        getMonthsList();

        //month recyclerview
        setMonthRecycler();

        //recent budget transaction recyclerview
        setRecentRecycler();
    }

    private void initializeViews() {

        textViewBudgetCatName =findViewById(R.id.item_1);

        textViewCurrency1=findViewById(R.id.text_currency1);
        textViewCurrency2=findViewById(R.id.text_currency_2_budget);
        textViewCurrency3=findViewById(R.id.text_currency_budget_history);

        //setting the currency to textviews
        textViewCurrency1.setText(CURRENCY_);
        textViewCurrency2.setText(CURRENCY_);
        textViewCurrency3.setText(CURRENCY_);

        textViewTotalBudget=findViewById(R.id.text_total_budget);
        textViewBudgetSpend=findViewById(R.id.text_total_budget_spend);
        textViewRemainingBudget=findViewById(R.id.text_remaining_budget_amount);

        textViewBudgetHistoryAmount=findViewById(R.id.text_amount_history_);
        textViewTotalNoOfTrans=findViewById(R.id.text_monthly_trans_bud_history);

        recyclerViewMonth=findViewById(R.id.recycler_months_budget_history);
        recyclerViewRecent=findViewById(R.id.recycler_recent_budget);

        progressBar=findViewById(R.id.progress_budget_history);
    }

    private void setToolbar(){
        toolbar=findViewById(R.id.toolbar_budget_history);

        setSupportActionBar(toolbar);

        //sets the back button on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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

    private void setMonthRecycler()
    {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewMonth.setLayoutManager(layoutManager);

        MonthAdapter monthAdapter=new MonthAdapter(this,monthsList,this);
        recyclerViewMonth.setAdapter(monthAdapter);

        //scroll recyclerview to last month (current month = last month -1)
        int newPosition = monthsList.size() - 1;
        recyclerViewMonth.scrollToPosition(newPosition);
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

    //months click listener
    @Override
    public void onMonthSelected(String monthName) {
        Toast.makeText(this, monthName, Toast.LENGTH_SHORT).show();
    }

    private void setRecentRecycler() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewRecent.setLayoutManager(layoutManager);

        HomeFragTransAdapter adapter=new HomeFragTransAdapter(this,this,3);
        recyclerViewRecent.setAdapter(adapter);
    }

    //recent transaction adapter listener
    @Override
    public void onRecentTransClick(int position) {
        Toast.makeText(this, "Position: "+position, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, ViewTransDetailsActivity.class));
    }

}