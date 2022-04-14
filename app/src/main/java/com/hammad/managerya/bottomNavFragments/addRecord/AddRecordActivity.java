package com.hammad.managerya.bottomNavFragments.addRecord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hammad.managerya.R;
import com.hammad.managerya.bottomNavFragments.addRecord.expense.AddExpenseFragment;
import com.hammad.managerya.bottomNavFragments.addRecord.income.AddIncomeFragment;
import com.hammad.managerya.bottomNavFragments.addRecord.transfer.AddTransferFragment;

public class AddRecordActivity extends AppCompatActivity {

    ImageView imageViewFinishActivity;
    ConstraintLayout constraintIncome,constraintTransfer,constraintExpense;
    FrameLayout frameLayout;
    ImageView imageViewIncome,imageViewExpense,imageViewTransfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        //flag for full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //in case of rounded corners, the white edges are removed by this
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //initializing views
        initializeViews();

        //this function will display add income fragment as default fragment in frame layout
        replaceFragment(new AddIncomeFragment());

        //finish activity button click listener
        imageViewFinishActivity.setOnClickListener(view -> finish());

        //constraint income click listener
        constraintIncome.setOnClickListener(view -> {

            //changing the background of income
            imageViewIncome.setImageResource(R.drawable.add_income_50_black);
            imageViewIncome.setBackgroundResource(R.drawable.white_circle_background);

            //changing the background of expense
            imageViewExpense.setImageResource(R.drawable.add_expense_50_white);
            imageViewExpense.setBackgroundResource(R.drawable.blue_circle_background);

            //changing the background of transfer
            imageViewTransfer.setImageResource(R.drawable.transfer_50_white);
            imageViewTransfer.setBackgroundResource(R.drawable.blue_circle_background);

            replaceFragment(new AddIncomeFragment());
        });

        //constraint expense click listener
        constraintExpense.setOnClickListener(view -> {

            //changing the background of income
            imageViewIncome.setImageResource(R.drawable.add_income_50_white);
            imageViewIncome.setBackgroundResource(R.drawable.blue_circle_background);

            //changing the background of expense
            imageViewExpense.setImageResource(R.drawable.add_expense_50_black);
            imageViewExpense.setBackgroundResource(R.drawable.white_circle_background);

            //changing the background of transfer
            imageViewTransfer.setImageResource(R.drawable.transfer_50_white);
            imageViewTransfer.setBackgroundResource(R.drawable.blue_circle_background);

            replaceFragment(new AddExpenseFragment());
        });

        //constraint transfer click listener
        constraintTransfer.setOnClickListener(view -> {

            //changing the background of income
            imageViewIncome.setImageResource(R.drawable.add_income_50_white);
            imageViewIncome.setBackgroundResource(R.drawable.blue_circle_background);

            //changing the background of expense
            imageViewExpense.setImageResource(R.drawable.add_expense_50_white);
            imageViewExpense.setBackgroundResource(R.drawable.blue_circle_background);

            //changing the background of transfer
            imageViewTransfer.setImageResource(R.drawable.transfer_50_black);
            imageViewTransfer.setBackgroundResource(R.drawable.white_circle_background);

            replaceFragment(new AddTransferFragment());
        });
    }

    private void initializeViews() {

        //finish activity button
        imageViewFinishActivity=findViewById(R.id.img_view_1_add_record);

        constraintIncome=findViewById(R.id.constraint_add_income);
        constraintExpense=findViewById(R.id.constraint_add_expense);
        constraintTransfer=findViewById(R.id.constraint_transfer);

        //image views of above constraints
        imageViewIncome=findViewById(R.id.img_view_add_income);
        imageViewExpense=findViewById(R.id.img_view_add_expense);
        imageViewTransfer=findViewById(R.id.img_view_transfer);

        frameLayout=findViewById(R.id.frame_layout_add_record);
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_add_record,fragment);
        fragmentTransaction.commit();
    }
}