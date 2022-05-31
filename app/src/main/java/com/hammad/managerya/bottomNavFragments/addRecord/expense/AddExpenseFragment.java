package com.hammad.managerya.bottomNavFragments.addRecord.expense;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.addRecord.ActivityAddTransactionDetail;
import com.hammad.managerya.bottomNavFragments.addRecord.expense.expenseDB.ExpenseCategoryEntity;
import com.hammad.managerya.bottomNavFragments.addRecord.expense.expenseDB.ExpenseDetailEntity;
import com.hammad.managerya.bottomNavFragments.addRecord.income.incomeDB.IncomeDetailEntity;
import com.hammad.managerya.mainActivity.HomeScreenActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    //this integer is used to store category id of recyclerview item
    private int categoryId=-1;

    //string to save the current date
    private String currentDate;

    //string to save current date time
    private String currentDateTime;

    //Database instance
    private RoomDBHelper database;

    //this income category list contain first 8 elements
    private List<ExpenseCategoryEntity> expenseCategoryList=new ArrayList<>();

    //this income category list contains all the elements
    private List<ExpenseCategoryEntity> expenseCategoryListFull=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        //initialize views
        initializeView(view);

        //initialize database helper class
        database=RoomDBHelper.getInstance(getContext());

        //getting the income categories list
        expenseCategoryListFull = database.expenseCategoryDao().getExpenseCategoryList();

        //this list contains the first 8 elements from complete list
        expenseCategoryList = expenseCategoryListFull.subList(0,8);

        //setting the recycler view
        setRecyclerView(expenseCategoryList);

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
        buttonFinish.setOnClickListener(v -> buttonFinishClickListener());

        return view;
    }

    private void initializeView(View view) {
        editTextEnterAmount = view.findViewById(R.id.edittext_add_expense);
        //setting the hint as currency and zero
        editTextEnterAmount.setHint(CURRENCY_ + "0");

        recyclerView = view.findViewById(R.id.recycler_add_expense);

        imageViewExpand = view.findViewById(R.id.img_expand_expense);

        buttonDetails = view.findViewById(R.id.btn_detail_expense);

        buttonFinish = view.findViewById(R.id.btn_finish_expense);

        //bring to front
        editTextEnterAmount.bringToFront();
    }

    private void setRecyclerView(List<ExpenseCategoryEntity> expenseCategoryList) {
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 4);
        recyclerView.setLayoutManager(layoutManager);

        AddExpenseAdapter expenseAdapter = new AddExpenseAdapter(requireContext(), expenseCategoryList, this::onExpenseItemClicked);
        recyclerView.setAdapter(expenseAdapter);
    }

    private void showMore() {
        setRecyclerView(expenseCategoryListFull);

        imageViewExpand.setImageResource(R.drawable.ic_arrow_up);

        clickValue = 1;
    }

    private void showLess() {
        setRecyclerView(expenseCategoryList);

        imageViewExpand.setImageResource(R.drawable.ic_arrow_down);

        clickValue = 0;
    }

    private void buttonDetailsClickListener() {
        boolean boolEditText = true, boolRecyclerItem = true;

        if (editTextEnterAmount.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Enter Amount", Toast.LENGTH_SHORT).show();
            boolEditText = false;
        }
        else if (recyclerItemPosition < 0) {
            Toast.makeText(requireContext(), "select Expense Category", Toast.LENGTH_SHORT).show();
            boolRecyclerItem = false;
        }

        else if (boolEditText && boolRecyclerItem) {
            boolEditText = false;
            boolRecyclerItem = false;

            Intent intent = new Intent(requireContext(), ActivityAddTransactionDetail.class);
            intent.putExtra("fragment", "expense");
            intent.putExtra("expenseAmount",editTextEnterAmount.getText().toString());
            intent.putExtra("expenseCat",categoryName);
            intent.putExtra("expenseDate",currentDate);
            intent.putExtra("expenseCatId",categoryId);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onExpenseItemClicked(int position,String catName) {
        recyclerItemPosition = position;
        categoryName = catName;
        categoryId = position + 1;
    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        currentDate= dateFormat.format(calendar.getTime());

        SimpleDateFormat dateTimeFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTime=dateTimeFormat.format(calendar.getTime());

    }

    private void buttonFinishClickListener(){
        boolean boolEditText=true,boolRecyclerItem=true;

        if (editTextEnterAmount.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Enter Amount", Toast.LENGTH_SHORT).show();
            boolEditText=false;
        }
        else if(recyclerItemPosition < 0) {
            Toast.makeText(requireContext(), "select Expense Category", Toast.LENGTH_SHORT).show();
            boolRecyclerItem=false;
        }
        else if(boolEditText && boolRecyclerItem) {
            boolEditText = false;
            boolRecyclerItem = false;

            //saving data in database
            database.expenseDetailDao().addExpenseDetail(new ExpenseDetailEntity(categoryId,Integer.valueOf(editTextEnterAmount.getText().toString()),currentDateTime,"","","",""));

            Toast.makeText(requireContext(), "Expense Added Successfully", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getActivity(), HomeScreenActivity.class));
                    getActivity().finish();
                }
            },1000);
        }
    }
}