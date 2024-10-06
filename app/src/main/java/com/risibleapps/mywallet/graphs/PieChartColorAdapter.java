package com.risibleapps.mywallet.graphs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.bottomNavFragments.homeFragment.homeDB.ExpenseCatDetailModel;

import java.util.List;

public class PieChartColorAdapter extends RecyclerView.Adapter<PieChartColorAdapter.MyViewHolder> {

    Context context;
    List<ExpenseCatDetailModel> pieChartDataList;
    int[] colorArray;

    public PieChartColorAdapter(Context context, List<ExpenseCatDetailModel> pieChartDataList,int[] colorArray) {
        this.context = context;
        this.pieChartDataList = pieChartDataList;
        this.colorArray = colorArray;
    }

    @NonNull
    @Override
    public PieChartColorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater =LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pie_chart_color_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PieChartColorAdapter.MyViewHolder holder, int position) {

        //list item
        ExpenseCatDetailModel item= pieChartDataList.get(position);

        holder.textViewColor.setBackgroundColor(colorArray[position]);

        String catName= item.getExpCatName();

        //if category name contains '\n', then removes the space
        if(catName.contains("\n"))
        {
            catName = catName.replace("\n","");
        }
        holder.textViewCat.setText(catName);
    }

    @Override
    public int getItemCount() {
        return pieChartDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewColor,textViewCat;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewColor = itemView.findViewById(R.id.color_text);
            textViewCat = itemView.findViewById(R.id.cat_text);
        }
    }
}
