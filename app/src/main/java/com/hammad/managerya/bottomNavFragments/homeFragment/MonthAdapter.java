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

    //we set the default position to 10, so that the current month is highlighted when appliation is loaded
    int selectedPosition = 10;

    private OnMonthClickListener mOnMonthClickListener;

    public MonthAdapter(Context context, List<String> stringList,OnMonthClickListener onMonthClickListener) {
        this.context = context;
        this.monthsList = stringList;
        this.mOnMonthClickListener = onMonthClickListener;
    }

    @NonNull
    @Override
    public MonthAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_month_reycycler_view, parent, false);
        return new MyViewHolder(view,mOnMonthClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView.setText(monthsList.get(position));

        //Bu default highlights the current selected month
        if(selectedPosition == 10)
        {
            holder.textView.setTextColor(Color.parseColor("#0179d9"));
            holder.constraintLayout.setBackgroundResource(R.drawable.white_circular_background);
        }

        //click listener textview
        holder.textView.setOnClickListener(view -> {

            if (selectedPosition == position) {
                selectedPosition = -1;
                notifyDataSetChanged();
                return;
            }

            selectedPosition = position;
            notifyDataSetChanged();

            //interface calling
            mOnMonthClickListener.onMonthSelected(holder.textView.getText().toString());

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
        OnMonthClickListener onMonthClickListener;

        public MyViewHolder(@NonNull View itemView,OnMonthClickListener monthClickListener) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.constraint_month_recycler);
            textView = itemView.findViewById(R.id.text_view_month_recycler);
            this.onMonthClickListener=monthClickListener;
        }
    }

    public interface OnMonthClickListener{
        void onMonthSelected(String monthName);
    }

}
