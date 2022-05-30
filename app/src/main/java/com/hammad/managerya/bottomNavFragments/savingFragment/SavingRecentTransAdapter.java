package com.hammad.managerya.bottomNavFragments.savingFragment;

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

public class SavingRecentTransAdapter extends RecyclerView.Adapter<SavingRecentTransAdapter.ViewHolder> {

    Context context;

    public SavingRecentTransAdapter(Context context) {
        this.context = context;
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

        holder.imageViewMore.setOnClickListener(view -> {
            Toast.makeText(context, "More Items Clicked", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return 5;
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

}
