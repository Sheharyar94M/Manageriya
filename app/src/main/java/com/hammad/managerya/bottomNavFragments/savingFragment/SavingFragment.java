package com.hammad.managerya.bottomNavFragments.savingFragment;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.hammad.managerya.R;
import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.savingFragment.DB.SavingEntity;
import com.hammad.managerya.bottomNavFragments.savingFragment.savingGoal.ActivityAddSavingGoal;
import com.hammad.managerya.bottomNavFragments.savingFragment.savingTransactionDetails.ActivitySavingTransactionDetail;

import java.util.ArrayList;
import java.util.List;

public class SavingFragment extends Fragment implements SavingsAdapter.OnSavingClickListener {

    private TextView textViewCurrency1, textViewCurrency2;
    private TextView textViewTotalSaved, textViewTotalSavingGoal, textViewRemainingSavingAmount;
    private LinearProgressIndicator progressBar;
    private TextView createSavingGoal;
    private RecyclerView recyclerViewSavings;

    //list of saving goals
    List<SavingEntity> savingGoalList = new ArrayList<>();

    //database instance
    RoomDBHelper database;

    //this is used to handle the if condition of onResume() when a new saving goal is added
    static int check = 0;

    //variables for saving total saving goal values
    int totalSavingGoalAmount =0, totalSavedAmount =0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saving, container, false);

        //initialize views
        initializeView(view);

        //database initialization
        database = RoomDBHelper.getInstance(requireContext());

        //getting the saving goal list
        savingGoalList = database.savingDao().getSavingGoalList();

        //create saving transaction click listener
        createSavingGoal.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ActivityAddSavingGoal.class));
        });

        //setting the recyclerview
        setupRecyclerview(savingGoalList);

        //setting the values to textviews
        setValuesToViews();

        return view;
    }

    private void initializeView(View view) {
        textViewCurrency1 = view.findViewById(R.id.txt_currency);
        textViewCurrency2 = view.findViewById(R.id.txt_currency_2);

        //setting the currency to the textviews
        textViewCurrency1.setText(CURRENCY_);
        textViewCurrency2.setText(CURRENCY_);

        textViewTotalSaved = view.findViewById(R.id.txt_total_saved);
        textViewTotalSavingGoal = view.findViewById(R.id.txt_total_saving_goal);

        textViewRemainingSavingAmount = view.findViewById(R.id.txt_remaining_saving_amount);

        progressBar = view.findViewById(R.id.progress_savings);

        createSavingGoal = view.findViewById(R.id.txt_create_saving_goal);

        recyclerViewSavings = view.findViewById(R.id.recycler_savings);
    }

    private void setupRecyclerview(List<SavingEntity> list) {
        //this static variable is incremented in recyclerview function to handle the if condition of onResume()
        check++;

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewSavings.setLayoutManager(layoutManager);

        SavingsAdapter savingsAdapter = new SavingsAdapter(requireContext(), list, this);
        recyclerViewSavings.setAdapter(savingsAdapter);
    }

    //Savings Adapter click listener
    @Override
    public void onSavingItemClicked(int position) {
        SavingEntity item= savingGoalList.get(position);

        Intent intent=new Intent(requireContext(), ActivitySavingTransactionDetail.class);
        intent.putExtra("id",item.getId());
        intent.putExtra("title",item.getSavingTitle());
        intent.putExtra("amount",item.getSavingGoalAmount());
        intent.putExtra("icon",item.getSavingIcon());
        intent.putExtra("date",item.getSavingTargetDate());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
            when saving fragment is called for first time the value is 1.
            when a new saving goal is added, and ActivityAddSavingGoal is finished,
            the onResume() of this (SavingFragment) is called
            */
        if(check >= 1)
        {
            //getting the saving goal list
            savingGoalList = database.savingDao().getSavingGoalList();

            //setting the latest list to DB
            setupRecyclerview(savingGoalList);

            //for the total savings record
            setValuesToViews();
        }

    }

    private void setValuesToViews(){

        //sum of total saving goals amount
        totalSavingGoalAmount = database.savingDao().getAllSavingGoalSum();

        //sum of all saving goals transactions
        totalSavedAmount = database.savingDao().getAllSavingTransactionSum();

        //setting values to views

        textViewTotalSavingGoal.setText(String.valueOf(totalSavingGoalAmount));

        textViewTotalSaved.setText(String.valueOf(totalSavedAmount));

        textViewRemainingSavingAmount.setText(String.valueOf(totalSavingGoalAmount - totalSavedAmount));

        //setting the progress bar values
        progressBar.setProgress(totalSavedAmount);
        progressBar.setMax(totalSavingGoalAmount);
    }
}