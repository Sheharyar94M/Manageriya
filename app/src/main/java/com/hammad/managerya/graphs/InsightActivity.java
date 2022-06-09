package com.hammad.managerya.graphs;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;
import com.hammad.managerya.R;
import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.homeFragment.MonthAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InsightActivity extends AppCompatActivity implements MonthAdapter.OnMonthClickListener {

    private HorizontalBarChart barChart;
    private RecyclerView recyclerViewMonth;
    private AppCompatButton buttonIncome,buttonExpense;

    private RoomDBHelper database;

    //for months recyclerview list
    private List<String> monthsList = new ArrayList<>();

    //for storing bar entries
    private ArrayList<BarEntry> barEntries=new ArrayList<>();

    //for storing bar entries names
    private ArrayList<String> xAxisName = new ArrayList<>();

    //variables for saving income and expense sum
    private int totalIncome = 0,totalExpense = 0;

    //string for saving current month and year
    private String currentMonth="";

    //bundle for moving the month name to Income & Expense pie chart fragment
    Bundle bundle;

    IncomePieChartFragment incomePieChartFragment;
    ExpensePieChartFragment expensePieChartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insight);

        //initialize views
        initializeViews();

        //getting the current date
        getCurrentDate();

        //initializing database instance
        database = RoomDBHelper.getInstance(this);

        totalIncome = database.incomeDetailDao().getTotalIncomeSum(monthDateConversion(currentMonth));
        totalExpense = database.expenseDetailDao().getTotalExpenseSum(monthDateConversion(currentMonth));

        //getting the months list
        getMonthsList();

        //setting the months recyclerview
        setMonthRecyclerView();

        //adding values in BarEntry ArrayList
        barEntries.add(new BarEntry(1f,totalExpense));
        barEntries.add(new BarEntry(2f,0));
        barEntries.add(new BarEntry(3f,0));
        barEntries.add(new BarEntry(4f,totalIncome));

        //adding the entries names
        xAxisName.add("Expense");
        xAxisName.add("Income");

        //setting the bar chart
        setBarchart(barChart,barEntries,xAxisName);

        //initializing the bundle instance
        bundle = new Bundle();
        bundle.putString("month",monthDateConversion(currentMonth));

        //default fragment (IncomePieChartFragment)
        incomePieChartFragment=new IncomePieChartFragment();
        incomePieChartFragment.setArguments(bundle);

        //expense pie chart
        expensePieChartFragment =new ExpensePieChartFragment();
        expensePieChartFragment.setArguments(bundle);

        //setting the income pie chart fragment as default
        replaceFragment(incomePieChartFragment);

        //button income click listener
        buttonIncome.setOnClickListener(v -> {

            //setting the background to white of button Expense
            buttonExpense.setBackgroundResource(R.drawable.edit_text_white_background);
            buttonExpense.setTextColor(getResources().getColor(R.color.colorPrimaryVariant));

            //setting the colors & background to button Income
            buttonIncome.setBackgroundResource(R.drawable.button_finish_background_circular);
            buttonIncome.setTextColor(getResources().getColor(R.color.white));

            //setting the income pie chart fragment
            replaceFragment(incomePieChartFragment);
        });

        //button expense click listener
        buttonExpense.setOnClickListener(v -> {

            //setting the background to white of button Income
            buttonIncome.setBackgroundResource(R.drawable.edit_text_white_background);
            buttonIncome.setTextColor(getResources().getColor(R.color.colorPrimaryVariant));

            //setting the colors & background to button Expense
            buttonExpense.setBackgroundResource(R.drawable.button_finish_background_circular);
            buttonExpense.setTextColor(getResources().getColor(R.color.white));

            //setting the expense pie chart fragment
            replaceFragment(expensePieChartFragment);
        });
    }

    private void initializeViews() {

        barChart = findViewById(R.id.horizontal_bar_chart);
        recyclerViewMonth = findViewById(R.id.recycler_month_insight);

        buttonIncome = findViewById(R.id.button_income);
        buttonExpense =findViewById(R.id.button_expense);
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMonth.setLayoutManager(layoutManager);

        MonthAdapter monthAdapter = new MonthAdapter(this, monthsList, this);
        recyclerViewMonth.setAdapter(monthAdapter);

        //scroll recyclerview to last month (current month = last month -1)
        int newPosition = monthsList.size() - 1;
        recyclerViewMonth.scrollToPosition(newPosition);
    }

    //months recyclerview click listener
    @Override
    public void onMonthSelected(String monthName) {

        //setting the selected month value to current month
        currentMonth = monthName;

        //fetching the updated details

        //setting the newly selected month value to bundle
        bundle.putString("month",monthDateConversion(currentMonth));

        //clearing the list before adding new values
        barEntries.clear();
        xAxisName.clear();

        totalIncome = database.incomeDetailDao().getTotalIncomeSum(monthDateConversion(currentMonth));
        totalExpense = database.expenseDetailDao().getTotalExpenseSum(monthDateConversion(currentMonth));

        //adding values in BarEntry ArrayList
        barEntries.add(new BarEntry(1f,totalExpense));
        barEntries.add(new BarEntry(2f,0));
        barEntries.add(new BarEntry(3f,0));
        barEntries.add(new BarEntry(4f,totalIncome));

        //adding the entries names
        xAxisName.add("Expense");
        xAxisName.add("Income");

        //setting the bar chart
        setBarchart(barChart,barEntries,xAxisName);

        //setting the currently selected month value to Income and Expense pie chart fragments
        incomePieChartFragment.setArguments(bundle);
        expensePieChartFragment.setArguments(bundle);

        //replacing the fragment
        replaceFragment(incomePieChartFragment);

    }

    //setting the horizontal bar chart
    private void setBarchart(BarChart barChart, ArrayList<BarEntry> arrayList, final ArrayList<String> xAxisValues) {
        barChart.setDrawValueAboveBar(false);
        barChart.setMaxVisibleValueCount(25);
        barChart.setPinchZoom(false);

        BarDataSet barDataSet = new BarDataSet(arrayList, "");
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(2f);
        barData.setValueTextSize(0f);

        barChart.setBackgroundColor(Color.TRANSPARENT);
        barChart.setDrawGridBackground(false);
        barChart.getDescription().setEnabled(false);


        XAxis xAxis = barChart.getXAxis();
        //xAxis.setTextSize(11f);
        //xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setLabelCount(0);
        //xAxis.setValueFormatter(barChart.getDefaultValueFormatter());
        //xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        xAxis.setDrawGridLines(false);

        barChart.setData(barData);

        final Snackbar[] snackBar = new Snackbar[1];

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int value = 0;
                int value1 = 0;

                if((int) h.getX() == 1)
                {
                    value = 0;
                    value1 = 0;
                }
                else if((int) h.getX() == 4)
                {
                    value = 1;
                    value1 = 3;
                }

                snackBar[0] = Snackbar.make(barChart,"Category: "+xAxisValues.get(value)+"\nAmount= "+CURRENCY_+(int) barEntries.get(value1).getY() , Snackbar.LENGTH_LONG);
                snackBar[0].setBackgroundTint(getResources().getColor(R.color.colorPrimaryVariant))
                        .setTextColor(Color.WHITE)
                        .show();
            }

            @Override
            public void onNothingSelected() {
                snackBar[0].dismiss();
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_charts,fragment);
        fragmentTransaction.commit();
    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat1 =new SimpleDateFormat("MMM yyyy");
        currentMonth = dateFormat1.format(calendar.getTime());
    }

    //for converting month format (from May 2022 to 2022-05)
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
}