package com.hammad.managerya.bottomNavFragments.walletFragment.budget;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;
import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragTransAdapter;
import com.hammad.managerya.bottomNavFragments.homeFragment.MonthAdapter;
import com.hammad.managerya.bottomNavFragments.homeFragment.ViewTransDetailsActivity;
import com.hammad.managerya.bottomNavFragments.homeFragment.homeDB.HomeRecentTransModel;
import com.hammad.managerya.bottomNavFragments.walletFragment.budget.RoomDB.BudgetDetailsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    //for saving the budget category id received through intent
    private int budgetCatId = -1;

    //list which contains the details of particular budget
    private List<BudgetDetailsModel> budgetDetailList=new ArrayList<>();

    //DB instance
    private RoomDBHelper database;

    private int budgetSpend=0;

    private List<HomeRecentTransModel> expenseDetailList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_history);

        //toolbar
        setToolbar();

        //initialize views
        initializeViews();

        //initializing database instance
        database=RoomDBHelper.getInstance(this);

        getIntentData();

        budgetDetailList = database.budgetDao().getBudgetById(budgetCatId);

        //getting the recent transaction list of specific budget categories
        expenseDetailList = database.expenseDetailDao().getExpenseDetailById(budgetCatId);

        //getting the months list
        getMonthsList();

        //month recyclerview
        setMonthRecycler();

        //setting the budget details to text views
        setBudgetDetails(budgetDetailList);

        //recent budget transaction recyclerview
        setRecentRecyclerView();
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

        //intent is called here to set the back stack of activity
        startActivity(new Intent(this,BudgetActivity.class));
        finish();

    }

    private void setMonthRecycler() {

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

    private void setRecentRecyclerView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewRecent.setLayoutManager(layoutManager);

        HomeFragTransAdapter adapter=new HomeFragTransAdapter(this,this,expenseDetailList);
        recyclerViewRecent.setAdapter(adapter);

        //setting the size of list to textViewTotalNoOfTrans
        textViewTotalNoOfTrans.setText(String.valueOf(expenseDetailList.size()));

    }

    //recent transaction adapter listener
    @Override
    public void onRecentTransClick(int position) {

        HomeRecentTransModel item=expenseDetailList.get(position);

        Intent intent = new Intent(this, ViewTransDetailsActivity.class);
        intent.putExtra("type", item.getTransType());
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

        budgetDetailList = database.budgetDao().getBudgetById(budgetCatId);

        //getting the recent transaction list of specific budget categories
        expenseDetailList = database.expenseDetailDao().getExpenseDetailById(budgetCatId);

        //setting the budget details to text views
        setBudgetDetails(budgetDetailList);

        //recent budget transaction recyclerview
        setRecentRecyclerView();

    }

    private void getIntentData(){
        Intent intent=getIntent();

        budgetCatId = intent.getIntExtra("BudgetCatId",-1);
    }

    private void setBudgetDetails(List<BudgetDetailsModel> list) {

        BudgetDetailsModel item= list.get(0);

        //getting the sum of spend budget for particular category
        budgetSpend = getBudgetSpend(budgetCatId);

        //if the category name contain new line literal (\n) then replace that liberal with no space so that the category can be displayed in single line
        if(item.getBudgetCatName().contains("\n")) {
            textViewBudgetCatName.setText(item.getBudgetCatName().replace("\n",""));
        }
        else if(!(item.getBudgetCatName().contains("\n"))) {
            textViewBudgetCatName.setText(item.getBudgetCatName());
        }

        textViewTotalBudget.setText(String.valueOf(item.getBudgetLimit()));

        textViewBudgetSpend.setText(String.valueOf(budgetSpend));

        textViewRemainingBudget.setText(String.valueOf(item.getBudgetLimit() - budgetSpend));

        //setting the progress bar values
        progressBar.setMax(item.getBudgetLimit());
        progressBar.setProgress(budgetSpend);

        //textviews of the transactions part
        textViewBudgetHistoryAmount.setText(String.valueOf(item.getBudgetLimit() - budgetSpend));

    }

    //function for getting the details of particular category (expense transactions)
    private int getBudgetSpend(int budgetCatId) {
        return database.expenseDetailDao().getExpenseCategorySum(budgetCatId);
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

}