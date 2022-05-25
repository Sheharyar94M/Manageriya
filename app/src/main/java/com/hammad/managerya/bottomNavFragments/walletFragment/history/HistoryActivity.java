package com.hammad.managerya.bottomNavFragments.walletFragment.history;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;
import com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragTransAdapter;
import com.hammad.managerya.bottomNavFragments.homeFragment.MonthAdapter;
import com.hammad.managerya.bottomNavFragments.homeFragment.ViewTransDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements MonthAdapter.OnMonthClickListener, HomeFragTransAdapter.RecentTransInterface, AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerViewMonth,recyclerViewTransaction;
    private TextView textViewCurrency,textViewAmount,textViewNoOfTrans;

    private List<String> monthsList=new ArrayList<>();
    private Spinner spinner;

    private Toolbar toolbar;

    //for Spinner
    String[] dateSortList={"Date (Ascending)","Date (Descending)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //initialize views
        initializeViews();

        //get the months list
        getMonthsList();

        //set months recyclerview
        setMonthRecyclerView();

        //set recent transaction recyclerview
        //setRecentTransRecyclerview();

        //setting the spinner
        setSpinner();

        //setting the toolbar as actionbar
        setSupportActionBar(toolbar);

        //setting the back button on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initializeViews()
    {
        //toolbar
        toolbar=findViewById(R.id.toolbar_history);

        recyclerViewMonth=findViewById(R.id.recycler_view_months_history);

        recyclerViewTransaction=findViewById(R.id.recycler_trans_history);

        textViewCurrency=findViewById(R.id.text_currency_history);

        //setting the currency
        textViewCurrency.setText(CURRENCY_);

        textViewAmount=findViewById(R.id.text_amount_history);
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

    private void setRecentTransRecyclerview()
    {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewTransaction.setLayoutManager(layoutManager);

        /*HomeFragTransAdapter recentTransAdapter=new HomeFragTransAdapter(this,this,3);
        recyclerViewTransaction.setAdapter(recentTransAdapter);*/
    }

    //recent transaction click listener
    @Override
    public void onRecentTransClick(int position) {

        Intent intent=new Intent(this, ViewTransDetailsActivity.class);
        //creating the bundle object
        Bundle bundle=new Bundle();
        bundle.putInt("position",position);
        intent.putExtras(bundle);
        startActivity(intent,bundle);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setSpinner()
    {
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
                Toast.makeText(this, dateSortList[0], Toast.LENGTH_SHORT).show();
                break;

            case 1:
                Toast.makeText(this, dateSortList[1], Toast.LENGTH_SHORT).show();
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
}