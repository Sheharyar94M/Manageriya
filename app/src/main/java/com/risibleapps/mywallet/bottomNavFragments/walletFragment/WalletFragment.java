package com.risibleapps.mywallet.bottomNavFragments.walletFragment;

import static com.risibleapps.mywallet.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;
import static com.risibleapps.mywallet.mainActivity.HomeScreenActivity.isHomeFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.risibleapps.mywallet.BuildConfig;
import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.homeFragment.HomeFragTransAdapter;
import com.risibleapps.mywallet.bottomNavFragments.homeFragment.MonthAdapter;
import com.risibleapps.mywallet.bottomNavFragments.homeFragment.ViewTransDetailsActivity;
import com.risibleapps.mywallet.bottomNavFragments.homeFragment.homeDB.HomeRecentTransModel;
import com.risibleapps.mywallet.bottomNavFragments.walletFragment.budget.BudgetActivity;
import com.risibleapps.mywallet.bottomNavFragments.walletFragment.history.HistoryActivity;
import com.risibleapps.mywallet.graphs.InsightActivity;

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

    private ConstraintLayout layoutHistory,layoutBudget;

    //recent transaction list
    List<HomeRecentTransModel> recentTranslList = new ArrayList<>();

    //database
    RoomDBHelper database;

    //variables for calculating the current earning,spending and percentage
    private float earning=0,amountSpend=0;
    private int percentage=0;

    /*
        Variable for storing the value if any record is deleted in HistoryActivity.
        If record is deleted in HistoryActivity, the logic in handled in onResume() of this fragment
    */
    public static int CHECK_VALUE = 0;

    //string for saving current month and year
    private String currentMonth="";

    private InterstitialAd mInterstitialAd;

    private TextView textViewNoRecentTrans;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing mobile ad
        MobileAds.initialize(requireContext(), initializationStatus -> {});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_wallet, container, false);

        //initialize views
        initialViews(view);

        //getting the current date
        getCurrentDate();

        //loading the ad
        loadAd();

        //initializing database instance
        database = RoomDBHelper.getInstance(requireContext());

        //getting the list of all recent transaction (income & expense)
        recentTranslList = database.homeFragmentDao().getAllTransactions(monthDateConversion(currentMonth));

        //sum of total income
        earning = database.incomeDetailDao().getTotalIncomeSum(monthDateConversion(currentMonth));
        //sum of total expense
        amountSpend = database.expenseDetailDao().getTotalExpenseSum(monthDateConversion(currentMonth));

        //get the months list
        getMonthsList();

        //getting the details of expenditure
        getExpenditureDetails();

        //setting the month recyclerview
        setMonthRecyclerView();

        //recent transaction recyclerview
        setRecentTransactionRecyclerview();

        //history click listener
        layoutHistory.setOnClickListener(v-> startActivity(new Intent(requireContext(), HistoryActivity.class)));

        //budget click listener
        layoutBudget.setOnClickListener(v -> startActivity(new Intent(requireContext(), BudgetActivity.class)));

        //insight (graphs) activity
        imageViewInsights.setOnClickListener(v -> showAd());

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

        //no recent trans
        textViewNoRecentTrans=view.findViewById(R.id.no_recent_trans_wallet);

        //layout History & Budget
        layoutHistory=view.findViewById(R.id.constraint_history);
        layoutBudget=view.findViewById(R.id.constraint_budget);

        //setting the current month to textview
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        String currentDate = dateFormat.format(calendar.getTime());

        textViewCurrentMonth.setText(currentDate);
    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat1 =new SimpleDateFormat("MMM yyyy");
        currentMonth = dateFormat1.format(calendar.getTime());

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

        //setting the current month to variable
        currentMonth = monthName;

        //getting the latest values
        getUpdatedData();
    }

    private void setRecentTransactionRecyclerview(){

        if(recentTranslList.size() == 0)
        {
            textViewNoRecentTrans.setVisibility(View.VISIBLE);
            recyclerViewRecentTransaction.setVisibility(View.INVISIBLE);
        }
        else if(recentTranslList.size() > 0)
        {
            textViewNoRecentTrans.setVisibility(View.GONE);
            recyclerViewRecentTransaction.setVisibility(View.VISIBLE);
        }

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

    //overridden long click listener of HomeFragTransAdapter
    @Override
    public void onRecentTransLongClick() {

        //getting the latest changes when a record is deleted
        getUpdatedData();
    }

    private void getExpenditureDetails() {

        percentage = (int) ((amountSpend / earning) * 100);

        //setting the values to text views
        textViewEarning.setText(String.valueOf((int) earning));
        textViewSpend.setText(String.valueOf((int) amountSpend));

        //checking if earning is greater than expense or not
        if (amountSpend > earning)
        {
            textViewPercentage.setVisibility(View.GONE);
        }
        else if (amountSpend < earning)
        {
            textViewPercentage.setVisibility(View.VISIBLE);

            textViewPercentage.setText(String.valueOf(percentage));
            textViewPercentage.append(" %");
        }
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

    //user-defined function for fetching the latest data in case any record is deleted
    private void getUpdatedData(){

        //getting the list of all recent transaction (income & expense)
        recentTranslList = database.homeFragmentDao().getAllTransactions(monthDateConversion(currentMonth));

        //sum of total income
        earning = database.incomeDetailDao().getTotalIncomeSum(monthDateConversion(currentMonth));
        //sum of total expense
        amountSpend = database.expenseDetailDao().getTotalExpenseSum(monthDateConversion(currentMonth));

        //getting the details of expenditure
        getExpenditureDetails();

        //recent transaction recyclerview
        setRecentTransactionRecyclerview();

    }

    @Override
    public void onResume() {
        super.onResume();

        if(CHECK_VALUE >= 1) {

            //if value is >=1, this means any record is deleted. So, we will fetch the new changes (Changes made from HistoryActivity)
            getUpdatedData();

            //setting the value to zero again
            CHECK_VALUE = 0;
        }

        //boolean variable for handling the exit dialog in HomeScreenActivity
        isHomeFragment = false;
    }

    private String monthDateConversion(String dateToConvert) {
        String convertedDate="";

        //current format
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MMM yyyy");

        //converting date to another format

        SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy-MM");
        try {
            Date date=dateFormat1.parse(dateToConvert);
            convertedDate = dateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate;
    }

    public void loadAd() {

        //checking whether app is running on release/debug version
        String interstitialAdId="";
        if(BuildConfig.DEBUG)
        {
            interstitialAdId=getString(R.string.interstitial_ad_debug);
            Log.i("WALLET_FRAG_AD", "WALLET_FRAG debug version: "+interstitialAdId);
        }
        else {
            interstitialAdId=getString(R.string.interstitial_ad_release);
            Log.i("WALLET_FRAG_AD", "WALLET_FRAG release version: "+interstitialAdId);
        }

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(requireContext(), interstitialAdId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                Log.i("WALLET_FRAG_AD", "WALLET_FRAG onAdLoaded: ");
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.i("WALLET_FRAG_AD", "WALLET_FRAG failed ad: "+loadAdError.getCode()+"\n"+loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });
    }

    public void showAd() {
        //checking if ad is loaded or not
        if (mInterstitialAd != null) {
            mInterstitialAd.show(requireActivity());

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    mInterstitialAd = null;

                    //moving to Insight Screen
                    startActivity(new Intent(requireContext(), InsightActivity.class));
                }

                //this function make sure no ad is loaded for second time
                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();

                    mInterstitialAd = null;
                }
            });
        }
        else
        {
            //if there is no internet connection, then no ad will be loaded and app will move onto Insight Activity
            Intent intent = new Intent(requireContext(), InsightActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mInterstitialAd = null;
    }

    @Override
    public void onDestroy() {
        mInterstitialAd = null;
        super.onDestroy();
    }
}