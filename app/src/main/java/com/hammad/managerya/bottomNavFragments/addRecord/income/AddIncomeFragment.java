package com.hammad.managerya.bottomNavFragments.addRecord.income;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

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

public class AddIncomeFragment extends Fragment implements AddIncomeAdapter.IncomeAdapterInterface {

    private EditText editTextEnterAmount;
    private RecyclerView recyclerView;
    private ImageView imageViewExpand;
    private AppCompatButton buttonDetails,buttonFinish;

    //condition for handling click listener of expansion layout
    private int clickValue=0;

    //this is used to check if any recyclerview item is selected or not
    private int recyclerItemPosition=-1;

    //this string stores the category name of recyclerview item
    private String categoryName="";

    //string to save the current date
    private String currentDate;

    //string to save the current dateTime
    private String currentDateTime;

    private int[] EIGHT_IMAGE_LIST_INCOME ={ R.drawable.allowance,R.drawable.bonus,R.drawable.business_profit,R.drawable.commission,
                             R.drawable.freelance,R.drawable.gifts_received,R.drawable.investment,R.drawable.loan_recived };

    private int[] THIRTEEN_IMAGE_LIST_INCOME ={ R.drawable.allowance,R.drawable.bonus,R.drawable.business_profit,R.drawable.commission,
                                R.drawable.freelance,R.drawable.gifts_received,R.drawable.investment,R.drawable.loan_recived,
                                R.drawable.pension,R.drawable.pocket_money,R.drawable.salary,R.drawable.savings,
                                R.drawable.tuition };


    private String[] EIGHT_CAT_LIST_INCOME ={"Allowance","Bonus","Business\nProfit","Commission",
            "Freelance","Gifts\nReceived","Investment","Loan\nReceived"};

    private String[] THIRTEEN_CAT_LIST_INCOME ={"Allowance","Bonus","Business\nProfit","Commission",
            "Freelance","Gifts\nReceived","Investment","Loan\nReceived",
    "Pension","Pocket\nMoney","Salary","Savings","Tuition"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_add_income, container, false);

        //initialize views
        initializeViews(view);

        //setting the recycler view
        setRecyclerView(EIGHT_IMAGE_LIST_INCOME, EIGHT_CAT_LIST_INCOME);

        //image view more click listener
        imageViewExpand.setOnClickListener(v -> {

            if(clickValue == 0)
            {
                showMore();

                //setting the selected recyclerview item position to null
                recyclerItemPosition = -1;

                //setting the selected category name to empty
                categoryName="";
            }
            else if(clickValue == 1)
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
        buttonDetails.setOnClickListener(v ->  buttonDetailsClickListener());

        //button finish click listener
        buttonFinish.setOnClickListener(v -> Toast.makeText(requireContext(), "Finish", Toast.LENGTH_SHORT).show());

        return view;
    }

    private void initializeViews(View view) {
        editTextEnterAmount=view.findViewById(R.id.edittext_add_income);
        //setting the hint as currency and zero
        editTextEnterAmount.setHint(CURRENCY_ +"0");

        recyclerView=view.findViewById(R.id.recycler_add_income);

        imageViewExpand=view.findViewById(R.id.img_expand_income);

        buttonDetails=view.findViewById(R.id.btn_detail_income);

        buttonFinish=view.findViewById(R.id.btn_finish_income);

        //bring to front
        editTextEnterAmount.bringToFront();
    }

    private void setRecyclerView(int numArray[], String[] catName) {
        GridLayoutManager layoutManager =new GridLayoutManager(requireContext(),4);
        recyclerView.setLayoutManager(layoutManager);

        AddIncomeAdapter incomeAdapter=new AddIncomeAdapter(requireContext(),numArray,catName,this::onIncomeItemClicked);
        recyclerView.setAdapter(incomeAdapter);
    }

    private void buttonDetailsClickListener() {
        boolean boolEditText=true,boolRecyclerItem=true;

        if (editTextEnterAmount.getText().toString().isEmpty())
        {
            Toast.makeText(requireContext(), "Enter Amount", Toast.LENGTH_SHORT).show();
            boolEditText=false;
        }
        else if(recyclerItemPosition < 0)
        {
            Toast.makeText(requireContext(), "select Income Category", Toast.LENGTH_SHORT).show();
            boolRecyclerItem=false;
        }

        else if(boolEditText && boolRecyclerItem)
        {
            boolEditText=false;
            boolRecyclerItem=false;

            Intent intent=new Intent(requireContext(), ActivityAddTransactionDetail.class);
            intent.putExtra("fragment","income");
            intent.putExtra("incomeAmount",editTextEnterAmount.getText().toString());
            intent.putExtra("incomeCat",categoryName);
            intent.putExtra("incomeDate",currentDate);
            intent.putExtra("incomeDateTime",currentDateTime);
            startActivity(intent);
            getActivity().finish();
        }

    }

    private void showMore()
    {
        setRecyclerView(THIRTEEN_IMAGE_LIST_INCOME, THIRTEEN_CAT_LIST_INCOME);

        imageViewExpand.setImageResource(R.drawable.ic_arrow_up);

        clickValue=1;
    }

    private void showLess()
    {
        setRecyclerView(EIGHT_IMAGE_LIST_INCOME, EIGHT_CAT_LIST_INCOME);

        imageViewExpand.setImageResource(R.drawable.ic_arrow_down);

        clickValue=0;
    }

    @Override
    public void onIncomeItemClicked(int position, String catName) {
        recyclerItemPosition=position;
        categoryName=catName;
    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        currentDate= dateFormat.format(calendar.getTime());

        SimpleDateFormat dateTimeFormat=new SimpleDateFormat("MMMM dd, yyyy hh:mm aaa");
        currentDateTime=dateTimeFormat.format(calendar.getTime());

    }
}