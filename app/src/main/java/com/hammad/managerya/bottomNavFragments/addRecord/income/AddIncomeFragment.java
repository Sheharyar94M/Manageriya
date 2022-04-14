package com.hammad.managerya.bottomNavFragments.addRecord.income;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hammad.managerya.R;

import java.util.ArrayList;
import java.util.List;

public class AddIncomeFragment extends Fragment {

    private EditText editTextEnterAmount;
    private RecyclerView recyclerView;
    private ImageView imageViewExpand;
    private AppCompatButton buttonDetails,buttonFinish;

    //condition for handling click listener of expansion layout
    private int clickValue=0;

    private int[] eightImagesList={ R.drawable.allowance,R.drawable.bonus,R.drawable.business_profit,R.drawable.commission,
                             R.drawable.freelance,R.drawable.gifts_received,R.drawable.investment,R.drawable.loan_recived };

    private int[] thirteenImagesList={ R.drawable.allowance,R.drawable.bonus,R.drawable.business_profit,R.drawable.commission,
                                R.drawable.freelance,R.drawable.gifts_received,R.drawable.investment,R.drawable.loan_recived,
                                R.drawable.pension,R.drawable.pocket_money,R.drawable.salary,R.drawable.savings,
                                R.drawable.tuition };


    private String[] eightCatNameList={"Allowance","Bonus","Business\nProfit","Commission",
            "Freelance","Gifts\nReceived","Investment","Loan\nReceived"};

    private String[] thirteenCatNameList={"Allowance","Bonus","Business\nProfit","Commission",
            "Freelance","Gifts\nReceived","Investment","Loan\nReceived",
    "Pension","Pocket\nMoney","Salary","Savings","Tuition"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_add_income, container, false);

        //initialize views
        initializeViews(view);

        //setting the recycler view
        setRecyclerView(eightImagesList,eightCatNameList);

        //image view more click listener
        imageViewExpand.setOnClickListener(v -> {

            if(clickValue == 0)
            {
                showMore();
            }

            else if(clickValue == 1)
            {
                showLess();
            }
        });

        //button details click listener
        buttonDetails.setOnClickListener(v -> Toast.makeText(requireContext(), "Details", Toast.LENGTH_SHORT).show());

        //button finish click listener
        buttonFinish.setOnClickListener(v -> Toast.makeText(requireContext(), "Finish", Toast.LENGTH_SHORT).show());

        return view;
    }

    private void initializeViews(View view)
    {
        editTextEnterAmount=view.findViewById(R.id.edittext_add_income);

        recyclerView=view.findViewById(R.id.recycler_add_income);

        imageViewExpand=view.findViewById(R.id.img_expand_income);

        buttonDetails=view.findViewById(R.id.btn_detail_income);

        buttonFinish=view.findViewById(R.id.btn_finish_income);

        //bring to front
        editTextEnterAmount.bringToFront();
    }

    private void setRecyclerView(int numArray[], String[] catName)
    {
        GridLayoutManager layoutManager =new GridLayoutManager(requireContext(),4);
        recyclerView.setLayoutManager(layoutManager);

        AddIncomeAdapter incomeAdapter=new AddIncomeAdapter(requireContext(),numArray,catName);
        recyclerView.setAdapter(incomeAdapter);
    }

    private void showMore()
    {
        setRecyclerView(thirteenImagesList,thirteenCatNameList);

        imageViewExpand.setImageResource(R.drawable.ic_arrow_up);

        clickValue=1;
    }

    private void showLess()
    {
        setRecyclerView(eightImagesList,eightCatNameList );

        imageViewExpand.setImageResource(R.drawable.ic_arrow_down);

        clickValue=0;
    }

}