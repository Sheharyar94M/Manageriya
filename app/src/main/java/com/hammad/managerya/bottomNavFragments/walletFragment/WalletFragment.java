package com.hammad.managerya.bottomNavFragments.walletFragment;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;
import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragTransAdapter;
import com.hammad.managerya.bottomNavFragments.homeFragment.MonthAdapter;
import com.hammad.managerya.bottomNavFragments.homeFragment.ViewTransDetailsActivity;
import com.hammad.managerya.bottomNavFragments.homeFragment.homeDB.HomeRecentTransModel;
import com.hammad.managerya.bottomNavFragments.walletFragment.budget.BudgetActivity;
import com.hammad.managerya.bottomNavFragments.walletFragment.history.HistoryActivity;
import com.hammad.managerya.graphs.InsightActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WalletFragment extends Fragment implements MonthAdapter.OnMonthClickListener,HomeFragTransAdapter.RecentTransInterface {

    private RecyclerView recyclerViewMonth;
    private ImageView imageViewInsights;
    private TextView textViewEarning,textViewBudgetCurrency,textViewSpend,textViewSpendCurrency,textViewPercentage;

    //months string list
    private List<String> monthsList = new ArrayList<>();

    private RecyclerView recyclerViewRecentTransaction;
    private TextView textViewCurrentMonth;

    private ImageView imageViewHistory,imageViewBudget;

    //recent transaction list
    List<HomeRecentTransModel> recentTranslList = new ArrayList<>();

    //database
    RoomDBHelper database;

    //variables for calculating the current earning,spending and percentage
    private float earning=0,amountSpend=0;
    private int percentage=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_wallet, container, false);

        //initialize views
        initialViews(view);

        //initializing database instance
        database = RoomDBHelper.getInstance(requireContext());

        //getting the list of all recent transaction (income & expense)
        recentTranslList = database.homeFragmentDao().getAllTransactions();

        //sum of total income
        earning = database.incomeDetailDao().getTotalIncomeSum();
        //sum of total expense
        amountSpend = database.expenseDetailDao().getTotalExpenseSum();

        //get the months list
        getMonthsList();

        //getting the details of expenditure
        getExpenditureDetails();

        //setting the month recyclerview
        setMonthRecyclerView();

        //recent transaction recyclerview
        setRecentTransactionRecyclerview();

        //history click listener
        imageViewHistory.setOnClickListener(v-> startActivity(new Intent(requireContext(), HistoryActivity.class)));

        //budget click listener
        imageViewBudget.setOnClickListener(v -> startActivity(new Intent(requireContext(), BudgetActivity.class)));

        //insight (graphs) activity
        imageViewInsights.setOnClickListener(v -> startActivity(new Intent(requireContext(), InsightActivity.class)));

        return view;
    }

    private void initialViews(View view) {
        recyclerViewMonth =view.findViewById(R.id.recycler_view_months_wallet);
        imageViewInsights=view.findViewById(R.id.image_view_insights_wallet);

        //textviews
        textViewEarning =view.findViewById(R.id.text_budget_wallet);
        textViewBudgetCurrency=view.findViewById(R.id.currency_1_wallet);

        textViewSpend=view.findViewById(R.id.text_spend_wallet);
        textViewSpendCurrency=view.findViewById(R.id.currency_2_wallet);

        textViewPercentage=view.findViewById(R.id.text_view_percentage_wallet);

        textViewCurrentMonth=view.findViewById(R.id.txt_month_wallet);

        //setting the currency of budget & spend
        textViewBudgetCurrency.setText(CURRENCY_);
        textViewSpendCurrency.setText(CURRENCY_);

        //recent transaction recyclerview
        recyclerViewRecentTransaction=view.findViewById(R.id.recycler_recent_trans_wallet);

        //imageview History & Budget
        imageViewHistory=view.findViewById(R.id.img_history_wallet);
        imageViewBudget=view.findViewById(R.id.img_budget_wallet);

        //setting the current month to textview
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        String currentDate = dateFormat.format(calendar.getTime());

        textViewCurrentMonth.setText(currentDate);
    }

    private void getMonthsList() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy");

        /*
            This returns the last 10 months from current month
            if current month is Apr 2022, then is will return values from Mar 2022 to Jun 2021
        */
        for (int i = 10; i >= 1; i--) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.add(Calendar.MONTH, -i);

            //adding the months in string list
            monthsList.add(dateFormat.format(calendar1.getTime()));

        }

        //returns the current month
        Calendar calendar2 = Calendar.getInstance();
        monthsList.add(dateFormat.format(calendar2.getTime()));

        //returns the next month (if current month is Apr 2022, it will return May 2022)
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.MONTH, +1);

        //adding in the list
        monthsList.add(dateFormat.format(calendar3.getTime()));

    }

    private void setMonthRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMonth.setLayoutManager(layoutManager);

        MonthAdapter monthAdapter = new MonthAdapter(requireActivity(), monthsList,this);
        recyclerViewMonth.setAdapter(monthAdapter);

        //scroll recyclerview to last month (current month = last month -1)
        int newPosition = monthsList.size() - 1;
        recyclerViewMonth.scrollToPosition(newPosition);
    }

    //month adapter click listener
    @Override
    public void onMonthSelected(String monthName) {

        //setting the current month to textview
        textViewCurrentMonth.setText(monthName);
    }

    private void setRecentTransactionRecyclerview(){

        LinearLayoutManager layoutManager=new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewRecentTransaction.setLayoutManager(layoutManager);

        HomeFragTransAdapter recentTransAdapter=new HomeFragTransAdapter(requireContext(),this,recentTranslList);
        recyclerViewRecentTransaction.setAdapter(recentTransAdapter);
    }

    //recent transaction adapter click listener
    @Override
    public void onRecentTransClick(int position) {
        HomeRecentTransModel item = recentTranslList.get(position);

        Intent intent = new Intent(requireContext(), ViewTransDetailsActivity.class);
        intent.putExtra("type",item.getTransType());
        intent.putExtra("catName", item.getCatName());
        intent.putExtra("amount", String.valueOf(item.getTransAmount()));
        intent.putExtra("date", getConvertedDate(item.getTransDate()));
        intent.putExtra("desc", item.getTransDesc());
        intent.putExtra("tag", item.getTransTag());
        intent.putExtra("loc", item.getTransLocation());
        intent.putExtra("img", item.getTransImagePath());

        startActivity(intent);
    }

    private void getExpenditureDetails() {

        percentage = (int) ((amountSpend / earning) * 100);

        //setting the values to text views
        textViewEarning.setText(String.valueOf((int) earning));
        textViewSpend.setText(String.valueOf((int) amountSpend));
        textViewPercentage.setText(String.valueOf(percentage));
        textViewPercentage.append(" %");
    }

    /*
       This function is used to convert date from 'yyyy-MM-dd HH:mm:ss' to 'MMM dd, yyyy hh:mm aaa' format
       2022-05-27 11:05:32 to May 27, 2022 11:05 am
   */
    private String getConvertedDate(String databaseDate) {
        String convertedDate = "";

        //database date format
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //converting date format to another
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM dd, yyyy hh:mm aaa");
        try {
            Date date = dateFormat1.parse(databaseDate);
            convertedDate = dateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate;
    }
}