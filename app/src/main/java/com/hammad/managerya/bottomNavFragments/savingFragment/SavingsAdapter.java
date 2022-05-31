package com.hammad.managerya.bottomNavFragments.savingFragment;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.hammad.managerya.R;
import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.savingFragment.DB.SavingEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SavingsAdapter extends RecyclerView.Adapter<SavingsAdapter.MyViewHolder> {

    Context context;
    List<SavingEntity> savingGoalList;
    private OnSavingClickListener mOnSavingClickListener;

    private RoomDBHelper database;

    private int amountSaved;

    public SavingsAdapter(Context context,List<SavingEntity> list,OnSavingClickListener onSavingClickListener) {
        this.context = context;
        this.savingGoalList = list;
        this.mOnSavingClickListener=onSavingClickListener;

        database = RoomDBHelper.getInstance(context);
        this.amountSaved = 0;
    }

    @NonNull
    @Override
    public SavingsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_saving_recycler,parent,false);
        return new MyViewHolder(view,mOnSavingClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SavingsAdapter.MyViewHolder holder, int position) {

        //holder item
        SavingEntity item = savingGoalList.get(position);

        //getting the sum of saving goal transactions
        amountSaved = database.savingDao().getSavingTransSumById(item.getId());

        //setting the data to views
        holder.imageView.setImageResource(item.getSavingIcon());
        holder.textViewSavingTitle.setText(item.getSavingTitle());
        holder.textViewGoalAmount.setText(String.valueOf(item.getSavingGoalAmount()));
        holder.textViewAmountSaved.setText(String.valueOf(amountSaved));

        //setting the date
        holder.textViewTargetSavingDate.setText(getConvertedDate(item.getSavingTargetDate()));

        //setting the progress bar
        holder.progressBar.setMax(item.getSavingGoalAmount());
        holder.progressBar.setProgress(amountSaved);

    }

    @Override
    public int getItemCount() {
        return savingGoalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewSavingTitle, textViewCurrency;
        TextView textViewGoalAmount,textViewAmountSaved, textViewTargetSavingDate;
        LinearProgressIndicator progressBar;

        OnSavingClickListener onSavingClickListener;

        public MyViewHolder(@NonNull View itemView,OnSavingClickListener onSavingClickListener) {
            super(itemView);

            imageView=itemView.findViewById(R.id.img_saving);

            textViewSavingTitle =itemView.findViewById(R.id.txt_cat_saving);

            textViewCurrency =itemView.findViewById(R.id.text_currency_1);
            textViewAmountSaved=itemView.findViewById(R.id.text_amount_saved);
            textViewGoalAmount=itemView.findViewById(R.id.text_goal_amount);

            textViewTargetSavingDate =itemView.findViewById(R.id.text_saving_target_date);

            progressBar=itemView.findViewById(R.id.progress_saving_recycler);

            //setting the current currency to textview
            textViewCurrency.setText(CURRENCY_);

            //initializing interface instance
            this.onSavingClickListener=onSavingClickListener;

            //interface click listener
            itemView.setOnClickListener(view -> mOnSavingClickListener.onSavingItemClicked(getAdapterPosition()));

        }
    }

    public interface OnSavingClickListener{
        void onSavingItemClicked(int position);
    }

    /*
        This function is used to convert date from 'yyyy-MM-dd' to 'MMM dd, yyyy' format
        2022-05-27 to May 27, 2022
    */
    private String getConvertedDate(String databaseDate) {
        String convertedDate = "";

        //database date format
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

        //converting date format to another
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date date = dateFormat1.parse(databaseDate);
            convertedDate = dateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate;
    }

}
