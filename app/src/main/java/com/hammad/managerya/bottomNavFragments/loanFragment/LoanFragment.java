package com.hammad.managerya.bottomNavFragments.loanFragment;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;
import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.loanFragment.DB.LoanEntity;
import com.hammad.managerya.bottomNavFragments.loanFragment.contact.ConsumerDetailActivity;
import com.hammad.managerya.bottomNavFragments.loanFragment.contact.addContact.AddConsumerActivity;

import java.util.ArrayList;
import java.util.List;

public class LoanFragment extends Fragment implements LoanAdapter.OnLoanInterface {

    public static final int REQUEST_CODE_READ_CONTACTS = 5;
    //variable for controlling the addedContactList refreshing
    private static int checkValue = 0;

    private Button buttonAddContact;
    private RecyclerView recyclerView;
    private TextView textViewCurrency1, textViewCurrency2, textViewTotalLend, textViewTotalBorrowed;
    private List<LoanEntity> addedContactList = new ArrayList<>();

    //room DB instance
    private RoomDBHelper database;

    //variables for saving total amount lend and borrowed
    int totalLend = 0, totalBorrowed = 0;

    //check variable 2 for handling total sum of lend and borrowed amounts
    private static int checkValueTotalSum = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_loan, container, false);

        //initialize Room DB
        database = RoomDBHelper.getInstance(requireContext());

        //initializing views
        initialViews(view);

        //retrieving added contact list
        addedContactList = database.loanDao().getAddedContactList();


        //setting the recyclerview
        setRecyclerView(addedContactList);

        //button add consumer click listener
        buttonAddContact.setOnClickListener(v -> checkContactPermission());

        //user defined function for calculating total lend and borrowed amounts
        setTotalLendBorrowedSum();

        return view;
    }

    private void initialViews(View view) {
        buttonAddContact = view.findViewById(R.id.button_add);
        recyclerView = view.findViewById(R.id.recycler_loan);

        textViewCurrency1 = view.findViewById(R.id.txt_currency_1_loan);
        textViewCurrency2 = view.findViewById(R.id.txt_currency_2_loan);

        textViewTotalLend = view.findViewById(R.id.txt_total_sent);
        textViewTotalBorrowed = view.findViewById(R.id.txt_total_borrowed);

        //setting the selected currency to textviews
        textViewCurrency1.setText(CURRENCY_);
        textViewCurrency2.setText(CURRENCY_);
    }

    private void setRecyclerView(List<LoanEntity> list) {

        //incrementing the check value each time this recyclerview function is called so that the updated list can be displayed
        checkValue++;

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        LoanAdapter loanAdapter = new LoanAdapter(requireContext(), list,this);
        recyclerView.setAdapter(loanAdapter);
    }

    private void checkContactPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //for fragment ActivityCompact is not used. Instead only requestPermissions is used
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        } else {
            addConsumer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_READ_CONTACTS && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //calling the addConsumer function
            addConsumer();
        } else {
            Toast.makeText(requireContext(), "Contacts Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void addConsumer() {
        startActivity(new Intent(requireContext(), AddConsumerActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
            when Loan fragment is called for first time the value is 1.
            when a contact is added, and AddConsumerActivity is finished,
            the onResume() of this (LoanFragment) is called
         */
        if(checkValue >= 1){
            //retrieving added contact list
            addedContactList = database.loanDao().getAddedContactList();

            //setting the recyclerview
            setRecyclerView(addedContactList);
        }

        if(checkValueTotalSum >= 1)
        {
            //user defined function for calculating total lend and borrowed amounts
            setTotalLendBorrowedSum();

            //setting the value to zero
            checkValueTotalSum = 0;
        }
    }

    //LoanAdapter click listener
    @Override
    public void onLoanClick(int position) {
        //incrementing the check value
        checkValueTotalSum++;

        LoanEntity item = addedContactList.get(position);

        Intent intent=new Intent(requireContext(), ConsumerDetailActivity.class);
        intent.putExtra("conName",item.getContactName());
        intent.putExtra("conPhone",item.getPhoneNo());
        intent.putExtra("conLetters",item.getContactLetter());
        startActivity(intent);
    }

    private void setTotalLendBorrowedSum()
    {
        //getting sum of total lended amount
        totalLend = database.loanDao().getTotalAmountLend();

        //getting sum of total borrowed amount
        totalBorrowed = database.loanDao().getTotalAmountBorrowed();

        //setting the values of total lend & borrowed to views
        textViewTotalLend.setText(String.valueOf(totalLend));
        textViewTotalBorrowed.setText(String.valueOf(totalBorrowed));
    }
}