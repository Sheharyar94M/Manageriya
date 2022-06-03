package com.hammad.managerya.bottomNavFragments.loanFragment.contact;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.loanFragment.DB.LoanDetailEntity;
import com.hammad.managerya.bottomNavFragments.loanFragment.contact.addContact.ConsumerTransactionAdapter;
import com.hammad.managerya.R;

import java.util.ArrayList;
import java.util.List;

public class ConsumerDetailActivity extends AppCompatActivity {

    private static String contactName,contactPhoneNo,contactLetters;

    private ImageView imageViewBack;
    private TextView textViewContactLetter,textViewContactName;
    private TextView textViewCurrency,textViewAmount,textViewLatestTransType;
    private RecyclerView recyclerViewTransactions;
    private AppCompatButton buttonLend,buttonBorrowed;

    private ConsumerTransactionAdapter consumerTransAdapter;
    List<LoanDetailEntity> loanDetailList =new ArrayList<>();

    //database instance
    private RoomDBHelper database;

    //static variable for handling the condition of onResume()
    private static int checkValue = 0;

    //variables for storing the values of lended and borrowed amounts sum (for individual user sums)
    private int lendedAmountSum = 0, borrowedAmountSum=0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_detail);

        //initializing database instance
        database = RoomDBHelper.getInstance(this);

        //initializing views
        initializeViews();

        //getting intent data from ContactAdapter
        getIntentData();

        //getting the loan detail list searched by phone number
        loanDetailList = database.loanDao().getLoanTransList(contactPhoneNo);

        //recyclerview
        setRecyclerView(loanDetailList);

        //buttons click listeners
        buttonsClickListener();

        //getting the current difference of amount lended and borrowed for this particular consumer
        currentBalanceStatus();
    }

    private void initializeViews() {

        //views on toolbar
        imageViewBack=findViewById(R.id.img_view_cus_toolbar);
        textViewContactLetter=findViewById(R.id.text_contact_letters);
        textViewContactName=findViewById(R.id.text_contact_name);

        //rest of views
        textViewCurrency=findViewById(R.id.text_currency_loan);
        textViewAmount=findViewById(R.id.text_rem_amount_loan);
        textViewLatestTransType=findViewById(R.id.text_amount);

        recyclerViewTransactions=findViewById(R.id.recycler_loan);

        buttonLend=findViewById(R.id.button_lend);
        buttonBorrowed=findViewById(R.id.button_borrow);

        //setting the currency
        textViewCurrency.setText(CURRENCY_);

    }

    private void getIntentData() {

        Intent intent=getIntent();

        contactName=intent.getStringExtra("conName");
        contactPhoneNo=intent.getStringExtra("conPhone");
        contactLetters=intent.getStringExtra("conLetters");

        //setting the details to textviews
        textViewContactLetter.setText(contactLetters);
        textViewContactName.setText(contactName);

    }

    private void buttonsClickListener() {

        //back button on toolbar
        imageViewBack.setOnClickListener(view -> finish());

        //textview contact name click listener (toolbar textview)
        textViewContactName.setOnClickListener(view -> contactDetailDialog());

        //button lend
        buttonLend.setOnClickListener(view -> amountLend());

        //button borrowed
        buttonBorrowed.setOnClickListener(view -> amountBorrowed());

    }

    private void contactDetailDialog() {
        //Alert Dialog views
        TextView textViewName,textViewPhoneNo;
        Button buttonOK;

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        LayoutInflater inflater=getLayoutInflater();

        View view=inflater.inflate(R.layout.layout_contact_detail_dialog,null);

        //initializing alert dialog views
        textViewName=view.findViewById(R.id.text_view_name);
        textViewPhoneNo=view.findViewById(R.id.text_view_phone_no);

        buttonOK=view.findViewById(R.id.btn_ok_dialog);

        builder.setView(view);

        AlertDialog dialog=builder.create();
        dialog.show();

        //Alert Dialog views
        textViewName.setText(contactName);
        textViewPhoneNo.setText(contactPhoneNo);

        //button OK click listener
        buttonOK.setOnClickListener(v -> dialog.dismiss());

    }

    private void setRecyclerView(List<LoanDetailEntity> list) {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewTransactions.setLayoutManager(layoutManager);

        //scroll recyclerview to last entered item position
        int newPosition= list.size() - 1;
        recyclerViewTransactions.scrollToPosition(newPosition);

        consumerTransAdapter=new ConsumerTransactionAdapter(this, list);
        recyclerViewTransactions.setAdapter(consumerTransAdapter);
    }

    private void amountLend() {
        //incrementing the value on button click
        checkValue++;

        Intent intent=new Intent(this, AddLoanTransactionActivity.class);
        intent.putExtra("type","Lend");
        intent.putExtra("phoneNo",contactPhoneNo);
        startActivity(intent);
    }

    private void amountBorrowed() {
        //incrementing the value on button click
        checkValue++;

        Intent intent=new Intent(this, AddLoanTransactionActivity.class);
        intent.putExtra("type","Borrow");
        intent.putExtra("phoneNo",contactPhoneNo);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(checkValue >= 1)
        {
            //getting the loan detail list searched by phone number
            loanDetailList = database.loanDao().getLoanTransList(contactPhoneNo);

            //calling the recyclerview
            setRecyclerView(loanDetailList);

            //getting the current difference of amount lended and borrowed for this particular consumer
            currentBalanceStatus();

            //setting the value to zero again so that it can only be called when transaction is added
            checkValue = 0;
        }
    }

    //function for calculating the difference between lended and borrowed amount
    private void currentBalanceStatus()
    {
        lendedAmountSum = database.loanDao().getLendedAmountSum(contactPhoneNo);
        borrowedAmountSum = database.loanDao().getBorrowedAmountSum(contactPhoneNo);

        if(lendedAmountSum > borrowedAmountSum)
        {
            textViewAmount.setText(String.valueOf(lendedAmountSum - borrowedAmountSum));
            textViewLatestTransType.setText("Amount Lended");

            //setting the color green
            textViewAmount.setTextColor(getResources().getColor(R.color.colorGreen));
            textViewLatestTransType.setTextColor(getResources().getColor(R.color.colorGreen));
            textViewCurrency.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        else if(lendedAmountSum < borrowedAmountSum)
        {
            textViewAmount.setText(String.valueOf(borrowedAmountSum - lendedAmountSum));
            textViewLatestTransType.setText("Amount Borrowed");

            //setting the color green
            textViewAmount.setTextColor(getResources().getColor(R.color.colorRed));
            textViewLatestTransType.setTextColor(getResources().getColor(R.color.colorRed));
            textViewCurrency.setTextColor(getResources().getColor(R.color.colorRed));
        }
    }
}