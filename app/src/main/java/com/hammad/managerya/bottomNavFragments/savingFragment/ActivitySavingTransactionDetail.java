package com.hammad.managerya.bottomNavFragments.savingFragment;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Intent;
import android.os.Bundle;
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

public class ActivitySavingTransactionDetail extends AppCompatActivity {

    private TextView textViewSavingGoalTitle,textViewDate,textViewCurrency,textViewAmountSaved,textViewSavingGoal,textViewPercentage;
    private ImageView imageViewCategory;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    private TextView textViewSavingTransaction;
    private RecyclerView recyclerViewSavingTransaction;

    private SavingRecentTransAdapter recentTransAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_transaction_detail);

        //initializing toolbar
        toolbar = findViewById(R.id.toolbar_saving_trans_detail);


        //setting the toolbar
        setToolbar();

        //initialize views
        initializeViews();

        //set the recent saving transaction recyclerview
        setRecyclerView();

        //create saving transaction
        textViewSavingTransaction.setOnClickListener(view -> {
            startActivity(new Intent(this,ActivityAddSavingTransaction.class));
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
}