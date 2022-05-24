package com.hammad.managerya.bottomNavFragments.loanFragment;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hammad.managerya.R;
import com.hammad.managerya.bottomNavFragments.loanFragment.contact.addContact.AddConsumerActivity;

public class LoanFragment extends Fragment {

    public static final int REQUEST_CODE_READ_CONTACTS = 5;
    private Button buttonAddContact;
    private RecyclerView recyclerView;
    private TextView textViewCurrency1,textViewCurrency2,textViewTotalSent,textViewTotalBorrowed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_loan, container, false);

       //initializing views
        initialViews(view);

        //setting the recyclerview
        setRecyclerView();

        //button add consumer click listener
        buttonAddContact.setOnClickListener(v ->
                checkContactPermission()
        );

        return view;
    }

    private void initialViews(View view)
    {
        buttonAddContact =view.findViewById(R.id.button_add);
        recyclerView=view.findViewById(R.id.recycler_loan);

        textViewCurrency1=view.findViewById(R.id.txt_currency_1_loan);
        textViewCurrency2=view.findViewById(R.id.txt_currency_2_loan);

        textViewTotalSent=view.findViewById(R.id.txt_total_sent);
        textViewTotalBorrowed=view.findViewById(R.id.txt_total_borrowed);

        //setting the selected currency to textviews
        textViewCurrency1.setText(CURRENCY_);
        textViewCurrency2.setText(CURRENCY_);
    }

    private void setRecyclerView() {

        LinearLayoutManager layoutManager=new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        LoanAdapter loanAdapter=new LoanAdapter(requireContext());
        recyclerView.setAdapter(loanAdapter);
    }

    private void checkContactPermission()
    {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            //for fragment ActivityCompact is not used. Instead only requestPermissions is used
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }
        else
        {
            addConsumer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE_READ_CONTACTS && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            //calling the addConsumer function
            addConsumer();
        }
        else {
            Toast.makeText(requireContext(), "Contacts Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void addConsumer()
    {
        startActivity(new Intent(requireContext(), AddConsumerActivity.class));
        getActivity().finish();
    }
}