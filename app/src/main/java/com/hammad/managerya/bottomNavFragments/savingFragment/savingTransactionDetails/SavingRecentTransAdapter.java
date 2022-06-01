package com.hammad.managerya.bottomNavFragments.savingFragment.savingTransactionDetails;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;
import com.hammad.managerya.bottomNavFragments.savingFragment.DB.SavingModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SavingRecentTransAdapter extends RecyclerView.Adapter<SavingRecentTransAdapter.ViewHolder> {

    Context context;
    List<SavingModel> savingTransactionList;

    public SavingRecentTransAdapter(Context context,List<SavingModel> list) {
        this.context = context;
        this.savingTransactionList = list;
    }

    @NonNull
    @Override
    public SavingRecentTransAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_recent_saving_transaction,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavingRecentTransAdapter.ViewHolder holder, int position) {

        SavingModel item = savingTransactionList.get(position);

        //setting the values to textviews
        holder.imageViewCategory.setImageResource(item.getSavingIcon());
        holder.textViewSavingGoalTitle.setText(item.getSavingTitle());
        holder.textViewAmount.setText(String.valueOf(item.getSavingGoalAmount()));
        holder.textViewDate.setText(getConvertedDate(item.getSavingTargetDate()));

        holder.imageViewMore.setOnClickListener(view -> {
            Toast.makeText(context, "More Items Clicked", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return savingTransactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewSavingGoalTitle, textViewCurrency,textViewAmount,textViewDate;
        ImageView imageViewCategory,imageViewMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewSavingGoalTitle =itemView.findViewById(R.id.text_category);
            textViewCurrency=itemView.findViewById(R.id.text_currency_);
            textViewAmount=itemView.findViewById(R.id.text_amount_);
            textViewDate=itemView.findViewById(R.id.text_date_);

            imageViewCategory=itemView.findViewById(R.id.img__1);
            imageViewMore=itemView.findViewById(R.id.img_more);

            //setting the currency to textview
            textViewCurrency.setText(CURRENCY_);

        }
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
