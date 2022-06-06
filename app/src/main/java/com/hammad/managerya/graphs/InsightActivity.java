package com.hammad.managerya.graphs;

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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hammad.managerya.R;
import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.homeFragment.MonthAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insight);

        //initialize views
        initializeViews();

        //initializing database instance
        database = RoomDBHelper.getInstance(this);

        totalIncome = database.incomeDetailDao().getTotalIncomeSum();
        totalExpense = database.expenseDetailDao().getTotalExpenseSum();

        //getting the months list
        getMonthsList();

        //setting the months recyclerview
        setMonthRecyclerView();

        //adding values in BarEntry ArrayList
        barEntries.add(new BarEntry(1,totalExpense));
        barEntries.add(new BarEntry(2,totalIncome));

        //adding the entries names
        xAxisName.add("Expense");
        xAxisName.add("Income");

        //setting the bar chart
        setBarchart(barChart,barEntries,xAxisName);

        //setting the income pie chart fragment as default
        replaceFragment(new IncomePieChartFragment());

        //button income click listener
        buttonIncome.setOnClickListener(v -> {

            //setting the background to white of button Expense
            buttonExpense.setBackgroundResource(R.drawable.edit_text_white_background);
            buttonExpense.setTextColor(getResources().getColor(R.color.colorPrimaryVariant));

            //setting the colors & background to button Income
            buttonIncome.setBackgroundResource(R.drawable.button_finish_background_circular);
            buttonIncome.setTextColor(getResources().getColor(R.color.white));

            //setting the income pie chart fragment
            replaceFragment(new IncomePieChartFragment());
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
            replaceFragment(new ExpensePieChartFragment());
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
        Toast.makeText(this, monthName, Toast.LENGTH_SHORT).show();
    }

    //setting the horizontal bar chart
    private void setBarChart(){

        float barChartWidth = 9f;
        float spaceForBar = 10f;
        ArrayList<BarEntry> barEntries=new ArrayList<>();

        for(int i=0; i < 2; i++)
        {
            float val = (float) Math.random();
            barEntries.add(new BarEntry(i*spaceForBar,val));
        }


        /*barEntries.add(new BarEntry(0,2300));
        barEntries.add(new BarEntry(1,5000));*/

        BarDataSet barDataSet=new BarDataSet(barEntries,"");

        BarData data = new BarData(barDataSet);
        data.setValueTextSize(10f);
        data.setBarWidth(barChartWidth);

        barDataSet.setHighlightEnabled(false);

        barChart.setData(data);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);


        /*Legend l = mHorizontalBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);*/

    }

    private void setBarchart(BarChart barChart, ArrayList<BarEntry> arrayList, final ArrayList<String> xAxisValues) {
       /* barChart.setDrawBarShadow(false);
        barChart.setFitBars(false);*/
        barChart.setDrawValueAboveBar(false);
        barChart.setMaxVisibleValueCount(25);
        barChart.setPinchZoom(false);


        BarDataSet barDataSet = new BarDataSet(arrayList, "Values");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        barData.setValueTextSize(0f);

        barChart.setBackgroundColor(Color.TRANSPARENT);
        barChart.setDrawGridBackground(false);
        barChart.getDescription().setEnabled(false);

        Legend l = barChart.getLegend();
        //l.setTextSize(10f);
        //l.setFormSize(10f);
        //To set components of x axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextSize(11f);
        //xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setLabelCount(0);
        //xAxis.setValueFormatter(barChart.getDefaultValueFormatter());
        //xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        xAxis.setDrawGridLines(false);

        barChart.setData(barData);

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_charts,fragment);
        fragmentTransaction.commit();
    }
}