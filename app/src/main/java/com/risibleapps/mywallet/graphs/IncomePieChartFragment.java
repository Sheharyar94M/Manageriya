package com.risibleapps.mywallet.graphs;

import static com.risibleapps.mywallet.bottomNavFragments.homeFragment.HomeFragment.COLOR_PALETTE_2;
import static com.risibleapps.mywallet.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.google.android.material.snackbar.Snackbar;
import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.homeFragment.homeDB.ExpenseCatDetailModel;

import java.util.ArrayList;
import java.util.List;

public class IncomePieChartFragment extends Fragment {

    private PieChart pieChartIncome;

    private String earned = "\nEarned";
    private int incomeAmount = 0;
    private RoomDBHelper database;

    //list to be passed to pie chart (income category details)
    private List<ExpenseCatDetailModel> pieChartDataList = new ArrayList<>();

    //array list for pie chart entries
    private ArrayList<PieEntry> pieEntries = new ArrayList<>();

    //string for saving the currently selected month value
    private String currentMonth="";

    //recyclerview for displaying the category names and allocated color
    private RecyclerView recyclerViewColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_income_pie_chart, container, false);

        //clearing the array list everytime the onCreateView is called so that no previous data is present in list
        pieChartDataList.clear();
        pieEntries.clear();

        //initialize pie chart
        pieChartIncome=view.findViewById(R.id.pie_chart_income);

        //initializing recyclerview
        recyclerViewColor=view.findViewById(R.id.recycler_income);

        //getting the bundle value from InsightActivity
        Bundle bundle=getArguments();
        currentMonth = bundle.getString("month");

        //database instance initialization
        database = RoomDBHelper.getInstance(requireContext());

        incomeAmount = database.incomeDetailDao().getTotalIncomeSum(currentMonth);

        //getting the list of income details
        pieChartDataList = database.incomeDetailDao().getIncomeCategoriesSum(currentMonth);

        //setting the pie chart
        setupPieChart();

        loadPieChartData();

        //set recyclerview
        setRecyclerViewColor();

        return view;
    }

    private void setupPieChart() {

        //the centre of pie chart
        pieChartIncome.setDrawHoleEnabled(true);
        pieChartIncome.setHoleColor(Color.parseColor("#FFFFFF"));
        pieChartIncome.setUsePercentValues(true);

        pieChartIncome.setCenterText(CURRENCY_.concat(String.valueOf(incomeAmount)).concat(earned));
        pieChartIncome.setCenterTextColor(getContext().getResources().getColor(R.color.colorPrimaryVariant));
        pieChartIncome.setCenterTextSize(16);
        pieChartIncome.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        //sets the entry label text to false (not text is shown with pie chart slices)
        pieChartIncome.setDrawEntryLabels(false);
        pieChartIncome.setEntryLabelTextSize(12);
        pieChartIncome.setEntryLabelColor(Color.WHITE);

        pieChartIncome.getDescription().setEnabled(false);

        Legend legend = pieChartIncome.getLegend();
        legend.setTextSize(16);

        //these lines set the description line of pie chart
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        //hides/show the description line (like color red = grocery, color green = health)
        legend.setEnabled(false);

        //set the vertical & horizontal spacing between pie chart descriptions
        legend.setXEntrySpace(12);
        legend.setYEntrySpace(12);

        //wrap the description list according to current view
        legend.setWordWrapEnabled(true);

        final Snackbar[] snackBar = new Snackbar[1];

        pieChartIncome.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                //for saving position of item clicked
                ExpenseCatDetailModel position = pieChartDataList.get((int) h.getX());

                String catName = position.getExpCatName();

                if(catName.contains("\n"))
                {
                    catName=catName.replace("\n","");
                }

                snackBar[0] = Snackbar.make(pieChartIncome, "Category Name: "+catName+"\nCategory Amount= "+CURRENCY_+(int) position.getExpCatAmount(), Snackbar.LENGTH_LONG);
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
            pieEntries.add(new PieEntry(pieChartDataList.get(i).getExpCatAmount() / incomeAmount, pieChartDataList.get(i).getExpCatName()));
        }

        ArrayList<Integer> colors = new ArrayList<>();

        for (int color : ColorTemplate./*PASTEL_COLORS*/createColors(COLOR_PALETTE_2)) {
            colors.add(color);
        }

        /*for (int color : ColorTemplate.*//*MATERIAL_COLORS*//*createColors(COLOR_PALETTE_1)) {
            colors.add(color);
        }*/

        PieDataSet pieDataSet = new PieDataSet(pieEntries, " ");
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);

        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(pieChartIncome));
        pieData.setValueTextSize(9f);
        pieData.setValueTextColor(Color.WHITE);

        pieChartIncome.setData(pieData);
        pieChartIncome.invalidate();
    }

    private void setRecyclerViewColor() {

        if(pieChartDataList.size() == 0)
        {
            recyclerViewColor.setVisibility(View.GONE);
        }
        else if (pieChartDataList.size() > 0)
        {
            recyclerViewColor.setVisibility(View.VISIBLE);
        }

        GridLayoutManager layoutManager=new GridLayoutManager(requireContext(),2);
        recyclerViewColor.setLayoutManager(layoutManager);

        PieChartColorAdapter pieChartColorAdapter = new PieChartColorAdapter(requireContext(),pieChartDataList,COLOR_PALETTE_2);
        recyclerViewColor.setAdapter(pieChartColorAdapter);

    }

}