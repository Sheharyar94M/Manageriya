package com.hammad.managerya.bottomNavFragments.homeFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MyViewHolder> {

    Context context;
    List<String> monthsList;
    int selectedPosition = -1;
    int firstSelectedPosition=11;

    //for getting the current date so that the current month can be highlighted
    String currentDate;

    public MonthAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.monthsList = stringList;
        getCurrentDate();
    }

    @NonNull
    @Override
    public MonthAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_month_reycycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView.setText(monthsList.get(position));

        //highlighting the current month by default
        if(monthsList.get(position).contains(currentDate))
        {
            Log.i("HOLDER_1", "called");
            holder.textView.setTextColor(Color.parseColor("#0179d9"));
            holder.itemView.setBackgroundResource(R.drawable.white_circular_background);
        }

        /*if(firstSelectedPosition==position)
        {
            Log.i("HOLDER_1", "called");
            holder.textView.setTextColor(Color.parseColor("#0179d9"));
            holder.constraintLayout.setBackgroundResource(R.drawable.white_circular_background);
        }*/

        //click listener textview
        holder.textView.setOnClickListener(view -> {

            if (selectedPosition == position) {
                selectedPosition = -1;
                notifyDataSetChanged();
                return;
            }

            selectedPosition = position;
            notifyDataSetChanged();

            //showing toast
            Toast.makeText(context, holder.textView.getText(), Toast.LENGTH_SHORT).show();

        });

        //condition for handling the click listeners background & text changes
        if (selectedPosition == position) {
            holder.textView.setTextColor(Color.parseColor("#0179d9"));
            holder.constraintLayout.setBackgroundResource(R.drawable.white_circular_background);
        } else {
            holder.textView.setTextColor(Color.parseColor("#FFFFFF"));
            holder.constraintLayout.setBackgroundResource(android.R.color.transparent);
        }


    }

    @Override
    public int getItemCount() {
        return monthsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.constraint_month_recycler);
            textView = itemView.findViewById(R.id.text_view_month_recycler);
        }
    }

    private void getCurrentDate(){
        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat dateFormat=new SimpleDateFormat("MMM yyyy");
        currentDate= dateFormat.format(calendar.getTime());
    }
}
