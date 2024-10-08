package com.risibleapps.mywallet.bottomNavFragments.homeFragment;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;
import static com.risibleapps.mywallet.mainActivity.HomeScreenActivity.isHomeFragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.risibleapps.mywallet.BuildConfig;
import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.homeFragment.homeDB.ExpenseCatDetailModel;
import com.risibleapps.mywallet.bottomNavFragments.homeFragment.homeDB.HomeRecentTransModel;
import com.risibleapps.mywallet.bottomNavFragments.walletFragment.budget.RoomDB.BudgetDetailsModel;
import com.risibleapps.mywallet.graphs.InsightActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements HomeFragTransAdapter.RecentTransInterface, MonthAdapter.OnMonthClickListener {

    public static final int[] COLOR_PALETTE_1 = {
            rgb("#ffa226"), rgb("#7fff23"), rgb("#ff257b"),
            rgb("#FF4000"), rgb("#aaaaaa"), rgb("#ffd921"),
            rgb("#80FF00"), rgb("#006666"), rgb("#FD6302"),
            rgb("#BFFF00"), rgb("#ff0044"), rgb("#00FFBF"),
            rgb("#21c1ff"), rgb("#256fff"), rgb("#483728"),
            rgb("#6c4d4b"), rgb("#DF013A"), rgb("#F61104"),
            rgb("#3020ff"), rgb("#2EFE9A"), rgb("#7800bd"),
            rgb("#F78181"), rgb("#AE04FD"), rgb("#FB2C08")

    };

    public static final int[] COLOR_PALETTE_2 = {
            rgb("#21c1ff"), rgb("#256fff"), rgb("#00000"),
            rgb("#6c4d4b"), rgb("#DF013A"), rgb("#F61104"),
            rgb("#3020ff"), rgb("#2EFE9A"), rgb("#7800bd"),
            rgb("#F78181"), rgb("#AE04FD"), rgb("#FB2C08"),
            rgb("#006666")
    };

    //string for inner circle of pie graph
    public static String CURRENCY_ = "$ ";

    String spend = "\nSpend";

    //array list for pie chart entries
    ArrayList<PieEntry> pieEntries = new ArrayList<>();

    //recent transaction list
    List<HomeRecentTransModel> recentTranslList = new ArrayList<>();

    //database
    RoomDBHelper database;

    //list to be passed to pie chart
    List<ExpenseCatDetailModel> pieChartDataList = new ArrayList<>();

    private TextView textViewCurrentDate;
    private TextView textViewTotalIncome, textViewSpend, textViewPercentage;
    private TextView textViewCurrencyIncome, textViewCurrencyExpense;
    private RecyclerView recyclerViewRecentTransaction, recyclerViewMonth;
    private PieChart pieChart;
    private RecyclerView recyclerViewRecentBudget;
    private ImageView imageViewInsights;

    private List<String> monthsList = new ArrayList<>();

    //variables for calculating the current earning,spending and percentage
    private float earning = 0, amountSpend = 0;
    private int percentage = 0;

    //budget list
    private List<BudgetDetailsModel> addedBudgetList=new ArrayList<>();

    //string for saving current month and year
    private String currentMonth="";

    private InterstitialAd mInterstitialAd;

    private ImageView placeHolder1, placeHolder2;
    private TextView textViewNoBudget,textViewNoRecentTrans;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing mobile ad
        MobileAds.initialize(requireContext(), initializationStatus -> {});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*
            Clearing the list before adding new data.
            This is because when Home Fragment is called through back stack the onCreate of this fragment is not called.
            OnCreateView is called, due to which repetitive data is added in list
        */
        pieEntries.clear();
        recentTranslList.clear();
        pieChartDataList.clear();
        addedBudgetList.clear();

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //initializing views
        initializeViews(view);

        //get current date
        getCurrentDate();

        //loading the ad
        loadAd();

        //initializing database instance
        database = RoomDBHelper.getInstance(requireActivity());

        //getting the list of all recent transaction (income & expense)
        recentTranslList = database.homeFragmentDao().getAllTransactions(monthDateConversion(currentMonth));

        //getting the list of addedBudgetList
        addedBudgetList = database.budgetDao().getBudgetList(monthDateConversion(currentMonth));

        //sum of total income
        earning = database.incomeDetailDao().getTotalIncomeSum(monthDateConversion(currentMonth));
        //sum of total expense
        amountSpend = database.expenseDetailDao().getTotalExpenseSum(monthDateConversion(currentMonth));

        //getting the current expenditure
        getExpenditureDetails();

        //getting sum of expense categories (for pie chart data)
        getExpenseSumByCategory();

        //get the months list
        getMonthsList();

        //setting the recyclerview
        setRecentTransactionRecyclerView();

        //setting the month recyclerview
        setMonthRecyclerView();

        //setting the pie chart
        setupPieChart();
        loadPieChartData();

        //setting the recent recyclerview
        setBudgetRecentRecyclerview();

        //insight (graphs) activity
        imageViewInsights.setOnClickListener(v -> showAd());

        return view;
    }

    private void initializeViews(View view) {
        textViewCurrentDate = view.findViewById(R.id.text_view_current_date);
        imageViewInsights = view.findViewById(R.id.image_view_insights);

        //Recent Transaction recyclerview
        recyclerViewRecentTransaction = view.findViewById(R.id.recycler_home_frag);

        //Month recyclerview
        recyclerViewMonth = view.findViewById(R.id.recycler_view_months);

        textViewTotalIncome = view.findViewById(R.id.text_view_total_income);
        textViewSpend = view.findViewById(R.id.text_view_total_expense);
        textViewPercentage = view.findViewById(R.id.text_view_percentage);

        textViewCurrencyIncome = view.findViewById(R.id.text_view_currency_1);

        textViewCurrencyExpense = view.findViewById(R.id.text_view_currency_2);

        //setting the currency to relevant textviews
        textViewCurrencyIncome.setText(CURRENCY_);
        textViewCurrencyExpense.setText(CURRENCY_);

        pieChart = view.findViewById(R.id.pie_chart);

        //recyclerview recent
        recyclerViewRecentBudget = view.findViewById(R.id.recycler_view_recent);

        //place holders
        placeHolder1 = view.findViewById(R.id.chart_place_holder);
        //placeHolder2 = view.findViewById(R.id.place_holder_2);
        textViewNoBudget = view.findViewById(R.id.no_budget);
        textViewNoRecentTrans = view.findViewById(R.id.no_recent_trans);

    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String currentDate = dateFormat.format(calendar.getTime());

        SimpleDateFormat dateFormat1 =new SimpleDateFormat("MMM yyyy");
        currentMonth = dateFormat1.format(calendar.getTime());

        textViewCurrentDate.setText(currentDate);
    }

    private void setRecentTransactionRecyclerView() {

        if(recentTranslList.size() == 0)
        {
            recyclerViewRecentTransaction.setVisibility(View.INVISIBLE);
            textViewNoRecentTrans.setVisibility(View.VISIBLE);
        }
        else if(recentTranslList.size() > 0)
        {
            recyclerViewRecentTransaction.setVisibility(View.VISIBLE);
            textViewNoRecentTrans.setVisibility(View.INVISIBLE);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewRecentTransaction.setLayoutManager(layoutManager);

        HomeFragTransAdapter adapter = new HomeFragTransAdapter(requireActivity(), this, recentTranslList);
        recyclerViewRecentTransaction.setAdapter(adapter);
    }

    private void setMonthRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMonth.setLayoutManager(layoutManager);

        MonthAdapter monthAdapter = new MonthAdapter(requireActivity(), monthsList, this);
        recyclerViewMonth.setAdapter(monthAdapter);

        //scroll recyclerview to last month (current month = last month -1)
        int newPosition = monthsList.size() - 1;
        recyclerViewMonth.scrollToPosition(newPosition);
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

    private void loadPieChartData() {

        if(pieChartDataList.size() == 0)
        {
            pieChart.setVisibility(View.INVISIBLE);
            placeHolder1.setVisibility(View.VISIBLE);
        }
        else if(pieChartDataList.size() > 0)
        {
            pieChart.setVisibility(View.VISIBLE);
            placeHolder1.setVisibility(View.INVISIBLE);
        }

        //setting the data to pie chart
        for (int i = 0; i < pieChartDataList.size(); i++) {
            pieEntries.add(new PieEntry(pieChartDataList.get(i).getExpCatAmount() / amountSpend, pieChartDataList.get(i).getExpCatName()));
        }

        ArrayList<Integer> colors = new ArrayList<>();

        for (int color : ColorTemplate./*PASTEL_COLORS*/createColors(COLOR_PALETTE_1)) {
            colors.add(color);
        }

        /*for (int color : ColorTemplate.*//*MATERIAL_COLORS*//*createColors(COLOR_PALETTE_2)) {
            colors.add(color);
        }*/

        PieDataSet pieDataSet = new PieDataSet(pieEntries, " ");
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);

        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(9f);
        pieData.setValueTextColor(Color.WHITE);

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void setupPieChart() {

        //the centre of pie chart
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.parseColor("#06a6ff"));
        pieChart.setUsePercentValues(true);

        pieChart.setCenterText(CURRENCY_.concat(String.valueOf((int) amountSpend)).concat(spend));
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.setCenterTextSize(13);
        pieChart.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        //sets the entry label text to false (not text is shown with pie chart slices)
        pieChart.setDrawEntryLabels(false);
        /*pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.WHITE);*/

        pieChart.getDescription().setEnabled(false);

        Legend legend = pieChart.getLegend();

        //hides the description line (like color red = grocery, color green = health)
        legend.setEnabled(false);

        //these lines set the description line of pie chart
        /*legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(true);*/

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                //String for saving category name
                String catName = pieEntries.get((int) h.getX()).getLabel();

                //checking if category name contains '\n'
                if(catName.contains("\n"))
                {
                    catName = catName.replace("\n","");
                }

                //highlights the selected pie chart slice
                Toast.makeText(requireContext(), catName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private void setBudgetRecentRecyclerview() {

        if(addedBudgetList.size() == 0)
        {
            recyclerViewRecentBudget.setVisibility(View.INVISIBLE);
            //placeHolder2.setVisibility(View.VISIBLE);
            textViewNoBudget.setVisibility(View.VISIBLE);
        }
        else if(addedBudgetList.size() > 0)
        {
            recyclerViewRecentBudget.setVisibility(View.VISIBLE);
            //placeHolder2.setVisibility(View.INVISIBLE);
            textViewNoBudget.setVisibility(View.INVISIBLE);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRecentBudget.setLayoutManager(layoutManager);

        RecentAdapter recentAdapter = new RecentAdapter(requireContext(),monthDateConversion(currentMonth),addedBudgetList);
        recyclerViewRecentBudget.setAdapter(recentAdapter);
    }

    //overridden click listener of HomeFragTransAdapter
    @Override
    public void onRecentTransClick(int position) {

        HomeRecentTransModel item = recentTranslList.get(position);

        Intent intent = new Intent(requireContext(), ViewTransDetailsActivity.class);
        intent.putExtra("type", item.getTransType());
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

        //refreshing the fragment to get latest changes
        getActivity().finish();
        startActivity(getActivity().getIntent());
    }

    //months adapter click listener
    @Override
    public void onMonthSelected(String monthName) {

        currentMonth = monthName;

        //getting the latest data changes

        //clearing the list before new data insertion
        pieEntries.clear();
        recentTranslList.clear();
        pieChartDataList.clear();
        addedBudgetList.clear();

        //getting the list of all recent transaction (income & expense)
        recentTranslList = database.homeFragmentDao().getAllTransactions(monthDateConversion(currentMonth));

        //getting the list of addedBudgetList
        addedBudgetList = database.budgetDao().getBudgetList(monthDateConversion(currentMonth));

        //sum of total income
        earning = database.incomeDetailDao().getTotalIncomeSum(monthDateConversion(currentMonth));
        //sum of total expense
        amountSpend = database.expenseDetailDao().getTotalExpenseSum(monthDateConversion(currentMonth));

        //getting the current expenditure
        getExpenditureDetails();

        //getting sum of expense categories (for pie chart data)
        getExpenseSumByCategory();

        //setting the recyclerview
        setRecentTransactionRecyclerView();

        //setting the pie chart
        setupPieChart();
        loadPieChartData();

        //setting the recent recyclerview
        setBudgetRecentRecyclerview();

    }

    private void getExpenditureDetails() {

        percentage = (int) ((amountSpend / earning) * 100);

        //setting the values to text views
        textViewTotalIncome.setText(String.valueOf((int) earning));
        textViewSpend.setText(String.valueOf((int) amountSpend));

        //checking whether income is greater than expense or not
        if(amountSpend > earning)
        {
            textViewPercentage.setVisibility(View.GONE);
        }
        else if(amountSpend < earning)
        {
            textViewPercentage.setVisibility(View.VISIBLE);

            textViewPercentage.setText(String.valueOf(percentage));
            textViewPercentage.append(" %");
        }

    }

    private void getExpenseSumByCategory() {
        pieChartDataList = database.expenseDetailDao().getExpenseCategoriesSum(monthDateConversion(currentMonth));
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
            Log.i("HOME_FRAG_AD", "HOME_FRAG debug version: "+interstitialAdId);
        }
        else {
            interstitialAdId=getString(R.string.interstitial_ad_release);
            Log.i("HOME_FRAG_AD", "HOME_FRAG release version: "+interstitialAdId);
        }

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(requireContext(), interstitialAdId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                Log.i("HOME_FRAG_AD", "HOME_FRAG onAdLoaded: ");
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.i("HOME_FRAG_AD", "HOME_FRAG failed ad: "+loadAdError.getCode()+"\n"+loadAdError.getMessage());
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
    public void onResume() {
        super.onResume();

        //fetching the latest changes

        //clearing the previous values
        earning = 0;
        amountSpend = 0;
        percentage = 0;

        pieEntries.clear();
        recentTranslList.clear();
        pieChartDataList.clear();
        addedBudgetList.clear();

        pieChart.clear();

        //getting the list of all recent transaction (income & expense)
        recentTranslList = database.homeFragmentDao().getAllTransactions(monthDateConversion(currentMonth));

        //getting the list of addedBudgetList
        addedBudgetList = database.budgetDao().getBudgetList(monthDateConversion(currentMonth));

        //sum of total income
        earning = database.incomeDetailDao().getTotalIncomeSum(monthDateConversion(currentMonth));
        //sum of total expense
        amountSpend = database.expenseDetailDao().getTotalExpenseSum(monthDateConversion(currentMonth));

        //getting the current expenditure
        getExpenditureDetails();

        //getting sum of expense categories (for pie chart data)
        getExpenseSumByCategory();

        //setting the recyclerview
        setRecentTransactionRecyclerView();

        //setting the pie chart
        setupPieChart();
        loadPieChartData();

        //setting the recent recyclerview
        setBudgetRecentRecyclerview();

        //boolean variable for handling the exit dialog in HomeScreenActivity
        isHomeFragment = true;

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