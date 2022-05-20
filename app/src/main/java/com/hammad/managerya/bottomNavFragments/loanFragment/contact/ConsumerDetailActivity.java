package com.hammad.managerya.bottomNavFragments.loanFragment.contact;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.AddLoanTransactionActivity;
import com.hammad.managerya.bottomNavFragments.loanFragment.contact.addContact.ConsumerTransactionAdapter;
import com.hammad.managerya.R;

import java.util.ArrayList;
import java.util.List;

public class ConsumerDetailActivity extends AppCompatActivity {

    private String contactName,contactPhoneNo,contactLetters;

    private ImageView imageViewBack;
    private TextView textViewContactLetter,textViewContactName;
    private TextView textViewCurrency,textViewAmount,textViewLatestTransType;
    private RecyclerView recyclerViewTransactions;
    private AppCompatButton buttonLend,buttonBorrowed;

    private ConsumerTransactionAdapter consumerTransAdapter;
    List<String> stringList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_detail);

        //initializing views
        initializeViews();

        //getting intent data from ContactAdapter
        getIntentData();

        //recyclerview
        setRecyclerView();

        //buttons click listeners
        buttonsClickListener();
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

    private void setRecyclerView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewTransactions.setLayoutManager(layoutManager);

        //scroll recyclerview to last entered item position
        int newPosition=stringList.size() - 1;
        recyclerViewTransactions.scrollToPosition(newPosition);

        consumerTransAdapter=new ConsumerTransactionAdapter(this,stringList);
        recyclerViewTransactions.setAdapter(consumerTransAdapter);
    }

    // scroll the recyclerview to newly entered position
    private void adapterPosition() {
        int newPosition=stringList.size() - 1;
        consumerTransAdapter.notifyItemInserted(newPosition);
        recyclerViewTransactions.scrollToPosition(newPosition);
    }

    private void amountLend() {
        /*stringList.add("Lend");
        adapterPosition();*/
        Intent intent=new Intent(this, AddLoanTransactionActivity.class);
        intent.putExtra("type","lend");
        startActivity(intent);

    }

    private void amountBorrowed() {
        /*stringList.add("Borrow");
        adapterPosition();*/
        Intent intent=new Intent(this, AddLoanTransactionActivity.class);
        intent.putExtra("type","borrow");
        startActivity(intent);
    }

}