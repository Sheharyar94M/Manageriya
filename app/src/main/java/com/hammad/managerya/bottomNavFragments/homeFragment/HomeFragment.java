package com.hammad.managerya.bottomNavFragments.homeFragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hammad.managerya.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView textViewCurrentDate;
    private TextView textViewTotalIncome, textViewSpend, textViewPercentage;
    private RecyclerView recyclerViewRecentTransaction, recyclerViewMonth;
    private List<String> monthsList = new ArrayList<>();
    private PieChart pieChart;
    private RecyclerView recyclerViewRecent;

    public  static final int[] COLORS_PALETTE_1 = {
            Color.rgb(210, 245, 255),Color.rgb(197, 230, 252),
            Color.rgb(131, 200, 247), Color.rgb(58, 166, 239),
            Color.rgb(29, 152, 255)
    };

    public static final int[] COLORS_PALETTE_2 = {
            Color.rgb(87, 187, 248), Color.rgb(3, 138, 231),
            Color.rgb(5, 159, 249), Color.rgb(4, 151, 243),
            Color.rgb(187, 217, 249)
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //initializing views
        initializeViews(view);

        //get current date
        getCurrentDate();

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
        setRecentRecyclerview();


        return view;
    }

    private void initializeViews(View view) {
        textViewCurrentDate = view.findViewById(R.id.text_view_current_date);

        //Recent Transaction recyclerview
        recyclerViewRecentTransaction = view.findViewById(R.id.recycler_home_frag);

        //Month recyclerview
        recyclerViewMonth = view.findViewById(R.id.recycler_view_months);

        textViewTotalIncome = view.findViewById(R.id.text_view_total_income);
        textViewSpend = view.findViewById(R.id.text_view_total_expense);
        textViewPercentage = view.findViewById(R.id.text_view_percentage);

        pieChart = view.findViewById(R.id.pie_chart);

        //recyclerview recent
        recyclerViewRecent=view.findViewById(R.id.recycler_view_recent);

    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String currentDate = dateFormat.format(calendar.getTime());

        textViewCurrentDate.setText(currentDate);
    }

    private void setRecentTransactionRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewRecentTransaction.setLayoutManager(layoutManager);

        HomeFragTransAdapter adapter = new HomeFragTransAdapter(requireActivity());
        recyclerViewRecentTransaction.setAdapter(adapter);
    }

    private void setMonthRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMonth.setLayoutManager(layoutManager);

        MonthAdapter monthAdapter = new MonthAdapter(requireActivity(), monthsList);
        recyclerViewMonth.setAdapter(monthAdapter);

        //scroll recyclerview to current month
        int newPosition = monthsList.size() - 2;
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
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        pieEntries.add(new PieEntry(0.3f, "Bills"));
        pieEntries.add(new PieEntry(0.2f, "Grocery"));
        pieEntries.add(new PieEntry(0.15f, "Fuel"));
        pieEntries.add(new PieEntry(0.35f, "Health"));
        pieEntries.add(new PieEntry(0.25f, "Education"));

        pieEntries.add(new PieEntry(0.12f, "Transport"));
        pieEntries.add(new PieEntry(0.28f, "Shopping"));
        pieEntries.add(new PieEntry(0.30f, "Travel"));
        pieEntries.add(new PieEntry(0.35f, "Entertainment"));
        pieEntries.add(new PieEntry(0.40f, "Wedding"));

        ArrayList<Integer> colors = new ArrayList<>();

        for (int color : ColorTemplate.createColors(COLORS_PALETTE_1)) {
            colors.add(color);
        }

        for (int color : ColorTemplate.createColors(COLORS_PALETTE_2)) {
            colors.add(color);
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, " ");
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);

        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(8f);
        pieData.setValueTextColor(Color.WHITE);

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void setupPieChart() {

        //the centre of pie chart
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.parseColor("#06a6ff"));
        pieChart.setUsePercentValues(true);

        pieChart.setCenterText("70\n\nSpend");
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

    }

    private void setRecentRecyclerview()
    {
        LinearLayoutManager layoutManager=new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewRecent.setLayoutManager(layoutManager);

        RecentAdapter recentAdapter=new RecentAdapter(requireContext());
        recyclerViewRecent.setAdapter(recentAdapter);
    }
}