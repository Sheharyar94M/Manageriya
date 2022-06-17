package com.risibleapps.mywallet.graphs;

import static com.risibleapps.mywallet.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.android.material.snackbar.Snackbar;
import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.homeFragment.homeDB.ExpenseCatDetailModel;

import java.util.ArrayList;
import java.util.List;

public class ExpensePieChartFragment extends Fragment {

    private PieChart pieChartExpense;

    private String spend = "\nSpend";
    private int expenseAmount = 0;
    private RoomDBHelper database;

    //list to be passed to pie chart (income category details)
    private List<ExpenseCatDetailModel> pieChartDataList = new ArrayList<>();

    //array list for pie chart entries
    private ArrayList<PieEntry> pieEntries = new ArrayList<>();

    //string for saving the currently selected month value
    private String currentMonth="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_expense_pie_chart, container, false);

        //clearing the array list everytime the onCreateView is called so that no previous data is present in list
        pieChartDataList.clear();
        pieEntries.clear();

        //initialize pie chart
        pieChartExpense=view.findViewById(R.id.pie_chart_expense);

        //getting the bundle value from InsightActivity
        Bundle bundle=getArguments();
        currentMonth = bundle.getString("month");

        //database instance initialization
        database = RoomDBHelper.getInstance(requireContext());

        expenseAmount = database.expenseDetailDao().getTotalExpenseSum(currentMonth);

        //getting the list of income details
        pieChartDataList = database.expenseDetailDao().getExpenseCategoriesSum(currentMonth);

        //setting the pie chart
        setupPieChart();

        loadPieChartData();

        return view;
    }

    private void setupPieChart() {

        //the centre of pie chart
        pieChartExpense.setDrawHoleEnabled(true);
        pieChartExpense.setHoleColor(Color.parseColor("#FFFFFF"));
        pieChartExpense.setUsePercentValues(true);

        pieChartExpense.setCenterText(CURRENCY_.concat(String.valueOf(expenseAmount)).concat(spend));
        pieChartExpense.setCenterTextColor(getContext().getResources().getColor(R.color.colorPrimaryVariant));
        pieChartExpense.setCenterTextSize(13);
        pieChartExpense.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        //sets the entry label text to false (not text is shown with pie chart slices)
        pieChartExpense.setDrawEntryLabels(false);
        pieChartExpense.setEntryLabelTextSize(12);
        pieChartExpense.setEntryLabelColor(Color.WHITE);

        pieChartExpense.getDescription().setEnabled(false);

        Legend legend = pieChartExpense.getLegend();
        legend.setTextSize(16);

        //these lines set the description line of pie chart
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        //hides/show the description line (like color red = grocery, color green = health)
        legend.setEnabled(true);

        //set the vertical & horizontal spacing between pie chart descriptions
        legend.setXEntrySpace(12);
        legend.setYEntrySpace(12);

        //wrap the description list according to current view
        legend.setWordWrapEnabled(true);

        final Snackbar[] snackBar = new Snackbar[1];

        pieChartExpense.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                //for saving position of item clicked
                ExpenseCatDetailModel position = pieChartDataList.get((int) h.getX());

                String catName = position.getExpCatName();

                if(catName.contains("\n"))
                {
                    catName=catName.replace("\n","");
                }

                snackBar[0] = Snackbar.make(pieChartExpense, "Category Name: "+catName+"\nCategory Amount= "+CURRENCY_+(int) position.getExpCatAmount(), Snackbar.LENGTH_LONG);
                snackBar[0].setBackgroundTint(getContext().getResources().getColor(R.color.colorPrimaryVariant))
                        .setTextColor(Color.WHITE)
                        .show();
            }

            @Override
            public void onNothingSelected() {
                snackBar[0].dismiss();
            }
        });
    }

    private void loadPieChartData() {

        //setting the data to pie chart
        for (int i = 0; i < pieChartDataList.size(); i++) {
            pieEntries.add(new PieEntry(pieChartDataList.get(i).getExpCatAmount() / expenseAmount, pieChartDataList.get(i).getExpCatName()));
        }

        ArrayList<Integer> colors = new ArrayList<>();

        for (int color : ColorTemplate.PASTEL_COLORS) {
            colors.add(color);
        }

        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, " ");
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);

        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(pieChartExpense));
        pieData.setValueTextSize(9f);
        pieData.setValueTextColor(Color.WHITE);

        pieChartExpense.setData(pieData);
        pieChartExpense.invalidate();
    }
}