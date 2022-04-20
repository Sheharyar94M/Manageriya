package com.hammad.managerya.bottomNavFragments.addRecord.expense;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;
import com.hammad.managerya.bottomNavFragments.addRecord.ActivityAddTransactionDetail;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddExpenseFragment extends Fragment implements AddExpenseAdapter.ExpenseInterfaceListener {

    private EditText editTextEnterAmount;
    private RecyclerView recyclerView;
    private ImageView imageViewExpand;
    private AppCompatButton buttonDetails, buttonFinish;

    //condition for handling click listener of expansion layout
    private int clickValue = 0;

    //this is used to check if any recyclerview item is selected or not
    private int recyclerItemPosition = -1;

    //this string stores the category name of recyclerview item
    private String categoryName="";

    //string to save the current date
    private String currentDate;

    private int eightImagesList[] = {R.drawable.bills, R.drawable.charity, R.drawable.committee, R.drawable.entertainment,
            R.drawable.electronics, R.drawable.education, R.drawable.family, R.drawable.food};

    private int twentyFourImagesList[] = {R.drawable.bills, R.drawable.charity, R.drawable.committee, R.drawable.entertainment,
            R.drawable.electronics, R.drawable.education, R.drawable.family, R.drawable.food,
            R.drawable.fuel, R.drawable.grocery, R.drawable.health, R.drawable.home_e,
            R.drawable.installment, R.drawable.insurance, R.drawable.loan_paid, R.drawable.medical,
            R.drawable.mobile, R.drawable.office, R.drawable.other_expenses, R.drawable.rent_paid,
            R.drawable.shopping, R.drawable.transport, R.drawable.travel, R.drawable.wedding};

    private String[] eightCatNameList = {"Bills &\nUtilities", "Charity", "Committee", "Entertain\nment",
            "Electronics", "Education", "Family", "Food"};

    private String[] twentyFourCatNameList = {"Bills &\nUtilities", "Charity", "Committee", "Entertain\nment",
            "Electronics", "Education", "Family", "Food"
            , "Fuel", "Grocery", "Health", "Home"
            , "Installment", "Insurance", "Loan\nPaid", "Medical"
            , "Mobile", "Office", "Other\nExpenses", "Rent\nPaid"
            , "Shopping", "Transport", "Travel", "Wedding"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        //initialize views
        initializeView(view);

        //setting the recycler view
        setRecyclerView(eightImagesList, eightCatNameList);

        //image view more click listener
        imageViewExpand.setOnClickListener(v -> {

            if (clickValue == 0)
            {
                showMore();

                //setting the selected recyclerview item position to null
                recyclerItemPosition = -1;

                //setting the selected category name to empty
                categoryName="";
            }
            else if (clickValue == 1)
            {
                showLess();

                //setting the selected recyclerview item position to null
                recyclerItemPosition = -1;

                //setting the selected category name to empty
                categoryName="";
            }
        });

        //get the current date
        getCurrentDate();

        //button details click listener
        buttonDetails.setOnClickListener(v -> buttonDetailsClickListener());

        //button finish click listener
        buttonFinish.setOnClickListener(v -> Toast.makeText(requireContext(), "Finish", Toast.LENGTH_SHORT).show());

        return view;
    }

    private void initializeView(View view) {
        editTextEnterAmount = view.findViewById(R.id.edittext_add_expense);
        //setting the hint as currency and zero
        editTextEnterAmount.setHint(CURRENCY + "0");

        recyclerView = view.findViewById(R.id.recycler_add_expense);

        imageViewExpand = view.findViewById(R.id.img_expand_expense);

        buttonDetails = view.findViewById(R.id.btn_detail_expense);

        buttonFinish = view.findViewById(R.id.btn_finish_expense);

        //bring to front
        editTextEnterAmount.bringToFront();
    }

    private void setRecyclerView(int numArray[], String[] catName) {
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 4);
        recyclerView.setLayoutManager(layoutManager);

        AddExpenseAdapter expenseAdapter = new AddExpenseAdapter(requireContext(), numArray, catName, this::onExpenseItemClicked);
        recyclerView.setAdapter(expenseAdapter);
    }

    private void showMore() {
        setRecyclerView(twentyFourImagesList, twentyFourCatNameList);

        imageViewExpand.setImageResource(R.drawable.ic_arrow_up);

        clickValue = 1;
    }

    private void showLess() {
        setRecyclerView(eightImagesList, eightCatNameList);

        imageViewExpand.setImageResource(R.drawable.ic_arrow_down);

        clickValue = 0;
    }

    private void buttonDetailsClickListener() {
        boolean boolEditText = true, boolRecyclerItem = true;

        if (editTextEnterAmount.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Enter amount", Toast.LENGTH_SHORT).show();
            boolEditText = false;
        }
        if (recyclerItemPosition < 0) {
            Toast.makeText(requireContext(), "select any Expense category", Toast.LENGTH_SHORT).show();
            boolRecyclerItem = false;
        }

        if (boolEditText && boolRecyclerItem) {
            boolEditText = false;
            boolRecyclerItem = false;

            Intent intent = new Intent(requireContext(), ActivityAddTransactionDetail.class);
            intent.putExtra("fragment", "expense");
            intent.putExtra("expenseAmount",editTextEnterAmount.getText().toString());
            intent.putExtra("expenseCat",categoryName);
            intent.putExtra("expenseDate",currentDate);
            startActivity(intent);
        }
    }

    @Override
    public void onExpenseItemClicked(int position,String catName) {
        recyclerItemPosition = position;
        categoryName=catName;
    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        currentDate= dateFormat.format(calendar.getTime());

    }
}