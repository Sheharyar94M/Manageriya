package com.hammad.managerya.bottomNavFragments.savingFragment;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.hammad.managerya.R;

public class SavingFragment extends Fragment implements SavingsAdapter.OnSavingClickListener {

    private TextView textViewCurrency1,textViewCurrency2;
    private TextView textViewTotalSaved,textViewTotalSavingGoal,textViewRemainingSavingAmount;
    private LinearProgressIndicator progressBar;
    private TextView createSavingTransaction;
    private RecyclerView recyclerViewSavings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_saving, container, false);

        //initialize views
        initializeView(view);

        //create saving transaction click listener
        createSavingTransaction.setOnClickListener(v -> Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show());

        //setting the recyclerview
        setupRecyclerview();

        return view;
    }

    private void initializeView(View view)
    {
        textViewCurrency1=view.findViewById(R.id.txt_currency);
        textViewCurrency2=view.findViewById(R.id.txt_currency_2);

        //setting the currency to the textviews
        textViewCurrency1.setText(CURRENCY_);
        textViewCurrency1.setText(CURRENCY_);

        textViewTotalSaved=view.findViewById(R.id.txt_total_saved);
        textViewTotalSavingGoal=view.findViewById(R.id.txt_total_saving_goal);

        textViewRemainingSavingAmount=view.findViewById(R.id.txt_remaining_saving_amount);

        progressBar=view.findViewById(R.id.progress_savings);

        createSavingTransaction =view.findViewById(R.id.txt_create_saving);

        recyclerViewSavings=view.findViewById(R.id.recycler_savings);
    }

    private void setupRecyclerview()
    {
        LinearLayoutManager layoutManager=new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewSavings.setLayoutManager(layoutManager);

        SavingsAdapter savingsAdapter=new SavingsAdapter(requireContext(),this);
        recyclerViewSavings.setAdapter(savingsAdapter);
    }

    //Savings Adapter click listener
    @Override
    public void onSavingItemClicked(int position) {
        Toast.makeText(requireContext(), "Position: "+position, Toast.LENGTH_SHORT).show();
    }
}