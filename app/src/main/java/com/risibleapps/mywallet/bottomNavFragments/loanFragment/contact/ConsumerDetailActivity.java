package com.risibleapps.mywallet.bottomNavFragments.loanFragment.contact;

import static com.risibleapps.mywallet.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.loanFragment.DB.LoanDetailEntity;
import com.risibleapps.mywallet.bottomNavFragments.loanFragment.contact.addContact.ConsumerTransactionAdapter;
import com.risibleapps.mywallet.R;

import java.util.ArrayList;
import java.util.List;

public class ConsumerDetailActivity extends AppCompatActivity implements ConsumerTransactionAdapter.OnConsumerTransactionListener {

    private static String contactName,contactPhoneNo,contactLetters;

    private ImageView imageViewBack;
    private TextView textViewContactLetter,textViewContactName;
    private TextView textViewCurrency,textViewAmount,textViewLatestTransType;
    private RecyclerView recyclerViewTransactions;
    private AppCompatButton buttonLend,buttonBorrowed;
    private ConstraintLayout constraintLayoutTransDetail;

    private ConsumerTransactionAdapter consumerTransAdapter;
    List<LoanDetailEntity> loanDetailList =new ArrayList<>();

    //database instance
    private RoomDBHelper database;

    //static variable for handling the condition of onResume()
    private static int checkValue = 0;

    //variables for storing the values of lended and borrowed amounts sum (for individual user sums)
    private int lendedAmountSum = 0, borrowedAmountSum=0 ;

    private TextView textViewNoTrans;
    private TextView textViewDate,textViewLend,textViewBorrowed;

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

        //no transaction
        textViewNoTrans=findViewById(R.id.no_trans_loan);

        //the views above recyclerview (date, lend,borrowed)
        textViewDate=findViewById(R.id.text_date);
        textViewLend=findViewById(R.id.text_lend);
        textViewBorrowed=findViewById(R.id.text_borrow);

        buttonLend=findViewById(R.id.button_lend);
        buttonBorrowed=findViewById(R.id.button_borrow);

        constraintLayoutTransDetail=findViewById(R.id.constraint_2_loan);

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

        if(list.size() == 0)
        {
            textViewNoTrans.setVisibility(View.VISIBLE);
            textViewDate.setVisibility(View.INVISIBLE);
            textViewLend.setVisibility(View.INVISIBLE);
            textViewBorrowed.setVisibility(View.INVISIBLE);
        }
        else if(list.size() > 0)
        {
            textViewNoTrans.setVisibility(View.GONE);
            textViewDate.setVisibility(View.VISIBLE);
            textViewLend.setVisibility(View.VISIBLE);
            textViewBorrowed.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewTransactions.setLayoutManager(layoutManager);

        //scroll recyclerview to last entered item position
        int newPosition= list.size() - 1;
        recyclerViewTransactions.scrollToPosition(newPosition);

        consumerTransAdapter=new ConsumerTransactionAdapter(this, list,this);
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
    private void currentBalanceStatus() {

        lendedAmountSum = database.loanDao().getLendedAmountSum(contactPhoneNo);
        borrowedAmountSum = database.loanDao().getBorrowedAmountSum(contactPhoneNo);

        //if loan detail list size is zero, then set the following details to currentBalanceStatus
        if(loanDetailList.size() == 0)
        {
            //setting the loan detail layout visibility to gone
            constraintLayoutTransDetail.setVisibility(View.GONE);
        }
        else if(loanDetailList.size() > 0)
        {
            //setting the loan detail layout visibility to visible
            constraintLayoutTransDetail.setVisibility(View.VISIBLE);
        }

        if(lendedAmountSum > borrowedAmountSum)
        {
            textViewAmount.setText(String.valueOf(lendedAmountSum - borrowedAmountSum));
            textViewLatestTransType.setText("Amount Lended");

            //setting the visibility to VISIBLE
            textViewLatestTransType.setVisibility(View.VISIBLE);

            //setting the color green
            textViewAmount.setTextColor(getResources().getColor(R.color.colorGreen));
            textViewLatestTransType.setTextColor(getResources().getColor(R.color.colorGreen));
            textViewCurrency.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        else if(lendedAmountSum < borrowedAmountSum)
        {
            textViewAmount.setText(String.valueOf(borrowedAmountSum - lendedAmountSum));
            textViewLatestTransType.setText("Amount Borrowed");

            //setting the visibility to VISIBLE
            textViewLatestTransType.setVisibility(View.VISIBLE);

            //setting the color red
            textViewAmount.setTextColor(getResources().getColor(R.color.colorRed));
            textViewLatestTransType.setTextColor(getResources().getColor(R.color.colorRed));
            textViewCurrency.setTextColor(getResources().getColor(R.color.colorRed));
        }
    }

    //ConsumerTransactionAdapter listener long click
    @Override
    public void onConsumerTransLongClick() {

        //getting the latest values

        //getting the loan detail list searched by phone number
        loanDetailList = database.loanDao().getLoanTransList(contactPhoneNo);

        //recyclerview
        setRecyclerView(loanDetailList);

        //setting the variable values to zero
        lendedAmountSum = 0;
        borrowedAmountSum=0 ;

        //getting the current difference of amount lended and borrowed for this particular consumer
        currentBalanceStatus();

    }

}